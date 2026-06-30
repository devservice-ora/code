package oracle.adf.model.adapter.bean.provider;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import oracle.adf.share.logging.ADFLogger;

public abstract class BeanCollectionModel2<E>
  extends AbstractCollection<E>
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

  /** Collection of list data (TBD) */
  private List<E> _data;

  /** Current pageInfo */
  private PageInfo _currentPageInfo;

  /** Logger */
  protected ADFLogger _logger = ADFLogger.createADFLogger(BeanCollectionModel2.class);

  /** Default fetch size */
  public static final int DEFAULT_FETCH_SIZE = 25;

  public BeanCollectionModel2()
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
    if (_currentPageInfo.get(getFetchStart()) == null)
    {
      _rowCount = 0;
    }
  }

  public void notifyRefresh()
  {
    _currentPageInfo.reset();
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
      if (supportsDefaultPaging())
      {
        _rowCount -= _data.size();
      }

      clear();
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

  //------------------------------------------------------------------------
  // Pagination implementation methods
  //------------------------------------------------------------------------

  /**
   * Set page size
   */
  public void setPageSize(int pageSize)
  {
    _pageSize = (pageSize > 0 ? pageSize : DEFAULT_FETCH_SIZE);
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
    implements Iterator<E>
  {
    RangePagingIterator(int start)
    {
      super(start);
      fetchIfNeeded(_index);
    }

    public boolean hasNext()
    {
      return true;
    }

    /**
     * Return the next element in the iterator
     * @return
     */
    synchronized public E next()
    {
      if (hasNext())
      {
        final E dataElement = get(_index);
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
    int _startIndex;
    int _index;
    int _currentRow;
    E _empty = (E)new HashMap();

    DefaultPagingIterator(int start)
    {
      _index = start;
      _startIndex = start;
    }

    public boolean hasNext()
    {
      return ((size() - _index) > 0);
    }

    /**
     * Return the next element in the iterator
     * @return
     */
    synchronized public E next()
    {
      if (hasNext())
      {
        fetchIfNeeded(_index);
        _currentRow++;
        final E dataElement = get(_index);
        _index++;
        return dataElement;
      }
      return _empty;
    }

    protected E get(int index)
    {
      return (index < _data.size() ? _data.get(index) : _empty);
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
    protected void fetchIfNeeded(int fetchStart)
    {
      if (fetchStart >= _data.size() && hasNext())
      {
        _index = 0;
        nextPage();
        getCollection();
      }
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
      int numberOfPagesCache;

      public PageMap(int cache)
      {
        super(cache);
        numberOfPagesCache = cache;
      }

      public V put(K key, V value)
      {
        if (this.size() == numberOfPagesCache)
        {
          clear();
        }
        return super.put(key, value);
      }
    }
    final PageMap<Integer, Collection<E>> pageMap = new PageMap<Integer, Collection<E>>(2);

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
