package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdtech.platform.framework.persistence.QueryDef;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lcheng
 * @version 1.0
 *          ${tags}
 */
@Entity
@Table(name = "sys_roles")
@QueryDef(queryTag = "role", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "roleDao")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",length = 60)
    private String name;

    @Column(name = "role",length = 60,nullable = false)
    private String role;
    @Column(name = "notes",length = 100)
    private String notes;

    @ManyToMany
    @JoinTable(name = "sys_role_func",joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "func_id"))
    @OrderBy("level asc,seqNo asc")
    @JsonIgnore
    private List<Func> funcs = new ArrayList<>();
    @ManyToMany(mappedBy = "roles")
//    @JoinTable(name = "s_user_role",joinColumns = @JoinColumn(name = "role_id"),
//            inverseJoinColumns = @JoinColumn(name = "sys_user_id"))
    @JsonIgnore
    private List<SysUser> users=new ArrayList<>();

    public List<SysUser> getUsers() {
        return users;
    }

    public void setUsers(List<SysUser> users) {
        this.users = users;
    }

    public Role(){}

    public Role(String name, String role, String notes) {
        this.name = name;
        this.role = role;
        this.notes = notes;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Func> getFuncs() {
        return funcs;
    }

    public void setFuncs(List<Func> funcs) {
        this.funcs = funcs;
    }
}
