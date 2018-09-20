package com.zdtech.platform.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 操作Excel表格的功能类
 * Created by yjli on 2017/10/19.
 */
public class ExcelUtils<T> {
    private POIFSFileSystem fs;
    private Workbook wb;
    private Sheet sheet;
    private Row row;

    public List<Map<String, String>> readExcel(CommonsMultipartFile file) throws Exception {
        List<Map<String, String>> list = new ArrayList<>();
        try {
            wb = WorkbookFactory.create(file.getInputStream());
            sheet = wb.getSheetAt(0);
            list = parseSheet(sheet, list);
        }catch (Exception e1){
            e1.printStackTrace();
            throw new Exception(e1.getMessage());
        }
        return list;
    }

    private List<Map<String, String>> parseSheet(Sheet sheet, List<Map<String, String>> list) {
        Row row;
        //取得有效的行数
        int rowcount = sheet.getLastRowNum();
        //过滤第一行标题行
        for(int i=1; i<=rowcount; i++){
            row = sheet.getRow(i);
            Map<String, String> map = new HashMap<>();
            for (int j=0; j<row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                if(cell==null){
                    break;
                }
                map.put(j+"", getCellValue(cell));
            }
            list.add(map);
        }
        return list;
    }

    private String getCellValue(Cell cell){
        String cellvalue = "";
        switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
            case HSSFCell.CELL_TYPE_NUMERIC: {
                short format = cell.getCellStyle().getDataFormat();
                if(format == 14 || format == 31 || format == 57 || format == 58){ 	//excel中的时间格式
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    double value = cell.getNumericCellValue();
                    Date date = DateUtil.getJavaDate(value);
                    cellvalue = sdf.format(date);
                }
                // 判断当前的cell是否为Date
                else if (HSSFDateUtil.isCellDateFormatted(cell)) {  //先注释日期类型的转换，在实际测试中发现HSSFDateUtil.isCellDateFormatted(cell)只识别2014/02/02这种格式。
                    // 如果是Date类型则，取得该Cell的Date值           // 对2014-02-02格式识别不出是日期格式
                    Date date = cell.getDateCellValue();
                    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                    cellvalue= formater.format(date);
                } else { // 如果是纯数字
                    // 取得当前Cell的数值
                    cellvalue = NumberToTextConverter.toText(cell.getNumericCellValue());

                }
                break;
            }
            // 如果当前Cell的Type为STRIN
            case HSSFCell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
                cellvalue = cell.getStringCellValue().trim();
                break;
            case  HSSFCell.CELL_TYPE_BLANK:
                cellvalue = "";
                break;
            // 默认的Cell值
            default:{
                cellvalue = " ";
            }
        }
        return cellvalue;
    }


    @SuppressWarnings("unchecked")
    public void exportExcel(String title, String[] headers,
                            List<T> dataset, OutputStream out) throws IOException {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 15);
        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = (T) it.next();
            // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();
            for (short i = 0; i < fields.length; i++) {
                HSSFCell cell = row.createCell(i);
                Field field = fields[i];
                String fieldName = field.getName();
                String getMethodName = "get"
                        + fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1);
                try {
                    Class tCls = t.getClass();
                    Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                    Object value = getMethod.invoke(t, new Object[]{});
                    // 判断值的类型后进行强制类型转换
                    String textValue = null;

                    if (value instanceof Date) {
                        Date date = (Date) value;
                        //SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        // textValue = sdf.format(date);
                    } else {
                        // 其它数据类型都当作字符串简单处理
                        if (null != value) {
                            textValue = value.toString();
                        }
                    }
                    // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                    if (textValue != null) {
                        Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                        Matcher matcher = p.matcher(textValue);
                        if (matcher.matches()) {
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            HSSFRichTextString richString = new HSSFRichTextString(
                                    textValue);
                            HSSFFont font3 = workbook.createFont();
                            font3.setColor(HSSFColor.BLUE.index);
                            richString.applyFont(font3);
                            cell.setCellValue(richString);
                        }
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
            }
        }
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void exportExcels(String[] headers, List<Map<String, Object>> data, OutputStream out) throws IOException {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
       for(Map<String, Object> map : data){
           String title = (String) map.get("title");
           List dataset = (List) map.get("dataset");
           // 生成一个表格
           HSSFSheet sheet = workbook.createSheet(title);
           // 设置表格默认列宽度为15个字节
           sheet.setDefaultColumnWidth((short) 15);
           // 产生表格标题行
           HSSFRow row = sheet.createRow(0);
           for (short i = 0; i < headers.length; i++) {
               HSSFCell cell = row.createCell(i);
               HSSFRichTextString text = new HSSFRichTextString(headers[i]);
               cell.setCellValue(text);
           }

           // 遍历集合数据，产生数据行
           Iterator<T> it = dataset.iterator();
           int index = 0;
           while (it.hasNext()) {
               index++;
               row = sheet.createRow(index);
               T t = (T) it.next();
               // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
               Field[] fields = t.getClass().getDeclaredFields();
               for (short i = 0; i < fields.length; i++) {
                   HSSFCell cell = row.createCell(i);
                   Field field = fields[i];
                   String fieldName = field.getName();
                   String getMethodName = "get"
                           + fieldName.substring(0, 1).toUpperCase()
                           + fieldName.substring(1);
                   try {
                       Class tCls = t.getClass();
                       Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                       Object value = getMethod.invoke(t, new Object[]{});
                       // 判断值的类型后进行强制类型转换
                       String textValue = null;

                       if (value instanceof Date) {
                           Date date = (Date) value;
                       } else {
                           // 其它数据类型都当作字符串简单处理
                           if (null != value) {
                               textValue = value.toString();
                           }
                       }
                       // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                       if (textValue != null) {
                           Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                           Matcher matcher = p.matcher(textValue);
                           if (matcher.matches()) {
                               // 是数字当作double处理
                               cell.setCellValue(Double.parseDouble(textValue));
                           } else {
                               HSSFRichTextString richString = new HSSFRichTextString(
                                       textValue);
                               cell.setCellValue(richString);
                           }
                       }
                   } catch (SecurityException e) {
                       e.printStackTrace();
                   } catch (NoSuchMethodException e) {
                       e.printStackTrace();
                   } catch (IllegalArgumentException e) {
                       e.printStackTrace();
                   } catch (IllegalAccessException e) {
                       e.printStackTrace();
                   } catch (InvocationTargetException e) {
                       e.printStackTrace();
                   } catch (Exception e) {
                       e.printStackTrace();
                   } finally {

                   }
               }
           }
       }
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
