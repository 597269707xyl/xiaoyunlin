package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdtech.platform.framework.persistence.QueryDef;
import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;
import org.springframework.data.domain.Sort;
import org.springframework.format.datetime.DateFormatter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 仿真系统管理信息
 *
 * @author htma
 *
 */
@Entity
@Table(name = "sim_system")
@QueryDef(queryTag = "simSystem", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "simSystemDao")
@EntityListeners(value = EntityCodeListener.class)
public class SimSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 60, nullable = false)
    private String name;
    @Column(name = "version",length = 50)
    private String version;
    @Column(name = "protocol",length = 80)
    @FieldEnum(code = "PROTOCOL")
    private String protocol;

    @Transient
    private String protocolDis;

    @Column(name = "tcp_mode",length = 100)
    private String tcpMode;

    @Column(name = "msg_type",length = 100)
    @FieldEnum(code = "MSGTYPE")
    private String msgType;

    @Transient
    private String msgTypeDis;

    @Column(name = "md5_flag",length = 100)
    private Boolean md5Flag;

    @Column(name = "mac_flag",length = 100)
    private Boolean macFlag;
    @Column(name = "schema_flag",length = 100)
    private Boolean schemaFlag;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "msg_format_id")
    private MsgFormat msgFormat;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "msg_field_set_id")
    private MsgFieldSet headFieldSet;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd",locale ="zh",timezone = "GMT+8")
    @Column(name = "create_time",length = 100)
    private Date createTime;

    @Column(name = "descript",length = 100)
    private String descript;

    @OneToMany(mappedBy = "simSystem", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnore
    List<SimSystemInstance> instances = new ArrayList<>();


    public String getMsgTypeDis() {
        return msgTypeDis;
    }

    public void setMsgTypeDis(String msgTypeDis) {
        this.msgTypeDis = msgTypeDis;
    }

    public String getProtocolDis() {
        return protocolDis;
    }

    public void setProtocolDis(String protocolDis) {
        this.protocolDis = protocolDis;
    }


    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getTcpMode() {
        return tcpMode;
    }

    public void setTcpMode(String tcpMode) {
        this.tcpMode = tcpMode;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Boolean getMd5Flag() {
        return md5Flag;
    }

    public void setMd5Flag(Boolean md5Flag) {
        this.md5Flag = md5Flag;
    }

    public Boolean getMacFlag() {
        return macFlag;
    }

    public void setMacFlag(Boolean macFlag) {
        this.macFlag = macFlag;
    }

    public Boolean getSchemaFlag() {
        return schemaFlag;
    }

    public void setSchemaFlag(Boolean schemaFlag) {
        this.schemaFlag = schemaFlag;
    }

    public MsgFormat getMsgFormat() {
        return msgFormat;
    }

    public void setMsgFormat(MsgFormat msgFormat) {
        this.msgFormat = msgFormat;
    }

    public MsgFieldSet getHeadFieldSet() {
        return headFieldSet;
    }

    public void setHeadFieldSet(MsgFieldSet headFieldSet) {
        this.headFieldSet = headFieldSet;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<SimSystemInstance> getInstances() {
        return instances;
    }

    public void setInstances(List<SimSystemInstance> instances) {
        this.instances = instances;
    }

    @Override
    public String toString() {
        return "SimSystem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", protocol='" + protocol + '\'' +
                ", tcpMode='" + tcpMode + '\'' +
                ", msgType='" + msgType + '\'' +
                ", md5Flag=" + md5Flag +
                ", macFlag=" + macFlag +
                ", schemaFlag=" + schemaFlag +
                ", createTime=" + createTime +
                ", descript='" + descript + '\'' +
                '}';
    }


}
