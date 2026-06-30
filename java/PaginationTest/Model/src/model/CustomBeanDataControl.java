package model;

import oracle.adf.model.adapter.bean.BeanDataControl;
import oracle.adf.model.adapter.bean.BeanDefinition;

public class CustomBeanDataControl extends BeanDataControl {
    public CustomBeanDataControl(CustomDefinition beanDefinition) {
        super(beanDefinition);
    }
}
