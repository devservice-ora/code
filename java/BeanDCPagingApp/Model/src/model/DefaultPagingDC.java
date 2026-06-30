package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DefaultPagingDC {
    public DefaultPagingDC() {
        super();
        initList();
    }
    private List<SampleData> list;
    private int listSize = 50000;

    public void setList(List<SampleData> list) {
        this.list = list;
    }

    public List<SampleData> getList() {
        System.out.println("In getList");
        return getList(0, listSize);
    }

    public List<SampleData> getList(int index, int size) {
        System.out.println("In getList(index,size) :" + index + "," + size);
        List<SampleData> tempList = new ArrayList<SampleData>();
        for (int i = index; i < listSize && i < index + size; i++) {
            tempList.add(list.get(i));
        }
        return tempList;
    }

    private void initList() {
        System.out.println("In initList");
        list = new ArrayList<SampleData>();
        for (int i = 0; i < listSize; i++) {
            SampleData d = new SampleData();
            d.setCreatedBy("some user");
            d.setCreatedOn(new Date());
            d.setId(i);
            d.setDescription("some description " + new Random().nextInt());
            list.add(d);
        }
    }

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public int getListSize() {
        return listSize;
    }
}
