package com.zdtech.platform.framework.utils;

import com.zdtech.platform.framework.entity.Seq;
import com.zdtech.platform.framework.repository.SeqDao;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by lyj on 2018/7/21.
 */
public class SeqNoUtils {


    private static final SeqDao seqDao = init();
    private static final SeqNoUtils seqNoUtils = new SeqNoUtils();

    public static String getSeqNo(String sid, int length){
        return seqNoUtils.getNextSeqNo(sid, length);
    }

    private static SeqDao init(){
        return SysUtils.getBean("seqDao", SeqDao.class);
    }

    //获取length长度的唯一数字字符串
    private synchronized static String getNextSeqNo(String sid, int length){
        long val = 0L;
        Seq seq = seqDao.findOne(sid);
        if(seq != null){
            val = seq.getNextval();
            if(String.valueOf(val+1).length() >= length){
                seq.setNextval(val+1);
            } else {
                seq.setNextval(val+1);
            }
            seqDao.save(seq);
        }
        String value = String.format("%0" + length + "d", val);
        return value;
    }
}
