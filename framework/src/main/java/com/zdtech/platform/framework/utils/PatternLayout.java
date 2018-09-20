package com.zdtech.platform.framework.utils;


import com.zdtech.platform.framework.repository.SeqDao;
import com.zdtech.platform.framework.utils.converter.PatternConverter;

/**
 * 自定义规则生成编码
 * 
 * @author qfxu
 * 
 */
public class PatternLayout {

	/**
	 * 默认编码生成规则 年月日时分秒-5位随机数
	 */
	public final static String DEFAULT_PATTERN = "%d{yyyyMMddHHmmss}-%r{5}";

	protected final int BUF_SIZE = 256;
	protected final int MAX_CAPACITY = 1024;

	private StringBuffer sbuf = new StringBuffer(BUF_SIZE);

	private String pattern;

	private PatternConverter head;

    private SeqDao seqDao;

	/**
	 * Constructs a PatternLayout using the DEFAULT_PATTERN.
	 */
	public PatternLayout(SeqDao seqDao) {
		this(DEFAULT_PATTERN, seqDao);
	}

	/**
	 * Constructs a PatternLayout using the supplied conversion pattern.
	 */
	public PatternLayout(String pattern, SeqDao seqDao) {
		this.pattern = pattern;
        this.seqDao = seqDao;
		head = createPatternParser(
				(pattern == null) ? DEFAULT_PATTERN : pattern).parse();
	}

	/**
	 * Set the <b>ConversionPattern</b> option. This is the string which
	 * controls formatting and consists of a mix of literal content and
	 * conversion specifiers.
	 */
	public void setConversionPattern(String conversionPattern) {
		pattern = conversionPattern;
		head = createPatternParser(conversionPattern).parse();
	}

	/**
	 * Returns the value of the <b>ConversionPattern</b> option.
	 */
	public String getConversionPattern() {
		return pattern;
	}

	/**
	 * Returns PatternParser used to parse the conversion string. Subclasses may
	 * override this to return a subclass of PatternParser which recognize
	 * custom conversion characters.
	 */
	protected PatternParser createPatternParser(String pattern) {
		return new PatternParser(pattern, seqDao);
	}

	/**
	 * Produces a formatted string as specified by the conversion pattern.
	 */
	public String format(Bundle bundle) {
		// Reset working stringbuffer
		if (sbuf.capacity() > MAX_CAPACITY) {
			sbuf = new StringBuffer(BUF_SIZE);
		} else {
			sbuf.setLength(0);
		}

		PatternConverter c = head;

		while (c != null) {
			c.format(bundle, sbuf);
			c = c.next;
		}
		return sbuf.toString();
	}
}
