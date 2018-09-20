package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdtech.platform.framework.persistence.QueryDef;
import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.util.List;

/**
 * Created by htma on 2016/5/9.
 */
@Entity
@Table(name = "msg_format")
@QueryDef(queryTag = "msgFormat", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "msgFormatDao")
@EntityListeners(value = EntityCodeListener.class)
public class MsgFormat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @FieldEnum(code = "MSGTYPE")
    private String type;
    @Transient
    private String typeDis;

    @Column(name = "descript")
    private String descript;

    @OneToMany(mappedBy = "msgFormat", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @OrderBy("seqNo asc")
    @JsonIgnore
    private List<MsgFormatComp> comps;

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

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public List<MsgFormatComp> getComps() {
        return comps;
    }

    public void setComps(List<MsgFormatComp> comps) {
        this.comps = comps;
    }

    public String getTypeDis() {
        return typeDis;
    }

    public void setTypeDis(String typeDis) {
        this.typeDis = typeDis;
    }
}
