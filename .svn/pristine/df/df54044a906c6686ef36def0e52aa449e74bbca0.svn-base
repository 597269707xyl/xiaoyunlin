package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zdtech.platform.framework.persistence.QueryDef;
import org.springframework.data.domain.Sort;

import javax.persistence.*;

/**
 * @author lcheng
 * @version 1.0
 *          ${tags}
 */
@Entity
@Table(name = "sys_funcs")
public class Func {

    public static enum Type{
        MenuPage,ActionBtn,RightKeyMenu
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 60)
    private String name;

    private String url;
    private Long parent;
    @Column(name = "seq_no")
    private Integer seqNo;
    private Integer level;
    @JsonProperty("isLeaf")
    private boolean leaf;

    @Column(name = "icon_cls",length = 80)
    private String iconCls;

    @Enumerated(EnumType.ORDINAL)
    private Type type;

    @Column(name = "action",length = 100)
    private String action;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
