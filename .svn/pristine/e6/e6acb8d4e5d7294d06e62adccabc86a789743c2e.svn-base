package com.zdtech.platform.framework.utils;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * xml文件转换器
 * Created by lyj on 2018/1/12.
 */
public class XMLConverter {

    /**
     * Xml字符串转换为Document，并且是以组装的方式
     * @param strXml
     * @return
     */
    public static Document getXmlFromStrByAssemble(String strXml){
        try {
            Document dom = DocumentHelper.parseText(strXml);
            assemble(dom.getRootElement());
            return dom;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Xml字符串转换为Document，并且是以还原的方式
     * @param strXml
     * @return
     */
    public static Document getXmlFromStrByRestore(String strXml){
        try {
            Document dom = DocumentHelper.parseText(strXml);
            restore(dom.getRootElement());
            return dom;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Document转换为字符串，并且是以组装的方式
     * @param doc
     * @return
     */
    public static String asXmlByAssemble(Document doc){
        if(doc == null) return "";
        assemble(doc.getRootElement());
        return doc.asXML();
    }

    /**
     * Document转换为字符串，并且是以还原的方式
     * @param doc
     * @return
     */
    public static String asXmlByRestore(Document doc){
        if(doc == null) return "";
        restore(doc.getRootElement());
        return doc.asXML();
    }

    /**
     * Document转换为Document，并且是以组装的方式
     * @param doc
     * @return
     */
    public static Document asDocumentByAssemble(Document doc){
        if(doc == null) return doc;
        String msgCd = getNodeValue(doc, "/transaction/header/msg/msgCd");
        if(StringUtils.isNotEmpty(msgCd) && msgCd.startsWith("NPS")){
            assemble(doc.getRootElement());
        }
        return doc;
    }

    private static String getNodeValue(Document doc,String xPath){
        if (doc == null || StringUtils.isEmpty(xPath))
            return null;
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
        return null;
    }

    private static String getXMLNameSpaceFixed(String xpath,String ns)
    {
        xpath= xpath.replaceAll("/(\\w)", "/"+ns+":$1");
        xpath= xpath.replaceAll("^(\\w)", ns+":$1");
        return xpath;
    }

    /**
     * Document转换为Document，并且是以还原的方式
     * @param doc
     * @return
     */
    public static Document asDocumentByRestore(Document doc){
        if(doc == null) return doc;
        restore(doc.getRootElement());
        return doc;
    }

    /**
     * Xml字符串重新组装
     * @param strXml
     * @return
     */
    public static String xmlStrByAssemble(String strXml){
        try {
            Document dom = DocumentHelper.parseText(strXml);
            String msgCd = getNodeValue(dom, "/transaction/header/msg/msgCd");
            if(StringUtils.isNotEmpty(msgCd) && msgCd.startsWith("NPS")){
                assemble(dom.getRootElement());
            }
//            assemble(dom.getRootElement());
            return dom.asXML();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return strXml;
    }

    /**
     * Xml字符串还原
     * @param strXml
     * @return
     */
    public static String xmlStrByRestore(String strXml){
        try {
            Document dom = DocumentHelper.parseText(strXml);
            restore(dom.getRootElement());
            return dom.asXML();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return strXml;
    }

    //组装
    private static void assemble(Element element) {
        List elements = element.elements();
        if (elements.size() == 0) {
            String name = element.getName();
            String value = element.getTextTrim();
            int index = value.indexOf("/");
            int nextIndex = value.indexOf("/", index+1);
            if (!"signature".equals(name) && index != -1 && nextIndex != -1 && nextIndex-index>1) {
                value = value.substring(1);
                String path = value.substring(0, nextIndex-1);
                value = value.substring(nextIndex);
                name += "-" + path;
                QName qName = new QName(name, element.getNamespace());
                element.setQName(qName);
                element.setText(value);//重新设置域路径及域值<Ustrd>/Purpose/用途</Ustrd> 调整为<Ustrd-Purpose>用途</Ustrd-Purpose>
            }
        } else {
            Iterator iterator = elements.iterator();
            while (iterator.hasNext()) {
                Element elem = (Element) iterator.next();
                assemble(elem);
            }
        }
    }

    //还原
    private static void restore(Element element) {
        List elements = element.elements();
        if (elements.size() == 0) {
            String name = element.getName();
            String value = element.getTextTrim();
            if (name.indexOf("-") != -1) {
                String path = name.substring(0, name.indexOf("-"));
                name = name.substring(name.indexOf("-") + 1);
                value = "/" + name + "/" + value;
                QName qName = new QName(path, element.getNamespace());
                element.setQName(qName);
                element.setText(value);//重新设置域路径及域值<Ustrd-Purpose>用途</Ustrd-Purpose> 调整为<Ustrd>/Purpose/用途</Ustrd>
            }
        } else {
            Iterator iterator = elements.iterator();
            while (iterator.hasNext()) {
                Element elem = (Element) iterator.next();
                restore(elem);
            }
        }
    }

    public static void main(String[] args) throws DocumentException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<transaction xmlns=\"urn:iso:std :iso:20022:tech:xsd:NPS.321.001.01\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "\t<header>\n" +
                "\t\t<ver>02</ver>\n" +
                "\t\t<msg>\n" +
                "\t\t\t<msgCd>NPS.321.001.01</msgCd>\n" +
                "\t\t\t<sndAppCd>NPS</sndAppCd>\n" +
                "\t\t\t<sndMbrCd>402003000011</sndMbrCd>\n" +
                "\t\t\t<sndDt>20180620</sndDt>\n" +
                "\t\t\t<sndTm>173040</sndTm>\n" +
                "\t\t\t<rcvAppCd>MPS</rcvAppCd>\n" +
                "\t\t\t<rcvMbrCd>402002000011</rcvMbrCd>\n" +
                "\t\t\t<seqNb>B016526801</seqNb>\n" +
                "\t\t\t<structType>XML</structType>\n" +
                "\t\t\t<callTyp>ASYN</callTyp>\n" +
                "\t\t\t<reserve>B</reserve>\n" +
                "\t\t</msg>\n" +
                "\t</header>\n" +
                "\t<body>\n" +
                "\t\t<FIToFICstmrCdtTrf>\n" +
                "\t\t\t<GrpHdr>\n" +
                "\t\t\t\t<MsgId>201804220000286022</MsgId>\n" +
                "\t\t\t\t<CreDtTm>2018-06-20T17:30:40</CreDtTm>\n" +
                "\t\t\t\t<NbOfTxs>1</NbOfTxs>\n" +
                "\t\t\t\t<SttlmInf>\n" +
                "\t\t\t\t\t<SttlmMtd>CLRG</SttlmMtd>\n" +
                "\t\t\t\t</SttlmInf>\n" +
                "\t\t\t</GrpHdr>\n" +
                "\t\t\t<CdtTrfTxInf>\n" +
                "\t\t\t\t<PmtId>\n" +
                "\t\t\t\t\t<EndToEndId>201804220000286023</EndToEndId>\n" +
                "\t\t\t\t\t<TxId>201804220000286024</TxId>\n" +
                "\t\t\t\t</PmtId>\n" +
                "\t\t\t\t<PmtTpInf>\n" +
                "\t\t\t\t\t<CtgyPurp>\n" +
                "\t\t\t\t\t\t<Prtry>C102</Prtry>\n" +
                "\t\t\t\t\t</CtgyPurp>\n" +
                "\t\t\t\t</PmtTpInf>\n" +
                "\t\t\t\t<IntrBkSttlmAmt Ccy=\"CNY\">1.11</IntrBkSttlmAmt>\n" +
                "\t\t\t\t<ChrgBr>DEBT</ChrgBr>\n" +
                "\t\t\t\t<InstgAgt>\n" +
                "\t\t\t\t\t<FinInstnId>\n" +
                "\t\t\t\t\t\t<ClrSysMmbId>\n" +
                "\t\t\t\t\t\t\t<MmbId>402003000011</MmbId>\n" +
                "\t\t\t\t\t\t</ClrSysMmbId>\n" +
                "\t\t\t\t\t</FinInstnId>\n" +
                "\t\t\t\t\t<BrnchId>\n" +
                "\t\t\t\t\t\t<Id>402003000011</Id>\n" +
                "\t\t\t\t\t</BrnchId>\n" +
                "\t\t\t\t</InstgAgt>\n" +
                "\t\t\t\t<InstdAgt>\n" +
                "\t\t\t\t\t<FinInstnId>\n" +
                "\t\t\t\t\t\t<ClrSysMmbId>\n" +
                "\t\t\t\t\t\t\t<MmbId>402002000011</MmbId>\n" +
                "\t\t\t\t\t\t</ClrSysMmbId>\n" +
                "\t\t\t\t\t</FinInstnId>\n" +
                "\t\t\t\t\t<BrnchId>\n" +
                "\t\t\t\t\t\t<Id>402002000011</Id>\n" +
                "\t\t\t\t\t</BrnchId>\n" +
                "\t\t\t\t</InstdAgt>\n" +
                "\t\t\t\t<Dbtr>\n" +
                "\t\t\t\t\t<Nm>王大锤</Nm>\n" +
                "\t\t\t\t\t<PstlAdr>\n" +
                "\t\t\t\t\t\t<AdrLine>付款人地址</AdrLine>\n" +
                "\t\t\t\t\t</PstlAdr>\n" +
                "\t\t\t\t</Dbtr>\n" +
                "\t\t\t\t<DbtrAcct>\n" +
                "\t\t\t\t\t<Id>\n" +
                "\t\t\t\t\t\t<Othr>\n" +
                "\t\t\t\t\t\t\t<Id>6222020201023102412</Id>\n" +
                "\t\t\t\t\t\t</Othr>\n" +
                "\t\t\t\t\t</Id>\n" +
                "\t\t\t\t</DbtrAcct>\n" +
                "\t\t\t\t<DbtrAgt>\n" +
                "\t\t\t\t\t<FinInstnId>\n" +
                "\t\t\t\t\t\t<ClrSysMmbId>\n" +
                "\t\t\t\t\t\t\t<MmbId>402003000011</MmbId>\n" +
                "\t\t\t\t\t\t</ClrSysMmbId>\n" +
                "\t\t\t\t\t</FinInstnId>\n" +
                "\t\t\t\t</DbtrAgt>\n" +
                "\t\t\t\t<CdtrAgt>\n" +
                "\t\t\t\t\t<FinInstnId>\n" +
                "\t\t\t\t\t\t<ClrSysMmbId>\n" +
                "\t\t\t\t\t\t\t<MmbId>402002000011</MmbId>\n" +
                "\t\t\t\t\t\t</ClrSysMmbId>\n" +
                "\t\t\t\t\t</FinInstnId>\n" +
                "\t\t\t\t</CdtrAgt>\n" +
                "\t\t\t\t<Cdtr>\n" +
                "\t\t\t\t\t<Nm>王淑静</Nm>\n" +
                "\t\t\t\t\t<PstlAdr>\n" +
                "\t\t\t\t\t\t<AdrLine>收款人地址</AdrLine>\n" +
                "\t\t\t\t\t</PstlAdr>\n" +
                "\t\t\t\t</Cdtr>\n" +
                "\t\t\t\t<CdtrAcct>\n" +
                "\t\t\t\t\t<Id>\n" +
                "\t\t\t\t\t\t<Othr>\n" +
                "\t\t\t\t\t\t\t<Id>623594303000018415</Id>\n" +
                "\t\t\t\t\t\t</Othr>\n" +
                "\t\t\t\t\t</Id>\n" +
                "\t\t\t\t</CdtrAcct>\n" +
                "\t\t\t\t<Purp>\n" +
                "\t\t\t\t\t<Prtry>03302</Prtry>\n" +
                "\t\t\t\t</Purp>\n" +
                "\t\t\t\t<RmtInf>\n" +
                "\t\t\t\t\t<Ustrd-Postscript>附言</Ustrd-Postscript>\n" +
                "\t\t\t\t\t<Ustrd-Remark>M15_NCS_140_001</Ustrd-Remark>\n" +
                "\t\t\t\t\t<Ustrd-TransFee>0.00</Ustrd-TransFee>\n" +
                "\t\t\t\t\t<Ustrd-TranChnlTyp>06</Ustrd-TranChnlTyp>\n" +
                "\t\t\t\t\t<Ustrd-SttlmDt>2018-04-22</Ustrd-SttlmDt>\n" +
                "\t\t\t\t\t<Ustrd-PayerAccTyp>PT03</Ustrd-PayerAccTyp>\n" +
                "\t\t\t\t\t<Ustrd-PayeeAccTyp>PT05</Ustrd-PayeeAccTyp>\n" +
                "\t\t\t\t</RmtInf>\n" +
                "\t\t\t</CdtTrfTxInf>\n" +
                "\t\t</FIToFICstmrCdtTrf>\n" +
                "\t</body>\n" +
                "\t<signature>HQhmxw2hXwLnsd8qc3HgTkAJ9I+R7etXrB8MFnSWjezMuFILPzBzGXlx8y2h6ROtsiiFL9K7CqjNsF8vwBwxfemgOoZUdNckBLn/jhGQbgcjyGkxoLYIMTIEdc43F3/bGEMIGVEu5aAFQTK3dUz5nNBlI+AdmfUOK6c31dLEopW2Gih3mV33soOjfev713\n" +
                "aUMOCJUys6WJ9aUT29cSwlrMcImiJK+CFB4trkJcJQD1gMCxofmdGNoLFmXJv9Y/k0v7USUch7cJ3uiiDdB5NMJAobKJX9wwXd3o2dHW+nFWLz6Qyy3Bmq+BW/pKJzsVY48BHRSMIN4iXFoDBcrNMZ8w==</signature>\n" +
                "</transaction>";
//        xml = xmlStrByAssemble(xml);
        xml = xmlStrByRestore(xml);
        System.out.println(xml);
    }
}
