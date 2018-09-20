package com.zdtech.platform.framework.ui;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class EntityManager {
	private static final Log log = LogFactory.getLog(EntityManager.class);

	private Map<String, EntityInfo> entityMap = null;

	private static EntityManager INSTANCE = null;

	public static EntityManager getInstance() {
		if (INSTANCE == null)
			INSTANCE = new EntityManager();
		return INSTANCE;
	}

	private EntityManager() {
		entityMap = new HashMap<String, EntityInfo>();
	}

	public EntityInfo getEntityInfo(String entity) {
		if (entityMap == null)
			return null;
		return entityMap.get(entity);
	}

	public void init(String pkgName) {
		log.info("loading package: " + pkgName);
		Set<Class<?>> classes = getClasses(pkgName);
		if (classes.isEmpty())
			return;
		for (Class<?> entityClass : classes) {
			parseEntity(entityClass);
		}
	}

	private void parseEntity(Class<?> entityClass) {
		Entity entity = (Entity) entityClass.getAnnotation(Entity.class);
		if (entity == null)
			return;

		String entityName = entityClass.getSimpleName();
		EntityInfo entityInfo = new EntityInfo();
		entityInfo.setEntityClass(entityClass);
		entityInfo.setName(entityName);

        Filter filter = (Filter)entityClass.getAnnotation(Filter.class);
        if(filter != null){
           entityInfo.setFilter(filter.value());
        }

		Field[] fields = entityClass.getDeclaredFields();
		FieldInfo fieldInfo = null;
		for (Field field : fields) {
			if (field.getName().equals("serialVersionUID"))
				continue;
			fieldInfo = new FieldInfo();
			String name = field.getName();
			fieldInfo.setName(name);
			Class<?> fieldType = field.getType();

			if (isPrimitive(fieldType)) {
				Id id = field.getAnnotation(Id.class);
				if (id != null) {// 判断是否为主键
					entityInfo.addKeyField(name);
					entityInfo.setIDClass(fieldType);
				}
				fieldInfo.setType(fieldType);

				JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
				if (jsonFormat != null)// 判断是否jsonformat标注
					fieldInfo.setPattern(jsonFormat.pattern());

                FieldEnum fieldEnum = field.getAnnotation(FieldEnum.class);
                if (fieldEnum != null) {
                    fieldInfo.setEnumCode(fieldEnum.code());
                    entityInfo.addEnumField(field.getName());
                }

				entityInfo.addFieldInfo(fieldInfo);
			} else {
				processSubEntity(fieldType, entityInfo, name);
			}
		}

		if (entityMap == null)
			entityMap = new HashMap<String, EntityInfo>();
		entityMap.put(entityName, entityInfo);

	}

	private void processSubEntity(Class<?> subClass, EntityInfo entityInfo,
			String parentFieldName) {
		if (StringUtils.isEmpty(parentFieldName) || entityInfo == null)
			return;

		FieldInfo fieldInfo = null;
		Embeddable embeddable = subClass.getAnnotation(Embeddable.class);
		if (embeddable != null) {
			entityInfo.setIDClass(subClass);
		}
		Field[] fields = subClass.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals("serialVersionUID"))
				continue;
			fieldInfo = new FieldInfo();
			String fieldName = parentFieldName + "." + field.getName();
			fieldInfo.setName(fieldName);
            fieldInfo.setType(field.getType());

			if (embeddable != null) {
				entityInfo.addKeyField(fieldName);
			}

			FieldEnum fieldEnum = field.getAnnotation(FieldEnum.class);
			if (fieldEnum != null) {
                fieldInfo.setEnumCode(fieldEnum.code());
                entityInfo.addEnumField(field.getName());
            }
			entityInfo.addFieldInfo(fieldInfo);
		}
	}

	/**
	 * 判断是否为基础类型
	 * 
	 * @param clz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean isPrimitive(Class<?> clz) {
		boolean primitive = clz.isPrimitive();
		if (primitive)
			return true;
		if (clz.equals(String.class))
			return true;
		String name = clz.getName();
		if (name.contains("java"))
			return true;
		try {
			return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 从包中获取所有类
	 * 
	 * @param packageName
	 * @return
	 */
	private Set<Class<?>> getClasses(String packageName) {

		// 第一个class类的集合
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		// 是否循环迭代
		boolean recursive = true;
		// 获取包的名字 并进行替换
		String packageDirName = packageName.replace('.', '/');
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader()
					.getResources(packageDirName);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					log.info("scan file");
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findAndAddClassesInPackageByFile(packageName, filePath,
							recursive, classes);
				} else if ("jar".equals(protocol)) {
					// 如果是jar包文件
					// 定义一个JarFile
					log.info("scan jar");
					JarFile jar;
					try {
						// 获取jar
						jar = ((JarURLConnection) url.openConnection())
								.getJarFile();
						// 从此jar包 得到一个枚举类
						Enumeration<JarEntry> entries = jar.entries();
						// 同样的进行循环迭代
						while (entries.hasMoreElements()) {
							// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							// 如果是以/开头的
							if (name.charAt(0) == '/') {
								// 获取后面的字符串
								name = name.substring(1);
							}
							// 如果前半部分和定义的包名相同
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								// 如果以"/"结尾 是一个包
								if (idx != -1) {
									// 获取包名 把"/"替换成"."
									packageName = name.substring(0, idx)
											.replace('/', '.');
								}
								// 如果可以迭代下去 并且是一个包
								if ((idx != -1) || recursive) {
									// 如果是一个.class文件 而且不是目录
									if (name.endsWith(".class")
											&& !entry.isDirectory()) {
										// 去掉后面的".class" 获取真正的类名
										String className = name.substring(
												packageName.length() + 1,
												name.length() - 6);
										try {
											// 添加到classes
											classes.add(Class
													.forName(packageName + '.'
															+ className));
										} catch (ClassNotFoundException e) {
											// log
											// .error("添加用户自定义视图类错误 找不到此类的.class文件");
											e.printStackTrace();
										}
									}
								}
							}
						}
					} catch (IOException e) {
						// log.error("在扫描用户定义视图时从jar包获取文件出错");
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return classes;
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddClassesInPackageByFile(String packageName,
			String packagePath, final boolean recursive, Set<Class<?>> classes) {
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			// log.warn("用户定义包名 " + packageName + " 下没有任何文件");
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file) {
				return (recursive && file.isDirectory())
						|| (file.getName().endsWith(".class"));
			}
		});
		// 循环所有文件
		for (File file : dirfiles) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(
						packageName + "." + file.getName(),
						file.getAbsolutePath(), recursive, classes);
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0,
						file.getName().length() - 6);
				try {
					// 添加到集合中去
					// classes.add(Class.forName(packageName + '.' +
					// className));
					// 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
					classes.add(Thread.currentThread().getContextClassLoader()
							.loadClass(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					// log.error("添加用户自定义视图类错误 找不到此类的.class文件");
					e.printStackTrace();
				}
			}
		}
	}
}
