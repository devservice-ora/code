package oracle.adf.model.adapter.bean.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class BeanDataProvider2<K extends String, V extends BeanDataCollection2>
  extends HashMap<K, V>
{
  private Map<String, String> _viewMap;

  public BeanDataProvider2()
  {
    super();
    _viewMap = new WeakHashMap<String, String>();
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
    V provider = super.get(key);
    if (provider != null && view != null)
    {
      if (_viewMap.get(key).equals(view))
      {
        provider.refresh();
      }
      else
      {
        _viewMap.put(key, view);
      }
    }
    return provider;
  }

  /**
   * Use this method if users just want to maintain key and value
   * @param key
   * @param provider
   */
  public void setDataProvider(K key, V provider)
  {
    setDataProvider(key, null, provider);
  }

  /**
   * User this method in dcHandler to make sure provider/view are the same
   * @param key
   * @param view
   * @param provider
   */
  public void setDataProvider(K key, K view, V provider)
  {
    put(key, provider);
    if (view != null)
    {
      _viewMap.put(key, view);
    }
  }
}
