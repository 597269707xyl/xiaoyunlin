package com.zdtech.platform.simserver.net.http;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.security.*;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collections;
import java.util.Enumeration;

/**
 * 支付宝或者财付通加签
 */
public class XMLSignaturer {
    private static  String path = "/atsp/config/key/";
//    private static  String path = "D:/workspace/Simulator-zfb/config/key/";
    private static  Logger log = Logger.getLogger(XMLSignaturer.class);

    /**
     * NSPS签名验证
     * @param docStr
     */
    public static boolean verifySignature(String docStr, String msgTyp, String publickey) {
        try{
            if(StringUtils.isEmpty(publickey)){
                return false;
            }
            String publicKeyPath = path + publickey;
            FileInputStream inputStream = new FileInputStream(publicKeyPath);
            PublicKey publicKey = null;
            boolean valid = false;
            Document xml = DocumentHelper.parseText(docStr);
            String rootTag = xml.getRootElement().getName();
            if( "Tenpay".equals(rootTag) && ( "SWDReq".equals(msgTyp) || "SQReq".equals(msgTyp) )){
                String corpPubKey = readFile(publicKeyPath);
                byte[] publicKeyBytes = new Base64().decodeBase64(corpPubKey);
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                publicKey = keyFactory.generatePublic(keySpec);

                String msg = xml.selectSingleNode("/Tenpay/Message").asXML();
                String sign = xml.selectSingleNode("/Tenpay/Signature").getText();
                Signature signature = Signature.getInstance("MD5WithRSA");
                signature.initVerify(publicKey);
                signature.update( msg.getBytes());
                return signature.verify(new Base64().decodeBase64(sign) );
            }else{
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                Certificate certificate = cf.generateCertificate(inputStream);
                publicKey = (certificate != null ? certificate.getPublicKey() : null);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setNamespaceAware(true);
                org.w3c.dom.Document doc = dbf.newDocumentBuilder().parse(new InputSource(new ByteArrayInputStream(docStr.getBytes("UTF-8"))));

                NodeList nl = doc.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature");
                if (nl.getLength() == 0) {
                    log.debug("验证签名失败！");
                    return false;
                }
                Node signatureNode = nl.item(0);

                XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
                XMLSignature signature = fac.unmarshalXMLSignature(new DOMStructure(signatureNode));

                DOMValidateContext valCtx = new DOMValidateContext(publicKey, signatureNode);
                Element element = (Element)doc.getElementsByTagName("Message").item(0).getChildNodes().item(0);
                valCtx.setIdAttributeNS(element, null, "id");
                return signature.validate(valCtx);
            }
        }catch(Exception e){
            e.printStackTrace();
            log.debug("验证签名失败！");
        }
        return false;
    }

