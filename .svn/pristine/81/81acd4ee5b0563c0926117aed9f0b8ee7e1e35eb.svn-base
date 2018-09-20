package com.zdtech.platform.simserver.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.union.api.TUnionTransInfo;
import com.union.api.UnionEsscAPI;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 加密、解密
 */
public class PinUtil {

    private static final Log logger = LogFactory.getLog(PinUtil.class);
    private static Properties prop = new Properties();

    static {
        InputStream in = PinUtil.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("获取加密配置错误!");
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param bankid 加密行号
     * @param passwd PIN明文  最大长度为12
     * @param acctno 账号/卡号
     * @return
     * @throws Exception
     */
    public static String UnionEncryptPin(String bankid, String passwd, String acctno) {
        if (passwd == null || acctno == null) {
            return passwd;
        }
        passwd = passwd.trim();
        if(passwd.length() > 12 || !isNum(passwd)){
            logger.info("加密明文太长或者明文不是数字，最大长度为12位数字！");
            return passwd;
        }
        String loginfo;
        String pinHost = prop.getProperty("pinHost");
        String pinPort = prop.getProperty("pinPort");
        Integer pinTimeOut = Integer.parseInt(prop.getProperty("pinTimeOut"));
        String pinSys = prop.getProperty("pinSys");
        String pinZpk = prop.getProperty("pinZpk");
        String pinFlag = prop.getProperty("pinFlag");

        if(!"true".equals(pinFlag)){
            return passwd;
        }

        if(null == pinHost || "".equals(pinHost)){
            return "";
        }

        /**
         * ESSC API()
         * String UnionEncryptPin(String fullKeyName, String clearPin,String accNo)
         * pinCyperLen：PIN密文长度
         * clearPin：PIN明文
         * accNo：账号/卡号,长度13-19字节字符串(ASCII)
         */
        String encrypted = passwd;

        List<String> ips   = new ArrayList<String>();
        List<Integer> ports = new ArrayList<Integer>();

        ips.add(0, pinHost);
        ports.add(0, Integer.parseInt(pinPort));

        UnionEsscAPI unionEsscAPI = new UnionEsscAPI(ips, ports, pinTimeOut, pinSys, pinSys);

        try {
            String apiAcctno = getAccNo(acctno);
            pinZpk  = pinZpk.replace("${bankid}", bankid);
            loginfo = String.format("****acctno:%s", acctno); logger.info(loginfo);
            loginfo = String.format("****guomo:pinZpk=%s, passwd=%s, apiAcctno=%s", pinZpk, passwd, apiAcctno); logger.info(loginfo);

            TUnionTransInfo transInfo = unionEsscAPI.UnionEncryptPin(pinZpk, passwd, apiAcctno);

            int isSuccess  = transInfo.getIsSuccess();
            int respCode   = transInfo.getResponseCode();
            String respRmk = transInfo.getResponseRemark();

            loginfo = String.format("****guomo return: isSuccess=%d, respCode=%d, respRmk=%s", isSuccess, respCode, respRmk); logger.info(loginfo);

            if (respCode != 0 || isSuccess != 1) {
                logger.error("****guomo failed.");
                return encrypted;
            }

            encrypted = transInfo.getReturnBody().getPinBlock();
            loginfo = String.format("****guomo encrypted=%s", encrypted); logger.info(loginfo);
            return encrypted;

        } catch (Exception e) {
            e.printStackTrace();
            loginfo = String.format("****guomo failed,%s,return plaintext", e.getMessage()); logger.error(loginfo);
            return encrypted;
        }
    }

    private static boolean isNum(String str){
        if(StringUtils.isNotEmpty(str)){
            boolean flag = true;
            char[] cs = str.toCharArray();
            for(char c : cs){
                if(c < 48 || c > 57){
                    flag = false;
                    break;
                }
            }
            return flag;
        }
        return false;
    }

    /**
     * 调用加密服务器时传输的13位账户
     * 处理规则：不足13位，后补0；超过13位，截取后13位
     * @param accNo 卡号/账号
     * @return 返回13位账户
     */
    public static String getAccNo(String accNo){
        String result = accNo;
        if(accNo.length()==13){
        }else if(accNo.length()<13){
            for(int i=accNo.length();i<13;i++){
                result += "0";
            }
        }else{
            result = accNo.substring(accNo.length()-13);
        }

        return result;
    }

    public static void main(String arg[]) throws JsonProcessingException {
        String acctno = "623519533000160211";
        String bankid = "402041000011";
        String passwd = "6235390010002692286";
        String pinHost = prop.getProperty("pinHost");
        String pinPort = prop.getProperty("pinPort");
        Integer pinTimeOut = Integer.parseInt(prop.getProperty("pinTimeOut"));
        String pinSys = prop.getProperty("pinSys");
        String pinZpk = prop.getProperty("pinZpk");
        List<String> ips   = new ArrayList<String>();
        List<Integer> ports = new ArrayList<Integer>();

        ips.add(0, pinHost);
        ports.add(0, Integer.parseInt(pinPort));

        UnionEsscAPI unionEsscAPI = new UnionEsscAPI(ips, ports, pinTimeOut, pinSys, pinSys);
        String apiAcctno = getAccNo(acctno);
        String encrypted = "";
        pinZpk  = pinZpk.replace("${bankid}", bankid);

        TUnionTransInfo transInfo = unionEsscAPI.UnionEncryptPin(pinZpk, passwd, apiAcctno);
        System.out.println(new ObjectMapper().writeValueAsString(transInfo));
        int isSuccess  = transInfo.getIsSuccess();
        int respCode   = transInfo.getResponseCode();
        String respRmk = transInfo.getResponseRemark();
        System.out.println(respRmk);

        if (respCode != 0 || isSuccess != 1) {
            System.out.println("****guomo failed.");
        }

        encrypted = transInfo.getReturnBody().getPinBlock();
        System.out.println("666666666666"+encrypted);
    }

}
