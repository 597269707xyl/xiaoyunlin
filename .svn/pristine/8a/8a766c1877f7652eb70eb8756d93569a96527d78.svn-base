package com.zdtech.platform.simserver.net.http;

import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.nio.bootstrap.HttpServer;
import org.apache.http.impl.nio.bootstrap.ServerBootstrap;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.nio.protocol.*;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

/**
 * TestServer
 *
 * @author panli
 * @date 2017/11/14
 */
public class TestServer {
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException, CertificateException, KeyStoreException, IOException {

        IOReactorConfig config = IOReactorConfig.custom()
                .setSoTimeout(15000)
                .setTcpNoDelay(true)
                .build();
        String localUrl = "http://127.0.0.1:8081/test";
        URL url = null;
        InetAddress localAddress = null;
        try {
            url = new URL(localUrl);
            localAddress = InetAddress.getByName(url.getHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        int port = url.getPort();
        String ctx = url.getPath();
        SSLContext sslContext = null;
        /*try {
            sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            }).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        sslContext = SSLContexts.custom().loadKeyMaterial(new File("E:/test.keystore"),"123456".toCharArray(),"123456".toCharArray()).build();*/
        HttpServer httpServer = ServerBootstrap.bootstrap()
                //.setSslContext(sslContext)
                .setIOReactorConfig(config)
                .setLocalAddress(localAddress)
                .setListenerPort(port)
                .setConnectionReuseStrategy(new DefaultConnectionReuseStrategy())
                .registerHandler(ctx,new HttpAsyncRequestHandler<HttpRequest>(){
                    @Override
                    public HttpAsyncRequestConsumer<HttpRequest> processRequest(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
                        return new BasicAsyncRequestConsumer();
                    }

                    @Override
                    public void handle(HttpRequest httpRequest, HttpAsyncExchange httpAsyncExchange, HttpContext httpContext) throws HttpException, IOException {
                        final HttpResponse response = httpAsyncExchange.getResponse();
                        handleInternal(httpRequest, response, httpContext);
                        httpAsyncExchange.submitResponse(new BasicAsyncResponseProducer(response));
                    }
                })
                .create();
        try {
            httpServer.start();
            httpServer.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private static void handleInternal(
            final HttpRequest request,
            final HttpResponse response,
            final HttpContext context) throws HttpException, IOException {
        HttpEntity httpEntity = ((HttpEntityEnclosingRequest)request).getEntity();
        InputStream inputStream = httpEntity.getContent();
        String msg = IOUtils.toString(inputStream,"utf-8");
        System.out.println("收到报文："+msg);
        response.setStatusCode(HttpStatus.SC_OK);
        String reply = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root><MsgHeader><SndDt>2017-07-24T23:59:12</SndDt><MsgTp>epcc.402.001.01</MsgTp><IssrId>G4000311000018</IssrId><Drctn>22</Drctn><SignSN/><NcrptnSN></NcrptnSN><DgtlEnvlp></DgtlEnvlp></MsgHeader><MsgBody><InstgId>C1030844001362</InstgId></MsgBody></root>{S:null}\n";
        NStringEntity entity = new NStringEntity(
                reply,
                ContentType.create("application/xml", "utf-8"));
        /*try {
            Thread.sleep(40000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        response.setEntity(entity);
        System.out.println("应答报文："+reply);
    }

}
