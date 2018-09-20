package com.zdtech.platform.framework.utils.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 格式化输出信息，包含最小输出长度、最大输出长度和是否靠左
 * @author qfxu
 *
 */
public class FormattingInfo {
	public int min = -1;
	public int max = 0x7FFFFFFF;
	public boolean leftAlign = false;

	public Logger logger = LoggerFactory.getLogger(FormattingInfo.class);

	public void reset() {
		min = -1;
		max = 0x7FFFFFFF;
		leftAlign = false;
	}

	public void dump() {
		logger.debug("min=" + min + ", max=" + max + ", leftAlign=" + leftAlign);
	}
}
