package com.zdtech.platform.framework.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * 获取Spring容器，以访问容器中的bean对象
 *
 * @author qfxu
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        if (applicationContext == null)
            throw new RuntimeException("Spring Container is not started");
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clz) {
        if (applicationContext == null)
            throw new RuntimeException("Spring Container is not started");
        return applicationContext.getBean(clz);
    }
}
