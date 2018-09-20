package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;

import javax.persistence.*;
import java.util.List;

/**
 * Created by leepan on 2016/5/9.
 */
@Entity
@Table(name = "msg_format_comp")
@EntityListeners(value = EntityCodeListener.class)
public class MsgFormatComp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="format_id")
    @JsonIgnore
    private MsgFormat msgFormat;

    @FieldEnum(code = "MSG_FORMAT_COMP")
    @Column(name="type")
    private String typeComp;
    @Transient
    private String typeCompDis;

    @Column(name = "seq_no")
    private Integer seqNo;

    @OneToMany(mappedBy = "formatComp", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @OrderBy("seqNo asc")
    @JsonIgnore
    private List<MsgFormatCompField> fields;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MsgFormat getMsgFormat() {
        return msgFormat;
    }

    public void setMsgFormat(MsgFormat msgFormat) {
        this.msgFormat = msgFormat;
    }

    public String getTypeComp() {
        return typeComp;
    }

    public void setTypeComp(String typeComp) {
        this.typeComp = typeComp;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public List<MsgFormatCompField> getFields() {
        return fields;
    }

    public void setFields(List<MsgFormatCompField> fields) {
        this.fields = fields;
    }


    public String getTypeCompDis() {
        return typeCompDis;
    }

    public void setTypeCompDis(String typeCompDis) {
        this.typeCompDis = typeCompDis;
    }
}
