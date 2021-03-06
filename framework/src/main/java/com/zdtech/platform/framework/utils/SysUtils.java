package com.zdtech.platform.framework.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author lcheng
 * @version 1.0
 *          ${tags}
 */
@Component
public class SysUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    /**
     * 根据bean的名称获得Bean
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName){
        return SysUtils.context.getBean(beanName);
    }

    /**
     * 获得系统的某一个Bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return SysUtils.context.getBean(name, clazz);
    }

    /**
     * 按类型获得Bean
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        if (context != null)
            return SysUtils.context.getBean(clazz);
        return null;
    }

}
