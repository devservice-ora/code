package model;

import java.io.Serializable;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "Job.findAll", query = "select o from Job o") })
@Table(name = "JOBS")
public class Job implements Serializable {

    private static final long serialVersionUID = 7567654389623511918L;

    @Id
    @Column(name = "JOB_ID", nullable = false, length = 10)
    private String jobId;

    @Column(name = "JOB_TITLE", nullable = false, length = 35)
    private String jobTitle;

    @Column(name = "MAX_SALARY")
    private Integer maxSalary;

    @Column(name = "MIN_SALARY")
    private Integer minSalary;

    @OneToMany(mappedBy = "job", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<JobHistory> JOB_HISTORYSet;

    @OneToMany(mappedBy = "job1", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Employee> EMPLOYEESSet;

    public Job() {}

    public Job(String jobId, String jobTitle, Integer maxSalary, Integer minSalary) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.maxSalary = maxSalary;
        this.minSalary = minSalary;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Integer getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Integer maxSalary) {
        this.maxSalary = maxSalary;
    }

    public Integer getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Integer minSalary) {
        this.minSalary = minSalary;
    }

    public Set<JobHistory> getJOB_HISTORYSet() {
        return JOB_HISTORYSet;
    }

    public void setJOB_HISTORYSet(Set<JobHistory> JOB_HISTORYSet) {
        this.JOB_HISTORYSet = JOB_HISTORYSet;
    }

    public JobHistory addJobHistory(JobHistory jobHistory) {
        getJOB_HISTORYSet().add(jobHistory);
        jobHistory.setJob(this);
        return jobHistory;
    }

    public JobHistory removeJobHistory(JobHistory jobHistory) {
        getJOB_HISTORYSet().remove(jobHistory);
        jobHistory.setJob(null);
        return jobHistory;
    }

    public Set<Employee> getEMPLOYEESSet() {
        return EMPLOYEESSet;
    }

    public void setEMPLOYEESSet(Set<Employee> EMPLOYEESSet) {
        this.EMPLOYEESSet = EMPLOYEESSet;
    }

    public Employee addEmployee(Employee employee) {
        getEMPLOYEESSet().add(employee);
        employee.setJob1(this);
        return employee;
    }

    public Employee removeEmployee(Employee employee) {
        getEMPLOYEESSet().remove(employee);
        employee.setJob1(null);
        return employee;
    }
}
