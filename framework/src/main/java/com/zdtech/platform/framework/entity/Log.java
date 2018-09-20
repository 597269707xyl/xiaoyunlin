package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdtech.platform.framework.persistence.QueryDef;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by admin on 2016/4/28.
 */
@Entity
@Table(name="sys_logs")
@QueryDef(queryTag = "log", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "logDao")
public class Log {

    public static enum Type{
        MenuPage,ActionBtn,RightKeyMenu
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 60)
    private Long userid;

    @Column(name = "user_name", length = 60)
    private String username;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd",locale ="zh",timezone = "GMT+8")
    @Column(name = "log_time")
    private Date logtime;

    @Column(name = "client_ip", length = 60)
    private String clientip;

    @Column(name = "level", length = 60)
    private String level;

    @Column(name = "type", length = 60)
    private String type;

    @Column(name = "url", length = 60)
    private String url;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getLogtime() {
        return logtime;
    }

    public void setLogtime(Date logtime) {
        this.logtime = logtime;
    }

    public String getClientip() {
        return clientip;
    }

    public void setClientip(String clientip) {
        this.clientip = clientip;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "content", length = 60)
    private String content;



}
