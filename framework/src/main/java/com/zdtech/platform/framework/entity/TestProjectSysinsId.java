package com.zdtech.platform.framework.entity;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by huangbo on 2016/5/11.
 */
@Embeddable
public class TestProjectSysinsId implements Serializable{
    @ManyToOne
    private TestProject testProject;
    @ManyToOne
    private SimSystemInstance simSystemInstance;


    public TestProjectSysinsId() {
    }

    public TestProjectSysinsId(TestProject testProject, SimSystemInstance simSystemInstance) {
        this.testProject = testProject;
        this.simSystemInstance = simSystemInstance;
    }

    public TestProject getTestProject() {
        return testProject;
    }

    public void setTestProject(TestProject testProject) {
        this.testProject = testProject;
    }

    public SimSystemInstance getSimSystemInstance() {
        return simSystemInstance;
    }

    public void setSimSystemInstance(SimSystemInstance simSystemInstance) {
        this.simSystemInstance = simSystemInstance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestProjectSysinsId)) return false;

        TestProjectSysinsId that = (TestProjectSysinsId) o;

        if (getTestProject() != null ? !getTestProject().equals(that.getTestProject()) : that.getTestProject() != null)
            return false;
        return getSimSystemInstance() != null ? getSimSystemInstance().equals(that.getSimSystemInstance()) : that.getSimSystemInstance() == null;

    }

    @Override
    public int hashCode() {
        int result = getTestProject() != null ? getTestProject().hashCode() : 0;
        result = 31 * result + (getSimSystemInstance() != null ? getSimSystemInstance().hashCode() : 0);
        return result;
    }
}
