package oracle.adf.model.adapter.bean.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 *  @version $Header: bc4jrt/modules/adf-model/src/oracle/adf/model/adapter/bean/provider/BeanDataProvider.java /st_jdevadf_patchset_ias/2 2012/06/27 07:48:06 ppark Exp $
 *  @author  jpham
 *  @since   11.1.2
 */

public class BeanDataProvider2 <K extends String, V extends BeanDataCollection2>
  extends HashMap<K, V>
{
  private static final long serialVersionUID = 4713491054195923856L;
  private Map<String, String> _viewMap = new WeakHashMap<String, String>();
  
  public BeanDataProvider2()
  {
    super();    
  }
  
  /**
   * @param key
   * @return dataProvider for key matched
   */
  public V getDataProvider(K key)
  {
    return getDataProvider(key, null);  
  }
  
  /**   
   * @param key
   * @param view
   * @return dataProvider for key and view matched
   */
  public V getDataProvider(K key, K view)
  {
    V value = super.get(key);
    if (value != null && view != null)
    {
      if (_viewMap.get(key).equals(view))
      {
        value.refresh();        
      }
      else
      {        
        _viewMap.put(key, view);
      }
    }
    return value;
  }
  
  /**
   * Use this method if users just want to maintain key and value
   * @param key
   * @param value
   */
  public void setDataProvider(K key, V value)
  {
    setDataProvider(key, null, value);
  }
  
  /**
   * User this method in dcHandler to make sure provider/view are the same
   * @param key
   * @param view
   * @param value
   */
  public void setDataProvider(K key, K view, V value)
  {    
    put(key, value);
    if (view != null)
    {
      _viewMap.put(key, view);
    }
  }
  
  /**
   * Removes all of the mappings from this provider map.   
   */
  public void clear() 
  {
    _viewMap.clear();
    super.clear();
  }
}
