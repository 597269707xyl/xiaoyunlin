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
@Table(name = "msg_charset")
@QueryDef(queryTag = "msgCharset", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "msgCharsetDao")
@EntityListeners(value = EntityCodeListener.class)
public class MsgCharset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @FieldEnum(code = "MSGTYPE")
    @Column(name = "msg_type")
    private String msgType;

    @Transient
    private String msgTypeDis;

    @Column(name = "descript")
    private String descript;

    @Column(name = "identify")
    private String identify;

    @OneToMany(mappedBy = "charset", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JsonIgnore
    private List<MsgCharsetEnum> chars;

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

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public List<MsgCharsetEnum> getChars() {
        return chars;
    }

    public void setChars(List<MsgCharsetEnum> chars) {
        this.chars = chars;
    }
}
