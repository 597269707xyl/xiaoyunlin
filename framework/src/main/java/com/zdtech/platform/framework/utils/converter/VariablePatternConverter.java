package com.zdtech.platform.framework.utils.converter;


import com.zdtech.platform.framework.utils.Bundle;

/**
 * 变量转换器：通过变量名找出值并且输出
 * @author qfxu
 *
 */
public class VariablePatternConverter extends PatternConverter {

	private String key;

	public VariablePatternConverter(String key) {
		this.key = key;
	}

	@Override
	protected String convert(Bundle bundle) {
		return bundle.getString(key);
	}

}
