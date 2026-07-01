package model;

import java.io.Serializable;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ @NamedQuery(name = "Employee.findAll", query = "select o from Employee o") })
@Table(name = "EMPLOYEES")
public class Employee implements Serializable {

    private static final long serialVersionUID = -4380364559051133679L;

    @Column(name = "COMMISSION_PCT")
    private Integer commissionPct;


    @Column(nullable = false, unique = true, length = 25)
    private String email;

    @Id
    @Column(name = "EMPLOYEE_ID", nullable = false)
    private Integer employeeId;

    @Column(name = "FIRST_NAME", length = 20)
    private String firstName;

    @Temporal(TemporalType.DATE)
    @Column(name = "HIRE_DATE", nullable = false)
    private Date hireDate;


    @Column(name = "LAST_NAME", nullable = false, length = 25)
    private String lastName;


    @Column(name = "PHONE_NUMBER", length = 20)
    private String phoneNumber;

    private Integer salary;

    @ManyToOne
    @JoinColumn(name = "MANAGER_ID")
    private Employee employee;

    @OneToMany(mappedBy = "employee", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Employee> EMPLOYEESSet;

    @OneToMany(mappedBy = "employee2", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Department> DEPARTMENTSSet;

    @OneToMany(mappedBy = "employee1", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<JobHistory> JOB_HISTORYSet;

    @ManyToOne
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department1;

    @ManyToOne
    @JoinColumn(name = "JOB_ID")
    private Job job1;

    public Employee() {}

    public Employee(Integer commissionPct, Department department1, String email, Integer employeeId, String firstName,
                    Date hireDate, Job job1, String lastName, Employee employee, String phoneNumber, Integer salary) {
        this.commissionPct = commissionPct;
        this.department1 = department1;
        this.email = email;
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.hireDate = hireDate;
        this.job1 = job1;
        this.lastName = lastName;
        this.employee = employee;
        this.phoneNumber = phoneNumber;
        this.salary = salary;
    }

    public Integer getCommissionPct() {
        return commissionPct;
    }

    public void setCommissionPct(Integer commissionPct) {
        this.commissionPct = commissionPct;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Set<Employee> getEMPLOYEESSet() {
        return EMPLOYEESSet;
    }

    public void setEMPLOYEESSet(Set<Employee> EMPLOYEESSet) {
        this.EMPLOYEESSet = EMPLOYEESSet;
    }

    public Employee addEmployee(Employee employee) {
        getEMPLOYEESSet().add(employee);
        employee.setEmployee(this);
        return employee;
    }

    public Employee removeEmployee(Employee employee) {
        getEMPLOYEESSet().remove(employee);
        employee.setEmployee(null);
        return employee;
    }

    public Set<Department> getDEPARTMENTSSet() {
        return DEPARTMENTSSet;
    }

    public void setDEPARTMENTSSet(Set<Department> DEPARTMENTSSet) {
        this.DEPARTMENTSSet = DEPARTMENTSSet;
    }

    public Department addDepartment(Department department) {
        getDEPARTMENTSSet().add(department);
        department.setEmployee2(this);
        return department;
    }

    public Department removeDepartment(Department department) {
        getDEPARTMENTSSet().remove(department);
        department.setEmployee2(null);
        return department;
    }

    public Set<JobHistory> getJOB_HISTORYSet() {
        return JOB_HISTORYSet;
    }

    public void setJOB_HISTORYSet(Set<JobHistory> JOB_HISTORYSet) {
        this.JOB_HISTORYSet = JOB_HISTORYSet;
    }

    public JobHistory addJobHistory(JobHistory jobHistory) {
        getJOB_HISTORYSet().add(jobHistory);
        jobHistory.setEmployee1(this);
        return jobHistory;
    }

    public JobHistory removeJobHistory(JobHistory jobHistory) {
        getJOB_HISTORYSet().remove(jobHistory);
        jobHistory.setEmployee1(null);
        return jobHistory;
    }

    public Department getDepartment1() {
        return department1;
    }

    public void setDepartment1(Department department1) {
        this.department1 = department1;
    }

    public Job getJob1() {
        return job1;
    }

    public void setJob1(Job job1) {
        this.job1 = job1;
    }
}
