package oracle.adf.model.adapter.bean.provider;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.adf.model.adapter.bean.BeanDCInvocationHandler2;
import oracle.adf.model.adapter.bean.pagination.PaginationCollectionModel2;
import oracle.adf.model.bean.DCDataVO;
import oracle.adf.model.bean.DCRowContext;
import oracle.adf.model.binding.DCInvokeMethod;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.model.generic.BeanUtils;
import oracle.adf.model.generic.StructureDefImpl;
import oracle.adfinternal.model.adapter.bean.BeanDCUtils;
import oracle.binding.DataFilter;
import oracle.binding.OperationBinding;
import oracle.binding.OperationInfo;
import oracle.binding.RowContext;
import oracle.binding.criteria.SearchCriteria;
import oracle.binding.criteria.SortCriteria;
import oracle.jbo.RowSet;
import oracle.jbo.Variable;
import oracle.jbo.VariableManager;
import oracle.jbo.ViewObject;
import oracle.jbo.common.JboNameUtil;

/**
 *  @version $Header: bc4jrt/modules/adf-model/src/oracle/adf/model/adapter/bean/provider/BeanDataCollection.java /st_jdevadf_pt-11.1.1.9.0/1 2013/11/06 10:16:46 jpham Exp $
 *  @author  jpham
 *  @since   11.1.2
 */
