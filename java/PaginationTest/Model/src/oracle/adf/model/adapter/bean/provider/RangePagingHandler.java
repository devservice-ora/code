package oracle.adf.model.adapter.bean.provider;


import oracle.binding.DataControl;

public class RangePagingHandler extends DefaultFilterableHandler {
    public RangePagingHandler(DataControl dataControl) {
        super(dataControl);
    }
    
    @Override
    public boolean supportsRangePaging() {
        return true;
    }
}
