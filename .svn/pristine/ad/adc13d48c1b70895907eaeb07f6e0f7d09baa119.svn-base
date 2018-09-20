package com.zdtech.platform.framework.entity;

import com.zdtech.platform.framework.persistence.QueryDef;
import org.springframework.data.domain.Sort;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by huangbo on 2018/7/12.
 */
@Entity
@Table(name = "sys_adapter_tcp")
@QueryDef(queryTag = "sysAdapterTcp", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "sysAdapterTcpDao")
@PrimaryKeyJoinColumn(name = "id")
public class SysAdapterTcp extends SysAdapter {
    @Column(name="tcp_mode")
    private String tcpMode;
    @Column(name="client_ip")
    private String clientIp;
    @Column(name="client_port")
    private String clientPort;
    @Column(name="server_ip")
    private String serverIp;
    @Column(name="server_port")
    private String serverPort;
    @Column(name="heartbeat_flag")
    private boolean heartbeatFlag;
    @Column(name="heartbeat_recv_data")
    private String heartbeatRecvData;
    @Column(name="heartbeat_send_data")
    private String heartbeatSendData;

    public String getTcpMode() {
        return tcpMode;
    }

    public void setTcpMode(String tcpMode) {
        this.tcpMode = tcpMode;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getClientPort() {
        return clientPort;
    }

    public void setClientPort(String clientPort) {
        this.clientPort = clientPort;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
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

    public boolean isHeartbeatFlag() {
        return heartbeatFlag;
    }

    public void setHeartbeatFlag(boolean heartbeatFlag) {
        this.heartbeatFlag = heartbeatFlag;
    }
}
