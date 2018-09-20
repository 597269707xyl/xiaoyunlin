package com.zdtech.platform.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by leepan on 2016/5/10.
 */
@Embeddable
public class TestProjectUserId  implements Serializable {
    @ManyToOne
//    @JsonIgnore
    private TestProject project;
    @ManyToOne
    private User user;

    public TestProjectUserId() {
    }

    public TestProjectUserId(TestProject project, User user) {
        this.project = project;
        this.user = user;
    }

    public TestProject getProject() {
        return project;
    }

    public void setProject(TestProject project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestProjectUserId)) return false;

        TestProjectUserId that = (TestProjectUserId) o;

        if (getProject() != null ? !getProject().equals(that.getProject()) : that.getProject() != null) return false;
        return getUser() != null ? getUser().equals(that.getUser()) : that.getUser() == null;

    }

    @Override
    public int hashCode() {
        int result = getProject() != null ? getProject().hashCode() : 0;
        result = 31 * result + (getUser() != null ? getUser().hashCode() : 0);
        return result;
    }
}
