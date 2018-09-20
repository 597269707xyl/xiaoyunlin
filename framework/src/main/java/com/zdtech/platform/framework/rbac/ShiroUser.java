package com.zdtech.platform.framework.rbac;

import java.io.Serializable;

/**
 * @author lcheng
 * @version 1.0
 *          ${tags}
 */
public class ShiroUser implements Serializable{

    private Long id;
    private String userName;
    private String name;
    private Long userId;


    public ShiroUser(Long id,String userName) {
        this.userName = userName;
        this.id = id;
    }

    public ShiroUser(Long id, Long userId, String userName, String name) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

