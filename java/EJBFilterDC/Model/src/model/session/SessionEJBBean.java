package model.session;

import java.util.List;

import javax.annotation.Resource;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import model.Country;
import model.Department;
import model.Employee;
import model.Job;
import model.JobHistory;
import model.Location;
import model.Region;

@Stateless(name = "SessionEJB", mappedName = "EJBFilter-Model-SessionEJB")
public class SessionEJBBean implements SessionEJB, SessionEJBLocal {

    @Resource
    SessionContext sessionContext;

//    @PersistenceContext(unitName = "HRModel")
//    private EntityManager em;

    public SessionEJBBean() {}

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Object queryByRange(String jpqlStmt, int firstResult, int maxResults) {
        Query query = getEntityManager().createQuery(jpqlStmt);
        if (firstResult > 0) {
            query = query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query = query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }

    /** <code>select o from Department o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Department> getDepartmentFindAll() {
        return getEntityManager().createNamedQuery("Department.findAll", Department.class).getResultList();
    }

    /** <code>select o from Region o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Region> getRegionFindAll() {
        return getEntityManager().createNamedQuery("Region.findAll", Region.class).getResultList();
    }

    /** <code>select o from Country o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Country> getCountryFindAll() {
        return getEntityManager().createNamedQuery("Country.findAll", Country.class).getResultList();
    }

    /** <code>select o from Employee o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Employee> getEmployeeFindAll() {
        return getEntityManager().createNamedQuery("Employee.findAll", Employee.class).getResultList();
    }

    /** <code>select o from Job o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Job> getJobFindAll() {
        return getEntityManager().createNamedQuery("Job.findAll", Job.class).getResultList();
    }

    /** <code>select o from JobHistory o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<JobHistory> getJobHistoryFindAll() {
        return getEntityManager().createNamedQuery("JobHistory.findAll", JobHistory.class).getResultList();
    }

    /** <code>select o from Location o</code> */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Location> getLocationFindAll() {
        return getEntityManager().createNamedQuery("Location.findAll", Location.class).getResultList();
    }
    
    protected EntityManager getEntityManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HRModel");
        EntityManager ecm = emf.createEntityManager(); 
        return ecm;
    }  
}
