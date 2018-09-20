package com.zdtech.platform.framework.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实体信息
 * 
 * @author qfxu
 * 
 */
public class EntityInfo {
	private String name;
    private String filter;
	private Class<?> entityClass;
	private Class<?> IDClass;
	private List<String> keyFields;
	private Map<String, FieldInfo> fieldInfoMap;
    private List<String> enumFields;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

	public Class<?> getIDClass() {
		return IDClass;
	}

	public void setIDClass(Class<?> iDClass) {
		IDClass = iDClass;
	}

	public List<String> getKeyFields() {
		return keyFields;
	}

	public void addKeyField(String keyField) {
		if (keyFields == null)
			keyFields = new ArrayList<String>();
		keyFields.add(keyField);
	}

    public List<String> getEnumFields(){
        return this.enumFields;
    }

    public void addEnumField(String enumField){
        if(enumFields == null)
            enumFields = new ArrayList<String>();
        enumFields.add(enumField);
    }

	public FieldInfo getFieldInfo(String name) {
		if (fieldInfoMap == null)
			return null;
		return fieldInfoMap.get(name);
	}

	public void addFieldInfo(FieldInfo fieldInfo) {
		if (fieldInfoMap == null)
			fieldInfoMap = new HashMap<String, FieldInfo>();
		fieldInfoMap.put(fieldInfo.getName(), fieldInfo);

	}

}
