package com.zdtech.platform.framework.entity;

import com.zdtech.platform.framework.persistence.QueryDef;
import com.zdtech.platform.framework.utils.EntityCodeListener;
import org.springframework.data.domain.Sort;

import javax.persistence.*;

/**
 * Created by wx on 2017/10/12.
 */
@Entity
@Table(name = "sys_exec_server")
@QueryDef(queryTag = "sysExecServer", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "sysExecServerDao")
@EntityListeners(value = EntityCodeListener.class)
public class SysExecServer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "ip", length = 15)
    private String ip;

    @Column(name="port")
    private Integer port;

    public Long getId() { return id;}

    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getIp() {return ip;}

    public void setIp(String ip) {this.ip = ip;}

    public Integer getPort() {return port;}

    public void setPort(Integer port) {this.port = port;}
}
