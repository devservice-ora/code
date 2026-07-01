package oracle.adf.model.adapter.bean.jpa;

import java.util.Collection;
import java.util.Map;
import oracle.adf.model.adapter.bean.provider.BeanDataCollection2;
import oracle.binding.DataFilter;
import oracle.binding.OperationBinding;
import oracle.binding.RowContext;
import oracle.jbo.common.JboNameUtil;

/**
 *  @version $Header: bc4jrt/modules/adf-model/src/oracle/adf/model/adapter/bean/jpa/JPQLBeanDataCollection.java /st_jdevadf_patchset_ias/2 2012/07/30 14:17:21 jpham Exp $
 *  @author  jpham
 *  @since   11.1.2
 */
public class JPQLBeanDataCollection2<E>
  extends BeanDataCollection2<E>
{
  private JPQLEntity _entity;

  /** JPQL query and count statements */
  private static final String JPQL_STMT_BY_CRITERIA =
    "_jpqlStmtByCriteria"; // NORES
  private static final String JPQL_STMT_COUNT = 
    "_jpqlStmtCount"; // NORES
  public static final String METHOD_QUERY_NAME = 
    "queryByRange"; // NORES
  public static final String METHOD_QUERY_COUNT_NAME = 
    METHOD_QUERY_NAME;
  public static final String METHOD_PERSIST_ENTITY =
    "persistEntity"; // NORES
  public static final String METHOD_MERGE_ENTITY =
    "mergeEntity"; // NORES

  public JPQLBeanDataCollection2(Map bindingContext, OperationBinding action, DataFilter filter)
  {
    super(bindingContext, action, filter);
  }

  public JPQLBeanDataCollection2(RowContext rowCtx, String name, DataFilter filter)
  {
    super(rowCtx, name, filter);
  }

  //------------------------------------------------------------------------
  // Public Methods
  //------------------------------------------------------------------------
  
  public void setQueryStmt(String jpqlStmt)
  {
    setProperty(JPQL_STMT_BY_CRITERIA, jpqlStmt);
  }
  
  public String getQueryStmt()
  {
    return (String)getProperty(JPQL_STMT_BY_CRITERIA); 
  }
  
  public void setQueryStmtCount(String jpqlStmt)
  {
    setProperty(JPQL_STMT_COUNT, jpqlStmt);
  }
  
  public String getQueryStmtCount()
  {
    return (String)getProperty(JPQL_STMT_COUNT);
  }
  
  //------------------------------------------------------------------------
  // Override Methods
  //------------------------------------------------------------------------
  
  /**
   * @return <code>true</code> if criteria has applied.
   * Sorting will be applied on next time when this collection is re-initialized.
   */
  @Override
  protected boolean appliedCriteria()
  {    
    boolean searchCriteriaChanged = searchCriteriaChanged();
    if (searchCriteriaChanged)
    {
      notifyCriteriaChanged();
    }
    // Apply criteria
    if ((supportsSorting() && hasSortCriteria()) || searchCriteriaChanged)
    {
      clearAll();
      init();
      return true;
    }
    
    return super.appliedCriteria();
  }
  
  /**
   * Initialize the jpql paging collection
   */  
  @Override
  public void init()
  {
    try
    {
      if (getAssociatedBeanClassName() != null)
      {
        /** Entity class */
        if (_entity == null)
        {
          _entity = new JPQLEntity(getAssociatedBeanClassName(), _methodName);          
        }
        if (_entity.isEntity() && _entity.getQueryStmt() != null)
        {
          /** Initialize JPQL Stmts */
          initJPQLStatements();
          if (getQueryStmt() != null && getQueryStmtCount() != null)
          {
            /** Initialize the paging collection */
            super.init();
          }
        }
      }      
    }
    catch (ClassNotFoundException e)
    {
      _logger.severe("Exception occurred at forName(" +
                     getAssociatedBeanClassName() + ")"); // NORES
    }    
  }  

  @Override
  protected void initOperations()
  {
    // Build the query and queryCount methods name:
    // gueryByRange(jpqlStmt, #, #) for query result
    // queryByRange(jpqlStmtCount, 0, 0) for query count
    if (getMethodHandlerName() == null || getMethodHandlerSizeName() == null)
    {
      String methodName = METHOD_QUERY_NAME;
      final Class[] paramTypes =
        new Class[] { String.class, int.class, int.class };
      if (JboNameUtil.findMethod(_beanInstance.getClass(), methodName,
                                 paramTypes, null, false) != null)
      {
        // Query method
        setMethodHandlerName(methodName);
        setMethodHandlerParamTypes(paramTypes);
  
        // The same query method is used for count
        String methodCountName = METHOD_QUERY_COUNT_NAME;
        setMethodHandlerSizeName(methodCountName);
        setMethodCountParamTypes(paramTypes);
      }
    }
  }
  
  /**
   * Method handler
   * @param fetchStart <code>int</code> firstResult
   * @param fetchSize <code>int</code> maxResults
   * @return collection
   */
  @Override
  public Collection<E> invokeMethodHandler(int fetchStart, int fetchSize)
  {
    return (Collection<E>)invokeMethod(_beanInstance, getMethodHandlerName(),
                                       getMethodHandlerParamTypes(),
                                       new Object[] { getQueryStmt(),
                                                      new Integer(fetchStart),
                                                      new Integer(getFetchSize()) });
  }
  
  /**
   * @return The result of the query count
   */
  @Override
  public Object invokeMethodHandlerSize()
  {
    return invokeMethod(_beanInstance, getMethodHandlerSizeName(),
                        getMethodCountParamTypes(),
                        new Object[] { getQueryStmtCount(),
                                       new Integer(0), new Integer(0) });
  }
  
  //------------------------------------------------------------------------
  // Private Methods
  //------------------------------------------------------------------------
  
  /**
   * Commom utility method for build JPQL statement
   * that needs to be converted into a JPQL clause
   * @return JPQL statement
   */
  private void initJPQLStatements()
  {
    _logger.fine(getClass().getName() + ".initJPQLStatements");
    // Find the filter criteria
    final String filter =
      JPQLUtils.getFilter(_entity.getEntityClass(), _entity.getAliasName(),
                          getSearchCriteria());
    final String orderBy =
      JPQLUtils.getOrderBy(_entity.getEntityClass(), _entity.getAliasName(),
                           getSortCriteria());
    // Initialize JPQL stmt
    _entity.setFilter(filter);
    _entity.setOrderBy(orderBy);
    String jpqlStmtCount = _entity.getQueryStmtCount();
    String jpqlStmtByCriteria = _entity.getQueryStmt();
    setQueryStmt(jpqlStmtByCriteria);    
    setQueryStmtCount(jpqlStmtCount);
  }
  
  /**
   * @return <code>true</code> if criteria changes
   */
  private boolean searchCriteriaChanged()
  {
    String lastCriteria = _entity.getQueryStmtCount();
    // Initialize new filter
    initJPQLStatements();
    // Use stmtCount because it does not contain sort criteria.
    return (!lastCriteria.equals(_entity.getQueryStmtCount()));
  }
  
  /**
   * Notify criteria changed on next time 
   * when this collection is re-initialized.
   */  
  private void notifyCriteriaChanged()
  {
    // Set _rowCount=0 to calculate new estimatedRowCount.
    setRowCount(0);
  }
}
