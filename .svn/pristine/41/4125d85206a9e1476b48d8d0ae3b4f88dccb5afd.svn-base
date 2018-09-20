package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdtech.platform.framework.persistence.QueryDef;
import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by admin on 2016/5/9.
 */
@Entity
@Table(name = "test_project")
@QueryDef(queryTag = "testProject", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "testProjectDao")
@EntityListeners(value = EntityCodeListener.class)
public class TestProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "instance_id")
    private SimSystemInstance instance;

    @Column(name = "name")
    private String name;

    @Column(name = "project_no")
    private String abb;

    @Column(name = "test_system")
    private String testsystem;


    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",locale ="zh",timezone = "GMT+8")
    @Column(name = "start_time")
    private Date starttime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",locale ="zh",timezone = "GMT+8")
    @Column(name = "end_time")
    private Date endtime;

    @Column(name = "state")
    private String state;

    @Column(name = "descript")
    private String descript;

    @Column(name = "type")
    @FieldEnum(code = "PROJECT_TYPE")
    private String type;

    @Transient
    private String typeDis;

    @OneToMany(mappedBy = "pk.project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<TestProjectUser> projectUsers = new HashSet<>();

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

    public String getTestsystem() {
        return testsystem;
    }

    public void setTestsystem(String testsystem) {
        this.testsystem = testsystem;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public Set<TestProjectUser> getProjectUsers() {
        return projectUsers;
    }

    public void setProjectUsers(Set<TestProjectUser> projectUsers) {
        this.projectUsers = projectUsers;
    }

    public String getAbb() {
        return abb;
    }

    public void setAbb(String abb) {
        this.abb = abb;
    }

    public SimSystemInstance getInstance() {
        return instance;
    }

    public void setInstance(SimSystemInstance instance) {
        this.instance = instance;
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
}
