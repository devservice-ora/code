package oracle.adf.model.adapter.bean;


import java.util.Map;
import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCInvokeMethod;
import oracle.adf.model.binding.DCMethodParameter;
import oracle.adf.share.logging.ADFLogger;
import oracle.adfinternal.model.adapter.bean.BeanDCUtils;
import oracle.binding.OperationBinding;
import oracle.jbo.uicli.binding.JUCtrlActionBinding;

/**
 *  @version $Header: bc4jrt/modules/adf-model/src/oracle/adf/model/adapter/bean/BeanDCInvokeMethodHandler.java /main/6 2012/04/12 12:35:34 jpham Exp $
 *  @author  jpham
 *  @since   11.1.2
 */
public class BeanDCInvokeMethodHandler2
{
  private Object _dataProvider;
  private Map _bindingContext;
  private OperationBinding _operation;
  private DCInvokeMethod _invokeMethod;
  
  protected ADFLogger _logger = ADFLogger.createADFLogger(BeanDCInvokeMethodHandler2.class);

  public BeanDCInvokeMethodHandler2(Map bindingContext, OperationBinding operation)
  {
    _bindingContext = bindingContext;
    _operation = operation;    
    if (operation.getOperationInfo() instanceof DCInvokeMethod)
    {
      _invokeMethod = (DCInvokeMethod)operation.getOperationInfo();
    }
  }
  
  /**
   * @return true if beandc can invoke method
   */
  public boolean canHandle() 
  {
    if ((_invokeMethod != null) &&  (_invokeMethod.getInstanceName() != null))
    {
      try
      {
        // Handle beandc methods only
        _dataProvider =
          _invokeMethod.getInvokeInstance(((JUCtrlActionBinding)_operation).getDataControl(), 
                                          (BindingContext)_bindingContext);
      }
      catch (Exception e)
      {
        // Can't handle this operation
        _logger.info("Unable to handle methodName: " + _invokeMethod.getInstanceName()); // NORES
      }
      return (_dataProvider != null);
    }
    return false;
  }
  
  /**
   * Name of the operation to invoke
   */
  public String getOperationName() 
  {
    return (_invokeMethod != null ? _invokeMethod.getOperationName() : "");
  }

  /**
   * Invoke method operation
   * @return result
   * @throws Exception
   */
  public Object invoke()
    throws Exception
  {    
    if (_dataProvider != null)
    {
      String operationName = _invokeMethod.getOperationName();      
      // Formatting parameter types
      DCMethodParameter[] ops = (DCMethodParameter[])_invokeMethod.getParameters();
      Class[] signature = new Class[ops.length];
      for (int i = 0; i < ops.length; i++)
      {
        signature[i] = getType(ops[i].getType());
      }

      // Binding parameters
      Object[] transVals = _invokeMethod.getTransientValues();
      Object[] params = (transVals != null) ? transVals
        : _invokeMethod.fetchParameterValues((BindingContext)_bindingContext, 
                                              _operation.getParamsMap());      
      
      // Patch up the operationName and dataProvider
      if ("executeWithParams".equals(operationName))
      {
        final String accessorName = ((JUCtrlActionBinding)_operation).getDCIteratorBinding().getDef().getBindsName();
        if (accessorName != null)
        {
          operationName = BeanDCUtils.makeReaderName(accessorName);
        }        
        _dataProvider = ((JUCtrlActionBinding)_operation).getDataControl().getAdaptedDC().getDataProvider();
      }
            
      return new BeanDCInvocationHandler(_dataProvider).invoke(operationName, signature, params);      
    }

    return null;
  }
  
  /**
   * @param type
   * @return class type or java.lang.Object for generic type
   */
  private Class getType(Class type) 
  {
    Class clz = type;
    
    // null - re-type as java.lang.Object
    if ( type == null)
    {
      clz = Object.class;
    }
    
    return clz;
  }
}


