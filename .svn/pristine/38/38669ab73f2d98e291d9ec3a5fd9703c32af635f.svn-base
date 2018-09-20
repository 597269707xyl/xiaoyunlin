package com.zdtech.platform.simserver.utils.reflect;

import com.zdtech.platform.simserver.jar.Reflections;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * InvokedClass
 *
 * @author panli
 * @date 2016/6/14
 */
public class ReflectWraper {
    public static String REFLECTPATH_FORMAT = "com.zdtech.platform.simserver.utils.reflect.%s";

    public static String invoke(String invokeMethod,Object ...args){
        String ret = "";
        if (StringUtils.isEmpty(invokeMethod))
            return ret;
        String a[] = invokeMethod.split(":");
        if (a.length < 2)
            return ret;
        String classpath = String.format(REFLECTPATH_FORMAT,a[0]);
        String method = a[1];
        Class<?> clz = buildClass(classpath);
        if (clz == null)
            return ret;
        Object object = buildInstance(clz);
        if (object == null)
            return ret;
        ret = invokeMethod(object,method,args);
        return ret;
    }
    public static Class buildClass(String classpath){
        Class<?> clz = null;
        try {
            clz = Class.forName(classpath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clz;
    }
    public static Object buildInstance(Class clz){
        Object object = null;
        try {
            //object = clz.newInstance();
            Constructor<?> cs[] = clz.getConstructors();
            if (cs.length < 1)
                return object;
            Constructor<?> c = cs[0];
            int pCount = c.getParameterCount();
            object = c.newInstance(new Object[pCount]);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return object;
    }
    public static String invokeMethod(Object obj,String method,Object ...args){
        String ret = "";
        if (obj == null)
            return ret;
        if (StringUtils.isEmpty(method))
            return ret;
        Object params[] = new Object[args.length];
        Class<?> paramTypes[] = new Class[args.length];
        for (int i = 0; i < args.length; i++){
            paramTypes[i] = args[i].getClass();
            params[i] = args[i];
        }
        try {
            ret = (String) Reflections.invokeMethod(obj,method,paramTypes,params);
        }catch (IllegalArgumentException e1){
            e1.printStackTrace();
        }catch (RuntimeException e2){
            e2.printStackTrace();
        }
        return ret;

    }


}
