package oracle.adf.model.adapter.bean;

import java.util.Map;

import oracle.adf.model.adapter.bean.handler.DCHandler;
import oracle.adf.model.adapter.bean.handler.DCHandler;
import oracle.adf.model.adapter.bean.provider.BeanDataCollection2;
import oracle.adf.model.adapter.bean.provider.BeanDataProvider2;
import oracle.adf.model.bean.DCDataVO;
import oracle.adf.model.bean.DCRowContext;
import oracle.adf.model.binding.DCInvokeMethod;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.model.binding.DCUtil;
import oracle.adf.model.generic.StructureDefImpl;
import oracle.adf.share.logging.ADFLogger;
import oracle.binding.DataControl;
import oracle.binding.DataFilter;
import oracle.binding.FilterableDataControl;
import oracle.binding.ManagedDataControl;
import oracle.binding.OperationBinding;
import oracle.binding.OperationInfo;
import oracle.binding.RowContext;
import oracle.jbo.RowSet;
import oracle.jbo.uicli.binding.JUAccessorIteratorDef;
import oracle.jbo.uicli.binding.JUCtrlActionBinding;
import oracle.jbo.uicli.binding.JUIteratorBinding;

/**
 * DataFilterHandler can be extended from <code>BeanDataCollection</code>
 * to add custom data provider.
 *
 * @version $Header: bc4jrt/modules/adf-model/src/oracle/adf/model/adapter/bean/DataFilterHandler.java /st_jdevadf_patchset_ias/3 2012/07/30 14:17:21 jpham Exp $
 * @author  jpham
 * @since   11.1.2
 */
