package com.zdtech.platform.utils;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lyj on 2018/3/29.
 */
public class WordUtils {
    private static Logger logger = LoggerFactory.getLogger(WordUtils.class);
    /**
     * 根据指定的参数值、模板，生成 word 文档
     * @param param 需要替换的变量
     * @param wordFilePath 模板地址
     */
    public static XWPFDocument generateWord(Map<String, Object> param, String wordFilePath) {
        XWPFDocument doc = null;
        try {
            FileInputStream in = new FileInputStream(new File(wordFilePath));
            doc = new XWPFDocument(OPCPackage.open(in));
            if (param != null && param.size() > 0) {
                //处理段落
                List<XWPFParagraph> paragraphList = doc.getParagraphs();
                processParagraphs(paragraphList, param, doc, false);

                //处理表格
                Iterator<XWPFTable> it = doc.getTablesIterator();
                while (it.hasNext()) {
                    XWPFTable table = it.next();
                    List<XWPFTableRow> rows = table.getRows();
                    for (XWPFTableRow row : rows) {
                        List<XWPFTableCell> cells = row.getTableCells();
                        for (XWPFTableCell cell : cells) {
                            List<XWPFParagraph> paragraphListTable =  cell.getParagraphs();
                            processParagraphs(paragraphListTable, param, doc, true);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }
    /**
     * 处理段落
     * @param paragraphList
     */
    public static void processParagraphs(List<XWPFParagraph> paragraphList,Map<String, Object> param,XWPFDocument doc, boolean flag){
        if(paragraphList != null && paragraphList.size() > 0){
            for(XWPFParagraph paragraph:paragraphList){
                List<XWPFRun> runs = paragraph.getRuns();
                for (int i = 0; i < runs.size(); i++) {
                    XWPFRun run = runs.get(i);
                    String text = run.getText(0);
                    logger.info("报告模板段落信息：" + text);
                    if(text != null){
                        boolean isSetText = false;
                        for (Map.Entry<String, Object> entry : param.entrySet()) {
                            String key = entry.getKey();
                            if(text.indexOf(key) != -1){
                                isSetText = true;
                                Object value = entry.getValue();
                                if (value instanceof String) {//文本替换
                                    text = text.replace(key, value.toString());
                                } else if (value instanceof Map) {//图片替换
                                    flag = true;
                                    text = text.replace(key, "");
                                    Map pic = (Map)value;
//                                    int width = Integer.parseInt(pic.get("width").toString());
//                                    int height = Integer.parseInt(pic.get("height").toString());
//                                    int picType = getPictureType(pic.get("type").toString());
                                    int width = 700;
                                    int height = 350;
                                    int picType = getPictureType("png");
                                    byte[] byteArray = (byte[]) pic.get("content");
                                    ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);
                                    try {
                                        String ind = doc.addPictureData(byteInputStream,picType);
                                        createPicture(ind, width , height,paragraph, doc);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if(flag){ //如果是 表格或者图片 则居中显示
                                    paragraph.setAlignment(ParagraphAlignment.CENTER);//内容居中显
                                }
                            }
                        }
                        if(isSetText){
                            run.setText(text,0);  //填充替换之后的数据
                        }
                    }
                }
            }
        }
    }
    /**
     * 根据图片类型，取得对应的图片类型代码
     * @param picType
     * @return int
     */
    private static int getPictureType(String picType){
        int res = XWPFDocument.PICTURE_TYPE_PICT;
        if(picType != null){
            if(picType.equalsIgnoreCase("png")){
                res = XWPFDocument.PICTURE_TYPE_PNG;
            }else if(picType.equalsIgnoreCase("dib")){
                res = XWPFDocument.PICTURE_TYPE_DIB;
            }else if(picType.equalsIgnoreCase("emf")){
                res = XWPFDocument.PICTURE_TYPE_EMF;
            }else if(picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")){
                res = XWPFDocument.PICTURE_TYPE_JPEG;
            }else if(picType.equalsIgnoreCase("wmf")){
                res = XWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }
    /**
     * 将输入流中的数据写入字节数组
     * @param in
     * @return
     */
    public static byte[] inputStream2ByteArray(InputStream in){
        byte[] byteArray = null;
        try {
            int total = in.available();
            byteArray = new byte[total];
            in.read(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                in.close();
            } catch (Exception e2) {
                logger.error("关闭流失败");
            }
        }
        return byteArray;
    }

    /**
     * 插入图片
     * @param ind
     * @param width 宽
     * @param height 高
     * @param paragraph  段落
     */
    public static void createPicture(String ind, int width, int height,XWPFParagraph paragraph,XWPFDocument doc) {
        final int EMU = 9525;
        width *= EMU;
        height *= EMU;
//        String blipId = doc.getAllPictures().get(id).getPackageRelationship().getId();
        int id = doc.getAllPictures().size()-1;
        CTInline inline = paragraph.createRun().getCTR().addNewDrawing().addNewInline();
        String picXml = ""
                + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
                + "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
                + "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
                + "         <pic:nvPicPr>" + "            <pic:cNvPr id=\""
                + id
                + "\" name=\"Generated\"/>"
                + "            <pic:cNvPicPr/>"
                + "         </pic:nvPicPr>"
                + "         <pic:blipFill>"
                + "            <a:blip r:embed=\""
                + ind
                + "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>"
                + "            <a:stretch>"
                + "               <a:fillRect/>"
                + "            </a:stretch>"
                + "         </pic:blipFill>"
                + "         <pic:spPr>"
                + "            <a:xfrm>"
                + "               <a:off x=\"0\" y=\"0\"/>"
                + "               <a:ext cx=\""
                + width
                + "\" cy=\""
                + height
                + "\"/>"
                + "            </a:xfrm>"
                + "            <a:prstGeom prst=\"rect\">"
                + "               <a:avLst/>"
                + "            </a:prstGeom>"
                + "         </pic:spPr>"
                + "      </pic:pic>"
                + "   </a:graphicData>" + "</a:graphic>";

        inline.addNewGraphic().addNewGraphicData();
        XmlToken xmlToken = null;
        try {
            xmlToken = XmlToken.Factory.parse(picXml);
        } catch (XmlException xe) {
            xe.printStackTrace();
        }
        inline.set(xmlToken);

        inline.setDistT(0);
        inline.setDistB(0);
        inline.setDistL(0);
        inline.setDistR(0);

        CTPositiveSize2D extent = inline.addNewExtent();
        extent.setCx(width);
        extent.setCy(height);

        CTNonVisualDrawingProps docPr = inline.addNewDocPr();
        docPr.setId(id);
        docPr.setName("图片" + id);
        docPr.setDescr("测试");
    }

    public static void main(String[] args) throws IOException {
        /*boolean flag = false;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(flag?"mudi":"${mudi}", "目的");
        param.put(flag?"buzhou":"${buzhou}", "步骤");
        param.put(flag?"changjing":"${changjing}", "场景");
        param.put(flag?"duixiang":"${duixiang}", "对象");
        param.put(flag?"shijian":"${shijian}", new Date().toString());
        param.put(flag?"shichang":"${shichang}", "60");
        param.put(flag?"bingfashu":"${bingfashu}", "10000");
        param.put(flag?"tps":"${tps}", "10000");
        param.put(flag?"tpm":"${tpm}", "10000");
        param.put(flag?"jiaoyipjhs":"${jiaoyipjhs}", "100");
        param.put(flag?"jiaoyizdhs":"${jiaoyizdhs}", "100");
        param.put(flag?"jiaoyifdbfb":"${jiaoyifdbfb}", "100");

        Map<String,Object> header = new HashMap<String, Object>();
        header.put("width", 550);
        header.put("height", 270);
        header.put("type", "jpg");
        //header.put("content", WordUtils.inputStream2ByteArray(new FileInputStream("C:/Users/guohuahuang/Desktop/b.jpg")));
        param.put(flag?"yiaoyitu":"${yiaoyitu}",header);

        Map<String,Object> twocode = new HashMap<String, Object>();
        twocode.put("width", 550);
        twocode.put("height", 270);
        twocode.put("type", "png");
        //twocode.put("content", WordUtils.inputStream2ByteArray(new FileInputStream("C:/Users/guohuahuang/Desktop/a.png")));
        param.put(flag?"yewutu":"${yewutu}",twocode);*/
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userName","执行人"); //执行人
        param.put("breakType", "熔断策略"); //熔断策略
        param.put("scene", "场景"); //场景
        param.put("target", "压测对象");//压测对象
        param.put("datetime", "压测时间"); //压测时间
        param.put("duration", "压测时长"); //压测时长
        param.put("concurrency", "用户并发数目标值"); //用户并发数目标值
        param.put("start", "用户并发数起始值"); //用户并发数起始值
        param.put("time", "用户并发数爬坡时长"); //用户并发数爬坡时长(秒)
        param.put("tpsmax", "tps最大值");//tps最大值
        param.put("tpsavg", "tps平均值");//tps最小值
        param.put("tpmmax", "tpm最大值");//tpm最大值
        param.put("tpmavg", "tpm平均值");//tpm最小值
        param.put("jiaoyipingjunhaoshi", "交易平均耗时");//交易平均耗时（毫秒）
        param.put("jiaoyizuidahaoshi", "交易最大耗时");//交易最大耗时（毫秒）
        param.put("jiaoyizuixiaohaoshi", "交易最小耗时");//交易最小耗时（毫秒）

        Map<String, Object> jiaoyituanmiao = new HashMap<String, Object>();
        jiaoyituanmiao.put("content", WordUtils.inputStream2ByteArray(new FileInputStream("C:/Users/guohuahuang/Desktop/a.png")));
        param.put("jiaoyituanmiao", jiaoyituanmiao);//交易成功趋势图（按秒）

        Map<String, Object> jiaoyituanfen = new HashMap<String, Object>();
        jiaoyituanfen.put("content", WordUtils.inputStream2ByteArray(new FileInputStream("C:/Users/guohuahuang/Desktop/a.png")));
        param.put("jiaoyituanfen", jiaoyituanfen);//交易成功趋势图（按分）

        Map<String, Object> yewutuanmiao = new HashMap<String, Object>();
        yewutuanmiao.put("content", WordUtils.inputStream2ByteArray(new FileInputStream("C:/Users/guohuahuang/Desktop/a.png")));
        param.put("yewutuanmiao", yewutuanmiao);//业务成功趋势图(按秒)

        Map<String, Object> yewutuanfen = new HashMap<String, Object>();
        yewutuanfen.put("content", WordUtils.inputStream2ByteArray(new FileInputStream("C:/Users/guohuahuang/Desktop/a.png")));
        param.put("yewutuanfen", yewutuanfen);//业务成功趋势图(按分)

        Map<String, Object> pingjunhaoshituanmiao = new HashMap<String, Object>();
        pingjunhaoshituanmiao.put("content", WordUtils.inputStream2ByteArray(new FileInputStream("C:/Users/guohuahuang/Desktop/a.png")));
        param.put("pingjunhaoshituanmiao", pingjunhaoshituanmiao);//交易平均耗时趋势图(按秒)

        Map<String, Object> jiaoyibingfaanmiao = new HashMap<String, Object>();
        jiaoyibingfaanmiao.put("content", WordUtils.inputStream2ByteArray(new FileInputStream("C:/Users/guohuahuang/Desktop/a.png")));
        param.put("jiaoyibingfaanmiao", jiaoyibingfaanmiao);//交易并发趋势图(按秒)

        Map<String, Object> jiaoyibingfaanfen = new HashMap<String, Object>();
        jiaoyibingfaanfen.put("content", WordUtils.inputStream2ByteArray(new FileInputStream("C:/Users/guohuahuang/Desktop/a.png")));
        param.put("jiaoyibingfaanfen", jiaoyibingfaanfen);//交易并发趋势图(按分)

        Map<String, Object> diaodanlvanfen = new HashMap<String, Object>();
        diaodanlvanfen.put("content", WordUtils.inputStream2ByteArray(new FileInputStream("C:/Users/guohuahuang/Desktop/a.png")));
        param.put("diaodanlvanfen", diaodanlvanfen);//掉单率趋势图(按分)

        Map<String, Object> Jiaoyihaoshifenduantongji = new HashMap<String, Object>();
        Jiaoyihaoshifenduantongji.put("content", WordUtils.inputStream2ByteArray(new FileInputStream("C:/Users/guohuahuang/Desktop/a.png")));
        param.put("jiaoyihaoshifenduantongji", Jiaoyihaoshifenduantongji); //交易耗时分段统计百分比
        String wordFilePath = "D:\\workspace\\stress2\\web-server\\src\\main\\resources\\ycbg.docx";
        XWPFDocument doc = WordUtils.generateWord(param, wordFilePath);
        FileOutputStream out = new FileOutputStream("C:\\Users\\guohuahuang\\Desktop\\hgla.docx");
        doc.write(out);
        out.close();
        System.out.println("修改word完啦");
    }
}
