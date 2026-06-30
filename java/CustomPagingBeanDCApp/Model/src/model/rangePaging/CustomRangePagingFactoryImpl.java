package model.rangePaging;


import oracle.adf.model.DataControl;
import oracle.adf.model.adapter.AbstractDefinition;
import oracle.adf.model.adapter.DataControlFactoryImpl;

public class CustomRangePagingFactoryImpl extends DataControlFactoryImpl {
    public CustomRangePagingFactoryImpl() {
        super();
    }

    @Override
    protected DataControl createDataControl(String name, String def,
                                            Object dcInstance,
                                            AbstractDefinition dcDef) {
        return new CustomRangePagingDC(name, def, dcInstance, dcDef);
    }
}
