package com.zdtech.platform.simserver.net.http;

import cn.com.infosec.netsign.agent.PBCAgent2G;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * MsgSecurityHelper
 *
 * @author panli
 * @date 2017/11/14
 */
public class MsgSecurityHelper {
    private static Logger log = LoggerFactory.getLogger(MsgSecurityHelper.class);

    public static String sign(String xml, String ip, Integer port, String pwd, String dn, String alg) {
        String result = "";
        PBCAgent2G agent = new PBCAgent2G();
        try {
            boolean ok = agent.openSignServer(ip,port,pwd);
            if (!ok) {
                log.error("加签服务器连接失败:" + agent.getReturnCode());
            } else {
                log.info("加签服务器连接成功:" + agent.getReturnCode());
            }
            //去掉xml前面的内容，从root开始加签
            xml = xml.substring(xml.lastIndexOf("<root"));
            byte[] origBytes = xml.getBytes("UTF-8");
            log.info("加签之前xml:" + xml);
            result = agent.rawSign(origBytes, dn, alg);
            if (result == null)
                log.error("加签失败: " + agent.getReturnCode());
            else {
                log.info("加签成功: " + agent.getReturnCode());
                log.info("加签结果: " + result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("加签异常：{}",e.getMessage());
        } finally {
            // 关闭签名服务连接
            try {
                agent.closeSignServer();
            } catch (Exception e) {
                e.printStackTrace();
                log.error("关闭签名服务异常：{}",e.getMessage());
            }
        }
        return result;
    }

    public static List<String> encrypt(List<String> strs, String ip, Integer port, String pwd, String dn, String alg) {
        List<String> result = new ArrayList<>();
        if (strs.size() < 1){
            return result;
        }
        // 构造agent对象,连接签名服务器
        PBCAgent2G agent = new PBCAgent2G();
        try {
            boolean ok = agent.openSignServer(ip,port,pwd);
            if (!ok) {
                log.error("加签服务器连接失败:" + agent.getReturnCode());
            } else {
                log.info("加签服务器连接成功:" + agent.getReturnCode());
            }
            log.info("加密域个数：{}，第一个：{}",strs.size(),strs.get(0));
            byte[][] plains = new byte[strs.size()][];
            for(int i=0;i<strs.size();i++){
                byte[] plain = strs.get(i).getBytes("UTF-8");
                plains[i] = plain;
            }
            // 网联加密
            String[] envelope = null;
            agent.makeWangLianEnvelope(plains, dn, alg);
            if (envelope == null) {
                log.error("加密失败");
                log.error("失败返回码:" + agent.getReturnCode());
            }else{
                for (String s:envelope){
                    result.add(s);
                }
                log.info("加密成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("加密失败："+e.getMessage());
        } finally {
            // 关闭签名服务连接
            try {
                agent.closeSignServer();
            } catch (Exception e) {
                log.error("关闭签名服务连接失败："+e.getMessage());
                e.printStackTrace();
            }
        }
        return result;
    }
}
