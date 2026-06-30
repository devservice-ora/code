package model.rangePaging;

import oracle.adf.model.adapter.AbstractDefinition;
import oracle.adf.model.adapter.AdapterDCService;

import oracle.jbo.AttributeDef;
import oracle.jbo.RowSet;
import oracle.jbo.server.ViewDefImpl;

public class CustomRangePagingDC extends AdapterDCService {

    public CustomRangePagingDC(String string, String string1, Object object,
                               AbstractDefinition abstractDefinition) {
        super(string, string1, object, abstractDefinition);
    }

    @Override
    protected ViewDefImpl createViewDef(String strDefFullName, String vdName,
                                        oracle.jbo.StructureDef sd,
                                        AttributeDef[] fkAttrs, int vdKind) {
        ViewDefImpl vd =
            super.createViewDef(strDefFullName, vdName, sd, fkAttrs, vdKind);
        // Comment out this line if you do not want rangePaging.
        vd.setAccessMode(RowSet.RANGE_PAGING);        
        return vd;
    }
}
