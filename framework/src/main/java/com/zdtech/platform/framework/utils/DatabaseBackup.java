package com.zdtech.platform.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by huangbo on 2016/10/20.
 */
public class DatabaseBackup {
    private static Logger logger = LoggerFactory.getLogger(DatabaseBackup.class);
    private String dumpPath;
    private String dbUserName;
    private String dbPassword;
    private String dbHost;

    public String getDumpPath() {
        return dumpPath;
    }

    public void setDumpPath(String dumpPath) {
        this.dumpPath = dumpPath;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public DatabaseBackup(String dumpPath, String dbUserName, String dbPassword, String dbHost) {
        if (!dumpPath.endsWith(File.separator)) {
            dumpPath = dumpPath + File.separator;
        }
        this.dumpPath = dumpPath;
        this.dbUserName = dbUserName;
        this.dbPassword = dbPassword;
        this.dbHost = dbHost;
    }

    public void backup(String backupFilePath, String dbName) {
        String command = "cmd /c " + dumpPath + "\\mysqldump -h" + dbHost + " -u" + dbUserName
                + " -p" + dbPassword + " --set-charset=utf8 " + dbName;
        PrintWriter p = null;
        BufferedReader reader = null;
        try {
            OutputStream outputStream = new FileOutputStream(backupFilePath);
            p = new PrintWriter(new OutputStreamWriter(outputStream, "utf8"));
            Process process = Runtime.getRuntime().exec(command);
            logger.info("备份命令：{}",command);
            InputStreamReader inputStreamReader = new InputStreamReader(process
                    .getInputStream(), "utf8");
            reader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = reader.readLine()) != null) {
                p.println(line);
            }
            p.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (p != null) {
                    p.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void restore(String filePath, String dbName) {
        String command = "cmd /c " + dumpPath + "\\mysql -h"+dbHost+" -u" + dbUserName + " -p" + dbPassword + " " + dbName + " < " + filePath;
        logger.info("恢复命令：{}",command);
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
