package oracle.adf.model.adapter.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import oracle.adf.model.adapter.AdapterException;
import oracle.binding.DataControl;
import oracle.binding.DefinitionProviderDataControl;
import oracle.binding.ManagedDataControl;
import oracle.binding.OperationBinding;
import oracle.binding.TransactionalDataControl;
import oracle.binding.UpdateableDataControl;

/**
 *  @version $Header: bc4jrt/modules/adf-model/src/oracle/adf/model/adapter/bean/BeanDataControl.java /st_jdevadf_patchset_ias/2 2012/07/30 14:17:21 jpham Exp $
 *  @author  jpham
 *  @since   11.1.1.0
 */

public class BeanDataControl2
  extends JavaBeanDataControl2
  implements TransactionalDataControl, oracle.adf.model.ManagedDataControl
{
  private Object _dataProvider;
  private TransactionalDataControl _txnAdapter;
  private UpdateableDataControl _updateAdapter;
  private ManagedDataControl _managedAdapter;
  private DataControl _mAdapter;
  private DefinitionProviderDataControl _definitionAdapter;

  /**
   * Create bean data control provider
   *
   * @param beanDefinition
   */
  public BeanDataControl2(BeanDefinition2 beanDefinition)
  {    
    final String beanClass =
      (String)beanDefinition.getProperty(BeanDefinition.BEAN_CLASS);
    setBeanClass(beanClass);
   
    // Load bean source as data control provider 
    _dataProvider = loadProvider(beanClass);
    
    // Get data filter handler if it is available
    final String handlerName =
      (String)beanDefinition.getProperty(BeanDefinition.DATA_CONTROL_HANDLER);
    _dcHandler = getDCHandler(handlerName);
    
    // Set data access mode    
    setAccessMode((String)beanDefinition.getProperty(BeanDefinition.ACCESS_MODE));

    // Initilize data control handler    
    setDataControlHandler(_dcHandler);

    // Override all _dcHandler and initialize beanDC properties
    initDCProperties(_dataProvider);
  }

  /**
   * Initialize Data Control properties
   * @param instance
   */
  @Override
  protected void initDCProperties(Object instance)
  {
    super.initDCProperties(instance);
    if (instance instanceof DataControl)
    {
      DataControl adapter = (DataControl)instance;
      _mAdapter = adapter;
      if (adapter instanceof TransactionalDataControl)
      {
        _txnAdapter = (TransactionalDataControl)adapter;
      }
      if (adapter instanceof UpdateableDataControl)
      {
        _updateAdapter = (UpdateableDataControl)adapter;
      }
      if (adapter instanceof ManagedDataControl)
      {
        _managedAdapter = (ManagedDataControl)adapter;
      }
      if (adapter instanceof DefinitionProviderDataControl)
      {
        _definitionAdapter = (DefinitionProviderDataControl)adapter;
      }
    }
  }

  /**
   * Load the bean data control provider
   */
  private Object loadProvider(String beanClass)
    throws AdapterException
  {
    try
    {
      if (_logger.isLoggable(Level.FINE))
      {
        _logger.fine("Attempting to load provider : " + beanClass);
      }

      // Create bean provider
      return oracle.jbo.common.JBOClass.forName(beanClass).newInstance();
    }
    catch (Exception e)
    {
      _logger.severe("Failed to load the provider: " + beanClass);
      throw new AdapterException(e);
    }
  }

  //------------------------------------------------------------------------
  // oracle.binding.DataControl support
  //------------------------------------------------------------------------

  /**
   * Returns name to identify this datacontrol inside a BindingContext.
   */
  public String getName()
  {
    String name = null;
    /** Bean provider to implement getName */
    if (_mAdapter != null)
    {
      name = _mAdapter.getName();
    }
    return (name != null ? name : super.getName());
  }

  /**
   * Releases the DataControl.  The DataControl should be in a
   * collectable state after release() has been invokd.
   * <p>
   * This should close all open DataControl resources.
   **/
  public void release()
  {
    /** Bean provider to implement release */
    if (_mAdapter != null)
    {
      _mAdapter.release();
    }
    super.release();
  }

  /**
   * Returns the data provider for this data control
   */
  public Object getDataProvider()
  {
    Object provider = null;
    /** Bean provider to implement getDataProvider */
    if (_mAdapter != null)
    {
      provider = _mAdapter.getDataProvider();
    }
    return (provider != null ? provider : _dataProvider);
  }

  /**
   * All OperationBindings should first delegate to the DataControl associated
   * with the binding to perform the action. DataControls may choose to interpret
   * a given action differently based on the data/collection that action is bound to.
   * @return true if this datacontrol has handled this action, false if the action
   * should be interpreted in the bindings framework or in the caller.
   */
  public boolean invokeOperation(Map bindingContext,
                                 OperationBinding action)
  {
    Object handler = getDataControlHandler();
    if (handler != null)
    {
      Boolean result =
          (Boolean)new BeanDCInvocationHandler2(handler).invokeMethod("invokeOperation", 
                                                                     new Class[] { Map.class, OperationBinding.class },
                                                                     new Object[] { bindingContext, action });
      if (result != null && result.booleanValue())
      {
        return true;
      }
    }
    /** Bean provider to implement invokeOperation */
    if (_mAdapter != null)
    {
      return _mAdapter.invokeOperation(bindingContext, action);
    }
    return super.invokeOperation(bindingContext, action);
  }

  //------------------------------------------------------------------------
  // oracle.binding.ManagedDataControl support
  //------------------------------------------------------------------------

  /**
   * Implemented per the ManagedDataControl contract. Tracks when this data
   * control begins an application request.
   */
  public void beginRequest(HashMap requestCtx)
  {
    /** Bean provider to implement beginRequest */
    if (_managedAdapter != null)
    {
      _managedAdapter.beginRequest(requestCtx);
    }
  }

  /**
   * Implemented per the ManagedDataControl contract. Tracks when this data
   * control has ended an application request.
   */
  public void endRequest(HashMap requestCtx)
  {
    /** Bean provider to implement endRequest */
    if (_managedAdapter != null)
    {
      _managedAdapter.endRequest(requestCtx);
    }
  }

  /**
   * Resets the state of the BeanDataControl to it's initial state upon
   * entering the application: a new UnitOfWork is acquired and all RSIs
   * associated with this DataControl are reset to their initial state.
   */
  public boolean resetState()
  {
    /** Bean provider to implement resetState */
    if (_managedAdapter != null)
    {
      return _managedAdapter.resetState();
    }
    return false;
  }

  //------------------------------------------------------------------------
  // oracle.adf.model.ManagedDataControl support
  //------------------------------------------------------------------------

  /**
   * Creates a snapshot of the state of the DataControl.
   * A snapshot may be used to save the DataControl state so that
   * it may be restored some time later.  Returns a serializable handle to
   * the snapshot.
   *
   * @return a snapshot handle
   */
  public Serializable createSnapshot()
  {
    /** Bean provider to implement createSnapshot */
    if (_managedAdapter != null &&
        _managedAdapter instanceof oracle.adf.model.ManagedDataControl)
    {
      return ((oracle.adf.model.ManagedDataControl)_managedAdapter).createSnapshot();
    }
    return null;
  }

  /**
   * Restore the state of the DataControl with the snapshot
   *   state that is referenced by the handle.
   *
   * @param handle Serializable a snaphsot handle that was created with
   *   {@link #createSnapshot()}.
   */
  public void restoreSnapshot(Serializable handle)
  {
    /** Bean provider to implement restoreSnapshot */
    if (_managedAdapter != null &&
        _managedAdapter instanceof oracle.adf.model.ManagedDataControl)
    {
      ((oracle.adf.model.ManagedDataControl)_managedAdapter).restoreSnapshot(handle);
    }
  }

  /**
   * Removes the snapshot associated with the snapshot handle.
   *
   * @param handle Serializable a snapshot handle that was created with
   *    {@link #createSnapshot()}.
   */
  public void removeSnapshot(Serializable handle)
  {
    /** Bean provider to implement removeSnapshot */
    if (_managedAdapter != null &&
        _managedAdapter instanceof oracle.adf.model.ManagedDataControl)
    {
      ((oracle.adf.model.ManagedDataControl)_managedAdapter).removeSnapshot(handle);
    }
  }

  //------------------------------------------------------------------------
  // oracle.binding.TransactionalDataControl support for the bean class
  // implements TransactionalDataControl
  //------------------------------------------------------------------------

  /**
   * Indicates whether the UnitOfWork has any changes.
   */
  public boolean isTransactionDirty()
  {
    /** Bean provider to implement isTransactionDirty */
    if (_txnAdapter != null)
    {
      return _txnAdapter.isTransactionDirty();
    }
    return false;
  }

  /**
   * Rollsback the transaction for this data control. The side effect of this
   * method call is that all data controls sharing the same transaction will
   * be commited and have their transactional state reset as well.
   *
   * @see oracle.adf.model.binding.DCDataControl#rollbackTransaction()
   */
  public void rollbackTransaction()
  {
    /** Bean provider to implement rollbackTransaction */
    if (_txnAdapter != null)
    {
      _txnAdapter.rollbackTransaction();
    }
  }

  /**
   * Commits the transaction for this data control.
   *
   * @see oracle.adf.model.binding.DCDataControl#commitTransaction()
   */
  public void commitTransaction()
  {
    /** Bean provider to implement commitTransaction */
    if (_txnAdapter != null)
    {
      _txnAdapter.commitTransaction();
    }
  }

  /**
   * Handles releasing transactional resources when the data control
   * is release from the binding container.
   */
  public void release(int flags)
  {
    /** Bean provider to implement release */
    if (_mAdapter instanceof oracle.adf.model.DataControl)
    {
      ((oracle.adf.model.DataControl)_mAdapter).release(flags);
    }
  }
}

