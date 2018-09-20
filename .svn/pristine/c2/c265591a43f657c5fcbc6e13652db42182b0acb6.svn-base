package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdtech.platform.framework.persistence.QueryDef;
import com.zdtech.platform.framework.ui.FieldEnum;
import com.zdtech.platform.framework.utils.EntityCodeListener;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 人员信息
 *
 * @author lcheng
 * @version 1.0
 */
@Entity
@Table(name = "sys_users")
@QueryDef(queryTag = "user", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "userDao")
@EntityListeners(value = EntityCodeListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 60, nullable = false)
    private String name;
    @Column(name = "dept",length = 50)
    private String dept;
    @Column(name = "title",length = 80)
    private String title;

    @FieldEnum(code = "DEGREE")
    private String degree;

    @Transient
    private String degreeDis;

    @Column(name = "school",length = 100)
    private String school;

    @FieldEnum(code = "SEX")
    private String sex;

    @Transient
    private String sexDis;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd",locale ="zh",timezone = "GMT+8")
    private Date bod;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd",locale ="zh",timezone = "GMT+8")
    @Column(name = "hire_date")
    private Date hireDate;

    @Column(name = "phone_num", length = 13)
    private String phoneNum;
    @Column(name = "tel_num", length = 13)
    private String telNum;


    @OneToOne()
    @JoinColumn(name = "sysuser_id")
    private SysUser sysUser;

    @Lob()
    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    private byte[] img;

    /*@ManyToMany
    @JoinTable(name = "sys_users_tasks",joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id"))
    @OrderBy("level asc,seqNo asc")
    @JsonIgnore
    private List<TestTask> testTasks = new ArrayList<>();*/

    public User(){}

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

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBod() {
        return bod;
    }

    public void setBod(Date bod) {
        this.bod = bod;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getSexDis() {
        return sexDis;
    }

    public void setSexDis(String sexDis) {
        this.sexDis = sexDis;
    }

    public String getDegreeDis() {
        return degreeDis;
    }

    public void setDegreeDis(String degreeDis) {
        this.degreeDis = degreeDis;
    }

    /*public List<TestTask> getTestTasks() {
        return testTasks;
    }

    public void setTestTasks(List<TestTask> testTasks) {
        this.testTasks = testTasks;
    }*/
}
