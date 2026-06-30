package model;

import java.util.Map;
import oracle.adf.model.adapter.bean.BeanDataControl;
import oracle.adf.model.adapter.bean.provider.BeanDCInvocationHandler2;
import oracle.binding.OperationBinding;

public class CustomBeanDataControl
  extends BeanDataControl
{
  public CustomBeanDataControl(CustomDefinition definition)
  {
    super(definition);
  }

  public boolean invokeOperation(Map bindingContext, OperationBinding action)
  {
    Boolean result = null;
    Object handler = getDataControlHandler();
    if (handler != null)
    {
      result =
          (Boolean)new BeanDCInvocationHandler2(handler).invokeMethod("invokeOperation", 
                                                                      new Class[] { Map.class, OperationBinding.class },
                                                                      new Object[] { bindingContext, action });
    }
    return ((result != null && !result.booleanValue()) ? super.invokeOperation(bindingContext, action) : true);
  }
}
