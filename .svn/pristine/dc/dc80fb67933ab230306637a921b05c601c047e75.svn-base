package com.zdtech.platform.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类扫描工具类
 */
public class ClassScanners {

    private static Logger log = LoggerFactory.getLogger(ClassScanners.class);

    private ClassScanners() {
    }

    public static Set<Class> scanClassForPackage(Package pkg) {
        Set<Class> classes = new HashSet<>();
        String pkgname = pkg.getName();
        String relPath = pkgname.replace('.', '/');

        URL resource = Thread.currentThread().getContextClassLoader().getResource(relPath);
        if (resource == null) {
            throw new RuntimeException("Unexpected problem: No resource for " + relPath);
        }
        log.info("Package:'{}' becomes Resource:'{}' ", pkgname, resource.toString());

        resource.getPath();
        if (resource.toString().startsWith("jar:")) {
            processJarFile(resource, pkgname, classes);
        } else {
            processDirectory(new File(resource.getPath()), pkgname, classes);
        }
        return classes;
    }

    private static void processJarFile(URL resource, String pkgname, Set<Class> classes) {
        String relPath = pkgname.replace('.', '/');
        String resPath = resource.getPath();
        String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
        log.debug("Reading JAR file: '{}'", jarPath);
        JarFile jarFile;
        try {
            jarFile = new JarFile(jarPath);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IOException reading JAR File '" + jarPath + "'", e);
        }
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            String className = null;
            if (entryName.endsWith(".class") && entryName.startsWith(relPath) && entryName.length() > (relPath.length() + "/".length())) {
                className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
            }
            log.debug("JarEntry '{}' => class '{}'", entryName, className);
            if (className != null) {
                Class clazz = loadClass(className);
                if (null!=clazz){
                    classes.add(clazz);
                }
            }
        }
    }

    private static void processDirectory(File directory, String pkgname, Set<Class> classes) {
        log.debug("Scan class for dir '{}'", directory.toString());
        String[] files = directory.list();
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i];
            String className = null;
            //加载.class文件
            if (fileName.endsWith(".class")) {
                // 去掉后缀
                className = pkgname + '.' + fileName.substring(0, fileName.length() - 6);
            }
            log.debug("FileName '{}' => class '{}'", fileName, className);
            if (className != null) {
                Class clazz = loadClass(className);
                if (null!=clazz){
                    classes.add(clazz);
                }
            }
            File subdir = new File(directory, fileName);
            if (subdir.isDirectory()) {
                processDirectory(subdir, pkgname + '.' + fileName, classes);
            }
        }
    }

    private static Class loadClass(String className) {
        try {
            return Thread.currentThread().getContextClassLoader()
                    .loadClass(className);
        } catch (ClassNotFoundException e) {
            log.error("ClassScanner load class error", e);
        }
        return null;
    }
}
