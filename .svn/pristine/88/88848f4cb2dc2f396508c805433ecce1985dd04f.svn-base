package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdtech.platform.framework.persistence.QueryDef;
import org.springframework.data.domain.Sort;

import javax.persistence.*;

/**
 * MsgFieldCode
 *
 * @author panli
 * @date 2017/3/3
 */
@Entity
@Table(name = "sim_message_field_codes")
@QueryDef(queryTag = "simMessageFieldCode", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "simMsgFieldCodeDao")
public class SimMsgFieldCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="field_id")
    @JsonIgnore
    private SimMessageField msgField;

    @Column(name="`key`")
    private String key;

    @Column(name="`value`")
    private String value;

    @Column(name="seq_no")
    private Integer seqNo;

    @Column(name="parent")
    private Long parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SimMessageField getMsgField() {
        return msgField;
    }

    public void setMsgField(SimMessageField msgField) {
        this.msgField = msgField;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }
}
