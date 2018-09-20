package com.zdtech.platform.framework.entity;

/**
 * Created by leepan on 2016/4/20.
 */
public class SysMenuModel {
    private Long id;
    private String text;
    private String iconCls;
    private String attributes;
    private String url;
    private String state;

    public SysMenuModel(Long id, String text, String iconCls, String url, String state, String attributes) {
        this.id = id;
        this.text = text;
        this.iconCls = iconCls;
        this.url = url;
        this.state = state;
        this.attributes = attributes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
