package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdtech.platform.framework.persistence.QueryDef;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by huangbo on 2018/8/1.
 */
@Entity
@Table(name = "nxy_defect")
@QueryDef(queryTag = "nxyDefect", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "nxyDefectDao")
public class NxyDefect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private TestProject testProject;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private NxyFuncItem nxyFuncItem;

    @ManyToOne
    @JoinColumn(name = "usecase_id", referencedColumnName = "id")
    private NxyFuncUsecase nxyFuncUsecase;

    private String mark;

    private String descript;

    @Column(name = "fix_status")
    private Boolean fixStatus;

    private String grade;

    private String type;

    private String abbreviation;

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_user")
    private String createUser;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @Column(name = "fix_time")
    private Date fixTime;

    @Column(name = "fix_user")
    private String fixUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestProject getTestProject() {
        return testProject;
    }

    public void setTestProject(TestProject testProject) {
        this.testProject = testProject;
    }

    public NxyFuncItem getNxyFuncItem() {
        return nxyFuncItem;
    }

    public void setNxyFuncItem(NxyFuncItem nxyFuncItem) {
        this.nxyFuncItem = nxyFuncItem;
    }

    public NxyFuncUsecase getNxyFuncUsecase() {
        return nxyFuncUsecase;
    }

    public void setNxyFuncUsecase(NxyFuncUsecase nxyFuncUsecase) {
        this.nxyFuncUsecase = nxyFuncUsecase;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public Boolean getFixStatus() {
        return fixStatus;
    }

    public void setFixStatus(Boolean fixStatus) {
        this.fixStatus = fixStatus;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getFixTime() {
        return fixTime;
    }

    public void setFixTime(Date fixTime) {
        this.fixTime = fixTime;
    }

    public String getFixUser() {
        return fixUser;
    }

    public void setFixUser(String fixUser) {
        this.fixUser = fixUser;
    }
}