public class DataFilterHandler2
  implements FilterableDataControl, DCHandler
{  
  protected DataControl _dc;  
  protected BeanDataProvider2<String, BeanDataCollection2> _beanDataProvider =
    new BeanDataProvider2<String, BeanDataCollection2>();

  /** Logger */
  protected ADFLogger _logger = ADFLogger.createADFLogger(DataFilterHandler2.class);

  public DataFilterHandler2(DataControl dc)
  {
    _dc = dc;
  }

  //------------------------------------------------------------------------
  // oracle.binding.FilterableDataControl implementation methods
  //------------------------------------------------------------------------

  public boolean invokeOperation(Map bindingContext, OperationBinding action, DataFilter filter)
  {
    _logger.fine(getClass().getName() + ".invokeOperation");
    OperationInfo operation = action.getOperationInfo();
    if (operation instanceof DCInvokeMethod)
    {
      DCInvokeMethod methodInfo = (DCInvokeMethod)operation;
      DCIteratorBinding iter = methodInfo.getAssociatedIteratorBinding();
      if (iter != null && methodInfo.getDef().getReturnName() != null && methodInfo.getParameters().length == 0)
      {
        String viewName = null;
        if (!(this instanceof ManagedDataControl))
        {
          viewName = iter.getBindingContainer().getFullName();
        }        
        final String providerName = _dc.getName() + "." + methodInfo.getInstanceName() + "." + methodInfo.getMethodName();
        BeanDataCollection2 provider = _beanDataProvider.getDataProvider(providerName);
        if (provider == null)
        {
          provider = (BeanDataCollection2) invoke(bindingContext, action, filter);
          if (provider != null)
          {
            _beanDataProvider.setDataProvider(providerName, viewName, provider);
          }
        }
        else
        {            
          provider.setDataFilter(filter);
          provider.refresh();
        }
        
        if (provider != null)
        {          
          DCUtil.putValueInPath((oracle.adf.model.BindingContext)bindingContext, action.getOperationInfo().getReturnName(),
                                provider);
          return true;
        }
      }
    }
    return false;
  }

  public boolean invokeOperation(Map bindingContext, OperationBinding action)
  {
    _logger.fine(getClass().getName() + ".invokeOperation");
    if (action != null && action instanceof JUCtrlActionBinding)
    {
      JUCtrlActionBinding actionCtrl = (JUCtrlActionBinding)action;
      if (actionCtrl.getActionId() == JUCtrlActionBinding.ACTION_ITERATOR_BINDING_EXECUTE)
      {
        JUIteratorBinding iter = actionCtrl.getIteratorBinding();
        if (iter != null && (iter.getDef() instanceof JUAccessorIteratorDef))
        {
          JUAccessorIteratorDef def = (JUAccessorIteratorDef)iter.getDef();
          String providerName = _dc.getName() + "." + def.getBindsName() + "." + def.getBeanClassName();
          BeanDataCollection2 provider = _beanDataProvider.getDataProvider(providerName);
          if (provider != null)
          {
            provider.notifyRefresh();
          }
        }
      }
    }
    return false;
  }

  public Object invokeAccessor(RowContext rowCtx, String name, DataFilter filter)
  {
    _logger.fine(getClass().getName() + "invokeAccessor");
    if (rowCtx instanceof DCRowContext)
    {
      DCRowContext ctx = (DCRowContext)rowCtx;
      StructureDefImpl def = ((StructureDefImpl)ctx.getStructureDef()).getAccessorDef(name);
      if (def != null)
      {
        String viewName = null;
        if (!(this instanceof ManagedDataControl))
        {
          viewName = ((DCDataVO)ctx.getRowSetIterator()).getViewObject().getName();
        }
        String providerName = _dc.getName() + "." + name + "." + def.getFullName();
        BeanDataCollection2 provider = _beanDataProvider.getDataProvider(providerName);
        if (provider == null)
        {
          provider = (BeanDataCollection2) invoke(rowCtx, name, filter);
          if (provider != null)
          {
            _beanDataProvider.setDataProvider(providerName, viewName, provider);
          }
        }
        else
        {            
          provider.setDataFilter(filter);
          provider.refresh();
        }
        
        if (provider != null)
        {            
          return provider;
        }
      }
    }
    return null;
  }

  //------------------------------------------------------------------------
  // Override the dc framework
  //------------------------------------------------------------------------  
  
  /**
   * Invoke the operation for filter
   * @param rowCtx <code>RowContext</code> rowCtx for this accessor
   * @param name <code>String</code> accessor method
   * @param filter <code>DataFilter</code> filter criteria
   * @return result
   */
  public Object invoke(RowContext rowCtx, String name, DataFilter filter)
  {
    return getDataCollection(new BeanDataCollection2(rowCtx, name, filter));
  }

  /**
   * Invoke the operation for filter
   * @param bindingContext <code>Map</code> BindingContext
   * @param action <code>OperationBinding</code> accessor method
   * @param filter <code>DataFilter</code> filter criteria
   * @return result
   */
  public Object invoke(Map bindingContext, OperationBinding action, DataFilter filter)
  {
    return getDataCollection(new BeanDataCollection2(bindingContext, action, filter));
  }

  protected BeanDataCollection2 getDataCollection(BeanDataCollection2 dataCollection)
  {
    boolean canHandle = (dataCollection != null ? dataCollection.canHandle() : false);
    if (canHandle && supportsPaging(dataCollection.getFullName()))
    {
      initDataCollection(dataCollection);
    }
    else
    {
      // dataFilter can't handle this operation
      dataCollection = null;
    }
    
    return dataCollection;
  }
  
  /**
   * Initialize the paging collection and settings
   */
  protected void initDataCollection(BeanDataCollection2 dataCollection)
  {
    if (dataCollection != null)
    {
      // Initialize the paging collection
      dataCollection.init();
      
      if (supportsRangePaging(dataCollection.getFullName()))
      {
        dataCollection.setAccessMode(RowSet.RANGE_PAGING);
      }
      
      // Supports ImplementsSort
      dataCollection.supportsSorting(supportsSorting());
    }
  }
  

  public boolean supportsPaging()
  {
    if (_dc != null && _dc instanceof DCHandler)
    {
      return ((DCHandler)_dc).supportsPaging();
    }
    return false;
  }
  
  public boolean supportsRangePaging()
  {
    if (_dc != null && _dc instanceof DCHandler)
    {
      return ((DCHandler)_dc).supportsRangePaging();
    }
    return false;
  }

  /**
   * @param name <code>String</code> accessor name
   * @return true <code>boolean</code> for accessor supports paging
   */
  public boolean supportsPaging(String name)
  {
    if (_dc != null && _dc instanceof DCHandler)
    {
      return ((DCHandler)_dc).supportsPaging(name);
    }
    return false;
  }

  /**
   * @param name <code>String</code> accessor name
   * @return true <code>boolean</code> for accessor supports range paging
   */
  public boolean supportsRangePaging(String name)
  {
    if (_dc != null && _dc instanceof DCHandler)
    {
      return ((DCHandler)_dc).supportsRangePaging(name);
    }
    return false;
  }
  
  /**
   * @return true <code>boolean</code> for beandc supports criteria
   */
  public boolean supportsCriteria()
  {
    if (_dc != null && _dc instanceof DCHandler)
    {
      return ((DCHandler)_dc).supportsCriteria();
    }
    return false;
  }
  
  /**
   * @return true <code>boolean</code> for supports sorting
   */
  public boolean supportsSorting()
  {
    if (_dc != null && _dc instanceof DCHandler)
    {
      return ((DCHandler)_dc).supportsSorting();
    }
    return false;
  }
}
