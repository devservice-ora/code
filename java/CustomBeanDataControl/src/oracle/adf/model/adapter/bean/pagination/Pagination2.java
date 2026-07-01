package oracle.adf.model.adapter.bean.pagination;

import java.util.Collection;

/**
 *  @version $Header: bc4jrt/modules/adf-model/src/oracle/adf/model/adapter/bean/pagination/Pagination.java /st_jdevadf_patchset_ias/1 2012/06/27 07:48:06 ppark Exp $
 *  @author  jpham
 *  @since   11.1.2
 */

public interface Pagination2<E>
{
  /**
   * Set the rows count
   */
  public void setRowCount(long rowCount);

  /**
   * Return the total rows count
   */
  public long getRowCount();

  /**
   * Set fetch size
   */
  public void setFetchSize(int pageSize);
  
  /**
   * Set page size - row per page
   */
  public void setPageSize(int pageSize);

  /**
   * Return the fetch size per page
   */
  public int getFetchSize();

  /**
   * Set fetch start
   */
  public void setFetchStart(int fetchStart);

  /**
   * Return fetch start
   */
  public int getFetchStart();

  /**
   * Action method bound to the "First" link.  Increment the
   * row cursor by the increment size.
   */
  public void firstPage();

  /**
   * Action method bound to the "Next" link.  Increment the
   * row cursor by the increment size.
   */
  public void nextPage();

  /**
   * Action method bound to the "Last" link.  Increment the
   * row cursor by the increment size.
   */
  public void lastPage();

  /**
   * Action method bound to the "Previous" link.  Decrements the
   * row cursor by the increment size.  Action status returned
   * is unused.
   */

  public void previousPage();

  /**
   * Method queried by the "Next" link to determine its enabled
   * state. Returns true if another page exists.
   */
  public boolean hasNextPage();

  /**
   * Method queried by the "Previous" link to determine its enabled
   * state. Returns true iff a previous page exists.
   */
  public boolean hasPreviousPage();

}
