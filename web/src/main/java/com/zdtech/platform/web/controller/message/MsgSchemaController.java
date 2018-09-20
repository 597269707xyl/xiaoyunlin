package com.zdtech.platform.web.controller.message;

import com.zdtech.platform.framework.entity.MsgSchemaFile;
import com.zdtech.platform.framework.entity.Result;
import com.zdtech.platform.framework.repository.MsgSchemaFileDao;
import com.zdtech.platform.framework.utils.FileUtils;
import com.zdtech.platform.framework.utils.HttpFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * Created by leepan on 2016/5/9.
 */
@Controller
@RequestMapping("/msg/schema")
public class MsgSchemaController {
    private static Logger logger = LoggerFactory.getLogger(MsgSchemaController.class);
    @Autowired
    private MsgSchemaFileDao msgSchemaFileDao;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list() {
        return "message/schema/schema-list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "message/schema/schema-add";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadResource(MultipartFile file) {
        String taskDocPath = "/atsp/schema";
        Result result = null;
        Boolean saved = HttpFiles.saveFile(taskDocPath, file, new HttpFiles.SaveFileCallback<Boolean>() {
            @Override
            public Boolean doInSaveFile(MultipartFile file, String filePath, String fileName) {
                MsgSchemaFile msgSchemaFile = new MsgSchemaFile(null, filePath, fileName, null);
                try {
                    msgSchemaFileDao.save(msgSchemaFile);
                } catch (Exception e) {
                    return false;
                }
                return true;
            }
        });
        if (saved != null && saved) {
            result = new Result(true, "");
            logger.info("@M|true|上传schema模板成功！");
        } else {
            result = new Result(false, "");
            logger.error("@M|false|上传schema模板失败！");
        }
        return result;
    }

    @RequestMapping(value = "/download")
    public void downloadResource(Long id, HttpServletResponse response) {
        MsgSchemaFile msgSchemaFile = msgSchemaFileDao.findOne(id);
        String filePath = msgSchemaFile.getPath();
        String fileName = msgSchemaFile.getFileName();
        HttpFiles.downloadFile(fileName, filePath, response);
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Result delResource(Long id) {
        Result result = null;
        MsgSchemaFile msgSchemaFile = msgSchemaFileDao.findOne(id);
        if (msgSchemaFile != null) {
            String filePath = msgSchemaFile.getPath();
            try {
                FileUtils.deleteQuietly(new File(filePath));
                msgSchemaFileDao.delete(msgSchemaFile);
                result = new Result(true, "");
                logger.info("@M|true|删除schema模板成功！");
            } catch (Exception e) {
                result = new Result(false, "删除schema模板失败!");
                logger.error("@M|false|删除schema模板失败！");
            }

        }
        return result;
/*        TaskResource res = resourceDao.findOne(resId);
        if (res != null) {
            boolean isFinished = taskService.isTaskFinished(res.getTaskId());
            if (isFinished) {
                result = new Result(false, "测试任务已经结束或者任务已经归档，不能删除该文档!");
                return result;
            }

            String filePath = res.getPath();
            try {
                FileUtils.deleteQuietly(new File(filePath));
                resourceDao.delete(res);
                result = new Result(true, "");
            } catch (Exception e) {
                logger.error("删除任务资源失败!", e);
                result = new Result(false, "删除任务资源失败!");
            }
        } else {
            result = new Result(true, "");
        }
        return result;*/
    }
}
