package com.zdtech.platform.framework.persistence;

import org.springframework.data.domain.Sort;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lcheng on 2015/5/14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface QueryDef {

    String queryTag();
//    Class entityClass();
    String daoName();
    String[] genericQueryFields() default {};
    String[] sortFields() default {};
    Sort.Direction direction() default Sort.Direction.ASC;

}
