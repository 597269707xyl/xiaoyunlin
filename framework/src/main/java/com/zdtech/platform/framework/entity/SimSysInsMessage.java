package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdtech.platform.framework.persistence.QueryDef;
import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SimSysInsMessage
 *
 * @author panli
 * @date 2016/5/17
 */
@Entity
@Table(name="sim_sysins_message")
@QueryDef(queryTag = "simSysInsMessage", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "simSysInsMessageDao")
@EntityListeners(value = EntityCodeListener.class)
public class SimSysInsMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String type;
    private String code;
    @Column(name="mesg_type")
    private String msgType;

    @Column(name="trs_code")
    private String trsCode;

    @Column(name="msg_type_code")
    @FieldEnum(code = "MSG_TYPE_CODE")
    private String msgTypeCode;

    @Transient
    private String msgTypeCodeDis;

    @Column(name="sign_flag")
    private Boolean signFlag;

    @Column(name="schema_flag")
    private Boolean schemaFlag;

    @Column(name="md5_flag")
    private Boolean md5Flag;

    @Column(name="mac_flag")
    private Boolean macFlag;

    @Column(name="model_file_path")
    private String modelFilePath;

    @Column(name="model_file_name")
    private String modelFileName;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "schema_file_id")
    private MsgSchemaFile schemaFile;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "system_ins_id", referencedColumnName = "id")
    private SimSystemInstance systemInstance;

    @OneToMany(mappedBy = "simSysInsMessage", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnore
    private List<SimSysInsMessageField> msgFields;

    @OneToMany(mappedBy = "pk.reqMessage", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<SimSysInsReplyRule> replyRuleSet = new HashSet<>();

    @Column(name="model_file_content")
    private String  modelFileContent;

    @Column(name="schema_file_content")
    private String  schemaFileContent;

    @FieldEnum(code = "STANDARD")
    private String standard;
    @Transient
    private String standardDis;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getTrsCode() {
        return trsCode;
    }

    public void setTrsCode(String trsCode) {
        this.trsCode = trsCode;
    }

    public String getMsgTypeCode() {
        return msgTypeCode;
    }

    public void setMsgTypeCode(String msgTypeCode) {
        this.msgTypeCode = msgTypeCode;
    }

    public Boolean getSignFlag() {
        return signFlag;
    }

    public void setSignFlag(Boolean signFlag) {
        this.signFlag = signFlag;
    }

    public Boolean getSchemaFlag() {
        return schemaFlag;
    }

    public void setSchemaFlag(Boolean schemaFlag) {
        this.schemaFlag = schemaFlag;
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

    public String getModelFilePath() {
        return modelFilePath;
    }

    public void setModelFilePath(String modelFilePath) {
        this.modelFilePath = modelFilePath;
    }

    public String getModelFileName() {
        return modelFileName;
    }

    public void setModelFileName(String modelFileName) {
        this.modelFileName = modelFileName;
    }

    public MsgSchemaFile getSchemaFile() {
        return schemaFile;
    }

    public void setSchemaFile(MsgSchemaFile schemaFile) {
        this.schemaFile = schemaFile;
    }

    public SimSystemInstance getSystemInstance() {
        return systemInstance;
    }

    public void setSystemInstance(SimSystemInstance systemInstance) {
        this.systemInstance = systemInstance;
    }

    public List<SimSysInsMessageField> getMsgFields() {
        return msgFields;
    }

    public void setMsgFields(List<SimSysInsMessageField> msgFields) {
        this.msgFields = msgFields;
    }

    public Set<SimSysInsReplyRule> getReplyRuleSet() {
        return replyRuleSet;
    }

    public void setReplyRuleSet(Set<SimSysInsReplyRule> replyRuleSet) {
        this.replyRuleSet = replyRuleSet;
    }

    public String getModelFileContent() {
        return modelFileContent;
    }

    public void setModelFileContent(String modelFileContent) {
        this.modelFileContent = modelFileContent;
    }

    public String getSchemaFileContent() {
        return schemaFileContent;
    }

    public void setSchemaFileContent(String schemaFileContent) {
        this.schemaFileContent = schemaFileContent;
    }

    public String getMsgTypeCodeDis() {
        return msgTypeCodeDis;
    }

    public void setMsgTypeCodeDis(String msgTypeCodeDis) {
        this.msgTypeCodeDis = msgTypeCodeDis;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getStandardDis() {
        return standardDis;
    }

    public void setStandardDis(String standardDis) {
        this.standardDis = standardDis;
    }
}
