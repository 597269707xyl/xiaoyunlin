package com.zdtech.platform.framework.message.xml;

import com.cfca.toolkit.CastleProperties;
import com.zdtech.platform.framework.entity.*;
import com.zdtech.platform.framework.repository.MsgFieldDao;
import com.zdtech.platform.framework.repository.SimSysInsMessageDao;
import com.zdtech.platform.framework.repository.SimSystemInstanceDao;
import com.zdtech.platform.framework.repository.SysCodeDao;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by ll007 on 2016/8/16.
 * [[1,0001],[2,0002],[2,0003]]
 */
@Service
public class XmlMessagePacker {
    private static Logger logger = LoggerFactory.getLogger(XmlMessagePacker.class);

    @Value("${ecds.pfx.name}")
    private String pfxName;
    @Value("${ecds.pfx.pwd}")
    private String pfxPwd;
    @Autowired
    private SimSysInsMessageDao simSysInsMessageDao;
    @Autowired
    private MsgFieldDao msgFieldDao;
    @Autowired
    private SysCodeDao sysCodeDao;
    @Autowired
    private SimSystemInstanceDao simSystemInstanceDao;

    public Result packXmlMessage(Long msgId, Map<Long, String> values, String body) {
        Result ret = new Result();
        StringBuffer headBuffer = new StringBuffer();
        StringBuffer bodyBuffer = new StringBuffer();
        StringBuffer buffer = new StringBuffer();
        try {
            String str = null;
            SimSysInsMessage simSysInsMessage = simSysInsMessageDao.findOne(msgId);
            List<SimSysInsMessageField> simSysInsMessageFields = simSysInsMessage.getMsgFields();
            List<SimSysInsMessageField> headlists = new ArrayList<>();
            for (SimSysInsMessageField simSysInsMessageField : simSysInsMessageFields) {
                if (simSysInsMessageField.getFieldType().equalsIgnoreCase("HEAD")) {
                    headlists.add(simSysInsMessageField);
                }
            }
            for (SimSysInsMessageField s : headlists) {
                int length = s.getMsgField().getLength();
                if (length == values.get(s.getMsgField().getId()).length()) {
                    headBuffer.append(values.get(s.getMsgField().getId()));
                } else if (length > values.get(s.getMsgField().getId()).length()) {
                    int interval = length - values.get(s.getMsgField().getId()).length();
                    if (s.getMsgField().getDataType().getCode().equals("n")) {
                        for (int i = 0; i < interval; i++) {
                            headBuffer.append(0);
                        }
                        headBuffer.append(values.get(s.getMsgField().getId()));
                    } else if (s.getMsgField().getDataType().getCode().equals("x")) {
                        headBuffer.append(values.get(s.getMsgField().getId()));
                        for (int i = 0; i < interval; i++) {
                            headBuffer.append(" ");
                        }
                    }
                }
            }
            Document document = DocumentHelper.parseText(body);

            SysCode sysCode = sysCodeDao.findByCategoryAndKey("ECDS_SIGNATURE_CODE", simSysInsMessage.getMsgType());
            Document bodyDom = signatureMessage(document, simSysInsMessage.getMsgType(), sysCode.getValue());
            bodyBuffer.append(bodyDom.asXML());
            byte[] bodyByte = getByteFromBodyDom(bodyDom, simSysInsMessage.getMsgType());

            byte[] headerByte = md5MessageForECDS(bodyByte, headBuffer.toString().getBytes());
            StringBuffer headerBuffer = new StringBuffer(new String(headerByte));
            int bodyLength = bodyByte.length;
            String bodyLengthString = paddedStr(8, String.valueOf(bodyLength), 1, "0");
            headerBuffer.replace(149, 157, bodyLengthString);
            if (headerBuffer.length() != 200) {
                ret.setSuccess(false);
                ret.setMsg("组包失败，主动下发报文的报文头长度不正确！");
                return ret;
            }

        } catch (DocumentException e1) {
            e1.printStackTrace();
        }
        String src = headBuffer.toString() + bodyBuffer.toString();
        ret.setSuccess(true);
        ret.setMsg("组包成功");
        String key = "result";
        ret.addData(key, src);
        return ret;
    }


