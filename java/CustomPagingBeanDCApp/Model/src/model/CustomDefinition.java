package model;

import oracle.binding.DataControl;
import oracle.adf.model.adapter.bean.BeanDefinition;

import oracle.adf.model.adapter.generic.DataControlStructure;

public class CustomDefinition extends BeanDefinition {
    public CustomDefinition(DataControlStructure dcStructure) {
        super(dcStructure);
    }

    public CustomDefinition() {
        super();
    }
    
    public DataControl createDataControl() {
        return new CustomBeanDataControl(this);
    }
}