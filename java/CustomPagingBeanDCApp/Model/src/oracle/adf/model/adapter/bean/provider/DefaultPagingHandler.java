package oracle.adf.model.adapter.bean.provider;

import java.util.List;
import java.util.Map;

import oracle.adf.model.ManagedDataControl;
import oracle.adf.model.adapter.bean.DataFilterHandler;

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
import oracle.binding.OperationBinding;
import oracle.binding.OperationInfo;
import oracle.binding.RowContext;

import oracle.jbo.RowSet;
import oracle.jbo.uicli.binding.JUCtrlActionBinding;
import oracle.jbo.uicli.binding.JUIteratorBinding;

public class DefaultPagingHandler
  implements FilterableDataControl
{
  private DataControl _dc;
  protected BeanDataProvider2<String, BeanDataCollection2> _beanDataProvider =
    new BeanDataProvider2<String, BeanDataCollection2>();

  /** Logger */
  protected ADFLogger _logger = ADFLogger.createADFLogger(DataFilterHandler.class);

  public DefaultPagingHandler(DataControl dc)
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
        final String providerName = _dc.getName() + "." + iter.getDataControl().getClass().getName() + "." + iter.getName();
        Object result = _beanDataProvider.getDataProvider(providerName, viewName);
        if (result == null)
        {
          result = invoke(bindingContext, action, filter);
          if (result != null)
          {
            _beanDataProvider.setDataProvider(providerName, viewName, (BeanDataCollection2)result);
          }
        }
        if (result != null)
        {
          DCUtil.putValueInPath((oracle.adf.model.BindingContext)bindingContext, action.getOperationInfo().getReturnName(),
                                result);
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
        if (iter != null && supportsRangePaging())
        {          
          String providerName =
            _dc.getName() + "." + iter.getDataControl().getDataProvider().getClass().getName() + "." + iter.getName();
          BeanDataCollection2 beanCollection = _beanDataProvider.getDataProvider(providerName);
          if (beanCollection != null)
          {
            beanCollection.notifyRefresh();
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
        final String providerName = _dc.getName() + "." + ctx.getRowDataProvider().getClass().getName() + "." + name + "Iterator";
        Object result = _beanDataProvider.getDataProvider(providerName, viewName);
        if (result == null)
        {
          result = invoke(rowCtx, name, filter);
          if (result != null)
          {
            _beanDataProvider.setDataProvider(providerName, viewName, (BeanDataCollection2)result);
          }
        }
        if (result != null)
        {
          return result;
        }
      }
    }
    return null;
  }

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
    if (canHandle)
    {
      initDataCollection(dataCollection);
    }
    else
    {
      dataCollection = null;
    }

    return dataCollection;
  }

  protected void initDataCollection(BeanDataCollection2 dataCollection)
  {
    if (dataCollection != null)
    {
      if (supportsRangePaging())
      {
        dataCollection.setAccessMode(RowSet.RANGE_PAGING);
      }
    }
  }

  public boolean supportsPaging()
  {
    return true;
  }

  public boolean supportsRangePaging()
  {
    return false;
  }
}
