package oracle.adf.model.adapter.bean;


import oracle.adf.model.adapter.AbstractDefinition;
import oracle.adf.model.adapter.bean.jpa.JPQLDataFilterHandler;
import oracle.adf.model.adapter.generic.DataControlStructure;
import oracle.adf.model.meta.DataControlDefinition;

import oracle.adf.share.logging.ADFLogger;
import oracle.adfinternal.model.adapter.bean.BeanAnnotationAbstractDefinition;

import oracle.binding.DataControl;
import oracle.jbo.common.JBOClass;
import oracle.jbo.uicli.mom.JUTags;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *  @version $Header: bc4jrt/modules/adf-model/src/oracle/adf/model/adapter/bean/BeanDefinition.java /st_jdevadf_patchset_ias/1 2012/06/27 07:48:06 ppark Exp $
 *  @author  jpham
 *  @since   11.1.1.0
 */

public class BeanDefinition2
  extends BeanAnnotationAbstractDefinition
{
  public static final String BEAN_DEFINITION = 
    "bean-definition"; //NORES
  public static final String BEAN_XMLNS = 
    "xmlns"; //NORES
  public static final String BEAN_DEFINITION_URI =
    "http://xmlns.oracle.com/adfm/adapter/bean"; //NORES

  /** Java bean specifics */
  public static final String BEAN_CLASS = JUTags.BeanClass;    

  /** DataControlHandler class */
  public static final String DataControlHandlerName =
    DataFilterHandler.class.getName();

  /** JPA Data handler class */
  public static final String JPQLDataControlHandlerName =
    JPQLDataFilterHandler.class.getName();

  /** bean DataControlStructureProvider identifier */
  public static final String BEAN_PROVIDER = 
    "bean"; //NORES

  /** Must match adapter-definition.xml file */
  private static final String ADAPTER_TYPE =
    "oracle.adfm.adapter.BeanDataControl"; //NORES

  //===========================================================================
  //  -- logging support
  //============================================================================

  public static final String LOGGER = "oracle.adf.model.adapter.bean";
  private ADFLogger _logger = ADFLogger.createADFLogger(LOGGER);

  /**
   * Zero arg constructor required by the framework.
   */
  public BeanDefinition2()
  {
    super();
  }

  /**
   * Constructor used by the BeanAbstractAdapter.
   */
  public BeanDefinition2(DataControlStructure definition)
  {
    super(definition);
  }

  /**
   * Creates data control at runtime.
   */
  public DataControl createDataControl()
  {
    _logger.fine("Creating " + BeanDataControl2.class.getName());
    return new BeanDataControl2(this);
  }

  /**
   * Returns the dc structure provider type for this BeanAbstractDefinition.
   */
  protected String getProviderType()
  {
    return BEAN_PROVIDER;
  }

  public String getFullName()
  {
    // overrides superclass' getFullName() because Bean DC uses
    // ".dataProvider" as path to its instance instead of the default
    // ".root".
    return getName() + ".dataProvider";
  }

  public String getAdapterType()
  {
    return ADAPTER_TYPE;
  }

  /**
   * @return false to indicate that EJB data controls maintain
   * their own package info in the structure defs they create.
   */
  public boolean assignDefaultPackage()
  {
    return false;
  }

  public int getCachingMode()
  {
    return AbstractDefinition.CACHE_TO_SOURCEPATH;
  }

  public boolean usePersistedStructure()
  {
    return getCachingMode() == AbstractDefinition.CACHE_TO_SOURCEPATH;
  }

  public boolean shouldCompileProjectOnCreate()
  {
    return true;
  }

  /**
   * Indicates if the adapter support updates, sort, transaction or criteria
   */
  public boolean isSupported(String operation)
  {
    if (DataControlDefinition.SUPPORTS_UPDATES.equals(operation) ||
        DataControlDefinition.SUPPORTS_SORT_COLLECTION.equals(operation) ||
        isSupportsOperation(operation))
    {
      return true;
    }

    return super.isSupported(operation);
  }
  
  //------------------------------------------------------------------------
  // private methods
  //------------------------------------------------------------------------

  /**
   * Determine if TransactionalDC|CriteriaDC support -
   * see oracle.adfdtinternal.model.adapter.bean.BeanDataControlProviderImpl
   * @param operation
   * @return true/false
   */
  private boolean isSupportsOperation(String operation)
  {
    if (DataControlDefinition.SUPPORTS_TRANSACTION.equals(operation) ||
        DataControlDefinition.SUPPORTS_CRITERIA.equals(operation))
    {
      final Object obj = getDCProperty(operation);
      if (obj != null && obj instanceof Boolean &&
          ((Boolean)obj).booleanValue())
      {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Get property value fo this name
   * @param propName
   * @return Object
   */
  private Object getDCProperty(String propName)
  {
    if (getStructure() != null)
    {
      return getStructure().getProperty(propName);
    }
    return null;
  }  

  //------------------------------------------------------------------------
  // Overrided methods
  //------------------------------------------------------------------------

  @Override
  protected void initDefinition()
  {
    _logger.fine("Loading bean-definition.");    
    final Node node = getRuntimeMetaData();
    if (node != null)
    {
      final NodeList childList = node.getChildNodes();
      if (childList != null && childList.getLength() == 1)
      {
        Node beanDefNode = childList.item(0);
        if (beanDefNode != null)
        {
          NamedNodeMap beanDefAttrs = beanDefNode.getAttributes();
          if (beanDefAttrs != null)
          {
            // <BeanClass>
            Node attr =
              beanDefAttrs.getNamedItem(BeanDefinition.BEAN_CLASS);
            if (attr != null)
            {
              setProperty(BeanDefinition.BEAN_CLASS, attr.getNodeValue());
            }

            // <AccessMode>
            attr = beanDefAttrs.getNamedItem(ACCESS_MODE);
            if (attr != null)
            {
              setProperty(ACCESS_MODE, attr.getNodeValue());
            }
            
            // <DataControlHandler>
            attr =
                beanDefAttrs.getNamedItem(BeanDefinition.DATA_CONTROL_HANDLER);
            if (attr != null)
            {
              setProperty(BeanDefinition.DATA_CONTROL_HANDLER,
                          attr.getNodeValue());
              // Add property _SupportsCriteria for 'Named Criteria'
              try
              {
                Class clz = JBOClass.forName(attr.getNodeValue());
                if (clz.getName().endsWith(JPQLDataControlHandlerName) ||
                    clz.getSuperclass().getName().endsWith(JPQLDataControlHandlerName))
                {
                  setProperty(DataControlDefinition.SUPPORTS_CRITERIA,
                              Boolean.valueOf(true));
                }
              }
              catch (ClassNotFoundException e)
              {
                _logger.severe("Exception occurred at forName(" +
                               attr.getNodeValue() + ")"); // NORES
              }
            }
          }
        }
      }
    }
  }
}
