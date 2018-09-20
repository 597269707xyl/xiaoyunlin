package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wzx on 2016/11/3.
 */
@Entity
@Table(name = "sys_logs")
public class SysLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;

    @FieldCell(cellIndex =0)
    @Column(name = "user_name",length = 50)
    private String userName;

    @FieldCell(cellIndex =3)
    @Column(name = "client_ip",length = 15)
    private String clientIp;

    @FieldCell(cellIndex =2)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd hh:mm:ss",locale ="zh",timezone = "GMT+8")
    @Column(name = "log_time")
    private Date logTime;

    @FieldCell(cellIndex =4)
    @Column(name = "url",length = 100)
    private String url;

    @FieldCell(cellIndex =1,needConvert = true,
            convertValues = {"S:系统管理", "A:自动化测试", "M:报文数据管理","C:仿真配置管理","P:测试项目管理","T:仿真测试执行"})
    @Column(name = "type",length = 2)
    private String type;

    @Column(length = 8)
    private String level;

    @FieldCell(cellIndex =5)
    @Column(length = 100)
    private String content;
    private boolean result;

    public SysLog(){}

    public SysLog(Long userId, String userName, String clientIp, String url,
                  String level, String logType, String content, boolean result){
        this.userId = userId;
        this.userName = userName;
        this.url = url;
        this.clientIp = clientIp;
        this.level = level;
        this.type = logType;
        this.content = content;
        this.result = result;
        this.logTime = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
