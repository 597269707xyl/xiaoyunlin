package com.zdtech.platform.framework.entity;

import javax.persistence.*;

/**
 * Created by huangbo on 2018/7/17.
 */
@Entity
@Table(name="sys_adapter_queue")
public class SysAdapterQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*@ManyToOne
    @JoinColumn(name="adapter_id")*/
    @Column(name = "adapter_id")
    private Long adapterId;
    //private SysAdapterMq sysAdapter;
    @Column(name = "type")
    private String type;
    @Column(name = "ip",length = 15)
    private String ip;
    @Column(name = "port")
    private Integer port;
    @Column(name = "queue_mgr",length = 50)
    private String queueMgr;
    @Column(name = "channel",length = 50)
    private String channel;
    @Column(name = "ccsid",length = 10)
    private String ccsid;
    @Column(name = "queue",length = 50)
    private String queue;
    @Column(name = "user")
    private String user;
    @Column(name = "password")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /*public SysAdapter getSysAdapter() {
        return sysAdapter;
    }

    public void setSysAdapter(SysAdapterMq sysAdapter) {
        this.sysAdapter = sysAdapter;
    }
*/

    public Long getAdapterId() {
        return adapterId;
    }

    public void setAdapterId(Long adapterId) {
        this.adapterId = adapterId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getQueueMgr() {
        return queueMgr;
    }

    public void setQueueMgr(String queueMgr) {
        this.queueMgr = queueMgr;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCcsid() {
        return ccsid;
    }

    public void setCcsid(String ccsid) {
        this.ccsid = ccsid;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
