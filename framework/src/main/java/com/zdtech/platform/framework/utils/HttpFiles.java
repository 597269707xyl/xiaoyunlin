package com.zdtech.platform.framework.utils;

import com.zdtech.platform.framework.utils.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huangbo on 2016/5/25.
 */
public class HttpFiles {

    public static <T> T saveFile(String dir,MultipartFile file,SaveFileCallback<T> callback){
        if (file == null){
            return null;
        }
        String fileName = "", filePath = "";
        fileName = file.getOriginalFilename();
        FileUtils.createDir(dir);
        filePath = dir + "/" + fileName;
        File f = new File(filePath);
        try {
            file.transferTo(f);
            return callback.doInSaveFile(file,filePath,fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> saveFiles(String dir, MultipartFile[] files, SaveFileCallback<T> callback){
        if (files!=null){
            try{
                List<T> result = new ArrayList<>();
                for (MultipartFile file:files){
                    if (file == null){
                        continue;
                    }
                    String fileName = "", filePath = "";
                    fileName = file.getOriginalFilename();
                    fileName = FileUtils.generateDateTimeFileName(fileName, new Date());
                    filePath = dir + "/" + fileName;
                    FileUtils.createDir(dir);
                    File f = new File(filePath);
                    file.transferTo(f);
                    result.add(callback.doInSaveFile(file,filePath,fileName));
                }
                return result;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    public static interface SaveFileCallback<T> {

        T doInSaveFile(MultipartFile file,String filePath,String fileName);
    }

    public static void downloadFile(String fileName, String filePath,
                                    HttpServletResponse response) {
        downloadFile("application/octet-stream",fileName,filePath,response);
    }

    public static void downloadFile(String contentType,String fileName, String filePath,
                                    HttpServletResponse response){
        try {
            fileName = new String(fileName.getBytes(), "ISO8859-1");
        } catch (UnsupportedEncodingException e) {
        }
        response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setContentType(contentType);
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             InputStream is = new BufferedInputStream(fis);
             BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());) {

            byte[] buffer = new byte[2048];
            int read = -1;
            while ((read = is.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
            }
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
