package com.zdtech.platform.framework.utils;

import com.zdtech.platform.framework.service.FieldCacheService;
import net.sf.json.JSONArray;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by huangbo on 2016/9/5.
 */
public class XmlJsonHelper {
    public static String XmltoJson(String xmlString) {
        String jsonString = "";
        try {
            Document doc = new SAXReader().read(new ByteArrayInputStream(xmlString.getBytes("UTF8")));
            Element root = doc.getRootElement();
            int i = 0;
            int count = 0;
            jsonString = getJsonString(jsonString, root, i, count).get(0);
            count = Integer.parseInt(getJsonString(jsonString, root, i, count).get(1));
            jsonString = "{\"total\":" + count + ",\"rows\":" + jsonString + "}]}";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public static String JsonToXml(String jsonString) {
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        xmlString = getXml(jsonArray, xmlString);
        return xmlString;
    }

    private static String getFormatPath(Element element){
        String ret = "";
        if (element == null){
            return ret;
        }
        String name = element.getName();
        ret = "/"+name;
        while (element.getParent() != null){
            element = element.getParent();
            name = element.getName();
            ret = "/" + name + ret;
        }
        return ret;
    }

    private static List<String> getJsonString(String jString, Element element, int isFirst, int count) {
        List<String> list = new ArrayList<>();
        count++;
        String tempString = "[";
        List elements = element.elements();
        String name = "\"" + element.getQualifiedName().trim() + "\"";
        String value = "\"" + element.getTextTrim() + "\"";
        String path = getFormatPath(element);
        if (element.getName().trim().equals("PtcptSgntr") || element.getName().trim().equals("ElctrncSgntr")) {
            value = "\"\"";
        }
        if (isFirst != 0) {
            tempString = "},";
        }
        if (elements.size() == 0) {
            String nameZh = FieldCacheService.getXmlFieldNameZh(path);
            nameZh = "\""+nameZh+"\"";
            jString = jString + tempString + "{\"id\":" + count + ",\"name\":" + name + ",\"value\":" + value + ",\"nameZh\":" + nameZh;

        } else {
            jString = jString + tempString + "{\"id\":" + count + ",\"name\":" + name + ",\"value\":" + value + ",\"children\":";
            Iterator it = elements.iterator();
            for (int i = 0; it.hasNext(); i++) {
                Element elem = (Element) it.next();
                jString = getJsonString(jString, elem, i, count).get(0);
                count = Integer.parseInt(getJsonString(jString, elem, i, count).get(1));
            }
            jString = jString + "}]";
        }
        list.add(0, jString);
        list.add(1, String.valueOf(count));
        return list;
    }

    private static String getXml(JSONArray jsonArray, String xmlString) {


        Iterator<Object> it = jsonArray.iterator();
        for (int i = 0; it.hasNext(); i++) {
            Boolean hasChildren = true;
            String name = jsonArray.getJSONObject(i).getString("name");
            String value = jsonArray.getJSONObject(i).getString("value");
            try {
                JSONArray jsonArrayChildren = jsonArray.getJSONObject(i).getJSONArray("children");
            } catch (Exception e) {
                hasChildren = false;
            }
            if (!hasChildren) {
                xmlString = xmlString + "<" + name + ">" + value + "</" + name + ">";
            } else {
                JSONArray jsonArrayChildren = jsonArray.getJSONObject(i).getJSONArray("children");
                xmlString = xmlString + "<" + name + ">";
                xmlString = getXml(jsonArrayChildren, xmlString);
                xmlString = xmlString + "</" + name + ">";
            }
            it.next();
        }
        return xmlString;
    }
}
