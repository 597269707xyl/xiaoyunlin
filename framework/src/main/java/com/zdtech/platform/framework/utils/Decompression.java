package com.zdtech.platform.framework.utils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 通过Java的Zip输入输出流实现压缩和解压文件
 *
 * @author wzx
 *
 */
public  class Decompression {

    public Decompression() {
    }
    /**
     * 解压缩
     * @param sourceFile 要解压缩的文件的路径
     * @param destDir 解压缩后的目录路径
     * @throws Exception
     */
    public  void deCompress(String sourceFile,String destDir) throws Exception{
        //创建需要解压缩的文件对象
        File file = new File(sourceFile);
        if (!file.exists()){
            throw new RuntimeException(sourceFile + "不存在！");
        }
        //创建解压缩的文件目录对象
        File destDiretory  = new File(destDir);
        if(!destDiretory.exists()){
            destDiretory.mkdirs();
        }
        /*
         * 保证文件夹路径最后是"/"或者"\"
         * charAt()返回指定索引位置的char值
         */
       char lastChar = destDir.charAt(destDir.length()-1);
        if(lastChar!='/'&&lastChar!='\\'){
            //在最后加上分隔符
            destDir += File.separator;
        }
        unzip(sourceFile, destDir);

    }


    /**
     * 解压方法
     * 需要ant.jar
     */
    private  void unzip(String sourceZip,String destDir) throws Exception{
        try{
            Project p = new Project();
            Expand e = new Expand();
            e.setProject(p);
            e.setSrc(new File(sourceZip));
            e.setOverwrite(false);
            e.setDest(new File(destDir));
            e.execute();
        }catch(Exception e){
            throw e;
        }
    }
    public void ungz(String inFileName) {

        try {

            if (!getExtension(inFileName).equalsIgnoreCase("gz")) {
                System.err.println("File name must have extension of \".gz\"");
                System.exit(1);
            }

            System.out.println("Opening the compressed file.");
            GZIPInputStream in = null;
            try {
                in = new GZIPInputStream(new FileInputStream(inFileName));
            } catch(FileNotFoundException e) {
                System.err.println("File not found. " + inFileName);
                System.exit(1);
            }

            System.out.println("Open the output file.");
            String outFileName = getFileName(inFileName);
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(outFileName);
            } catch (FileNotFoundException e) {
                System.err.println("Could not write to file. " + outFileName);
                System.exit(1);
            }

            System.out.println("Transfering bytes from compressed file to the output file.");
            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            System.out.println("Closing the file and stream");
            in.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
    public  void unTarGz(String file,String outputDir) throws IOException{
                TarInputStream tarIn = null;
               try{
                       tarIn = new TarInputStream(new GZIPInputStream(new FileInputStream(file)),
                                 1024 * 2);

                        createDirectory(outputDir,null);//创建输出目录

                        TarEntry entry = null;
                        while( (entry = tarIn.getNextEntry()) != null ){

                                 if(entry.isDirectory()){//是目录
                                         entry.getName();
                                       createDirectory(outputDir,entry.getName());//创建空目录
                                    }else{//是文件
                                        File tmpFile = new File(outputDir + "/" + entry.getName());
                                       createDirectory(tmpFile.getParent() + "/",null);//创建输出目录
                                        OutputStream out = null;
                                         try{
                                                  out = new FileOutputStream(tmpFile);
                                                 int length = 0;

                                                  byte[] b = new byte[2048];

                                                  while((length = tarIn.read(b)) != -1){
                                                          out.write(b, 0, length);
                                                      }

                                              }catch(IOException ex){
                                                  throw ex;
                                              }finally{

                                                  if(out!=null)
                                                          out.close();
                                              }
                                      }
                              }
                      }catch(IOException ex){
                          throw new IOException("解压归档文件出现异常",ex);
                      } finally{
                          try{
                                  if(tarIn != null){
                                          tarIn.close();
                                      }
                              }catch(IOException ex){
                                  throw new IOException("关闭tarFile出现异常",ex);
                              }
                      }
              }
 /*private  void unrar(String sourceRar,String destDir) throws Exception{
        Archive a = null;
        FileOutputStream fos = null;
        try{
            a = new Archive(new File(sourceRar));
            FileHeader fh = a.nextFileHeader();
            while(fh!=null){
                if(!fh.isDirectory()){
                    //1 根据不同的操作系统拿到相应的 destDirName 和 destFileName
                    String compressFileName = fh.getFileNameString().trim();
                    String destFileName = "";
                    String destDirName = "";
                    //非windows系统
                    if(File.separator.equals("/")){
                        destFileName = destDir + compressFileName.replaceAll("\\\\", "/");
                        destDirName = destFileName.substring(0, destFileName.lastIndexOf("/"));
                        //windows系统
                    }else{
                        destFileName = destDir + compressFileName.replaceAll("/", "\\\\");
                        destDirName = destFileName.substring(0, destFileName.lastIndexOf("\\"));
                    }
                    //2创建文件夹
                    File dir = new File(destDirName);
                    if(!dir.exists()||!dir.isDirectory()){
                        dir.mkdirs();
                    }
                    //3解压缩文件
                    fos = new FileOutputStream(new File(destFileName));
                    a.extractFile(fh, fos);
                    fos.close();
                    fos = null;
                }
                fh = a.nextFileHeader();
            }
            a.close();
            a = null;
        }catch(Exception e){
            throw e;
        }finally{
            if(fos!=null){
                try{fos.close();fos=null;}catch(Exception e){e.printStackTrace();}
            }
            if(a!=null){
                try{a.close();a=null;}catch(Exception e){e.printStackTrace();}
            }
        }
    }
*/
    public static void createDirectory(String outputDir,String subDir){
                  File file = new File(outputDir);
                  if(!(subDir == null || subDir.trim().equals(""))){//子目录不为空
                          file = new File(outputDir + "/" + subDir);
                      }
                  if(!file.exists()){
                            if(!file.getParentFile().exists())
                                    file.getParentFile().mkdirs();
                          file.mkdirs();
                      }
              }

    /**
     * Used to extract and return the extension of a given file.
     * @param f Incoming file to get the extension of
     * @return <code>String</code> representing the extension of the incoming
     *         file.
     */
    public static String getExtension(String f) {
        String ext = "";
        int i = f.lastIndexOf('.');

        if (i > 0 &&  i < f.length() - 1) {
            ext = f.substring(i+1);
        }
        return ext;
    }

    /**
     * Used to extract the filename without its extension.
     * @param f Incoming file to get the filename
     * @return <code>String</code> representing the filename without its
     *         extension.
     */
    public static String getFileName(String f) {
        String fname = "";
        int i = f.lastIndexOf('.');

        if (i > 0 &&  i < f.length() - 1) {
            fname = f.substring(0,i);
        }
        return fname;
    }
    public static void main(String[] args) {
  //     String sourceFile = "C:/test/me.zip";
  //      String destDir = "C:/test";
//       try{
 //          Decompression decompression=new Decompression();
//           decompression.deCompress(sourceFile, destDir);
//        }catch(Exception e){
 //          e.printStackTrace();
 //       }
        Decompression decompression=new Decompression();
        decompression.ungz ("C:/test/NIG日志rizhi.tar.gz");
    }

    /**
     * 关闭一个或多个流对象
     *
     * @param closeables 可关闭的流对象列表
     * @throws IOException
     */
    public static void close(Closeable... closeables) throws IOException {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        }
    }

    /**
     * 关闭一个或多个流对象
     *
     * @param closeables 可关闭的流对象列表
     */
    public static void closeQuietly(Closeable... closeables) {
        try {
            close(closeables);
        } catch (IOException e) {
            // do nothing

        }
    }
}