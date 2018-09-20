package com.zdtech.platform.simserver.net.http;

import com.zdtech.platform.framework.entity.SysConf;
import com.zdtech.platform.framework.repository.SysConfDao;
import com.zdtech.platform.framework.utils.SpringContextUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HttpSynClient
 * 同步
 * @author panli
 * @date 2017/9/25
 */
public class HttpSynClient{
    private static Logger logger = LoggerFactory.getLogger(HttpSynClient.class);

    private static CloseableHttpClient httpClient = null;
    private final static Object syncLock = new Object();
    private static IdleMonitor idleMonitor;
    //池最大连接数
    private static Integer connMaxTotal = 1000;
    //每个路由的最大连接数
    private static Integer connmaxPerRoute = 1000;
    //从连接池中获取连接的超时时间
    private static Integer connReqTimeout = 15000;
    //建立连接的超时时间
    private static Integer connTimeout = 15000;
    //等待数据超时时间-大于等于业务超时时间
    private static Integer soTimeout = 400000;

    private static File ksFile;
    private static String filePath = "wl.keystore";

    public static String getKsPath(){
        URL url = HttpSynClient.class.getProtectionDomain().getCodeSource().getLocation();
        String path = null;
        try {
            path = java.net.URLDecoder.decode(url.getPath(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (path.endsWith(".jar"))
            path = path.substring(0, path.lastIndexOf("/") + 1);
        File file = new File(path);
        return file.getAbsolutePath();
    }

    public static CloseableHttpClient getHttpClient(){
        if (httpClient == null) {
            synchronized (syncLock) {
                if (httpClient == null) {
                    try{
                        ksFile = new File(getKsPath()+"/"+filePath);
                        logger.warn("======="+ksFile.getAbsolutePath());
                    } catch (Exception e){
                        logger.warn("找不到SSH加密文件!");
                    }

                    httpClient = createHttpClient(connMaxTotal, connmaxPerRoute,connReqTimeout, connTimeout,soTimeout);
                }
            }
        }
        return httpClient;
    }
    public static void close(){
        if (idleMonitor != null){
            idleMonitor.shutdown();
            idleMonitor = null;
        }

        if (httpClient != null){
            try {
                httpClient.close();
                httpClient = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static CloseableHttpClient createHttpClient(int maxTotal,
                                                       int maxPerRoute, int connReqTimeout, int connTimeout, int soTimeout) {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory
                .getSocketFactory();
        SSLContext context = null;
        try {
            context = SSLContexts.custom().loadTrustMaterial(ksFile,"1111111a".toCharArray(), new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            }).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            logger.error("sslcontex初始化错误：{}",e.getMessage());
        } catch (KeyStoreException e) {
            e.printStackTrace();
            logger.error("sslcontex初始化错误：{}",e.getMessage());
        } catch (KeyManagementException e) {
            e.printStackTrace();
            logger.error("sslcontex初始化错误：{}",e.getMessage());
        } catch (CertificateException e) {
            e.printStackTrace();
            logger.error("sslcontex初始化错误：{}",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            context.init(null,new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }},null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
            logger.error("sslcontex初始化错误：{}",e.getMessage());
        }
        LayeredConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(context, new String[] { "TLSv1","TLSv1.1","TLSv1.2" },null,new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        Registry<ConnectionSocketFactory> registry = RegistryBuilder
                .<ConnectionSocketFactory> create().register("http", plainsf)
                .register("https", sslsf).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);

        //池最大连接数
        cm.setMaxTotal(maxTotal);
        //每个路由的最大连接数
        cm.setDefaultMaxPerRoute(maxPerRoute);
        //销毁过期连接
        idleMonitor = new IdleMonitor(cm);
        idleMonitor.start();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connReqTimeout)//从连接池中获取连接的超时时间
                .setConnectTimeout(connTimeout)//建立连接的超时时间
                .setSocketTimeout(soTimeout)//等待数据超时时间
                .build();

        // 请求重试处理-禁止
        HttpRequestRetryHandler httpRequestRetryHandler = new DefaultHttpRequestRetryHandler(0, false);
        //若没有keep-alive参数
        ConnectionKeepAliveStrategy keepAliveStrategy = new ConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                BasicHeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator("Keep-Alive"));
                while(true) {
                    String param;
                    String value;
                    do {
                        do {
                            if(!it.hasNext()) {
                                // Keep alive for 5 seconds only
                                return 5 * 1000;
                                //return -1L;
                            }
                            HeaderElement he = it.nextElement();
                            param = he.getName();
                            value = he.getValue();
                        } while(value == null);
                    } while(!param.equalsIgnoreCase("timeout"));
                    try {
                        return Long.parseLong(value) * 1000L;
                    } catch (NumberFormatException var8) {
                        ;
                    }
                }
            }
        };

        CloseableHttpClient closeableHttpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setRetryHandler(httpRequestRetryHandler)
                .setKeepAliveStrategy(keepAliveStrategy)
                /*.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(1).build())*/
                .setDefaultRequestConfig(requestConfig).build();
        return closeableHttpClient;
    }

    public static String post(String url,String body, String dstInstgId){
        if(!body.contains("{S:")){
            body += "{S:aaabbbccadfjalsdkjflasdjfaskjdfhaksdhfalsdkjhfalskdfhalsdkfhalsdfhlaskdjhfc}";
        }
        Pattern p = Pattern.compile(">\\s*|\t|\r|\n");
        Matcher m = p.matcher(body);
        body = m.replaceAll(">");
        String result = "";
        CloseableHttpResponse response = null;
        try {
            logger.info("[调试]发送url:{}",url);
            CloseableHttpClient client = getHttpClient();
            HttpPost httppost = new HttpPost(url.trim());
            setPostParams(httppost,body, dstInstgId);
            logger.info("[调试]发送成功，发送报文：{}",body);
            response = client
                    .execute(httppost, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            logger.info("[调试]接收报文：{}",result);
        } catch (Exception e) {
            if (e instanceof SocketTimeoutException){
                result = "resptimeout";
            }else {
                result = "execerror";
            }
            e.printStackTrace();
            logger.error("[调试]发送失败,发送报文：{},url:{}",body,url);
            logger.error("错误原因：{}",e.getMessage());
            logger.error("错误堆栈：{}", ExceptionUtils.getFullStackTrace(e));
        } finally {
            try {
                if (response != null){
                    EntityUtils.consume(response.getEntity());
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String post(String url,String body, String msgCode, Map<String, String> params){
        String result = "";
        CloseableHttpResponse response = null;
        try {
            logger.info("[调试]发送url:{}",url);
            CloseableHttpClient client = getHttpClient();
            HttpPost httppost = new HttpPost(url.trim());
            setPostParams(httppost,body);
            logger.info("[调试]发送成功，发送报文：{}",body);
            response = client
                    .execute(httppost, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            logger.info("[调试]接收报文：{}",result);
            if("true".equals(params.get("nxy_is_sign")) && !XMLSignaturer.verifySignature(result, msgCode, params.get("public_key"))){
                logger.info("验签失败!");
            }
        } catch (Exception e) {
            if (e instanceof SocketTimeoutException){
                result = "resptimeout";
            }else {
                result = "execerror";
            }
            e.printStackTrace();
            logger.error("[调试]发送失败,发送报文：{},url:{}",body,url);
            logger.error("错误原因：{}",e.getMessage());
            logger.error("错误堆栈：{}", ExceptionUtils.getFullStackTrace(e));
        } finally {
            try {
                if (response != null){
                    EntityUtils.consume(response.getEntity());
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String get(String url){
        String result = "";
        CloseableHttpResponse response = null;
        try {
            HttpGet httpget = new HttpGet(url.trim());
            httpget.setHeader("Connection", "Keep-Alive");
            response = getHttpClient().execute(httpget,
                    HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (response != null){
                    EntityUtils.consume(response.getEntity());
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static void setPostParams(HttpPost httpost, String body){
        StringEntity stringEntity = new StringEntity(body, ContentType.create("application/xml", Charset.forName("utf-8")));

        httpost.addHeader("Connection","keep-alive");
        httpost.addHeader("Cache-Control", "no-cache");
        httpost.addHeader("Pragma", "no-cache");
        httpost.addHeader("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
        httpost.setEntity(stringEntity);
    }

    private static void setPostParams(HttpPost httpost,
                                      String body, String dstInstgId){
        StringEntity stringEntity = new StringEntity(body, ContentType.create("application/xml", Charset.forName("utf-8")));
        Pattern pattern = Pattern.compile("<MsgTp>(.*?)</MsgTp>");
        Matcher matcher = pattern.matcher(body);
        matcher.find();
        String msgTp = matcher.group(1);
        pattern = Pattern.compile("<IssrId>(.*?)</IssrId>");
        matcher = pattern.matcher(body);
        matcher.find();
        String issrId = matcher.group(1);

        httpost.addHeader("EpccExtField", String.format("{\"DstInstgId\" : \"%s\"}", dstInstgId));
        httpost.addHeader("MsgTp",msgTp);
        httpost.addHeader("OriIssrId",issrId);
        httpost.addHeader("Connection","keep-alive");
        httpost.addHeader("Cache-Control", "no-cache");
        httpost.addHeader("Pragma", "no-cache");
        httpost.addHeader("Accept", "text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
        httpost.setEntity(stringEntity);
    }
    private static void setPostParams(HttpPost httpost,
                                      Map<String, Object> params) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key).toString()));
        }
        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        String body = "<root>\n" +
                "    <MsgHeader>\n" +
                "        <SndDt>2017-09-16T15:01:21</SndDt>\n" +
                "        <MsgTp>epcc.101.001.01</MsgTp>\n" +
                "        <IssrId>Z2004944000010</IssrId>\n" +
                "        <Drctn>22</Drctn>\n" +
                "        <SignSN>4000520010</SignSN>\n" +
                "        <NcrptnSN>4000520011</NcrptnSN>\n" +
                "        <DgtlEnvlp></DgtlEnvlp>\n" +
                "    </MsgHeader>\n" +
                "    <MsgBody>\n" +
                "        <SgnInf>\n" +
                "            <SgnAcctIssrId>C1104412000011</SgnAcctIssrId>\n" +
                "            <SgnAcctTp>00</SgnAcctTp>\n" +
                "            <SgnAcctId></SgnAcctId>\n" +
                "            <SgnAcctNm></SgnAcctNm>\n" +
                "            <IDTp>01</IDTp>\n" +
                "            <IDNo></IDNo>\n" +
                "            <MobNo></MobNo>\n" +
                "        </SgnInf>\n" +
                "        <TrxInf>\n" +
                "            <TrxCtgy>0201</TrxCtgy>\n" +
                "            <TrxId>666666666666666</TrxId>\n" +
                "            <TrxDtTm>2017-09-16T20:47:18</TrxDtTm>\n" +
                "            <AuthMsg>123987</AuthMsg>\n" +
                "        </TrxInf>\n" +
                "        <InstgInf>\n" +
                "            <InstgId>Z2004944000010</InstgId>\n" +
                "            <InstgAcct>汉字</InstgAcct>\n" +
                "        </InstgInf>\n" +
                "    </MsgBody>\n" +
                "</root>";
        String s  = HttpSynClient.post("http://localhost:10005/preSvr",body, "C1104311000033");
        System.out.println(s);
    }
}
