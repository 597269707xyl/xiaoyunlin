package com.zdtech.platform.framework.entity;

import java.util.List;

/**
 * Created by leepan on 2016/4/27.
 */
public class TreeNodeModel {
    private String id;
    private String text;
    private String parentId;
    private String state;
    private String attributes;
    private String iconCls;
    private String url;
    private List<TreeNodeModel> children;

    public TreeNodeModel() {
    }

    public TreeNodeModel(String id, String text, String parentId) {
        this.id = id;
        this.text = text;
        this.parentId = parentId;
    }

    public TreeNodeModel(String id, String text, String parentId, String state) {
        this.id = id;
        this.text = text;
        this.parentId = parentId;
        this.state = state;
    }

    public TreeNodeModel(String id, String text, String iconCls, String url, String state, String attributes) {
        this.id = id;
        this.text = text;
        this.iconCls = iconCls;
        this.url = url;
        this.state = state;
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public List<TreeNodeModel> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNodeModel> children) {
        this.children = children;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
