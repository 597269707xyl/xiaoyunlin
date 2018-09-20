package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdtech.platform.framework.persistence.QueryDef;
import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by huangbo on 2016/5/9.
 */
@Entity
@Table(name = "sim_system_instance")
@QueryDef(queryTag = "simSystemInstance", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "simSystemInstanceDao")
@EntityListeners(value = EntityCodeListener.class)
public class SimSystemInstance {
    public enum SysInsState {
        Finishing, Finished, Starting,Started
    }
    public enum SysInsConnectStatus {
        DisConnect,Connected
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "adapter_id")
    private SysAdapter adapter;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "system_id")
    private SimSystem simSystem;

    @Column(name = "name")
    private String name;

    @Column(name = "test_environment")
    @FieldEnum(code = "TEST_ENVIRONMENT_LIST")
    private String testEnvironment;

    @Transient
    private String testEnvironmentDis;

    @Column(name = "tcp_mode")
    private String tcpMode;

    @Column(name = "client_ip")
    private String clientIp;

    @Column(name = "client_port")
    private Integer clientPort;

    @Column(name = "server_ip")
    private String serverIp;

    @Column(name = "server_port")
    private Integer serverPort;

    @Column(name = "heartbeat_flag")
    private Boolean heartbeatFlag;

    @Column(name = "heartbeat_recv_data")
    private String heartbeatRecvData;

    @Column(name = "heartbeat_send_data")
    private String heartbeatSendData;


    @Column(name = "send_queue")
    private String sendQueue;

    @Column(name = "imme_send_queue")
    private String immeSendQueue;

    @Column(name = "receive_queue")
    private String receiveQueue;

    @Column(name = "imme_recv_queue")
    private String immeRecvQueue;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd",locale ="zh",timezone = "GMT+8")
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "ins_server_ip")
    private String insServerIp;

    @Column(name = "ins_server_port")
    private Integer insServerPort;

    @Column(name = "descript")
    private String descript;

    @Enumerated()
    private SysInsState state = SysInsState.Finished;
    @Enumerated()
    @Column(name = "connect_status")
    private SysInsConnectStatus connectStatus = SysInsConnectStatus.DisConnect;

    @Column(name = "connect_flag")
    private Boolean connectFlag;

    @Column(name = "ip")
    private String ip;

    @Column(name = "queue_manager")
    private String queueManager;

    @Column(name = "host_name")
    private String hostName;

    @Column(name = "port")
    private Integer port;

    @Column(name = "channel")
    private String channel;

    @Column(name = "CCSID")
    private Integer ccsid;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "password")
    private String password;

    @Column(name = "remote_url")
    private String remoteUrl;

    @Column(name = "local_url")
    private String localUrl;

    @OneToMany(mappedBy = "simSystemInstance", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnore
    private List<SimRecvQueue> simRecvQueueData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SysAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(SysAdapter adapter) {
        this.adapter = adapter;
    }

    public SimSystem getSimSystem() {
        return simSystem;
    }

    public void setSimSystem(SimSystem simSystem) {
        this.simSystem = simSystem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    public void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public Boolean getHeartbeatFlag() {
        return heartbeatFlag;
    }

    public void setHeartbeatFlag(Boolean heartbeatFlag) {
        this.heartbeatFlag = heartbeatFlag;
    }

    public String getHeartbeatRecvData() {
        return heartbeatRecvData;
    }

    public void setHeartbeatRecvData(String heartbeatRecvData) {
        this.heartbeatRecvData = heartbeatRecvData;
    }

    public String getHeartbeatSendData() {
        return heartbeatSendData;
    }

    public void setHeartbeatSendData(String heartbeatSendData) {
        this.heartbeatSendData = heartbeatSendData;
    }

    public String getSendQueue() {
        return sendQueue;
    }

    public void setSendQueue(String sendQueue) {
        this.sendQueue = sendQueue;
    }

    public String getReceiveQueue() {
        return receiveQueue;
    }

    public void setReceiveQueue(String receiveQueue) {
        this.receiveQueue = receiveQueue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getInsServerIp() {
        return insServerIp;
    }

    public void setInsServerIp(String insServerIp) {
        this.insServerIp = insServerIp;
    }

    public Integer getInsServerPort() {
        return insServerPort;
    }

    public void setInsServerPort(Integer insServerPort) {
        this.insServerPort = insServerPort;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public SysInsState getState() {
        return state;
    }

    public void setState(SysInsState state) {
        this.state = state;
    }

    public Boolean getConnectFlag() {
        return connectFlag;
    }

    public void setConnectFlag(Boolean connectFlag) {
        this.connectFlag = connectFlag;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public SysInsConnectStatus getConnectStatus() {
        return connectStatus;
    }

    public void setConnectStatus(SysInsConnectStatus connectStatus) {
        this.connectStatus = connectStatus;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getTcpMode() {
        return tcpMode;
    }

    public void setTcpMode(String tcpMode) {
        this.tcpMode = tcpMode;
    }

    public String getQueueManager() {
        return queueManager;
    }

    public void setQueueManager(String queueManager) {
        this.queueManager = queueManager;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getCcsid() {
        return ccsid;
    }

    public void setCcsid(Integer ccsid) {
        this.ccsid = ccsid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImmeSendQueue() {
        return immeSendQueue;
    }

    public void setImmeSendQueue(String immeSendQueue) {
        this.immeSendQueue = immeSendQueue;
    }

    public String getImmeRecvQueue() {
        return immeRecvQueue;
    }

    public void setImmeRecvQueue(String immeRecvQueue) {
        this.immeRecvQueue = immeRecvQueue;
    }

    public String getTestEnvironment() {
        return testEnvironment;
    }

    public void setTestEnvironment(String testEnvironment) {
        this.testEnvironment = testEnvironment;
    }

    public String getTestEnvironmentDis() {
        return testEnvironmentDis;
    }

    public void setTestEnvironmentDis(String testEnvironmentDis) {
        this.testEnvironmentDis = testEnvironmentDis;
    }

    public List<SimRecvQueue> getSimRecvQueueData() {
        return simRecvQueueData;
    }

    public void setSimRecvQueueData(List<SimRecvQueue> simRecvQueueData) {
        this.simRecvQueueData = simRecvQueueData;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }
}
