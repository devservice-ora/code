package oracle.adf.model.adapter.bean.jpa;

import java.util.Map;
import oracle.adf.model.adapter.bean.DataFilterHandler2;
import oracle.binding.DataControl;
import oracle.binding.DataFilter;
import oracle.binding.OperationBinding;
import oracle.binding.RowContext;

/**
 * Default JPQLDataFilterHandler
 * 
 * This handler can be extended from <code>JPQLBeanDataCollection</code>
 * to add custom data provider.
 * 
 * @version $Header: bc4jrt/modules/adf-model/src/oracle/adf/model/adapter/bean/jpa/JPQLDataFilterHandler.java /st_jdevadf_patchset_ias/1 2012/06/27 07:48:06 ppark Exp $
 * @author  jpham
 * @since   11.1.2
 */
public class JPQLDataFilterHandler2
  extends DataFilterHandler2
{
  public JPQLDataFilterHandler2(DataControl dc)
  {
    super(dc);
  }

  /**
   * Invoke the operation for filter
   * @param rowCtx <code>RowContext</code> rowCtx for this accessor
   * @param name <code>String</code> accessor method
   * @param filter <code>DataFilter</code> filter criteria
   */
  @Override
  public Object invoke(RowContext rowCtx, String name, DataFilter filter)
  {
    return getDataCollection(new JPQLBeanDataCollection2(rowCtx, name, filter));
  }

  /**
   * Invoke the operation for filter
   * @param bindingContext <code>Map</code> BindingContext
   * @param action <code>OperationBinding</code> accessor method
   * @param filter <code>DataFilter</code> filter criteria
   * @return result
   */
  @Override
  public Object invoke(Map bindingContext, OperationBinding action,
                       DataFilter filter)
  {
    return getDataCollection(new JPQLBeanDataCollection2(bindingContext, action, filter));
  }
}
