package com.zdtech.platform.framework.entity;

import javax.persistence.*;

/**
 * Created by leepan on 2016/5/10.
 */
@Entity
@Table(name = "test_project_user")
@AssociationOverrides({
        @AssociationOverride(name="pk.project",joinColumns = @JoinColumn(name = "project_id")),
        @AssociationOverride(name = "pk.user",joinColumns = @JoinColumn(name = "user_id"))
})
public class TestProjectUser {
    @EmbeddedId
    private TestProjectUserId pk = new TestProjectUserId();

    public TestProjectUser() {
    }

    public TestProjectUser(TestProjectUserId pk) {
        this.pk = pk;
    }

    public TestProjectUserId getPk() {
        return pk;
    }

    public void setPk(TestProjectUserId pk) {
        this.pk = pk;
    }

    @Transient
    public TestProject getTestProject(){
        return pk.getProject();
    }

    public void setTestProject(TestProject project){
        pk.setProject(project);
    }

    @Transient
    public User getUser(){
        return pk.getUser();
    }

    public void setUser(User user){
        pk.setUser(user);
    }
}
