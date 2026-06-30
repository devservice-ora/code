package model;

import oracle.binding.DataControl;
import oracle.adf.model.adapter.bean.BeanDefinition;

import oracle.adfinternal.model.adapter.generic.DataControlStructure;

public class CustomDefinition extends BeanDefinition {
    public CustomDefinition(DataControlStructure dataControlStructure) {
        super(dataControlStructure);
    }

    public CustomDefinition() {
        super();
    }
    
    public DataControl createDataControl() {
        return new CustomBeanDataControl(this);
    }
}
