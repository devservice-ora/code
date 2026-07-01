package oracle.adf.model.adapter.bean;

import java.util.Map;
import oracle.adf.model.generic.BeanUtils;
import oracle.binding.DataFilter;
import oracle.binding.FilterableDataControl;
import oracle.binding.OperationBinding;
import oracle.binding.RowContext;

/**
 *  @version $Header: bc4jrt/modules/adf-model/src/oracle/adf/model/adapter/bean/BeanFilterableDataControl.java /main/4 2009/04/01 17:16:03 jpham Exp $
 *  @author  jpham
 *  @since   11.1.2.0.0
 */
public abstract class BeanFilterableDataControl2
  extends UpdatableBeanDataControl2
  implements FilterableDataControl
{
  private FilterableDataControl _filterableAdapter;

  /**
   * Initialize Data Control properties
   * @param instance
   */
  protected void initDCProperties(Object instance)
  {
    if (instance instanceof FilterableDataControl)
    {
      _filterableAdapter = (FilterableDataControl)instance;
    }
  }

  //------------------------------------------------------------------------
  // oracle.binding.FilterableDataControl implementation methods
  //------------------------------------------------------------------------

  public boolean invokeOperation(Map bindingContext,
                                 OperationBinding action,
                                 DataFilter filter)
  {
    /** Invoke provider - true if the provider can handle */
    if (_filterableAdapter != null &&
        _filterableAdapter.invokeOperation(bindingContext, action, filter))
    {
      return true;
    }

    return super.invokeOperation(bindingContext, action);
  }

  public Object invokeAccessor(RowContext rowCtx, String name,
                               DataFilter filter)
  {
    if (_filterableAdapter != null)
    {
      /**
       * Invoke provider - if result not null then the provider can handle
       * and if result is null then other dc can handle
       */
      final Object result =
        _filterableAdapter.invokeAccessor(rowCtx, name, filter);
      if (result != null)
      {
        return result;
      }
    }

    return BeanUtils.getPropertyInMapOrBean(rowCtx.getRowDataProvider(), name);
  }
}
