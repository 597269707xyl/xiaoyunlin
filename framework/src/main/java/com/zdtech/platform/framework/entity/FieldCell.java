package com.zdtech.platform.framework.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体字段对应的Excel 单元格
 * Created by wzx on 2016/11/3.
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface FieldCell {
    int cellIndex();
    boolean needConvert() default false;
    String[] convertValues() default {};
}