public class BeanDataCollection2<E>
  extends PaginationCollectionModel2<E>
{
  /** Bean Instance */
  protected Object _beanInstance;
  /** method Name */
  protected String _methodName;
  protected boolean _isAccessor;
  private boolean _isCollection;
  private boolean _supportsSorting;

  private byte _accessMode = RowSet.SCROLLABLE; // default

  /** Storing custom properties */
  private Map _mProps;

  /** Execute method names */
  private static final String METHOD_HANDLER_NAME = "_methodHandlerName"; // NORES
  private static final String METHOD_HANDLER_SIZE_NAME = "_methodHandlerSizeName"; // NORES
  private static final String DATA_FILTER = "_dataFilter"; // NORES
  private static final String ROW_CONTEXT = "_rowContext"; // NORES
  private static final String METHOD_HANDLER_PARAM_TYPES = "_methodHandlerParamTypes"; // NORES
  private static final String METHOD_HANDLER_SIZE_PARAM_TYPES = "_methodSizeParamTypes"; // NORES
  private static final String BEAN_CLASS_NAME = "_beanClassName"; // NORES
  private static final String METHOD_SIZE_FORMAT = "%sSize"; // NORES

  /**
   * Constructor for accessor operation
   * @param rowCtx
   * @param name
   * @param filter
   */
  public BeanDataCollection2(RowContext rowCtx, String name, DataFilter filter)
  {
    _methodName = name;
    _isAccessor = true;
    _beanInstance = rowCtx.getRowDataProvider();
    
    setDataFilter(filter);
    setProperty(ROW_CONTEXT, rowCtx);

    /** Determine the associated beanClassName */
    if (rowCtx instanceof DCRowContext)
    {
      DCRowContext ctx = (DCRowContext)rowCtx;
      StructureDefImpl def = ((StructureDefImpl)ctx.getStructureDef()).getAccessorDef(name);
      if (def != null)
      {
        _isCollection = def.isCollection();
        PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(_beanInstance, _methodName);
        Method method = null;
        if (propertyDescriptor != null)
        {
          method = propertyDescriptor.getReadMethod();
        }
        else
        {
          final String actualMethodName = BeanDCUtils.makeReaderName(_methodName);
          final ViewObject vo = getDCDataVO();
          Class[] parameterTypes = null;
          if (vo != null)
          {
            final VariableManager varMgr = vo.getVariableManager();
            if (varMgr != null)
            {
              final Variable[] variables = varMgr.getVariables();
              if (variables != null)
              {
                parameterTypes = new Class[variables.length];
                for (int i = 0; i < variables.length; i++)
                {
                  parameterTypes[i] = variables[i].getJavaType();
                }
              }
            }
          }
          try
          {
            method = _beanInstance.getClass().getMethod(actualMethodName, parameterTypes);
          }
          catch (NoSuchMethodException e)
          {
            _logger.severe("NoSuchMethodException ->" + actualMethodName, e);
          }
        }
        initMethod(method);
        if (getAssociatedBeanClassName() == null)
        {
          // Try look for beanClass in def
          setProperty(BEAN_CLASS_NAME, def.getBeanClassName());
        }
      }
    }

    /** Initialize the paging operations */
    initOperations();
  }

  /**
   * Constructor for method operation
   * @param bindingContext
   * @param action
   * @param filter
   */
  public BeanDataCollection2(Map bindingContext, OperationBinding action, DataFilter filter)
  {
    // TBD: Query supports only for invokeAccessor
    OperationInfo operation = action.getOperationInfo();
    if (operation instanceof DCInvokeMethod)
    {
      DCInvokeMethod methodInfo = (DCInvokeMethod)operation;
      DCIteratorBinding iter = methodInfo.getAssociatedIteratorBinding();
      if (iter != null)
      {
        _beanInstance = iter.getDataControl().getDataProvider();
        _methodName = methodInfo.getMethodName();
        
        setDataFilter(filter);

        // Determine the associate beanClassName
        // from the return type (value must be the generic type, i.e. List<Emp>)
        Method method = JboNameUtil.findMethod(_beanInstance.getClass(), _methodName, null, null, false);
        initMethod(method);

        /** Initialize the paging operations */
        initOperations();
      }
    }
  }

  /**
   * Initialize the paging collection
   */
  public void init()
  {
    _logger.fine(getClass().getName() + ".init");    

    /** Initialize fetch size */
    int fetchSize = getDataFilter().getFetchSize();
    setFetchSize(fetchSize);
    setPageSize(fetchSize);

    /** Initialize max. rows count */
    getEstimatedRowCount();
  }

  /**
   * Initialize all the related operations:
   * getMethod(), getMethodCount, and getMethod(int, int)
   */
  protected void initOperations()
  {
    String methodName = _methodName;
    // Find method with this signature->getMethod(firstResult, maxResult)
    Class[] paramTypes = new Class[] { int.class, int.class };
    methodName = (_isAccessor ? BeanDCUtils.makeReaderName(_methodName) : _methodName);
    if (JboNameUtil.findMethod(_beanInstance.getClass(), methodName, paramTypes, null, false) != null)
    {
      setMethodHandlerName(methodName);
      setMethodHandlerParamTypes(paramTypes);

      // Find accessor name for this operation
      String methodCountName = String.format(METHOD_SIZE_FORMAT, (_isAccessor ? _methodName : methodName));
      if (_isAccessor && BeanUtils.getPropertyDescriptor(_beanInstance, methodCountName) != null)
      {
        setMethodHandlerSizeName(methodCountName);
      }
      else if (JboNameUtil.findMethod(_beanInstance.getClass(), methodCountName, null, null, false) != null)
      {
        setMethodHandlerName(methodCountName);
      }
    }
  }

  //------------------------------------------------------------------------
  // Public Methods
  //------------------------------------------------------------------------

  /**
   * @return name of the data object
   */
  public String getName()
  {
    return _methodName;
  }
  
  /**
   * @return the full name of the data object in the form of <i>accessor.className</i>.
   */
  public String getFullName()
  {
    if (getAssociatedBeanClassName() != null)
    {
      return _methodName + "." + getAssociatedBeanClassName();
    }
    return _methodName;
  }

  /**
   * Return whether or not can handle operation
   */
  public boolean canHandle()
  {
    return (canHandleMethodHandler() && canHandleMethodHandlerSize() && _isCollection);
  }

  public boolean canHandleMethodHandler()
  {
    return (getMethodHandlerName() != null);
  }

  public boolean canHandleMethodHandlerSize()
  {
    return (getMethodHandlerSizeName() != null);
  }

  public void setAccessMode(byte mode)
  {
    _accessMode = mode;
  }
  
  public boolean supportsRangePaging()
  {
    return (_accessMode == RowSet.RANGE_PAGING);
  }

  public boolean supportsSorting()
  {
    return _supportsSorting;
  }
  
  public void supportsSorting(boolean bool)
  {
    _supportsSorting = bool;
  }
  
  /**
   * Stores a property value.
   */
  public final void setProperty(String name, Object val)
  {
    if (_mProps == null)
    {
      _mProps = new HashMap();
    }
    _mProps.put(name, val);
  }

  /**
   * Gets a property value.
   */
  public final Object getProperty(String name)
  {
    return ((_mProps != null ) ? _mProps.get(name) : null);
  }

  /**
   *  Return the associate beanClass name
   */
  public String getAssociatedBeanClassName()
  {
    return (String)getProperty(BEAN_CLASS_NAME);
  }

  public void setMethodHandlerName(String name)
  {
    setProperty(METHOD_HANDLER_NAME, name);
  }

  public String getMethodHandlerName()
  {
    return (String)getProperty(METHOD_HANDLER_NAME);
  }

  public void setMethodHandlerParamTypes(Class[] paramTypes)
  {
    setProperty(METHOD_HANDLER_PARAM_TYPES, paramTypes);
  }

  public Class[] getMethodHandlerParamTypes()
  {
    return (Class[])getProperty(METHOD_HANDLER_PARAM_TYPES);
  }

  public void setMethodHandlerSizeName(String name)
  {
    setProperty(METHOD_HANDLER_SIZE_NAME, name);
  }

  public String getMethodHandlerSizeName()
  {
    return (String)getProperty(METHOD_HANDLER_SIZE_NAME);
  }

  public void setMethodCountParamTypes(Class[] paramTypes)
  {
    setProperty(METHOD_HANDLER_SIZE_PARAM_TYPES, paramTypes);
  }

  public Class[] getMethodCountParamTypes()
  {
    return (Class[])getProperty(METHOD_HANDLER_SIZE_PARAM_TYPES);
  }

  public SearchCriteria getSearchCriteria()
  {
    return (getDataFilter() != null ? getDataFilter().getSearchCriteria() : null);
  }

  public SortCriteria getSortCriteria()
  {
    return (getDataFilter() != null ? getDataFilter().getSortCriteria() : null);
  }

  public List<String> getSearchAttributeNames()
  {
    return (getDataFilter() != null ? getDataFilter().getSearchAttributeNames() : null);
  }

  /**
   * Method Handler
   * @param fetchStart <code>int</code> firstResult
   * @param fetchSize <code>int</code> maxResults
   * @return collection
   */
  public Collection<E> invokeMethodHandler(int fetchStart, int fetchSize)
  {
    return (Collection<E>)invokeMethod(_beanInstance, getMethodHandlerName(), getMethodHandlerParamTypes(),
                                       new Object[] { new Integer(fetchStart), new Integer(fetchSize) });
  }

  /**
   * @return The size of of method handler
   */
  public Object invokeMethodHandlerSize()
  {
    String actualMethodName = (_isAccessor
      ? BeanDCUtils.makeReaderName(getMethodHandlerSizeName())
      :getMethodHandlerSizeName());
    return invokeMethod(_beanInstance, actualMethodName, null, null);
  }

  /**
   * Invoke method by name, parameters, values
   * @param instance
   * @param methodName
   * @param paramTypes - A <code>Class[]</code>
   * @param params
   * @return result
   */
  public Object invokeMethod(Object instance, String methodName, Class[] paramTypes, Object[] params)
  {
    return new BeanDCInvocationHandler2(instance).invokeMethod(methodName, paramTypes, params);
  }

  //------------------------------------------------------------------------
  // Override pagination methods
  //------------------------------------------------------------------------

  /**
   * Get the total # of rows count.
   * @return max rows count
   */
  @Override
  public long getEstimatedRowCount()
  {
    /** Invoke the method find count for iterator */
    long rowCount = getRowCount();
    if (canHandleMethodHandlerSize() && rowCount == 0)
    {
      // Return the object of the max rowCount
      Object result = invokeMethodHandlerSize();
      if (result != null)
      {
        Object resultCount = result;
        if (result instanceof Collection && ((Collection)result).size() == 1)
        {
          resultCount = ((Collection)result).iterator().next();
        }
        if (resultCount instanceof Long || resultCount instanceof Integer)
        {
          rowCount = Long.valueOf(resultCount.toString()).longValue();
          _logger.info(getClass().getName() + "." + getMethodHandlerSizeName() + "(" + rowCount + ")"); // NORES
        }
        else
        {
          _logger.warning("Invalid return type for method " + getMethodHandlerSizeName() + ". Paging will be disabled."); // NORES
        }
      }
      // Set max. rows count
      setRowCount(rowCount);
    }
    return rowCount;
  }

  /**
   * @return fetch start position
   */
  @Override
  public int getFetchStart()
  {
    int fetchStart = getDataFilter().getFetchStart();
    if (supportsDefaultPaging())
    {
      fetchStart = getFetchedRowCount();
    }
    else if (fetchStart >= getRowCount())
    {
      // EPM's usecase - a workaround for refresh after change size
      fetchStart = 0;
    }
    // -1 indicates fetch all rows
    fetchStart = ((fetchStart == -1) ? 0 : fetchStart);
    return fetchStart;
  }

  @Override
  public int getFetchSize()
  {
    int fetchSize = super.getFetchSize();
    if (isLastRow())
    {
      fetchSize = 0;
    }
    return fetchSize;
  }

  private boolean isLastRow()
  {
    int fetchStart = getDataFilter().getFetchStart();
    long rowCount = getRowCount();
    return (supportsRangePaging() && rowCount > 1 && rowCount == (fetchStart + 1));    
  }

  /**
   * @return The collection <code>Collection</code> for this paginated collection.
   */
  @Override
  public Collection<E> getCollection()
  {
    if (getRowCount() > 0)
    {      
      // Data Collection
      int fetchStart = getFetchStart();
      int fetchSize = getFetchSize();
      Collection<E> collectionModel = getCollection(fetchStart);
      if (collectionModel == null && getMethodHandlerName() != null)
      {
        collectionModel = invokeMethodHandler(fetchStart, fetchSize);
        if (collectionModel != null)
        {
          _logger.info(getClass().getName() + "." + getMethodHandlerName() + "(" + fetchStart + ", " + collectionModel.size() +
                       ")"); // NORES
        }
      }
      addCollection(collectionModel);      
      return collectionModel;
    }
    
    return Collections.emptyList();
  }

  /**
   * Returns the data object for this collection represents.
   */
  @Override
  public Object getDataProvider()
  {
    return _beanInstance;
  }
  
   /**
    * Refresh the colleciton
    */
   public void refresh()
   {
    _logger.fine(getClass().getName() + ".refresh");
    
    // Refresh the cache for fetchStart at top range
    int fetchStart = getDataFilter().getFetchStart();
    if (fetchStart == 0 || fetchStart == -1)
    {
      clearAll();
    }    
    
    // Apply criteria
    if (appliedCriteria())
    {
      // Criteria had already been applied.
      return;
    }
    
    super.refresh();
    init();
   }
   
   /**
    * @return <code>true</code> if criteria has applied. 
    * Sorting will be applied on next time when this collection is re-initialized.
    */
   protected boolean appliedCriteria()
   {    
     // If need be, this method may be override for ImplementsSort.
     if (hasSortCriteria())
     {
       if (supportsSorting())
       {
         // Just needs to clearAll for refresh.
         clearAll();
       }       
       // The adf framework will apply criteria.
       return true;
     }
     
     return false;
   }
  
  /**
   * @return <code>true</code> if dataFilter has sortCriteria
   */
  protected boolean hasSortCriteria()
  {
    final SortCriteria sc = getSortCriteria();
    return ((sc != null) && (sc.getSortItems() != null) && (sc.getSortItems().size() > 0));
  }
  
  /**
   * @return <code>true</code> if dataFilter has searchCriteria
   */
  protected boolean hasSearchCriteria()
  {     
    final SearchCriteria sc = getSearchCriteria();
    return ((sc != null) && (sc.getSearchGroups() != null) && (sc.getSearchGroups().size() > 0));    
  }
  
  //------------------------------------------------------------------------
  // Private implementation
  //------------------------------------------------------------------------

  private void initMethod(Method m)
  {
    if (m != null)
    {
      // initMethodAnnotation(m);
      initMethodReturnType(m);
    }
  }

  /**
   * Initialize the method return type info
   */
  private void initMethodReturnType(Method m)
  {
    if (m != null)
    {
      String returnType = BeanDCUtils.getReturnType(m);
      if (returnType != null)
      {
        String className = BeanDCUtils.getGenericCollectionType(returnType);
        if (className != null)
        {
          setProperty(BEAN_CLASS_NAME, className);
        }
        _isCollection = BeanDCUtils.isCollection(returnType);
      }
    }
  }

  public void setDataFilter(DataFilter filter)
  {
    setProperty(DATA_FILTER, filter);  
  }
  
  /**
   * @return DataFilter <code>DataFilter</code> criteria
   */
  public DataFilter getDataFilter()
  {
    return (DataFilter)getProperty(DATA_FILTER);
  }

  /**
   * @return DCRowContext <code>RowContext</code>
   */
  public DCRowContext getDCRowContext()
  {
    RowContext ctx = (RowContext)getProperty(ROW_CONTEXT);
    return (ctx != null && (ctx instanceof DCRowContext) ? (DCRowContext)ctx : null);
  }

  /**
   * @return DCDataVO
   */
  public DCDataVO getDCDataVO()
  {
    DCRowContext ctx = getDCRowContext();
    if (ctx != null && ctx.getRowSetIterator() instanceof DCDataVO)
    {
      return (DCDataVO)ctx.getRowSetIterator();
    }
    return null;
  }
}
