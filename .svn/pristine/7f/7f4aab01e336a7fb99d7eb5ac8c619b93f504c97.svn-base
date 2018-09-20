package com.zdtech.platform.framework.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;



import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


/**
 * Created by wzx on 2016/8/17.
 */
public class XmlToJson {
    private  String src="";
    private  String rootname="";
    private  Map srcMap=new HashMap();
    private int number=0;
    private Document document=new Document();;
    public Map getSrcMap() {
        return srcMap;
    }

    public void setSrcMap(Map srcMap) {
        this.srcMap = srcMap;
    }

    @SuppressWarnings("unchecked")
    public String getTreeCamera(String xml)
    {   number=0;
        StringBuilder result=new StringBuilder();
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
            xml=xml.replaceAll("\r|\n|\r\n|\n\r", "");
            SAXBuilder sax = new SAXBuilder();
            Document doc = sax.build(is);
            Element root = doc.getRootElement();
            rootname=root.getName();
            src="/"+root.getName();
            if (root.getChildren().size()<=0)
                result.append("[]");
            else {
                List<Element> list=root.getChildren();
                result.append("[");
                result.append("{\"id\":\""+number+"\",\"name\":\"" +rootname + "\",\"text\":\"\",\"children\":[");
                for(int i=0;i<list.size();i++){
                    src="/"+rootname;
                    Element element=list.get(i);
                    sb=new StringBuilder();
                    String  r1 = getJsonByNode(element, sb);
                    result.append(r1);
                }
                result=result.delete(result.length() - 1, result.length());
                result.append("]}");
                result.append("]");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }
    /// 遍历结点，拼接json
    private String getJsonByNode(Element element, StringBuilder sb)
    {
        if (element.getChildren().size()<=0)
        {
            number=number+1;
            if(element.getTextTrim()==null){
                sb.append("{\"id\":\""+number+"\",\"name\":\"" + element.getName() + "\",\"text\":\"\"},");
            }else {
                String text=element.getTextTrim();
                text=text.replaceAll("\r|\n|\r\n|\n\r", "");
                sb.append("{\"id\":\"" + number + "\",\"name\":\"" + element.getName() + "\",\"text\":\"" + text + "\"},");
            }

        }
        else
        {
            src=src+"/"+element.getName();
            number=number+1;
            if(element.getTextTrim()==null){
                sb.append("{\"id\":\""+number+"\",\"name\":\"" + element.getName() + "\",\"text\":\"\",\"children\":[");
            }else {
                String text=element.getTextTrim();
                text=text.replaceAll("\r|\n|\r\n|\n\r", "");
                sb.append("{\"id\":\""+number+"\",\"name\":\"" + element.getName() + "\",\"text\":\"" +text+ "\",\"children\":[");
            }
            List list=element.getChildren();
            for(int i=0;i<list.size();i++){
                Element  et = (Element) list.get(i);
                getJsonByNode(et, sb);
            }
            sb=sb.delete(sb.length() - 1, sb.length());
            sb.append("]},");
        }
        return sb.toString();
    }


    public String tree2XML(String xml,int parent,HashMap<String,String> map)
    {
        StringBuilder result=new StringBuilder();
        StringBuilder sb = new StringBuilder();
        Document document=new Document();
        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
            SAXBuilder sax = new SAXBuilder();
            Document doc = sax.build(is);
            Element root = doc.getRootElement();
            Element newroot=new Element(root.getName());
            newroot.setText(root.getText());
            document=new Document(newroot);
            if (root.getChildren().size()<=0)
                result.append("[]");
            else {
                List<Element> list=root.getChildren();
                for(int i=0;i<list.size();i++){
                    Element element=list.get(i);
                    Element  r1 = formartELement(element,parent,map);
                    Element elemCopy = (Element)r1.clone();
                    elemCopy.detach();
                    newroot.addContent(elemCopy);
                }
            }
            Format format = Format.getPrettyFormat();
            format.setEncoding("utf-8");// 设置xml文件的字符为UTF-8，解决中文问题
            XMLOutputter xmlout = new XMLOutputter(format);
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            xmlout.output(document, bo);
            System.out.println(bo.toString());
            return bo.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
    private Element formartELement(Element element,int parent,HashMap<String,String> map)
    {
        if (element.getChildren().size()<=0)
        {
            number=number+1;

            if(!map.get("id").equals("addid")&&number==Integer.parseInt(map.get("id"))){
                element.setName(map.get("name"));
                element.setText(map.get("text"));
            }else if(map.get("type").equals("del")&&number==Integer.parseInt(map.get("id"))){
                element.removeAttribute(map.get("name"));
            }else  if(parent==number&&map.get("id").equals("addid")){
                Element element1=new Element(map.get("name"));
                element1.setText(map.get("text"));
                element.addContent(element1);
            }
        }
        else
        {
            number=number+1;
            if(!map.get("id").equals("addid")&&number==Integer.parseInt(map.get("id"))){
                element.setName(map.get("name"));
                element.setText(map.get("text"));
            }else if(map.get("type").equals("del")&&number==Integer.parseInt(map.get("id"))){
                element.removeAttribute(map.get("name"));
            }else  if(parent==number&&map.get("id").equals("addid")){
                Element element1=new Element(map.get("name"));
                element1.setText(map.get("text"));
                element.addContent(element1);
            }

            List list=element.getChildren();
            for(int i=0;i<list.size();i++){
                Element  et = (Element) list.get(i);
                formartELement(et, parent, map);
            }
        }
        return element;
    }


}
