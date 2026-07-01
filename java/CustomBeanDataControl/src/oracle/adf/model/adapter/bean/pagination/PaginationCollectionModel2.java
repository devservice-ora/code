package oracle.adf.model.adapter.bean.pagination;


import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import oracle.adf.share.logging.ADFLogger;

/**
 *  @version $Header: bc4jrt/modules/adf-model/src/oracle/adf/model/adapter/bean/pagination/PaginationCollectionModel.java /st_jdevadf_patchset_ias/2 2012/07/10 11:41:41 jpham Exp $
 *  @author  jpham
 *  @since   11.1.2
 */
public abstract class PaginationCollectionModel2<E>
  extends AbstractCollection<E>
  implements Pagination2<E>
{
  /** Max number of rows */
  private long _rowCount;

  /** Current page index */
  private int _pageIndex;

  /** Pagesize */
  private int _pageSize;

  /** Max number of pages count */
  private int _pageCount;

  /** Fetch size */
  private int _fetchSize;

  /** Fetch start */
  private int _fetchStart;
  
  /** Number of page map */
  private int _numberOfPageMap = 1;

  /** Collection of list data (TBD) */
  private List<E> _data;

  /** Current pageInfo */
  private PageInfo _currentPageInfo;

  /** Logger */
  protected ADFLogger _logger = ADFLogger.createADFLogger(PaginationCollectionModel.class);

  /** Default fetch size */
  public static final int DEFAULT_FETCH_SIZE = 25;

  public PaginationCollectionModel2()
  {
    /** Default first page */
    _pageIndex = 0;
    /** Current pageInfo */
    _currentPageInfo = new PageInfo();
    /** Paging data */
    _data = new ArrayList<E>();
  }

  /**
   * Refresh the colleciton
   */
  public void refresh()
  {
    _fetchSize = 0;
    _pageIndex = 0;
    _pageCount = 0;
    _data.clear();
  }
  
  public void notifyRefresh()
  {
    _currentPageInfo.reset();
    // Reset to calcuate new _rowCount.
    _rowCount = 0;    
  }

  public boolean canHandle()
  {
    return false;
  }

  public boolean supportsRangePaging()
  {
    return false;
  }

  public boolean supportsDefaultPaging()
  {
    return !supportsRangePaging();
  }

  //------------------------------------------------------------------------
  // AbstractCollection implementation methods
  //------------------------------------------------------------------------

  public boolean addAll(Collection<? extends E> c)
  {
    boolean modified = false;
    Iterator<? extends E> e = c.iterator();
    while (e.hasNext())
    {
      if (_data.add((E)e.next()))
      {
        modified = true;
      }
    }
    return modified;
  }

  public boolean add(E e)
  {
    boolean modified = false;
    if (_data.add(e))
    {
      _rowCount++;
      modified = true;
    }
    return modified;
  }

  public void add(int index, E e)
  {
    _data.add(index, e);
  }

  public boolean remove(Object o)
  {
    boolean modified = false;
    if (_data.remove(o))
    {
      _rowCount--;
      modified = true;
    }

    return modified;
  }

  public Object remove(int index)
  {
    return _data.remove(index);
  }

  public int size()
  {
    return (_rowCount > 0 ? Long.valueOf(_rowCount).intValue() : _data.size());
  }

  public void clear()
  {
    _data.clear();
  }
  
  /**
   * Clear data buffer and cache page
   */
  protected void clearAll()
  {
    _data.clear();
    _currentPageInfo.reset();
    // Bug 18048122
    calculateEstimatedRowCount();
  }
  
  /**
   * Calculate estimated rowCount
   */
  private void calculateEstimatedRowCount()
  {    
    // Setting _rowCount = 0 will trigger the pagination collection to calculate new _rowCount.
    _rowCount = 0;
    setRowCount(getEstimatedRowCount());    
  }

  /**
   * Return an iterator of this collection
   */
  public Iterator<E> iterator()
  {
    return (supportsRangePaging() ? new RangePagingIterator(getFetchStart()) : new DefaultPagingIterator(0));
  }

  //------------------------------------------------------------------------
  // PaginationCollectionModel implementation methods
  //------------------------------------------------------------------------

  /**
   * Return the total # of rows count for a query.
   */
  public long getEstimatedRowCount()
  {
    return getRowCount();
  }

  /**
   * Add data to managed collection
   */
  public void addCollection(Collection<E> data)
  {
    if (data != null)
    {
      // For range paging, there is no need to maintain previous range.
      if (supportsRangePaging())
      {
        clear();
      }

      addAll(data);

      _fetchStart = getFetchStart();
      _currentPageInfo.update(_fetchStart, data);
    }
  }

  /**
   * Returns a list of object that fit the given paging range.
   */
  public Collection<E> getCollection()
  {
    return this;
  }

  /**
   * Return the page collection at startIndex
   */
  public Collection<E> getCollection(int start)
  {
    return _currentPageInfo.get(start);
  }
  
  /**
   * Returns the data object for this collection represents.
   */
  public Object getDataProvider()
  {
    return null;
  }

  //------------------------------------------------------------------------
  // Pagination implementation methods
  //------------------------------------------------------------------------  
  /**
   * Set page size
   */
  public void setPageSize(int pageSize)
  {
    if (_pageSize != pageSize)
    {
      final int DEFAULT_PAGE_SIZE = 100;
      _pageSize = (pageSize > 0 ? pageSize : DEFAULT_FETCH_SIZE);      
      // Caculcate # of page needed to fill up the whole page
      _numberOfPageMap = 1;
      if (_pageSize < DEFAULT_PAGE_SIZE)
      {
        _numberOfPageMap = DEFAULT_PAGE_SIZE / _pageSize;
        if (((_numberOfPageMap * _pageSize) < DEFAULT_PAGE_SIZE) && (_pageSize % DEFAULT_PAGE_SIZE) > 0)
        {
          _numberOfPageMap++;
        }
      }
    }
  }

  /**
   * Set fetch size
   */
  public void setFetchSize(int fetchSize)
  {
    _fetchSize = fetchSize;
  }

  public int getFetchSize()
  {
    return _fetchSize;
  }

  /**
   * Set fetch start
   * @param fetchStart <code>int</code> rangeStart.
   */
  public void setFetchStart(int fetchStart)
  {
    _fetchStart = fetchStart;
  }

  /**
   * @return fetch start position
   */
  public int getFetchStart()
  {
    return _fetchStart;
  }

  /**
   * @return current page index
   */
  public int getPageIndex()
  {
    return _pageIndex;
  }

  /**
   * @return the number of pages in the collection
   */
  public int getPageCount()
  {
    if (_rowCount > 0)
    {
      _pageCount = (int)(_rowCount / _pageSize);
      if ((_rowCount % _pageSize) > 0)
      {
        _pageCount++;
      }
    }
    return _pageCount;
  }

  /**
   * Set the total number row count for the collection
   * @param rowCount
   */
  public void setRowCount(long rowCount)
  {
    _rowCount = rowCount;
  }

  /**
   * @return The total numer of row count
   */
  public long getRowCount()
  {
    return _rowCount;
  }

  public int getFetchedRowCount()
  {
    return _currentPageInfo.lastPosition;
  }

  /**
   * Action method bound to the "|< First" link.  The _fromRange row
   */
  public void firstPage()
  {
    _pageIndex = 0;
  }

  /**
   * Action method bound to the "Last >|" link.  Increment to the
   * row.
   */
  public void lastPage()
  {
    _pageIndex = ((int)_rowCount / _pageSize);
  }

  /**
   * Action method bound to the "Next >" link.  Increment the
   * row cursor by the increment size.
   */
  public void nextPage()
  {
    final int pageCount = getPageCount();
    if ((_pageIndex + 1) == pageCount || _data.size() == 0)
    {
      _pageIndex = 0;
    }
    else
    {
      _pageIndex++;
    }
  }

  /**
   * Action method bound to the "< Previous" link.  Decrements the
   * row cursor by the increment size.
   */
  public void previousPage()
  {
    if (_pageIndex == 0)
    {
      _pageIndex = 0;
    }
    else
    {
      _pageIndex--;
    }
  }

  /**
   * Method queried by the "Next" link to determine its enabled
   * state. Returns true if another _page exists.
   */
  public boolean hasNextPage()
  {
    return (_pageIndex < getPageCount());
  }

  /**
   * Method queried by the "Previous" link to determine its enabled
   * state. Returns true if a previous _page exists.
   */
  public boolean hasPreviousPage()
  {
    return (_pageIndex != 0 && _pageIndex > 1);
  }

  //------------------------------------------------------------------------
  // Range Paging iterator
  //------------------------------------------------------------------------

  class RangePagingIterator
    extends DefaultPagingIterator   
  {
    RangePagingIterator(int start)
    {
      super(start);
      fetchIfNeeded(start);
    }

    public boolean hasNext()
    {
      return ((size() > 0) && ((_start - 1) + _index) < size());
    }

    /**
     * @return the next element in the iterator
     */
    synchronized public E next()
    {
      // Last row
      if (((_start + 1) == size()))
      {
        E dataElement = ((_data.size() > 0) ? _data.get(0) : _empty);
        return dataElement;
      }
      else if (_index < _data.size())
      {
        E dataElement = _data.get(_index);
        _index++;
        return dataElement;
      }
      return _empty;
    }
  }

  //------------------------------------------------------------------------
  // Default Paging iterator
  //------------------------------------------------------------------------

  class DefaultPagingIterator
    implements Iterator<E>
  {
    int _index;
    int _start;
    int _currentRow;
    E _empty = (E)new HashMap();

    DefaultPagingIterator(int start)
    {
      _index = 0;
      _start = start;
    }

    public boolean hasNext()
    {
      return ((size() - _index) > 0);
    }

    /**
     * @return the next element in the iterator
     */
    synchronized public E next()
    {
      fetchIfNeeded(_index);
      _currentRow++;
      E dataElement = _empty;
      if (_index < _data.size())
      {
        dataElement = _data.get(_index);
        _index++;
      }
      return dataElement;
    }
    
    /**
     * Not supported by this Iterator
     * @exception UnsupportedOperationException if the <code>remove</code>
     * operation is not supported by this Iterator.
     */
    public void remove()
    {
      //throw new JboException(new UnsupportedOperationException("remove() is not supported by this Iterator."));
    }

    /**
     * Fetch the next page ifNeeded
     * @param fetchStart <code>int</code> fetch start.
     */
    protected boolean fetchIfNeeded(int fetchStart)
    {
      if (fetchStart >= _data.size() && hasNext())
      {
        nextPage();
        return (!getCollection().isEmpty());
      }
      return false;
    }
  }

  //------------------------------------------------------------------------
  // PageInfo implementation
  //------------------------------------------------------------------------

  class PageInfo
  {
    int prevPosition = -1;
    int startPosition = -1;
    int lastPosition = -1;

    class PageMap<K, V>
      extends HashMap<K, V>
    {
      public PageMap(int numberOfPage)
      {
        super(numberOfPage);
      }

      public V put(K key, V value)
      {
        if (this.size() == _numberOfPageMap)
        {
          clear();
        }
        return super.put(key, value);
      }
    }
    final PageMap<Integer, Collection<E>> pageMap = new PageMap<Integer, Collection<E>>(_numberOfPageMap);

    boolean isFirstPage()
    {
      return (prevPosition == -1);
    }

    Collection<E> get(int start)
    {
      return pageMap.get(start);
    }

    void update(int start, Collection<E> data)
    {
      prevPosition = startPosition;
      startPosition = start;
      lastPosition = start + data.size();
      pageMap.put(start, data);
    }

    void reset()
    {
      prevPosition = 0;
      startPosition = 0;
      lastPosition = 0;
      pageMap.clear();
    }
  }
}
