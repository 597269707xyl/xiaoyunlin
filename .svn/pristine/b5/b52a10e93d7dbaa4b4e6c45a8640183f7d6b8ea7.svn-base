package com.zdtech.platform.utils.reflect;


import com.zdtech.platform.framework.entity.SimSysinsConf;
import com.zdtech.platform.framework.repository.SimSysinsConfDao;
import com.zdtech.platform.framework.repository.SysAdapterConfDao;
import com.zdtech.platform.framework.service.UcodeService;
import com.zdtech.platform.framework.utils.Bundle;
import com.zdtech.platform.framework.utils.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
        Bundle bundle = new Bundle();
        bundle.putString("nxy_seqnb","nxy_seqnb");
        UcodeService ucodeService = SpringContextUtils.getBean(UcodeService.class);
        String no = ucodeService.format("%8s{{%v{nxy_seqnb}}}",bundle);
        String date = date("");
        return date + "0000" + no;
    }

    public String nextMsgid(String s){
        Bundle bundle = new Bundle();
        bundle.putString("nxy_msgid","nxy_msgid");
        UcodeService ucodeService = SpringContextUtils.getBean(UcodeService.class);
        SysAdapterConfDao dao = SpringContextUtils.getBean(SysAdapterConfDao.class);
        String no = ucodeService.format("%8s{{%v{nxy_msgid}}}",bundle);
        String date = "";
        if(s != null && s.startsWith("adapterId:")){
            Long insId = Long.parseLong(s.substring(10));
            String sysDate = dao.findByInsIdAndConf(insId, "nxy_sys_date");
            String mqNo = dao.findByInsIdAndConf(insId, "nxy_mq_no");
            if(StringUtils.isNotEmpty(sysDate)){
                date = sysDate;
            } else {
                date = date("");
            }
            if(StringUtils.isNotEmpty(mqNo)){
                return date + mqNo + no;
            }
        }
        if (StringUtils.isEmpty(date)){
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

    private static AtomicInteger adder = new  AtomicInteger(10000);
    private static Set<String> set = new HashSet<String>();;
    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(500);
        for(int i=0; i<500; i++){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0; i<100; i++){
                        set.add(seqNo());
                        set.add(msgId());
                    }
                    latch.countDown();
                }
            });
            thread.start();
        }
        latch.await();
        System.out.println(set.size());
        System.out.println(set.toString());
        for(String s : set){
            if(s.length()>20){
                System.out.println(s);
            }
        }
    }
    private final static Object syncLock = new Object();
    public static String seqNo(){
        synchronized (adder){
            String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String no = String.valueOf(System.currentTimeMillis()).substring(5,10) + adder.incrementAndGet();
            return date + no;
        }
    }

    public static String msgId(){
        synchronized (adder){
            String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String no = String.valueOf(System.currentTimeMillis()).substring(5,10) + adder.incrementAndGet();
            return date + "00" + no;
        }
    }
}
