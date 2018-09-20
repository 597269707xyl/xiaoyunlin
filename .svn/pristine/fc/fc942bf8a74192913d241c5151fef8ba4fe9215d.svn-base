package com.zdtech.platform.framework.utils;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * XmlDocHelper
 *
 * @author panli
 * @date 2016/9/7
 */
public class XmlDocHelper4wl {
    public static Document getXmlFromStr(String strXml){
        try {
            Document dom = DocumentHelper.parseText(strXml);
            return dom;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Document getXmlFromFile(String file){
        SAXReader reader = new SAXReader();
        reader.setIgnoreComments(true);
        reader.setStripWhitespaceText(true);
        try {
            return reader.read(new File(file));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNodeValue(Document doc, String xPath){
        if (doc == null || StringUtils.isEmpty(xPath))
            return "";
        String ns = doc.getRootElement().getNamespaceURI();
        if (StringUtils.isNotEmpty(ns)){
            HashMap map = new HashMap();
            map.put("temp",ns);

            xPath = getXMLNameSpaceFixed(xPath,"temp");
            XPath docXPath = doc.createXPath(xPath);
            docXPath.setNamespaceURIs(map);
            Node node = docXPath.selectSingleNode(doc);
            if (node!=null){
                return node.getText().trim();
            }
            return "";
        }
        Node node = doc.selectSingleNode(xPath);
        if (node!=null){
            return node.getText().trim();
        }
        return "";
    }
    public static String getXMLNameSpaceFixed(String xpath,String ns)
    {
        xpath= xpath.replaceAll("/(\\w)", "/"+ns+":$1");
        xpath= xpath.replaceAll("^(\\w)", ns+":$1");
        return xpath;
    }

    public static void setNodeValue(Document doc, String xPath, String value){
        if (doc == null || StringUtils.isEmpty(xPath) || value == null)
            return;
        String ns = doc.getRootElement().getNamespaceURI();
        if (StringUtils.isNotEmpty(ns)){
            HashMap map = new HashMap();
            map.put("temp",ns);

            xPath = getXMLNameSpaceFixed(xPath,"temp");
            XPath docXPath = doc.createXPath(xPath);
            docXPath.setNamespaceURIs(map);
            Node node = docXPath.selectSingleNode(doc);
            if (node!=null){
                node.setText(value);
            }
            return;
        }
        Node node = doc.selectSingleNode(xPath);
        if (node!=null){
            node.setText(value);
        }
    }
    public static String getNodeContent(Document doc, String xPath){
        if (doc == null || StringUtils.isEmpty(xPath))
            return null;
        Node node = doc.selectSingleNode(xPath);
        if (node!=null){
            return node.asXML();
        }
        return null;
    }
    public static void setNodeContent(Document doc, String xPath, String value){
        if (doc == null || StringUtils.isEmpty(xPath))
            return ;
        Node node = doc.selectSingleNode(xPath);
        if (node!=null){
            Element element = (Element)node;

            element.add(XmlDocHelper4wl.getXmlFromStr(value).getRootElement());
        }
    }

    public static void setNodeElements(Document doc, String xPath, List<Element> elements){
        if (doc == null || StringUtils.isEmpty(xPath))
            return ;
        Node node = doc.selectSingleNode(xPath);
        if (node!=null){
            for (Element element:elements){
                ((Element)node).add(element);
            }
        }
    }
    public static Element getNodeElement(Document doc, String xPath){
        if (doc == null || StringUtils.isEmpty(xPath))
            return null;
        Node node = doc.selectSingleNode(xPath);
        return ((Element)node).createCopy();
    }

    public static void setNodeElement(Document doc, String xPath, Element element){
        if (doc == null || StringUtils.isEmpty(xPath))
            return ;
        Node node = doc.selectSingleNode(xPath);
        if (node!=null){
            ((Element)node).add(element);
        }
    }
    public static List<Element> getNodeElements1(Document doc, String xPath) {
        if (doc == null || StringUtils.isEmpty(xPath))
            return null;
        List list = doc.selectNodes(xPath);
        return list;
    }
    public static void setCDATA(Document doc, String xPath, String orgMsg) {
        if (doc == null || StringUtils.isEmpty(xPath))
            return ;
        Node node = doc.selectSingleNode(xPath);
        if (node!=null){
            ((Element)node).addCDATA(orgMsg);
        }
    }

    public static List<Element> getNodeElements(Document doc, String xPath) {
        if (doc == null || StringUtils.isEmpty(xPath))
            return null;
        String ns = doc.getRootElement().getNamespaceURI();
        if (StringUtils.isNotEmpty(ns)){
            HashMap map = new HashMap();
            map.put("temp",ns);

            xPath = getXMLNameSpaceFixed(xPath,"temp");
            XPath docXPath = doc.createXPath(xPath);
            docXPath.setNamespaceURIs(map);
            return docXPath.selectNodes(doc);
        }
        List list = doc.selectNodes(xPath);
        return list;
    }

    public static void main(String[] args){
        String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root xmlns=\"epcc.211.001.01\">\n" +
                /*"<root>\n" +*/
                "\t<!--报文头-->\n" +
                "\t<MsgHeader>\n" +
                "\t\t<SndDt>2017-07-24T23:59:12</SndDt>\n" +
                "\t\t<MsgTp>epcc.211.001.01</MsgTp>\n" +
                "\t\t<IssrId>G4000311000018</IssrId>\n" +
                "\t\t<Drctn>22</Drctn>\n" +
                "\t\t<SignSN>4000372804</SignSN>\n" +
                "\t\t<NcrptnSN>4000068829</NcrptnSN>\n" +
                "\t\t<DgtlEnvlp></DgtlEnvlp>\n" +
                "\t</MsgHeader>\n" +
                "\t<!--报文体-->\n" +
                "\t<MsgBody>\n" +
                "\t\t<PyerAcctInf>\n" +
                "\t\t\t<PyerBkId>C1010511003703</PyerBkId>\n" +
                "\t\t\t<PyerBkNo>1001278619005510977</PyerBkNo>\n" +
                "\t\t\t<PyerBkNm>客户备付金</PyerBkNm>\n" +
                "\t\t</PyerAcctInf>\n" +
                "\t\t<PyerInf>\n" +
                "\t\t\t<PyerAcctIssrId>Z2007933000010</PyerAcctIssrId>\n" +
                "\t\t\t<PyerAcctTp>04</PyerAcctTp>\n" +
                "\t\t\t<PyerAcctNo>282704424720582705</PyerAcctNo>\n" +
                "\t\t\t<PyeeAcctNm>kerwing@cong.x</PyeeAcctNm>\n" +
                "\t\t\t<PyerMrchntNo/>\n" +
                "\t\t\t<PyerMrchntNm/>\n" +
                "\t\t\t<PyerMrchntShrtNm/>\n" +
                "\t\t</PyerInf>\n" +
                "\t\t<PyeeAcctInf>\n" +
                "\t\t\t<PyeeAcctTp>00</PyeeAcctTp>\n" +
                "\t\t\t<PyeeBkId>C1010411000013</PyeeBkId>\n" +
                "\t\t\t<PyeeSgnNo/>\n" +
                "\t\t\t<PyeeBkNo>6222020200102591212</PyeeBkNo>\n" +
                "\t\t\t<PyeeBkNm>李周吴</PyeeBkNm>\n" +
                "\t\t</PyeeAcctInf>\n" +
                "\t\t<TrxInf>\n" +
                "\t\t\t<RPFlg>1</RPFlg>\n" +
                "\t\t\t<TrxCtgy>0120</TrxCtgy>\n" +
                "\t\t\t<BizTp>130002</BizTp>\n" +
                "\t\t\t<TrxId>2017051712345678901234560010208</TrxId>\n" +
                "\t\t\t<TrxDtTm>2017-05-19T01:18:23</TrxDtTm>\n" +
                "\t\t\t<TrxAmt>CNY175.19</TrxAmt>\n" +
                "\t\t\t<BatchId>B201705190001</BatchId>\n" +
                "\t\t\t<TrxSmmry>收入-工资</TrxSmmry>\n" +
                "\t\t    <TrxPrps>0001</TrxPrps>\n" +
                "\t\t\t<TrxTrmTp>01</TrxTrmTp>\n" +
                "\t\t\t<TrxTrmNo>718868315366</TrxTrmNo>\n" +
                "\t\t\t<TrxDevcInf>125.39.23.1||399540335204406|||B4D3E2F00102||</TrxDevcInf>\n" +
                "\t\t</TrxInf>\n" +
                "\t</MsgBody>\n" +
                "</root>";
        Document doc = XmlDocHelper4wl.getXmlFromStr(s);
        List<Element> list = XmlDocHelper4wl.getNodeElements(doc, "aa");
        System.out.println(list);
//        System.out.println(XmlDocHelper4wl.getNodeValue(doc,"/root/MsgHeader/MsgTp"));
//        System.out.println(XmlDocHelper4wl.getNodeValue(doc,"root/MsgHeader/MsgTp"));
//        System.out.println(XmlDocHelper4wl.getNodeValue(doc,"//MsgHeader/MsgTp"));
//
//        XmlDocHelper4wl.setNodeValue(doc,"/root/MsgHeader/MsgTp","abc");
//        System.out.println(XmlDocHelper4wl.getNodeValue(doc,"/root/MsgHeader/MsgTp"));
//        System.out.println(XmlDocHelper4wl.getNodeValue(doc,"root/MsgHeader/MsgTp"));
//        System.out.println(XmlDocHelper4wl.getNodeValue(doc,"//MsgHeader/MsgTp"));
//
//        XmlDocHelper4wl.setNodeValue(doc,"root/MsgHeader/MsgTp","def");
//        System.out.println(XmlDocHelper4wl.getNodeValue(doc,"/root/MsgHeader/MsgTp"));
//        System.out.println(XmlDocHelper4wl.getNodeValue(doc,"root/MsgHeader/MsgTp"));
//        System.out.println(XmlDocHelper4wl.getNodeValue(doc,"//MsgHeader/MsgTp"));
//
//        XmlDocHelper4wl.setNodeValue(doc,"//MsgHeader/MsgTp","ghi");
//        System.out.println(XmlDocHelper4wl.getNodeValue(doc,"/root/MsgHeader/MsgTp"));
//        System.out.println(XmlDocHelper4wl.getNodeValue(doc,"root/MsgHeader/MsgTp"));
//        System.out.println(XmlDocHelper4wl.getNodeValue(doc,"//MsgHeader/MsgTp"));
    }



}
