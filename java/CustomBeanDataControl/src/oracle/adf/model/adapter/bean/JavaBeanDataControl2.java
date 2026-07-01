package oracle.adf.model.adapter.bean;


import java.util.HashMap;
import java.util.Map;
import oracle.adf.model.adapter.bean.annotation.AccessModeType;
import oracle.adf.model.adapter.bean.handler.DCHandler;
import oracle.adf.model.adapter.dataformat.BeanHandler;
import oracle.adf.model.generic.StructureDefImpl;

import oracle.binding.meta.Definition;
import oracle.binding.meta.StructureDefinition;

import oracle.jbo.uicli.mom.JUMetaObjectManager;

/**
 *  JavaBeanDataControl implements getDefinition().  This method, first finds
 *  beanMetadata object and loads it.  If there is no beanMetadata then it will
 *  load it from beanClass and associated annotations
 *
 *  @version $Header: bc4jrt/modules/adf-model/src/oracle/adf/model/adapter/bean/JavaBeanDataControl.java /st_jdevadf_patchset_ias/3 2012/07/30 14:17:21 jpham Exp $
 *  @author  jpham
 *  @since   11.1.2
 */
public abstract class JavaBeanDataControl2
  extends BeanFilterableDataControl2
  implements DCHandler
{
  private String _beanClassName;
  
  // Bean structure definition handler
  private BeanHandler _handler;

  // Structure def of the data in the jsr 227 specified format.
  private StructureDefinition _structDef = null;
  
  /** Data control handler (11.1.2) */
  protected Object _dcHandler;
  
  private boolean _implementsSort;
  private short _accessModeFlag;
  private Map<String, AccessModeType> _pagingTypeMap;
  
  public static final short ACCMODE_SCROLLABLE = 0x01;
  public static final short ACCMODE_RANGE_PAGING = 0x04;

  /**
   * Gets the data control definition.
   * @param name (beanClass)
   * @param type
   * @return The structure definition for data control
   */
  public Definition getDefinition(String name, int type)
  {
    _logger.finer("Getting the structure definitions for: " + name);

    // Find beanMetadata from cache
    _structDef = getStructureDef(name);

    // Try loading beanMetadata XML
    if (_structDef == null)
    {
      _structDef = getDefMetadataObject(name);
    }

    // No beanMetadata XML - try loading it from BeanHandler
    if (_structDef == null)
    {
      if (_handler == null)
      {
        _handler = new BeanHandler();
      }
      // Loading the structure from beanClass
      _structDef = _handler.getStructure(name, null);
    }

    return _structDef;
  }
  
  /**
   * Returns bean ClassName
   */
  public String getBeanClass()
  {
    return _beanClassName;
  }
  
  protected void setBeanClass(String className)
  {
    _beanClassName = className;
  }
  
  /**
   * Returns DataControlHandler
   */
  protected Object getDataControlHandler()
  {
    return _dcHandler;
  }

  /**
   * set DataControlHandler
   */
  protected void setDataControlHandler(Object dcHandler)
  {
    _dcHandler = dcHandler;
    // Initilize data control handler
    initDCProperties(dcHandler);
  }

  /**
   * Handle loading the structure from XML MetaData
   */
  private StructureDefinition getDefMetadataObject(String metaObjectName)
  {
    _logger.finer("Getting the structure definitions from bean XML file");

    // Contain this metadataObject - i.e. bean XML metadata
    if (JUMetaObjectManager.getJUMom().findMetadataObject(metaObjectName) !=
        null)
    {
      // Loading meta-objectBean from XML file into RT defImpl
      final StructureDefImpl defImpl =
        (StructureDefImpl)JUMetaObjectManager.getJUMom().findDefinitionObjectDontCheckName(metaObjectName,
                                                                                           JUMetaObjectManager.TYP_DEF_ANY,
                                                                                           StructureDefImpl.class,
                                                                                           false);

      // Convert defImpl to StructureDefinition - add to mTypeStructs
      if (defImpl != null)
      {
        setStructureDef(defImpl);
      }
      // Get defintion from mTypeStructs
      return getStructureDef(metaObjectName);
    }
    return null;
  }
  
  /**
   * Set access mode for scollable and range-paging
   */
  protected void setAccessMode(String accessMode)
  {
    if (accessMode != null)
    {
      if (accessMode.equals(BeanDefinition.SCROLLABLE))
      {
        _accessModeFlag = ACCMODE_SCROLLABLE;
      }
      if (accessMode.equals(BeanDefinition.RANGE_PAGING))
      {
        _accessModeFlag =  ACCMODE_RANGE_PAGING;
      }
    }
  }

  //------------------------------------------------------------------------
  // oracle.adf.model.adapter.bean.handler.DCHandler - supported options
  //------------------------------------------------------------------------
    
  /**
   * @return true <code>boolean</code> for supports criteria
   */
  public boolean supportsCriteria()
  {
    return false; /** To be determined by dc provider */
  }
  
  /**
   * @return true <code>boolean</code> for supports paging
   */
  public boolean supportsPaging()
  {
    return (_accessModeFlag > 0);
  }
    
  /**
   * @return true <code>boolean</code> for supports range_paging
   */
  public boolean supportsRangePaging()
  {
    return ((_accessModeFlag & ACCMODE_RANGE_PAGING) != 0);
  }
  
  /**
   * @return true <code>boolean</code> for supports sorting
   */
   public boolean supportsSorting()
   {
     return _implementsSort;
   }
   
   void setSupportsSorting(boolean bool)
   {
     _implementsSort = bool;
   }
  
  /**
   * @param name <code>String</code> accessor name
   * @return true <code>boolean</code> for accessor supports paging
   */
  public boolean supportsPaging(String name)
  {
    return (getAccessModeType(name) != AccessModeType.NO_PAGING);
  }

  /**
   * @param name <code>String</code> accessor name
   * @return true <code>boolean</code> for accessor supports range paging
   */
  public boolean supportsRangePaging(String name)
  {
    return (getAccessModeType(name) == AccessModeType.RANGE_PAGING);
  }
  
  private AccessModeType getAccessModeType(String name)
  {
    return ((_pagingTypeMap != null) ? _pagingTypeMap.get(name) : AccessModeType.SCROLLABLE);
  }
  
  void setPagingType(String name, AccessModeType type)
  {
    // Add name in form of accessor.className
    if (_pagingTypeMap == null)
    {
      _pagingTypeMap = new HashMap<String, AccessModeType>(10);
    }
    _pagingTypeMap.put(name, type);
  }
}
