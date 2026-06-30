package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DefaultPagingDC
{
  private List<SampleData> list;
  private int listSize;

  public DefaultPagingDC()
  {
    initList(10001); // Default paging with sizeof 10001
  }

  public DefaultPagingDC(int listSize)
  {
    initList(listSize);
  }

  public List<SampleData> getList()
  {
    System.out.println(this.getClass().getName() + "getList()");
    return getList(0, 51); /** default */
  }

  public List<SampleData> getList(int index, int size)
  {
    System.out.println(this.getClass().getName() + "->getList(" + index + "," + size + ")");
    List<SampleData> tempList = new ArrayList<SampleData>();
    for (int i = index; i < listSize && i < index + size; i++)
    {
      tempList.add(list.get(i));
    }
    return tempList;
  }

  private void initList(int size)
  {
    listSize = size;
    System.out.println(this.getClass().getName() + "->initList(" + listSize + ")");
    list = new ArrayList<SampleData>();
    for (int i = 1; i <= listSize; i++)
    {
      SampleData d = new SampleData();
      int num = new Random().nextInt();
      d.setCreatedBy("some user " + num);
      d.setCreatedOn(new Date());
      d.setId(i);
      d.setDescription("some description " + num);
      list.add(d);
    }
  }

  public void setListSize(int listSize)
  {
    this.listSize = listSize;
  }

  public int getListSize()
  {
    System.out.println(this.getClass().getName() + "->getListSize(" + listSize + ")");
    return listSize;
  }

  public void changeListSize(int size)
  {
    this.listSize = size;
    initList(size);
  }
}

