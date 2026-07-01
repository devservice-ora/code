package model;

import java.io.Serializable;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "Department.findAll", query = "select o from Department o") })
@Table(name = "DEPARTMENTS")
public class Department implements Serializable {

    private static final long serialVersionUID = -9204072984524371646L;

    @Id
    @Column(name = "DEPARTMENT_ID", nullable = false)
    private Integer departmentId;

    @Column(name = "DEPARTMENT_NAME", nullable = false, length = 30)
    private String departmentName;


    @OneToMany(mappedBy = "department", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<JobHistory> JOB_HISTORYSet;

    @ManyToOne
    @JoinColumn(name = "LOCATION_ID")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "MANAGER_ID")
    private Employee employee2;

    @OneToMany(mappedBy = "department1", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Employee> EMPLOYEESSet;

    public Department() {}

    public Department(Integer departmentId, String departmentName, Location location, Employee employee2) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.location = location;
        this.employee2 = employee2;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }


    public Set<JobHistory> getJOB_HISTORYSet() {
        return JOB_HISTORYSet;
    }

    public void setJOB_HISTORYSet(Set<JobHistory> JOB_HISTORYSet) {
        this.JOB_HISTORYSet = JOB_HISTORYSet;
    }

    public JobHistory addJobHistory(JobHistory jobHistory) {
        getJOB_HISTORYSet().add(jobHistory);
        jobHistory.setDepartment(this);
        return jobHistory;
    }

    public JobHistory removeJobHistory(JobHistory jobHistory) {
        getJOB_HISTORYSet().remove(jobHistory);
        jobHistory.setDepartment(null);
        return jobHistory;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Employee getEmployee2() {
        return employee2;
    }

    public void setEmployee2(Employee employee2) {
        this.employee2 = employee2;
    }

    public Set<Employee> getEMPLOYEESSet() {
        return EMPLOYEESSet;
    }

    public void setEMPLOYEESSet(Set<Employee> EMPLOYEESSet) {
        this.EMPLOYEESSet = EMPLOYEESSet;
    }

    public Employee addEmployee(Employee employee) {
        getEMPLOYEESSet().add(employee);
        employee.setDepartment1(this);
        return employee;
    }

    public Employee removeEmployee(Employee employee) {
        getEMPLOYEESSet().remove(employee);
        employee.setDepartment1(null);
        return employee;
    }
}
