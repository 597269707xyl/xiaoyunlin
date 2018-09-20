package com.zdtech.platform.framework.service;

import com.zdtech.platform.framework.entity.Backup;
import com.zdtech.platform.framework.repository.BackupDao;
import com.zdtech.platform.framework.repository.SimSystemInstanceDao;
import com.zdtech.platform.framework.utils.DatabaseBackup;
import com.zdtech.platform.framework.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by huangbo on 2016/10/19.
 */
@Service
public class BackupService {
    @Value("${backup.path}")
    private String backPath;
    @Value("${dump.path}")
    private String dumpPath;
    @Value("${jdbc.username}")
    private String userName;
    @Value("${jdbc.password}")
    private String passWord;
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Autowired
    private BackupDao backupDao;
    @Autowired
    private UserService userService;
    @Autowired
    private SimSystemInstanceDao simSystemInstanceDao;

    public Backup get(Long id){
        return backupDao.findOne(id);
    }
    public boolean safeCheck(){
        int count  = simSystemInstanceDao.countOfNoFinished();
        return count < 1;
    }
    public void backupDatabase(String file) {
        String backupPath = this.backPath;
        FileUtils.createDir(backupPath);
        String dbUserName = this.userName;
        String dbPassword = this.passWord;
        String dbHost = getHostAndDbNameFromUrl(this.jdbcUrl).get(0);
        String dbName = getHostAndDbNameFromUrl(this.jdbcUrl).get(1);
        DatabaseBackup databaseBackup = new DatabaseBackup(dumpPath, dbUserName, dbPassword, dbHost);
        databaseBackup.backup(file, dbName);
    }
    public String saveBackup(){
        Backup backup = new Backup();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = format.format(new Date());
        String fileName = "atsp." + date + ".sql";
        backup.setFileName(fileName);
        backup.setCreateTime(date);
        String creatorName = userService.getCurrentUser().getName();
        backup.setCreator(creatorName);
        String backupPath = this.backPath;
        String backupFilePath = backupPath + "/"+fileName;
        backup.setPath(backupFilePath);
        backupDao.save(backup);
        return backup.getPath();
    }

    public void restoreDatabase(String filePath) {
        String dumpPath = this.dumpPath;
        String dbUserName = this.userName;
        String dbPassword = this.passWord;
        String dbHost = getHostAndDbNameFromUrl(this.jdbcUrl).get(0);
        String dbName = getHostAndDbNameFromUrl(this.jdbcUrl).get(1);
        DatabaseBackup databaseBackup = new DatabaseBackup(dumpPath, dbUserName, dbPassword, dbHost);
        databaseBackup.restore(filePath,dbName);
    }

    private List<String> getHostAndDbNameFromUrl(String url) {
        List<String> list = new ArrayList<>();
        url = url.substring(13);
        int i = url.indexOf(":");
        int j = url.indexOf("/");
        int k = url.indexOf("?");
        String dbHost = url.substring(0, i);
        String dbName = url.substring(j + 1, k);
        list.add(0, dbHost);
        list.add(1, dbName);
        return list;
    }

    public void delBackup(List<Long> idList) {
        if (idList == null || idList.size() < 1){
            return;
        }
        for (Long id:idList){
            Backup b = backupDao.findOne(id);
            if (b != null){
                String path = b.getPath();
                FileUtils.deleteQuietly(path);
                backupDao.delete(id);
            }
        }
    }
}
