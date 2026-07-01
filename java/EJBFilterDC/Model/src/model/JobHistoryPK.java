package model;

import java.io.Serializable;

import java.util.Date;

public class JobHistoryPK implements Serializable {

    private Date startDate;

    private Integer employee1;

    public JobHistoryPK() {}

    public JobHistoryPK(Date startDate, Integer employee1) {
        this.startDate = startDate;
        this.employee1 = employee1;
    }

    public boolean equals(Object other) {
        if (other instanceof JobHistoryPK) {
            final JobHistoryPK otherJobHistoryPK = (JobHistoryPK) other;
            final boolean areEqual =
                (otherJobHistoryPK.startDate.equals(startDate) && otherJobHistoryPK.employee1.equals(employee1));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getEmployee1() {
        return employee1;
    }

    public void setEmployee1(Integer employee1) {
        this.employee1 = employee1;
    }
}
