package com.zdtech.platform.framework.utils.converter;



import com.zdtech.platform.framework.utils.Bundle;

import java.util.Random;

/**
 * 输出指定长度的随机数
 *
 * @author qfxu
 */
public class RandomNoPatternConverter extends PatternConverter {
    // 所有数字
    private static final char[] DIGIT_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9'};

    private int length;

    public RandomNoPatternConverter(FormattingInfo formattingInfo, int length) {
        super(formattingInfo);
        this.length = length;
    }

    @Override
    protected String convert(Bundle bundle) {
        return generateDigitNo(length);
    }

    /**
     * 返回指定长度的随机数，只包含数字
     *
     * @param length 指定长度
     * @return
     */
    private String generateDigitNo(int length) {
        return generateNo(length, DIGIT_CHAR);
    }

    /**
     * 在限定的字符范围内，返回指定长度的随机数
     *
     * @param length     指定长度
     * @param limitedStr 限定字符范围
     * @return
     */
    private String generateNo(int length, final char[] limitedStr) {
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int limited = limitedStr.length;
            buffer.append(limitedStr[random.nextInt(limited)]);
        }
        return buffer.toString();
    }

}
