package com.zdtech.platform.framework.entity;

import javax.persistence.*;

/**
 * Created by yjli on 2017/8/18.
 */
@Entity
@Table(name = "sim_recv_queue")
public class SimRecvQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "queue_name")
    private String queueNameRecv;
    @Column(name = "queue_manager")
    private String queueManagerRecv;
    @Column(name = "host_name")
    private String hostNameRecv;
    @Column(name = "port")
    private Integer portRecv;
    @Column(name = "channel")
    private String channelRecv;
    @Column(name = "CCSID")
    private Integer ccsidRecv;
    @Column(name = "user_id")
    private String userIdRecv;
    @Column(name = "password")
    private String passwordRecv;
    @Column(name = "receive_queue")
    private String receiveQueueRecv;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "instance_id")
    private SimSystemInstance simSystemInstance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQueueNameRecv() {
        return queueNameRecv;
    }

    public void setQueueNameRecv(String queueNameRecv) {
        this.queueNameRecv = queueNameRecv;
    }

    public String getQueueManagerRecv() {
        return queueManagerRecv;
    }

    public void setQueueManagerRecv(String queueManagerRecv) {
        this.queueManagerRecv = queueManagerRecv;
    }

    public String getHostNameRecv() {
        return hostNameRecv;
    }

    public void setHostNameRecv(String hostNameRecv) {
        this.hostNameRecv = hostNameRecv;
    }

    public Integer getPortRecv() {
        return portRecv;
    }

    public void setPortRecv(Integer portRecv) {
        this.portRecv = portRecv;
    }

    public String getChannelRecv() {
        return channelRecv;
    }

    public void setChannelRecv(String channelRecv) {
        this.channelRecv = channelRecv;
    }

    public Integer getCcsidRecv() {
        return ccsidRecv;
    }

    public void setCcsidRecv(Integer ccsidRecv) {
        this.ccsidRecv = ccsidRecv;
    }

    public String getUserIdRecv() {
        return userIdRecv;
    }

    public void setUserIdRecv(String userIdRecv) {
        this.userIdRecv = userIdRecv;
    }

    public String getPasswordRecv() {
        return passwordRecv;
    }

    public void setPasswordRecv(String passwordRecv) {
        this.passwordRecv = passwordRecv;
    }

    public String getReceiveQueueRecv() {
        return receiveQueueRecv;
    }

    public void setReceiveQueueRecv(String receiveQueueRecv) {
        this.receiveQueueRecv = receiveQueueRecv;
    }

    public SimSystemInstance getSimSystemInstance() {
        return simSystemInstance;
    }

    public void setSimSystemInstance(SimSystemInstance simSystemInstance) {
        this.simSystemInstance = simSystemInstance;
    }
}
