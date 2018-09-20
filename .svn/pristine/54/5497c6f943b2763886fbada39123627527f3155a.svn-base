package com.zdtech.platform.framework.entity;

import com.zdtech.platform.framework.persistence.QueryDef;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by huangbo on 2018/7/12.
 */
@Entity
@Table(name = "sys_adapter_mq")
@QueryDef(queryTag = "sysAdapterMq", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "sysAdapterMqDao")
@PrimaryKeyJoinColumn(name = "id")
public class SysAdapterMq extends SysAdapter {

    @Column(name = "bank_no")
    private String bankNo;


    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }
}
