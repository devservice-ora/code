package oracle.adf.model.adapter.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import oracle.adf.model.adapter.AbstractImpl;
import oracle.adf.model.generic.AccessorDefImpl;
import oracle.adf.model.generic.StructureDefImpl;
import oracle.adf.share.ADFContext;
import oracle.adf.share.logging.ADFLogger;
import oracle.binding.AttributeContext;
import oracle.binding.DataControl;
import oracle.binding.OperationBinding;
import oracle.binding.OperationInfo;
import oracle.binding.RowContext;
import oracle.binding.UpdateableDataControl;
import oracle.binding.meta.AccessorDefinition;
import oracle.binding.meta.StructureDefinition;
import oracle.jbo.common.JBOClass;
import oracle.jbo.common.JboNameUtil;

/**
 *  @version $Header: bc4jrt/modules/adf-model/src/oracle/adf/model/adapter/bean/UpdatableBeanDataControl.java /st_jdevadf_pt-11.1.1.9.0/1 2013/11/08 11:57:10 saabhatt Exp $
 *  @author  jpham
 *  @since   11.1.1.0
 */

public class UpdatableBeanDataControl2
  extends AbstractImpl
  implements UpdateableDataControl
{

  // Mapped of the accessors associated with this data control
  private Map accessors;

  //------------------------------------------------------------------------
  // - Logging suppport
  //------------------------------------------------------------------------

  protected ADFLogger _logger =
    ADFLogger.createADFLogger(BeanDefinition.LOGGER);

  public UpdatableBeanDataControl2()
  {
    super(); 
    if (ADFContext.hasCurrent() && ADFContext.getCurrent().isHighPerformanceEnvEnabled()) 
    {
        return;
    }
    this.accessors = new HashMap(5); 
  }

  public boolean setAttributeValue(AttributeContext ctx, Object value)
  {
    //let the framework set the attribute value.
    return false;
  }

  /**
   * Creates the object described by <code>RowContext</code>, adds it to
   * the underlying provider Collection, registers it in the given
   * transaction, and returns the newly created object.
   *
   * @param ctx - description of the Row to create
   */
  public Object createRowData(RowContext ctx)
  {
    RowContext genCtx = ctx;
    String rowTypeName = genCtx.getRowDataProviderType();
    Object masterObject = genCtx.getMasterRowDataProvider();
    Object rowDataContainer = genCtx.getRowDataContainer();

    // Create the new row object
    Object createdObject = null;
    try
    {
      createdObject = createObject(rowTypeName);
      // JRS:  TBD
      // createdObject = getTransaction().createNewInstance(
      // Thread.currentThread().getContextClassLoader().loadClass(rowTypeName));
    }
    catch (Exception e)
    {
      throw new RuntimeException("Cannot create object of type: " +
                                 rowTypeName, e);
    }

    // Only do the add/insert if the row data container is a Collection.
    // In the case of a 1:1 association the data container will be the
    // related object.  Leave it to ADF to handle the "setting".
    if (rowDataContainer instanceof Collection)
    {
      Collection rowObjectCollection = (Collection)rowDataContainer;
      // if the master provider is the accessor Map, or if the master
      // is null,  Assume this is a Collection resulting from a root
      // accessor or query.  In either case, do a direct insert in the
      // Collection
      //
      if (masterObject == this.accessors || masterObject == null)
      {
        performDirectInsert(genCtx, rowObjectCollection, createdObject);
      }
      // if not a root accessor or method invocation, assume this is a
      // regular accessor
      else
      {
        String masterClassName = masterObject.getClass().getName();
        StructureDefImpl masterDef =
          new StructureDefImpl(null, masterClassName, masterClassName);
        String accessorName = genCtx.getMasterAccessorName();
        AccessorDefImpl accessorDef =
          (AccessorDefImpl)masterDef.getAccessorDef(accessorName);
        String addMethodName = 
          (accessorDef != null ? accessorDef.getAddMethodName() : null);
        // make sure that an add method has been defined
        if (addMethodName != null && !"".equals(addMethodName))
        {
          performAddMethodInsert(genCtx, rowTypeName, masterObject,
                                 rowObjectCollection, createdObject,
                                 accessorName, addMethodName);
        }
        // If no add method defined, do a direct insert
        else
        {
          performDirectInsert(genCtx, rowObjectCollection, createdObject);
        }
      }
    }
    return createdObject;
  }

  private Object createObject(String className)
  {
    Class clz = null;
    try
    {
      clz = JBOClass.forName(className);

      Constructor cons = JboNameUtil.findConstructor(clz, null);

      return cons.newInstance((Object)null);
    }
    catch (Exception e)
    {
      throw new RuntimeException("Exception occurred invoking constructor for " +
                                 className, e);
    }
  }

  /**
   * Given the RowContext, master row object, and given accessor meta data, this method invokes
   * the user defined "add" method on the Master row to update an Accessor type Collection.
   */
  private void performAddMethodInsert(RowContext genCtx,
                                      String rowTypeName,
                                      Object masterObject,
                                      Collection rowObjectCollection,
                                      Object createdObject,
                                      String accessorName,
                                      String addMethodName)
  {
    try
    {
      Class objectClass = JBOClass.forName(rowTypeName);
      Method addMethod = null;
      int insertIndex = genCtx.getCurrentRowIndex();

      // if for some reason the currency has not been set (-1), set the index to the end of the list
      if (insertIndex < 0)
      {
        insertIndex = rowObjectCollection.size();
      }

      try
      {
        // first check and see if an addinsert method has been defined (methodName(int, ObjType))
        addMethod =
            masterObject.getClass().getMethod(addMethodName, new Class[] { int.class,
                                                                           objectClass });

      }
      catch (NoSuchMethodException e)
      {
        // if not, look for a plain add
        try
        {
          addMethod =
              masterObject.getClass().getMethod(addMethodName, new Class[] { objectClass });
        }
        catch (NoSuchMethodException eTwo)
        {
          throw new RuntimeException("Add method named " + addMethodName +
                                     " for accessor " + accessorName +
                                     " not defined.", eTwo);
        }
      }

      Object[] params;
      if (addMethod.getParameterTypes().length == 2)
      {
        params = new Object[] { new Integer(insertIndex), createdObject };
      }
      else
      {
        params = new Object[] { createdObject };
      }

      addMethod.invoke(masterObject, params);
    }
    catch (Exception e)
    {
      throw new RuntimeException("Exception occurred invoking Add method named " +
                                 addMethodName + " for accessor " +
                                 accessorName + ".", e);
    }
  }

  /**
   * Given the row context, the underlying provider Collection of the row us updated directly
   * with the newly create object.
   */
  private void performDirectInsert(RowContext genCtx,
                                   Collection rowObjectCollection,
                                   Object createdObject)
  {
    if (rowObjectCollection instanceof List)
    {
      List listRowContainer = (List)rowObjectCollection;
      int index = genCtx.getCurrentRowIndex();

      // if the index is 0 or -1 (not set yet), just add the object
      if (index < 0)
      {
        listRowContainer.add(createdObject);
      }
      else
      {
        listRowContainer.add(index, createdObject);
      }
    }
    else
    {
      Collection collectionRowContainer = rowObjectCollection;
      collectionRowContainer.add(createdObject);
    }    
  }

  public Object registerDataProvider(RowContext ctx)
  {
    return ctx.getRowDataProvider();
  }

  /**
   * Return true if the row at the given index is removed from the the RSI
   * associated with the given iterator-binding. In addition, this method goes
   * into the provider underlying collection removes the object contained in
   * the Row, and marks the object to be deleted in the given transaction.
   */
  public boolean removeRowData(RowContext ctx)
  {
    RowContext genCtx = ctx;
    String rowTypeName = genCtx.getRowDataProviderType();
    Object masterObject = genCtx.getMasterRowDataProvider();
    Object rowProvider = genCtx.getRowDataProvider();
    Object rowDataContainer = genCtx.getRowDataContainer();

    // Only do the delete if the row data container is a Collection. In the
    // case of a 1:1 association the data container will be the related
    // object.  Leave it to ADF to handle the "setting".
    if (rowDataContainer instanceof Collection)
    {
      // if the master provider is the accessor Map, or if the master is
      // null, assume this is a Collection resulting from a root accessor
      // or query.  In either case, do a direct insert in the Collection
      if (masterObject == null || masterObject == this.accessors)
      {
        Collection collectionDataContainer = (Collection)rowDataContainer;
        collectionDataContainer.remove(rowProvider);
      }
      else
      {
        String masterClassName = masterObject.getClass().getName();
        StructureDefImpl masterDef =
          new StructureDefImpl(null, masterClassName, masterClassName);
        String accessorName = genCtx.getMasterAccessorName();
        AccessorDefImpl accessorDef =
          (AccessorDefImpl)masterDef.getAccessorDef(accessorName);
        String removeMethodName = (accessorDef != null 
          ?accessorDef.getRemoveMethodName() : null);
        // if no remove method defined, do a direct remove on the
        // Collection
        if (removeMethodName == null || "".equals(removeMethodName))
        {
          Collection collectionDataContainer = (Collection)rowDataContainer;
          collectionDataContainer.remove(rowProvider);          
        }
        // otherwise, use the remove method
        else
        {
          try
          {
            Class objectClass = JBOClass.forName(rowTypeName);
            // first check and see if an addinsert method has been
            // defined (methodName(int, ObjType))
            Method removeMethod =
              masterObject.getClass().getMethod(removeMethodName,
                                                new Class[] { objectClass });
            removeMethod.invoke(masterObject,
                                new Object[] { rowProvider });
          }
          catch (Exception e)
          {
            throw new RuntimeException("Exception occurred invoking custom remove method named " +
                                       removeMethodName +
                                       " for accessor " + accessorName +
                                       ".", e);
          }
        }
      }
    }

    // return false so that the framework will re-execute
    // any associated RSI.
    return false;
  }

  public void validate()
  {
    // TBD: not sure what to do here....
  }

  /**
   * Returns the data provider for this data control.
   */
  public Object getDataProvider()
  {
    return this;
  }

  /**
   * Returns the name of this Data Control.
   */
  public String getName()
  {
    return this.mName;
  }

  /**
   * Initializes a Map containing all Accessor Collections defined on the
   * given
   * Bean Data Control.
   */
  private void initializeAccessor()
  {
    StructureDefinition dcStructure = getStructureDef();
    if (this.accessors == null) {
        this.accessors = new HashMap(10);
    }
    for (Iterator accessors =
         dcStructure.getAccessorDefinitions().iterator();
         accessors.hasNext(); )
    {
      AccessorDefinition acc = (AccessorDefinition)accessors.next();
      String accName = acc.getName();
      this.accessors.put(accName, new Vector());
    }
  }

  /**
   * ADVANCED: Resets all Accessors defined on this data control to their
   * initial empty state.
   */
  public void resetAccessors()
  {
    if (this.accessors != null) {
        this.accessors.clear();      
    }    
    initializeAccessor();
  }

  public boolean invokeOperation(Map bindingContext,
                                 OperationBinding action)
  {
    // BeanUtils.
    OperationInfo operation = action.getOperationInfo();
    if (operation == null)
      return false;

    String operationName = operation.getOperationName();

    // If data control execution, prime the accessor map
    if (METHOD_EXECUTE.equals(operationName))
    {
      initializeAccessor();
      processResult(this.accessors, bindingContext, action);
      return true;
    }

    return false;
  }

  public void release()
  {
    // flush any references in the accessor map
    resetAccessors();
  }

  //------------------------------------------------------------------------
  // Custom DC Handler - See ejb.xsd and bean.xsd for <DataControlHandler>
  //------------------------------------------------------------------------

  /**
   * Find DC handler instance
   * @param dcHandler <code>String</code> handler className
   * @return instance of DC handler
   */
  protected Object getDCHandler(String dcHandler)
  {
    if (dcHandler != null)
    {
      try
      {
        Class clz = JBOClass.forName(dcHandler);        
        Constructor constructor =
          JboNameUtil.findConstructor(clz, new Class[] { DataControl.class });
        if (constructor == null)
        {
          _logger.warning("Unable to find " + dcHandler); // NORES
          return null;
        }
        return (constructor.newInstance(new Object[] { this }));
      }
      catch (Exception e)
      {
        _logger.severe("Failed to find " + dcHandler); // NORES
      }
    }

    return null;
  }
}
