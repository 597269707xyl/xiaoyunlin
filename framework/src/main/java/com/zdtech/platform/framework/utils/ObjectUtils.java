package com.zdtech.platform.framework.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Timestamp;


/**
 * 对象操作工具类
 * 
 * @author qfxu
 * 
 */
public class ObjectUtils {
	private static Logger logger = LoggerFactory.getLogger(ObjectUtils.class);

	/**
	 * 属性拷贝
	 * 
	 * @param dest
	 *            目标对象
	 * @param orig
	 *            源对象
	 */
	public static void copyProperties(Object dest, Object orig)
			throws Exception {
		copyProperties(dest, orig, "");
	}

    /**
     * 属性拷贝，源对象每个属性有一个指定的后缀
     * @param dest
     * @param orig
     * @param suffix
     * @throws Exception
     */
    public static void copyProperties(Object dest, Object orig, String suffix) throws Exception{
        if (dest == null || orig == null)
            return;
        Field[] destFields = dest.getClass().getDeclaredFields();

        for (Field destField : destFields) {
            String destFieldName = destField.getName();
            String origFieldName = destFieldName + suffix;
            copyProperty(dest, orig, destFieldName, origFieldName);
        }
    }

	/**
	 * 拷贝对象的某个属性值
	 * 
	 * @param dest
	 *            目标对象
	 * @param orig
	 *            源对象
	 * @param name
	 *            属性名称
	 */
	public static void copyProperty(Object dest, Object orig, String name)
			throws Exception {
		copyProperty(dest, orig, name, name);
	}

    /**
     * 拷贝对象属性
     * @param dest
     * @param orig
     * @param destName
     * @param origName
     * @throws Exception
     */
    public static void copyProperty(Object dest, Object orig, String destName, String origName)
            throws Exception {
        if (dest == null || orig == null)
            return;

        Class<?> destClz = dest.getClass();
        Field destField = null;
        try {
            destField = destClz.getDeclaredField(destName);
        } catch (NoSuchFieldException ex) {
            logger.debug("目标对象没有属性：[" + destName + "]");
        }
        if (destField == null)
            return;

        Class<?> origClz = orig.getClass();
        Field origField = null;
        try {
            origField = origClz.getDeclaredField(origName);
        } catch (NoSuchFieldException ex) {
            logger.debug("源对象没有属性：[" + origName + "]");
        }
        if (origField == null)
            return;

        Object value = PropertyUtils.getProperty(orig, origName);
        if (value == null)
            return;
        String destFieldType = destField.getType().getName();
        String origFieldType = origField.getType().getName();
        if (!destFieldType.equals(origFieldType)) {
            if (destFieldType.equals(Long.class.getName()))
                value = Long.valueOf(value.toString());
            if (destFieldType.equals(Timestamp.class.getName()))
                value = new Timestamp(Long.valueOf(value.toString()));
        }
        PropertyUtils.setProperty(dest, destName, value);
    }
	/**
	 * 读取属性，如果name包含"."，则说明取的是子对象的值，目前只支持一级子对象
	 * 
	 * @param bean
	 * @param name
	 * @return
	 */
	public static Object getProperty(Object bean, String name) {
		if (bean == null)
			return null;
		Object result = null;
		if (!name.contains(".")) {
			try {
				result = PropertyUtils.getProperty(bean, name);
			} catch (Exception ex) {
				throw new RuntimeException("读取属性：[" + name + "]出错");
			}

		} else {
			String field = name.split("\\.")[0];
			String subField = name.split("\\.")[1];
			Object subObj = null;
			try {
				subObj = PropertyUtils.getProperty(bean, field);
				if (subObj != null)
					result = PropertyUtils.getProperty(subObj, subField);
			} catch (Exception ex) {
				throw new RuntimeException("读取属性：[" + name + "]出错");
			}
		}
		return result;
	}

	/**
	 * 设置属性
	 * 
	 * @param bean
	 * @param name
	 * @param value
	 */
	public static void setProperty(Object bean, String name, Object value) {
		try {
            Field f = bean.getClass().getDeclaredField(name);
            if (f!=null){
                PropertyUtils.setProperty(bean, name, value);
            }
		} catch (Exception ex) {
			//throw new RuntimeException("设置属性：[" + name + "]出错");
            logger.error("设置Bean 属性{} 出错!",name);
        }
	}
}
