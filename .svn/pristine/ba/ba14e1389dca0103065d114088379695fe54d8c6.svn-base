package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdtech.platform.framework.persistence.QueryDef;
import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.util.List;

/**
 * Created by leepan on 2016/5/5.
 */
@Entity
@Table(name = "msg_field_set")
@QueryDef(queryTag = "msgFieldSet", genericQueryFields = {}, sortFields = {"msgType","setType"}, direction = Sort.Direction.DESC,
        daoName = "msgFieldSetDao")
@EntityListeners(value = EntityCodeListener.class)
public class MsgFieldSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String descript;

    @Column(name = "set_type")
    @FieldEnum(code = "FIELDSET_TYPE")
    private String setType;

    @Transient
    private String setTypeDis;

    @Column(name = "msg_type")
    @FieldEnum(code = "MSGTYPE")
    private String msgType;

    @Transient
    private String msgTypeDis;

    @OneToMany(mappedBy = "fieldSet", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @OrderBy("id asc")
    @JsonIgnore
    private List<MsgFieldSetComp> fields;

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

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getSetType() {
        return setType;
    }

    public void setSetType(String setType) {
        this.setType = setType;
    }

    public String getSetTypeDis() {
        return setTypeDis;
    }

    public void setSetTypeDis(String setTypeDis) {
        this.setTypeDis = setTypeDis;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgTypeDis() {
        return msgTypeDis;
    }

    public void setMsgTypeDis(String msgTypeDis) {
        this.msgTypeDis = msgTypeDis;
    }

    public List<MsgFieldSetComp> getFields() {
        return fields;
    }

    public void setFields(List<MsgFieldSetComp> fields) {
        this.fields = fields;
    }
}
