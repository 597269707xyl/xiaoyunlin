package com.zdtech.platform.framework.utils;


import com.zdtech.platform.framework.repository.SeqDao;
import com.zdtech.platform.framework.utils.converter.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Stack;


/**
 * 编码规则解析，根据输入的规则产生一个转换器链表
 *
 * @author qfxu
 */
public class PatternParser {

    private static final char ESCAPE_CHAR = '%';

    private static final int LITERAL_STATE = 0;
    private static final int CONVERTER_STATE = 1;
    private static final int DOT_STATE = 3;
    private static final int MIN_STATE = 4;
    private static final int MAX_STATE = 5;

    private int state;
    protected int patternLength;
    protected int i;
    private PatternConverter head;
    private PatternConverter tail;
    protected String pattern;
    protected SeqDao seqDao;

    protected FormattingInfo formattingInfo = new FormattingInfo();
    protected StringBuffer currentLiteral = new StringBuffer(32);

    // 日志输出
    private Logger logger = LoggerFactory.getLogger(PatternParser.class);

    public PatternParser(String pattern, SeqDao seqDao) {
        this.pattern = pattern;
        this.seqDao = seqDao;
        patternLength = pattern.length();
        state = LITERAL_STATE;
    }

    /**
     * 将转换器加入到链表中
     *
     * @param pc
     */
    private void addToList(PatternConverter pc) {
        if (head == null) {
            head = pc;
            tail = pc;
        } else {
            tail.next = pc;
            tail = pc;
        }
    }

    /**
     * 添加一个转换器
     *
     * @param pc
     */
    protected void addConverter(PatternConverter pc) {
        currentLiteral.setLength(0);
        // Add the pattern converter to the list.
        addToList(pc);
        // Next pattern is assumed to be a literal.
        state = LITERAL_STATE;
        // Reset formatting info
        formattingInfo.reset();
    }

    /**
     * 抽取大括号{}之间的内容
     *
     * @return
     */
    protected String extractOption() {
        if ((i < patternLength) && (pattern.charAt(i) == '{')) {
            int end = pattern.indexOf('}', i);
            if (end > i) {
                String r = pattern.substring(i + 1, end);
                i = end + 1;
                return r;
            }
        }
        return null;
    }

    protected String extractOptions() {
        if ((i < patternLength) && (pattern.charAt(i) == '{')) {
            Stack stack = new Stack();
            for (int j = i + 1; j < patternLength; j++) {
                char c = pattern.charAt(j);
                if (c == '{')
                    stack.push(c);
                else if (c == '}')
                    stack.pop();

                if (stack.isEmpty()) {
                    String r = pattern.substring(i + 1, j + 1);
                    i = j + 2;
                    return r;
                }
            }
        }
        return null;
    }

    /**
     * 抽取大括号{}之间的内容，内容为数字
     *
     * @return
     */
    protected int extractPrecisionOption() {
        String opt = extractOption();
        int r = 0;
        if (opt != null) {
            try {
                r = Integer.parseInt(opt);
                if (r <= 0) {
                    r = 0;
                }
            } catch (NumberFormatException e) {
                logger.error("option " + opt + " not a decimal integer.");
            }
        }
        return r;
    }

    /**
     * 解析编码规则
     *
     * @return
     */
    public PatternConverter parse() {
        char c;
        i = 0;
        while (i < patternLength) {
            c = pattern.charAt(i++);
            switch (state) {
                case LITERAL_STATE:
                    // In literal state, the last char is always a literal.
                    if (i == patternLength) {
                        currentLiteral.append(c);
                        continue;
                    }
                    if (c == ESCAPE_CHAR) {
                        // peek at the next char.
                        switch (pattern.charAt(i)) {
                            case ESCAPE_CHAR:
                                currentLiteral.append(c);
                                i++; // move pointer
                                break;
                            default:
                                if (currentLiteral.length() != 0) {
                                    addToList(new LiteralPatternConverter(
                                            currentLiteral.toString()));
                                }
                                currentLiteral.setLength(0);
                                currentLiteral.append(c); // append %
                                state = CONVERTER_STATE;
                                formattingInfo.reset();
                        }
                    } else {
                        currentLiteral.append(c);
                    }
                    break;
                case CONVERTER_STATE:
                    currentLiteral.append(c);
                    switch (c) {
                        case '-':
                            formattingInfo.leftAlign = true;
                            break;
                        case '.':
                            state = DOT_STATE;
                            break;
                        default:
                            if (c >= '0' && c <= '9') {
                                formattingInfo.min = c - '0';
                                state = MIN_STATE;
                            } else
                                finalizeConverter(c);
                    } // switch
                    break;
                case MIN_STATE:
                    currentLiteral.append(c);
                    if (c >= '0' && c <= '9')
                        formattingInfo.min = formattingInfo.min * 10 + (c - '0');
                    else if (c == '.')
                        state = DOT_STATE;
                    else {
                        finalizeConverter(c);
                    }
                    break;
                case DOT_STATE:
                    currentLiteral.append(c);
                    if (c >= '0' && c <= '9') {
                        formattingInfo.max = c - '0';
                        state = MAX_STATE;
                    } else {
                        logger.error("Error occured in position");
                        state = LITERAL_STATE;
                    }
                    break;
                case MAX_STATE:
                    currentLiteral.append(c);
                    if (c >= '0' && c <= '9')
                        formattingInfo.max = formattingInfo.max * 10 + (c - '0');
                    else {
                        finalizeConverter(c);
                        state = LITERAL_STATE;
                    }
                    break;
            } // switch
        } // while
        if (currentLiteral.length() != 0) {
            addToList(new LiteralPatternConverter(currentLiteral.toString()));
        }
        return head;
    }

    /**
     * 遇到特殊字符，生成转换器，并加入到转换器链表中
     *
     * @param c
     */
    protected void finalizeConverter(char c) {
        PatternConverter pc = null;
        switch (c) {
            case 'd': // 进行日期转换
                String dateFormatStr = extractOption();
                DateFormat dateFormat = null;
                try {
                    dateFormat = new SimpleDateFormat(dateFormatStr);
                } catch (IllegalArgumentException e) {
                    logger.error("date transform error");
                }
                pc = new DatePatternConverter(formattingInfo, dateFormat);
                currentLiteral.setLength(0);
                break;
            case 'r': // 随机数转换
                int length = extractPrecisionOption();
                pc = new RandomNoPatternConverter(formattingInfo, length);
                currentLiteral.setLength(0);
                break;
            case 's': // 序列号转换
                String sequenceName = extractOptions();
                pc = new SerialNoPatternConverter(formattingInfo, sequenceName, seqDao);
                currentLiteral.setLength(0);
                break;
            case 'c': // 常量转换
                String cst = extractOption();
                pc = new LiteralPatternConverter(cst);
                currentLiteral.setLength(0);
                break;
            case 'v': // 变量转换
                String key = extractOption();
                pc = new VariablePatternConverter(key);
                currentLiteral.setLength(0);
                break;
            default: // 其它的用文本转换
                pc = new LiteralPatternConverter(currentLiteral.toString());
                currentLiteral.setLength(0);
        }
        addConverter(pc);
    }

}
