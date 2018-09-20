package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdtech.platform.framework.persistence.QueryDef;
import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.util.List;

/**
 * Created by leepan on 2016/5/4.
 */
@Entity
@Table(name = "msg_data_type")
@QueryDef(queryTag = "msgDataType", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "msgDataTypeDao")
@EntityListeners(value = EntityCodeListener.class)
public class MsgDataType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "charset_id")
    @JsonIgnore
    private MsgCharset charset;

    @FieldEnum(code = "DATATYPE_FORMAT")
    private String type;

    @OneToMany(mappedBy = "dataType", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnore
    private List<MsgDataTypeEnum> datas;

    @Transient
    private String typeDis;

    private String descript;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MsgCharset getCharset() {
        return charset;
    }

    public void setCharset(MsgCharset charset) {
        this.charset = charset;
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

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public List<MsgDataTypeEnum> getDatas() {
        return datas;
    }

    public void setDatas(List<MsgDataTypeEnum> datas) {
        this.datas = datas;
    }
    @FieldEnum(code = "SERIAL")
    private String serial;

    @Transient
    private String serialDis;

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getSerialDis() {
        return serialDis;
    }

    public void setSerialDis(String serialDis) {
        this.serialDis = serialDis;
    }
}
