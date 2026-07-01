package oracle.adf.model.adapter.bean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import oracle.adf.model.adapter.AdapterException;
import oracle.adf.share.logging.ADFLogger;

/**
 *  @version $Header: bc4jrt/modules/adf-model/src/oracle/adf/model/adapter/bean/BeanDCInvocationHandler.java /main/7 2013/04/17 13:43:16 jpham Exp $
 *  @author  jpham
 *  @since   12.1.1
 */
public class BeanDCInvocationHandler2
  implements InvocationHandler
{  
  /** Proxy instance */
  private Object _dataProvider;
  
  /** Logger */
  private ADFLogger _logger = ADFLogger.createADFLogger(BeanDCInvocationHandler2.class);

  public BeanDCInvocationHandler2(Object dataProvider)
  {
    _dataProvider = dataProvider;
  }
  
  public Object getInstance()
  {
    return _dataProvider;
  }
  
  /**
   * Invoke operation   
   * @param methodName <code>String</code>
   * @param parameterTypes <code>Class[]</code>
   * @param args <code>Object[]</code>
   * @return object
   */
  public Object invokeMethod(String methodName, Class[] parameterTypes, Object[] args)
  {
    _logger.finer(getClass().getName() + ".invokeMethod(" + methodName + ")");
    if (getInstance() != null)
    {    
      final Method method = findMethod(getInstance(), methodName, parameterTypes);
      if (method != null)
      {
        return invoke(getInstance(), method, args);
      }      
    }
    
    return null;
  }
  
  /**
   * Find method
   * @param proxy instance
   * @param methodName <code>String</code>
   * @param parameterTypes <code>Class[]</code>
   * @return method or null if there is no such methodName
   */
  public static Method findMethod(Object proxy, String methodName, Class[] parameterTypes)
  {
    Method method = null;
    if (proxy != null)
    {
      try
      {
        method = proxy.getClass().getMethod(methodName, parameterTypes);
      }
      catch (NoSuchMethodException e)
      {
        // No such method in the implementation bean class
        method = null;
      }
    }
    
    return method;
  }
  
  /**
   * Find all methods with a matching methodName
   * @param proxy
   * @param methodName
   * @return methods or null there is no such methodName
   */
  public static Method[] findMethods(Object proxy, String methodName)
  { 
    if (proxy != null && methodName != null)
    {
      final List<Method> methodlist = new ArrayList<Method>();
      final Method[] methods = proxy.getClass().getMethods();      
      for (Method m : methods)
      {
        if (m.getName().equals(methodName))
        {
          methodlist.add(m);
        }
      }
      if (methodlist.size() > 0)
      {
        return methodlist.toArray(new Method[methodlist.size()]);
      }
    }
    
    return null;
  }

  /**
   * Processes a method invocation on a proxy instance and returns
   * the result.  This method will be invoked on an invocation handler
   * when a method is invoked on a proxy instance that it is
   * associated with.
   */
  public Object invoke(String methodName, Class[] parameterTypes, Object[] args) 
    throws Exception
  {
    return invokeMethod(methodName, parameterTypes, args);    
  }
  
  //------------------------------------------------------------------------
  // InvocationHandler Impl.
  //------------------------------------------------------------------------
  
  /**
   * Processes a method invocation on a proxy instance and returns
   * the result.  This method will be invoked on an invocation handler
   * when a method is invoked on a proxy instance that it is
   * associated with.
   */
  public Object invoke(Object proxy, Method method, Object[] args)
  {    
    if (method != null)
    {
      try
      {
        Object result = method.invoke(proxy, args);
        return result;
      }
      catch (IllegalArgumentException e)
      {
        // Default to the framework for handling IllegalArgument.
        return null;
      }
      catch (IllegalAccessException e)
      {      
        _logger.severe("IllegalAccessException ->" + "invoke(" + method.getName() + ")", e);
        throw new AdapterException(e);
      }
      catch (InvocationTargetException e)
      {  
        _logger.severe("InvocationTargetException ->" + "invoke(" + method.getName() + ")", e);
        throw new AdapterException(e);
      }
    }
    
    return null;
  }
}

