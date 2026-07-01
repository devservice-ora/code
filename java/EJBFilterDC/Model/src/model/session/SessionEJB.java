package model.session;

import java.util.List;

import javax.ejb.Remote;

import model.Country;
import model.Department;
import model.Employee;
import model.Job;
import model.JobHistory;
import model.Location;
import model.Region;

@Remote
public interface SessionEJB {

    Object queryByRange(String jpqlStmt, int firstResult, int maxResults);

    List<Department> getDepartmentFindAll();

    List<Region> getRegionFindAll();

    List<Country> getCountryFindAll();

    List<Employee> getEmployeeFindAll();

    List<Job> getJobFindAll();

    List<JobHistory> getJobHistoryFindAll();

    List<Location> getLocationFindAll();
}
