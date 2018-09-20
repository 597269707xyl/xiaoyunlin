package com.zdtech.platform.framework.persistence;

import org.springframework.data.domain.Sort;

/**
 * Created by lcheng on 2015/5/14.
 */
public class RepQueryDefInfo {

    private String queryTag;
    private Class entityClass;
    private String[] genericQueryFields;
    private String[] sortFields;
    private Sort.Direction direction;
    private Object dao;

    public RepQueryDefInfo(String queryTag,Class entityClass,
                           String[] genericQueryFields, Object dao) {
        this.queryTag = queryTag;
        this.entityClass = entityClass;
        this.genericQueryFields = genericQueryFields;
        this.dao = dao;
    }

    public String getQueryTag() {
        return queryTag;
    }

    public void setQueryTag(String queryTag) {
        this.queryTag = queryTag;
    }

    public String[] getGenericQueryFields() {
        return genericQueryFields;
    }

    public void setGenericQueryFields(String[] genericQueryFields) {
        this.genericQueryFields = genericQueryFields;
    }

    public Object getDao() {
        return dao;
    }

    public void setDao(Object dao) {
        this.dao = dao;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    public String[] getSortFields() {
        return sortFields;
    }

    public void setSortFields(String[] sortFields) {
        this.sortFields = sortFields;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }
}