    public Map<Long, String> unPackMessage(Long simInstanceId, String src) {
        Map<Long, String> map = new HashMap<Long, String>();
        SimSystemInstance simSystemInstance = simSystemInstanceDao.findOne(simInstanceId);
        SimSystem simSystem = simSystemInstance.getSimSystem();
        MsgFieldSet headFieldSet = simSystem.getHeadFieldSet();
        List<MsgFieldSetComp> fields = headFieldSet.getFields();
        if (src != null) {
            StringBuffer headBuffer = new StringBuffer();
            String head = src.substring(0, 200);
            headBuffer.append(head);
            String msgType = null;
            for (MsgFieldSetComp m : fields) {
                MsgField msgField = m.getField();
                int fieldLength = msgField.getLength();
                String fieldValue = getBufferValue(headBuffer, 0, fieldLength);
                headBuffer.delete(0, fieldValue.length());
                if (msgField.getFieldId() == "MesgType") {
                    msgType = fieldValue;
                }
                map.put(m.getId(), fieldValue);
            }
            String body = src.substring(200);
            SAXReader saxReader = new SAXReader();
            Document showBody = null;
            ArrayList<Leaf> elemList = new ArrayList<Leaf>();
            try {
                showBody = saxReader.read(new ByteArrayInputStream(body.getBytes()));
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            Element root = showBody.getRootElement();
            elemList = getElementList(root, elemList);
            for (Leaf l : elemList) {
                MsgField msgField = msgFieldDao.findByFieldId(l.getXpath());
                map.put(msgField.getId(), l.getValue());
            }
            SysCode sysCode = sysCodeDao.findByCategoryAndKey("ECDS_MSG_CODE", msgType);
            String value = sysCode.getValue();
            List list = showBody.selectNodes("//PtcptSgntr");
            List list2 = showBody.selectNodes("//CntrlBkSgntr");
            Boolean verify = false;
            String signature = null;
            Element name = null;
            if (list.size() != 0 && list2.size() == 0) {
                name = showBody.getRootElement().element("PtcptSgntr");
            } else if (list2.size() != 0 && list.size() == 0) {
                name = showBody.getRootElement().element("CntrlBkSgntr");
            }
            signature = name.getText();
            Element orignalcontent = showBody.getRootElement().element(value);
            verify = vefifySignatureForECDS(orignalcontent, signature);
        }
        return map;
    }


    public static String pad(String src, int length, String lor, char c) {
        String ret = src;
        if (src.length() >= length) {
            return ret;
        }
        if (lor.equalsIgnoreCase("L")) {
            ret = StringUtils.leftPad(src, length, c);
        } else {
            ret = StringUtils.rightPad(src, length, c);
        }
        return ret;
    }

    private List<Element> getElementsByPath(Element element, String path) {
        List<Element> result = null;
        String url = element.getNamespaceURI();
        if (url.equals("")) {
            //不存在命名空间
            result = (List<Element>) element.selectNodes(path);
        } else {
            //存在命名空间
            Map<String, String> hm = new HashMap<String, String>();
            hm.put("x", url);
            if (path.substring(0, 1).equals("/")) {
                path = path.replaceAll("/", "/x:");
                path = "." + path;
            } else {
                path = path.replaceAll("/", "/x:");
                path = "./x:" + path;
            }

            XPath xPath = DocumentHelper.createXPath(path);
            xPath.setNamespaceURIs(hm);
            result = (List<Element>) xPath.selectNodes(element);
        }
        return result;
    }

    public ArrayList getElementList(Element element, ArrayList elemList) {
        //ArrayList<Leaf> elemList = new ArrayList<Leaf>();
        List elements = element.elements();
        if (elements.size() == 0) {
            //没有子元素
            String xpath = element.getPath();
            String value = element.getTextTrim();
            elemList.add(new Leaf(getNodeAttribute(element), xpath, value));
        } else {
            //有子元素
            Iterator it = elements.iterator();
            while (it.hasNext()) {
                Element elem = (Element) it.next();
                //递归遍历
                getElementList(elem, elemList);

            }
        }
        return elemList;
    }

    public String getNodeAttribute(Element element) {
        String xattribute = "";
        DefaultAttribute e = null;
        List list = element.attributes();
        for (int i = 0; i < list.size(); i++) {
            e = (DefaultAttribute) list.get(i);
            //xattribute += " [name = " + e.getName() + ", value = " + e.getText() + "]";
            xattribute += " " + e.getName() + "=" + "\"" + e.getText() + "\"";
        }
        return xattribute;
    }

    /**
     * 对报文体进行MD5加密，并将报文头修改后返回
     *
     * @param callBackMessageBodyByte
     * @param callBackMessageHeader
     * @return
     */
    public synchronized static byte[] md5MessageForECDS(byte[] callBackMessageBodyByte,
                                                        byte[] callBackMessageHeader) {
        String md5Value = getMD5(callBackMessageBodyByte);
        try {
            byte[] md5ValueByte = md5Value.getBytes("UTF-8");
            for (int i = 157; i < 157 + md5ValueByte.length; i++) {
                callBackMessageHeader[i] = md5ValueByte[i - 157];
            }
            return callBackMessageHeader;
        } catch (UnsupportedEncodingException e) {
        }
        return callBackMessageHeader;
    }

    public static String getMD5(byte[] source) {
        String s = null;
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                'e', 'f'};
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    private byte[] getByteFromBodyDom(Document bodyDom, String messageType) {

        //如果为此时的报文为075，则对<OrgnlMsg>节点进行单独处理
        if (messageType.equals("075")) {
            System.out.println("开始处理075报文");
            Element rootElement = bodyDom.getRootElement();
            Element ele = (Element) rootElement.selectSingleNode("CommercialDraftMoreDetailsInformation/MsgInf/OrgnlMsg");
            StringBuffer buffer = new StringBuffer();
            String s = (String) ele.getData();
            String header = s.substring(0, 198);
            String body = s.substring(198);
            buffer.append(header);
            buffer.append("\r\n");
            buffer.append(body);
            //ele.setData(buffer.toString());//没有起到作用
            ele.clearContent();//清空节点原值
            ele.addCDATA(buffer.toString());
        }
        byte[] result = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        try {
            XMLWriter writer = new XMLWriter(bos, format);
            writer.write(bodyDom);
            writer.close();
            result = new String(bos.toByteArray()).getBytes("UTF-8");
        } catch (IOException e) {

        }
        return result;
    }

    /**
     * 为特定的报文元素进行加签，返回加签
     *
     * @param signatureElement
     * @return
     */

    private String generateSignatureForECDS(Element signatureElement) {
        if (null != signatureElement) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            OutputFormat format = OutputFormat.createPrettyPrint();
            CnccCastle castle = initCastleConfig();
            format.setEncoding("UTF-8");
            try {
                XMLWriter writer = new XMLWriter(bos, format);
                writer.write(signatureElement);
                writer.close();
                byte[] signatureByte = bos.toByteArray();
                return castle.SignDataDetached(signatureByte);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 为特定的报文元素进行加签，返回加签
     *
     * @param signatureElement
     * @return
     */

    private Boolean vefifySignatureForECDS(Element signatureElement, String signature) {
        if (null != signatureElement) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            OutputFormat format = OutputFormat.createPrettyPrint();
            CnccCastle castle = initCastleConfig();
            format.setEncoding("UTF-8");
            try {
                XMLWriter writer = new XMLWriter(bos, format);
                writer.write(signatureElement);
                writer.close();
                byte[] signatureByte = bos.toByteArray();
                return castle.VerifySignedDataDetached(signature, signatureByte);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 为发送报文进行加签
     *
     * @param callBackDom
     * @param callBackMessageType @return
     * @param signatureTag
     */
    @SuppressWarnings("unchecked")
    public Document signatureMessage(Document callBackDom,
                                                         String callBackMessageType, String signatureTag) {
        Element root = callBackDom.getRootElement();
        if (callBackMessageType.equalsIgnoreCase("034")) {// 034报文的加签
            Element reqInf = (Element) root
                    .selectSingleNode("CommercialDraftCommonTransmission/ReqInf");
            if (reqInf != null) {
                Iterator it = reqInf.elements().iterator();
                while (it.hasNext()) {
                    Element e = (Element) it.next();
                    if (e.getName().indexOf("CommercialDraft") != -1) {
                        String ptcptSgntrString = generateSignatureForECDS(e);
                        processPtcptSgntr(root, ptcptSgntrString, "PtcptSgntr");
                        break;
                    }
                }
            }
            // 处理 "CntrlBkSgntr"签名
            Element commercialDraftElement = root
                    .element("CommercialDraftCommonTransmission");
            if (commercialDraftElement != null) {
                String commercialDraftString = generateSignatureForECDS(commercialDraftElement);
                processPtcptSgntr(root, commercialDraftString, "CntrlBkSgntr");
            }
        } else {
            if (null != signatureTag) {
                Iterator it = root.elements().iterator();
                while (it.hasNext()) {
                    Element e = (Element) it.next();
                    if (e.getName().indexOf("CommercialDraft") != -1) {
                        String cntrlBkSgntrString = generateSignatureForECDS(e);
                        processPtcptSgntr(root, cntrlBkSgntrString,
                                signatureTag);
                        break;
                    }
                }
            }
        }
        /*
         * else if(callBackMessageType.equalsIgnoreCase("033")){
		 * //033不需要加签，不做任何处理 }else{ Iterator it = root.elements().iterator();
		 * while(it.hasNext()){ Element e = (Element) it.next();
		 * if(e.getName().indexOf("CommercialDraft")!=-1){ String
		 * cntrlBkSgntrString = generateSignatureForECDS(e);
		 * processPtcptSgntr(root,cntrlBkSgntrString,"CntrlBkSgntr"); break; } }
		 * }
		 */
        return callBackDom;
    }

    /**
     * 处理报文体的签名
     *
     * @param root
     * @param ptcptSgntrString
     * @param flag
     */
    private static void processPtcptSgntr(Element root,
                                          String ptcptSgntrString, String flag) {
        Element signtrElement = null;
        if (flag.equalsIgnoreCase("PtcptSgntr")) {
            signtrElement = root.element("PtcptSgntr");
        } else {
            signtrElement = root.element("CntrlBkSgntr");
        }
        if (null != signtrElement) {
            if (signtrElement.getText() == null
                    || signtrElement.getText().equals("")) {
                signtrElement.setText(ptcptSgntrString);
            }
        } else {
            signtrElement = DocumentHelper.createElement(flag);
            signtrElement.setText(ptcptSgntrString);
            root.add(signtrElement);
        }
    }

    /**
     * 初始化报文加签参数
     */
    public CnccCastle initCastleConfig() {
        try {
            CastleProperties castleProperties = new CastleProperties();
            castleProperties.setmUserCertFilePath("/"+pfxName);
            castleProperties.setmUserCertPassword(pfxPwd);
            logger.info("密钥信息：{}，{}",pfxName,pfxPwd);
            CnccCastle castle = new CnccCastle(castleProperties);
            castle.initCertAppContext2();
            return castle;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String paddedStr(int maxLength, String orginString,
                                   int position, String padStr) {
        StringBuffer result = new StringBuffer(orginString);
        StringBuffer wPadStr = new StringBuffer();
        int diffLen = 0;
        diffLen = maxLength - orginString.length();
        for (int i = 0; i < diffLen; i++) {
            wPadStr.append(padStr);
        }
        if (position == 1) {
            // 在原字符串的左边追加
            result.insert(0, wPadStr.toString());
        } else if (position == 0) {
            // 在原字符串的右边追加
            result.append(wPadStr.toString());
        }
        return result.toString();
    }

    private static String getBufferValue(StringBuffer buffer1, int begin, int length) {
        byte[] chars = new byte[length];
        byte[] byteArray = buffer1.toString().getBytes();
        for (int i = 0; i < length; i++) {
            chars[i] = byteArray[i + begin];
        }
        return new String(chars);
    }

}
