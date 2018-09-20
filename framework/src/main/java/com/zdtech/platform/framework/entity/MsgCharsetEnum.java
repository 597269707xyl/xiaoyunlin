package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;

import javax.persistence.*;

/**
 * Created by leepan on 2016/5/4.
 */
@Entity
@Table(name = "msg_charset_enum")
@EntityListeners(value = EntityCodeListener.class)
public class MsgCharsetEnum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private String value;

    @FieldEnum(code = "DATATYPE_TYPE")
    private String type;

    @Transient
    private String typeDis;

    @Column(name = "seq_no")
    private Integer seqNo;

    @ManyToOne
    @JoinColumn(name="charset_id")
    @JsonIgnore
    private MsgCharset charset;

    public MsgCharset getCharset() {
        return charset;
    }

    public void setCharset(MsgCharset charset) {
        this.charset = charset;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeDis() {
        return typeDis;
    }

    public void setTypeDis(String typeDis) {
        this.typeDis = typeDis;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }
}
