package com.zdtech.platform.framework.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * User: qfxu
 * Date: 14-1-15
 */
@Entity
@Table(name = "sys_seq")
public class Seq implements Serializable {

    @Id
    private String sid;
    private long nextval;

    public Seq() {
    }

    public Seq(String sid, long nextval) {
        this.sid = sid;
        this.nextval = nextval;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public long getNextval() {
        return nextval;
    }

    public void setNextval(long nextval) {
        this.nextval = nextval;
    }
}
