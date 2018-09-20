package com.zdtech.platform.framework.utils.converter;



import com.zdtech.platform.framework.utils.Bundle;

import java.text.DateFormat;
import java.util.Date;


/**
 * 日期格式数据转换：按照指定格式输出日期
 * @author qfxu
 *
 */
public class DatePatternConverter extends PatternConverter {

	private DateFormat df;

	public DatePatternConverter(FormattingInfo formattingInfo, DateFormat df) {
		super(formattingInfo);
		this.df = df;
	}

	@Override
	protected String convert(Bundle bundle) {
		String converted = null;
		try {
			converted = df.format(new Date());
		} catch (Exception ex) {
		}
		return converted;
	}

}