    /**
     * NSPS签名生成  加签
     * @param xmlResNoSign
     */
    public static String sign( String xmlResNoSign, String msgTyp, String privatekey, String password) {
        String xmlResWithSign = null;
        try{
            String privateKeyPath = path + privatekey;
            String priKeyPwdNoCry = password;

            XMLSignaturer nss = new XMLSignaturer();

            PrivateKey privateKey = null;
            Document doc = DocumentHelper.parseText(xmlResNoSign);
            String rootTag = doc.getRootElement().getName();
            if( "Tenpay".equals(rootTag) && ( "SWDReq".equals(msgTyp) || "SQReq".equals(msgTyp) )){
                if(privateKeyPath.endsWith("txt")){
                    String tenPrivateKey = readFile(privateKeyPath);
                    byte[] privateKeyBytes = Base64.decodeBase64(tenPrivateKey);
                    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                    privateKey = keyFactory.generatePrivate(keySpec);
                }else{
                    privateKey = nss.readPrivateKeyfromPKCS12StoredFile(privateKeyPath, priKeyPwdNoCry);
                }
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                String signResult = null;
                doc = DocumentHelper.parseText(xmlResNoSign);
                String msgSign = doc.selectSingleNode("/Tenpay/Message").asXML();
                try {
                    Signature signature = Signature.getInstance("MD5withRSA");
                    signature.initSign(privateKey);
                    signature.update(msgSign.getBytes());
                    signResult = new Base64().encodeAsString(signature.sign());
                    doc.selectSingleNode("/Tenpay/Signature").setText(signResult);
                    return doc.asXML();

                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("错误信息: " + e.getMessage());
                } finally {
                    try {
                        outputStream.close();
                    } catch (IOException localIOException2) {
                    }
                }
                return doc.asXML();
            }else{
                privateKey = nss.readPrivateKeyfromPKCS12StoredFile(privateKeyPath, priKeyPwdNoCry);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try
                {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlResNoSign.getBytes("UTF-8"));

                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setNamespaceAware(true);
                    org.w3c.dom.Document document = dbf.newDocumentBuilder().parse(inputStream);
                    StreamResult streamResult = new StreamResult(outputStream);
                    NodeList messageNodeList = document.getDocumentElement().getChildNodes();
                    for (int index = 0; index < messageNodeList.getLength(); index++)
                    {
                        Node messageNode = messageNodeList.item(index);
                        if (messageNode.getNodeType() == 1)
                        {
                            if (!"Message".equals(messageNode.getLocalName())) {
                                break;
                            }
                            XMLSignatureFactory factory = XMLSignatureFactory.getInstance("DOM");
                            DOMSignContext domSignContext = new DOMSignContext(privateKey, messageNode);
                            Transform envelopedTransform = factory.newTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature", (XMLStructure)null);
                            DigestMethod digestMethod = factory.newDigestMethod("http://www.w3.org/2000/09/xmldsig#sha1", null);
                            String msgXml = getReferenceURI(messageNode);

                            String defaultNamespacePrefix= null;

                            if(document.getDocumentElement().getTagName().equals("Finance")){
                                defaultNamespacePrefix = "ds";
                                domSignContext.setDefaultNamespacePrefix(defaultNamespacePrefix);
                            }
                            Element element = (Element)document.getElementsByTagName(msgXml.substring(1)).item(0);
                            domSignContext.setIdAttributeNS(element, null, "id");

                            Reference ref = factory.newReference(msgXml, digestMethod, Collections.singletonList(envelopedTransform), null,	null);
                            CanonicalizationMethod canonicalizationMethod = factory.newCanonicalizationMethod("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", (C14NMethodParameterSpec)null);
                            SignatureMethod signatureMethod = factory.newSignatureMethod("http://www.w3.org/2000/09/xmldsig#rsa-sha1", null);
                            SignedInfo si = factory.newSignedInfo(canonicalizationMethod, signatureMethod, Collections.singletonList(ref));
                            XMLSignature signature = factory.newXMLSignature(si, null);
                            signature.sign(domSignContext);
                        }
                    }
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty("omit-xml-declaration", "yes");
                    transformer.transform(new DOMSource(document), streamResult);
                    String outputStr = outputStream.toString("UTF-8");
                    String signString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+outputStr;
                    return signString;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    log.error("签名后生成XML错误");
                }
                finally
                {
                    try
                    {
                        outputStream.close();
                    }
                    catch (IOException localIOException2) {}
                }
            }
        }catch(Exception me){
            me.printStackTrace();
            log.error("消息签名失败："+xmlResNoSign);
            return null;
        }
        return xmlResWithSign;
    }

    private PrivateKey readPrivateKeyfromPKCS12StoredFile(String resourceName, String password)
            throws Exception {
        InputStream istream = new FileInputStream(resourceName);
        KeyStore keystore;
        String alias;
        try {
            keystore = KeyStore.getInstance("PKCS12");
            keystore.load(istream, password.toCharArray());
            Enumeration enumeration = keystore.aliases();
            alias = null;
            for (int i = 0; enumeration.hasMoreElements(); i++) {
                alias = enumeration.nextElement().toString();
                if (i >= 1) {
                    log.warn("此文件中含有多个证书!");
                }
            }
            return (PrivateKey) keystore.getKey(alias, password.toCharArray());
        } finally {
            if (istream != null) {
                istream.close();
            }
        }
    }

    public static String readFile(String file) {
        String data = "";
        try {
            InputStreamReader fr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            BufferedReader br = new BufferedReader(fr);
            String buff = "";
            while ((buff = br.readLine()) != null) {
                data = data + buff + "\n";
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    private static String getReferenceURI(Node messageNode)
    {
        NodeList childNodes = messageNode.getChildNodes();
        if ((childNodes == null) || (childNodes.getLength() == 0)) {
            return null;
        }
        Node packetNode = null;
        int elementCount = 0;
        for (int index = 0; index < childNodes.getLength(); index++)
        {
            Node currentNode = childNodes.item(index);
            if (currentNode.getNodeType() == 1)
            {
                packetNode = currentNode;
                elementCount++;
            }
        }
        if (packetNode != null) {}
        String nodeId = getNodeIdAttribute(packetNode);
        return String.format("#%s", new Object[] { nodeId });
    }

    private static String getNodeIdAttribute(Node node)
    {
        NamedNodeMap attributes = node.getAttributes();
        if ((attributes == null) || (attributes.getLength() == 0)) {
            return null;
        }
        Node idNode = attributes.getNamedItem("id");
        if ((attributes == null) || (attributes.getLength() == 0)) {
            return null;
        }
        String idValue = idNode.getTextContent();
        if ((idValue == null) || (idValue.isEmpty())) {
            return null;
        }
        return idValue;
    }
}
