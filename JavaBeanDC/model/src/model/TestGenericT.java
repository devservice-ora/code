package model;

import java.util.List;

public class TestGenericT
{
  public TestGenericT()
  {
    super();
  }

  public List<?> getEntity()
  {
    return null;
  }

  public <E> E findT(E  t)
  {
    Object o = (Object) t;
    return null;
  }

}
