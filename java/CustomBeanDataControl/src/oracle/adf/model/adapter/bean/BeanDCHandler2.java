package oracle.adf.model.adapter.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import oracle.binding.AttributeContext;
import oracle.binding.DataControl;
import oracle.binding.OperationBinding;
import oracle.binding.RowContext;
import oracle.binding.TransactionalDataControl;
import oracle.binding.UpdateableDataControl;

/**
 *  @version $Header: BeanDCHandler.java 08-aug-2011.15:09:13 jpham    Exp $
 *  @author  jpham
 *  @since   12.1.1
 */
public class BeanDCHandler2
  extends BeanDCInvocationHandler2
  implements TransactionalDataControl, UpdateableDataControl, oracle.adf.model.ManagedDataControl
{
  /** BeanDC Data Control instance */
  protected DataControl _dc;
  
  /** Proxy DC methods */

  // oracle.binding.DataControl
  public static final String getName = "getName"; // NORES
  public static final String release = "release"; // NORES
  public static final String getDataProvider = "getDataProvider"; // NORES
  public static final String invokeOperation = "invokeOperation"; // NORES

  // oracle.binding.TransactionalDataControl
  public static final String isTransactionDirty = "isTransactionDirty"; // NORES
  public static final String rollbackTransaction = "rollbackTransaction"; // NORES
  public static final String commitTransaction = "commitTransaction"; // NORES

  // oracle.binding.UpdateableDataControl
  public static final String setAttributeValue = "setAttributeValue"; // NORES
  public static final String createRowData = "createRowData"; // NORES
  public static final String registerDataProvider = "registerDataProvider"; // NORES
  public static final String removeRowData = "removeRowData"; // NORES
  public static final String validate = "validate"; // NORES

  // oracle.binding.ManagedDataControl
  public static final String beginRequest = "beginRequest"; // NORES
  public static final String endRequest = "endRequest"; // NORES
  public static final String resetState = "resetState"; // NORES

  // oracle.adf.Model.ManagedDataControl
  public static final String createSnapshot = "createSnapshot"; // NORES
  public static final String restoreSnapshot = "restoreSnapshot"; // NORES
  public static final String removeSnapshot = "removeSnapshot"; // NORES

  public BeanDCHandler2(DataControl dc)
  {
    super(dc != null ? dc.getDataProvider() : null);    
    _dc = dc;
  }
  
  //------------------------------------------------------------------------
  // Support oracle.binding.DataControl
  //------------------------------------------------------------------------

  /**
   * Returns name to identify this datacontrol inside a BindingContext.
   */
  public String getName()
  {
    /** Not handle this method - default is the name of this data control. */
    return null;
  }

  /**
   * Releases the DataControl.  The DataControl should be in a
   * collectable state after release() has been invokd.
   * <p>
   * This should close all open DataControl resources.
   **/
  public void release()
  {
    invokeMethod(release, null, null);
  }

  /**
   * Returns the Business Service Object that this datacontrol is associated with.
   *
   * @return The underlying business service object.
   */
  public Object getDataProvider()
  {
    return invokeMethod(getDataProvider, null, null);
  }

  /**
   * Invokes an operation identified by the given method info and with ordered arguments in the params list.
   * <i>To be renamed to invokeOperation</i>
   *
   * @param bindingContext A binding context that provide access to all binding related objects.
   * @param action OperationBinding
   * @return the method return value.
   */
  public boolean invokeOperation(Map bindingContext, OperationBinding action)
  {
    Boolean result =
      (Boolean)invokeMethod(invokeOperation, 
                            new Class[] { Map.class, OperationBinding.class }, new Object[] { bindingContext, action });
    return (result != null ? result.booleanValue() : false);
  }

  //------------------------------------------------------------------------
  // Support oracle.binding.TransactionalDataControl
  //------------------------------------------------------------------------

  /**
   * Returns true if this transaction has been dirtied by this application, as a result
   * of data updates, deletes or inserts.
   *
   * @return true if transaction is dirty, false otherwise.
   */
  public boolean isTransactionDirty()
  {
    Boolean result = (Boolean)invokeMethod(isTransactionDirty, null, null);
    return (result != null ? result.booleanValue() : false);
  }

  /**
   * Rolls back the current transaction.
   */
  public void rollbackTransaction()
  {
    invokeMethod(rollbackTransaction, null, null);
  }

  /**
   * Commits the current transaction to save all changes to the data source.
   */
  public void commitTransaction()
  {
    invokeMethod(commitTransaction, null, null);
  }

  //------------------------------------------------------------------------
  // Support oracle.binding.UpdateableDataControl
  //------------------------------------------------------------------------

  /**
   * This method is called by the data binding framework when a new
   * value is to be set on an attribute in a bean. The attribute and bean
   * are provided in the attribute context along with other
   * framework context information.
   * <p>
   * Return true if the base framework should skip any further processing
   * of this attribute set. Otherwise return false so that framework can
   * perform a set or put of the attribute value based on introspection.
   */
  public boolean setAttributeValue(AttributeContext ctx, Object value)
  {
    Boolean result =
      (Boolean)invokeMethod(setAttributeValue, 
                            new Class[] { AttributeContext.class, Object.class }, new Object[] { ctx, value });
    return (result != null ? result.booleanValue() : false);
  }

  /**
   * This method is called by the data binding framework when a new
   * row is needed from the data control.
   *
   * @param ctx Context of the new row.
   * @return A data object for the new row.
   */
  public Object createRowData(RowContext ctx)
  {
    return invokeMethod(createRowData, new Class[] { RowContext.class }, new Object[] { ctx });
  }

  /**
   * This method is called by the data binding facility before the row in the RowContext object is
   * modified or marked as removed, so the row can be marked dirty by the data control.
   *
   * @param ctx Context of the row to be modified or removed.
   * @return  The data object that the row represents.
   */
  public Object registerDataProvider(RowContext ctx)
  {
    return invokeMethod(registerDataProvider, new Class[] { RowContext.class }, new Object[] { ctx });
  }

  /**
   * This method is called by the data binding facility when a row
   * should be removed from the underlying data source.
   *
   * @param ctx Context of the rowDefinitionProviderDataControl to be removed.
   * @return true if the operation is sucessful, false otherwise.
   */
  public boolean removeRowData(RowContext ctx)
  {
    Boolean result =
      (Boolean)invokeMethod(removeRowData, new Class[] { RowContext.class }, new Object[] { ctx });
    return (result != null ? result.booleanValue() : false);
  }

  /**
   * Validates transaction if dirty.
   */
  public void validate()
  {
    invokeMethod(validate, null, null);
  }

  //------------------------------------------------------------------------
  // Support oracle.adf.model.ManagedDataControl
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
    return (Serializable)invokeMethod(createSnapshot, null, null);
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
    invokeMethod(restoreSnapshot, new Class[] { Serializable.class }, new Object[] { handle });
  }

  /**
   * Removes the snapshot associated with the snapshot handle.
   *
   * @param handle Serializable a snapshot handle that was created with
   *    {@link #createSnapshot()}.
   */
  public void removeSnapshot(Serializable handle)
  {
    invokeMethod(removeSnapshot, new Class[] { Serializable.class }, new Object[] { handle });
  }

  public void beginRequest(HashMap requestCtx)
  {
    invokeMethod(beginRequest, new Class[] { HashMap.class }, new Object[] { requestCtx });
  }

  public void endRequest(HashMap requestCtx)
  {
    invokeMethod(endRequest, new Class[] { HashMap.class }, new Object[] { requestCtx });
  }

  public boolean resetState()
  {
    Boolean result = (Boolean)invokeMethod(resetState, null, null);
    return (result != null ? result.booleanValue() : false);
  }
}

