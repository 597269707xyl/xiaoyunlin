package com.zdtech.platform.framework.entity;

import com.zdtech.platform.framework.persistence.QueryDef;
import org.springframework.data.domain.Sort;

import javax.persistence.*;

/**
 * Created by huangbo on 2018/7/12.
 */
@Entity
@Table(name = "sys_adapter")
@Inheritance(strategy = InheritanceType.JOINED)
@QueryDef(queryTag = "sysAdapter", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "sysAdapterDao")
public class SysAdapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "adapter_name",length=50)
    private String name;
    @Column(name="adapter_type",length=50)
    private String adapterType;
    @Column(name="adapter_status")
    private Boolean adapterStatus;
    @Column(name= "response_model")
    private Boolean responseModel;
    @Column(name="description")
    private String description;
    @Column(name="internal_time")
    private Integer internalTime;

    public Boolean getResponseModel() {
        return responseModel;
    }

    public void setResponseModel(Boolean responseModel) {
        this.responseModel = responseModel;
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

    public String getAdapterType() {
        return adapterType;
    }

    public void setAdapterType(String adapterType) {
        this.adapterType = adapterType;
    }

    public Boolean getAdapterStatus() {
        return adapterStatus;
    }

    public void setAdapterStatus(Boolean adapterStatus) {
        this.adapterStatus = adapterStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getInternalTime() {
        return internalTime;
    }

    public void setInternalTime(Integer internalTime) {
        this.internalTime = internalTime;
    }
}
