package com.zdtech.platform.framework.entity;

import com.zdtech.platform.framework.persistence.QueryDef;
import org.springframework.data.domain.Sort;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by huangbo on 2018/7/12.
 */
@Entity
@Table(name = "sys_adapter_http")
@QueryDef(queryTag = "sysAdapterHttp", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "sysAdapterHttpDao")
@PrimaryKeyJoinColumn(name = "id")
public class SysAdapterHttp extends SysAdapter {
    @Column(name = "remote_url")
    private String remoteUrl;
    @Column(name = "local_url")
    private String localUrl;

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }
}
