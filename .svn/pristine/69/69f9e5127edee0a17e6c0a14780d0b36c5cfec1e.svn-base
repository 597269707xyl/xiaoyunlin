package com.zdtech.platform.simserver.utils.reflect;


import com.zdtech.platform.framework.entity.SimSysinsConf;
import com.zdtech.platform.framework.repository.SimSysinsConfDao;
import com.zdtech.platform.framework.service.FieldCacheService;
import com.zdtech.platform.framework.service.UcodeService;
import com.zdtech.platform.framework.utils.Bundle;
import com.zdtech.platform.framework.utils.SeqNoUtils;
import com.zdtech.platform.framework.utils.SpringContextUtils;
import com.zdtech.platform.simserver.service.WLSimService;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * InvokedClass
 *
 * @author panli
 * @date 2016/6/14
 */
public class InvokedClass {

    public String date(String id){
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        result = sdf.format(new Date());
        return result;
    }

    public String isoDate(String id){
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        result = sdf.format(new Date());
        return result;
    }

    public String dateTime(String id){
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH:mm:ss");
        Date date = new Date();
        result = sdf.format(date);
        return result;
    }

    public String dateTime1(String id){
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        result = sdf.format(date);
        return result;
    }

    public String dateTime2(String id){
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        result = sdf.format(date);
        result += "T";
        sdf = new SimpleDateFormat("HH:mm:ss");
        result += sdf.format(date);
        return result;
    }

    public String dateTime3(String id){
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        result = sdf.format(date);
        result += " ";
        sdf = new SimpleDateFormat("HH:mm:ss");
        result += sdf.format(date);
        return result;
    }

    public String time(String id){
        String result = "";
        SimpleDateFormat sdf =  new SimpleDateFormat("HHmmss");
        result = sdf.format(new Date());
        return result;
    }

    public String time1(String id){
        String result = "";
        SimpleDateFormat sdf =  new SimpleDateFormat("HH:mm:ss");
        result = sdf.format(new Date());
        return result;
    }

    public String nextSeqNb(String s){
        String no = SeqNoUtils.getSeqNo("nxy_seqnb", 8);
        String date = date("");
        return date + "0000" + no;
    }

    public String nextMsgid(String s) {
        String no = SeqNoUtils.getSeqNo("nxy_msgid", 8);
        String date = "";
        if(s != null && s.startsWith("adapterId:")){
            String insId = s.substring(10);
            String sysDate = WLSimService.simInsMap.get(insId + "nxy_sys_date");
            String mqNo = WLSimService.simInsMap.get(insId + "nxy_mq_no");
            if(StringUtils.isNotEmpty(sysDate)){
                date = sysDate;
            } else {
                date = date("");
            }
            if(StringUtils.isNotEmpty(mqNo)){
                return date + mqNo + no;
            } else{
                return date + "00" + no;
            }
        }
        if (StringUtils.isEmpty(date)) {
            date = date("");
        }
        //65 66 67 78
        return date + "67" + no;
    }

    private static String getNextNo(String old){
        synchronized (InvokedClass.class){
            String ret = "";
            Long l = Long.parseLong(old);
            l = l + 1;
            int lenOld = old.length();
            ret = l.toString();
            if (ret.length() >= lenOld){
                return ret;
            }
            ret = StringUtils.leftPad(ret,lenOld,'0');
            return ret;
        }
    }
}