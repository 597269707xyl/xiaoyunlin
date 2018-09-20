package com.zdtech.platform.framework.utils.converter;


import com.zdtech.platform.framework.utils.Bundle;

/**
 * 格式转换器
 * 
 * @author qfxu
 * 
 */
public abstract class PatternConverter {
	public PatternConverter next;
	private int min = -1;
	private int max = 0x7FFFFFFF;
	private boolean leftAlign = false;

	protected PatternConverter() {
	}

	protected PatternConverter(FormattingInfo fi) {
		min = fi.min;
		max = fi.max;
		leftAlign = fi.leftAlign;
	}

	/**
	 * 由子类实现具体转换过程
	 * 
	 * @param bundle
	 *            转换过程携带的变量信息
	 * @return 返回转换后的结果
	 */
	abstract protected String convert(Bundle bundle);

	/**
	 * 格式化输出转换后的内容
	 * 
	 * @param bundle
	 *            转换过程中携带的变量信息
	 * @param sbuf
	 */
	public void format(Bundle bundle, StringBuffer sbuf) {
		String s = convert(bundle);
		if (s == null) {
			if (0 < min)
				spacePad(sbuf, min);
			return;
		}

		int len = s.length();

		if (len > max)
			sbuf.append(s.substring(len - max));
		else if (len < min) {
			if (leftAlign) {
				sbuf.append(s);
				spacePad(sbuf, min - len);
			} else {
				spacePad(sbuf, min - len);
				sbuf.append(s);
			}
		} else
			sbuf.append(s);
	}

	static String[] SPACES = { "0", "00", "0000", "        ", // 1,2,4,8 spaces
			"                ", // 16 spaces
			"                                " }; // 32 spaces

	/**
	 * 格式化输出，补充空字符
	 */
	public void spacePad(StringBuffer sbuf, int length) {
		while (length >= 32) {
			sbuf.append(SPACES[5]);
			length -= 32;
		}

		for (int i = 4; i >= 0; i--) {
			if ((length & (1 << i)) != 0) {
				sbuf.append(SPACES[i]);
			}
		}
	}
}
