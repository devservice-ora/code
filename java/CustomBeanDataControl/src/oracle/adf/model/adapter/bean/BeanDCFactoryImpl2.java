package oracle.adf.model.adapter.bean;


import java.util.Map;
import oracle.adf.model.BindingContext;
import oracle.adf.model.DataControl;
import oracle.adf.model.adapter.AbstractDefinition;
import oracle.adf.model.adapter.AdapterDCService;
import oracle.adf.model.adapter.DataControlFactoryImpl;
import oracle.adf.model.adapter.bean.annotation.AccessModeType;
import oracle.adf.model.adapter.dataformat.AccessorDef;
import oracle.adf.model.adapter.dataformat.BeanHandler;
import oracle.adf.model.adapter.dataformat.StructureDef;
import oracle.adf.model.bean.DCDataVO;
import oracle.adf.model.generic.AccessorDefImpl;
import oracle.adfinternal.model.adapter.bean.BeanDataControlDefinition;
import oracle.jbo.AttributeDef;
import oracle.jbo.RowSet;
import oracle.jbo.server.ViewDefImpl;
import oracle.jbo.uicli.mom.JUTags;

/**
 *  @version $Header: bc4jrt/modules/adf-model/src/oracle/adf/model/adapter/bean/BeanDCFactoryImpl.java /st_jdevadf_patchset_ias/2 2012/07/30 14:17:21 jpham Exp $
 *  @author  jpham
 *  @since   12.1.2
 */
public class BeanDCFactoryImpl2
  extends DataControlFactoryImpl
{
  public BeanDCFactoryImpl2()
  {
    super();
  }

  /**
   * Creates a data control.
   * <p>
   * Incase of HttpSession, add APP_PARAM properties into applicationParams
   * before calling createSession. For types for these three properties see
   * HttpContainer.findSessionCookie().
   *
   * @param ctx         Binding context.
   * @param sName       Name of the data control.
   * @param appParams   Application parameters.
   * @param cpxMetaData Contains the metadata for the data control.
   * @return Created data control object.
   */
  public DataControl createSession(BindingContext ctx, String sName,
                                   Map appParams, Map cpxMetaData)
  {
    DataControl dc = super.createSession(ctx, sName, appParams, cpxMetaData);
    if (((AdapterDCService)dc).getAdaptedDC() instanceof JavaBeanDataControl2)
    {
      JavaBeanDataControl2 beandcImpl = 
        (JavaBeanDataControl2)((AdapterDCService)dc).getAdaptedDC();
      boolean implSort = 
        Boolean.parseBoolean((String)cpxMetaData.get(JUTags.ImplementsSort));
      beandcImpl.setSupportsSorting(implSort);
    }
    
    return dc;
  }
  
  /**
   * Creates a new <code>AdapterDCService</code> instance.
   * @param name Name of the data control.
   * @param def The location of the structure definition metadata.
   * @param dcInstance the data control instance.
   * @param dcDef the data control definition that has created this service.
   */
  protected DataControl createDataControl(String name, String def,
                                          Object dcInstance,
                                          AbstractDefinition dcDef)
  {
    final JavaBeanDataControl2 beandcImpl = (JavaBeanDataControl2)dcInstance;
    return new AdapterDCService(name, def, dcInstance, dcDef)
    { 
      @Override
      protected ViewDefImpl createViewDef(String strDefFullName,
                                          String vdName,
                                          oracle.jbo.StructureDef sd,
                                          AttributeDef[] fkAttrs,
                                          int vdKind)
      {
        ViewDefImpl vd =
          super.createViewDef(strDefFullName, vdName, sd, fkAttrs,
                              DCDataVO.VD_KIND_BEAN);
        if (sd instanceof AccessorDefImpl)
        {
          AccessModeType type = getAccessModeType(sd.getName(), sd.getFullName());
          // Require for range paging
          if (type == AccessModeType.RANGE_PAGING)
          {
            vd.setAccessMode(RowSet.RANGE_PAGING);
          }
          // Add fullname in form of accessor.className
          beandcImpl.setPagingType(sd.getName() + "." + sd.getFullName(), type);
        }
        
        return vd;
      }
      
      /**
       * Returns AccessModeType if accessor contains @AccessMode
       */
      private AccessModeType getAccessModeType(String accName, String voName)
      {
        // Scrollable (default)
        AccessModeType type = AccessModeType.SCROLLABLE;
        if (beandcImpl.supportsPaging() && beandcImpl.getDataProvider() != null)
        {
          BeanHandler h = new BeanHandler();
          h.setRecursive(false);          
          StructureDef def = (StructureDef) h.buildStructure(beandcImpl.getBeanClass());
          AccessorDef accDef = (AccessorDef) def.getAccessorDefinitions().find(accName);
          if (accDef != null)
          {
            Object o = accDef.getProperty(BeanDataControlDefinition.SUPPORTS_PAGING);
            if ((o instanceof String) && Boolean.parseBoolean((String)o))
            {
              StructureDef def2 = (StructureDef)accDef.getStructure();
              if (def2 != null && def2.getFullName().equals(voName))
              {
                // Find @AccessMode on accessor
                o = accDef.getProperty(BeanAbstractDefinition.ACCESS_MODE);
                if (o instanceof AccessModeType)
                {
                  type = (AccessModeType)o;
                }
                else if (beandcImpl.supportsRangePaging())
                {
                  type = AccessModeType.RANGE_PAGING;
                }
              }
            }
          }
        }
        return type;
      }
    };
  }
}
