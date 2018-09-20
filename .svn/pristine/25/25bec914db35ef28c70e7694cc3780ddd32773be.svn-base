package com.zdtech.platform.framework.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据规则生成编码过程包含的变量信息
 * 
 * @author qfxu
 * 
 */
public class Bundle {
	private Map<String, String> container = null;

	public Bundle() {
		container = new HashMap<String, String>();
	}

	/**
	 * 加入变量信息
	 * 
	 * @param key
	 *            变量名称
	 * @param value
	 *            变量值
	 */
	public void putString(String key, String value) {
		container.put(key, value);
	}

	/**
	 * 从对象中取出变量值
	 * 
	 * @param key
	 *            变量名称
	 * @return 变量值
	 */
	public String getString(String key) {
		if (container.containsKey(key))
			return container.get(key);
		return null;
	}

}
