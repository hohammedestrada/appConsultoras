package biz.belcorp.consultoras.util;

import biz.belcorp.library.annotation.AppId;
import biz.belcorp.library.annotation.BrandFocus;
import biz.belcorp.library.annotation.BrandFocusName;
import biz.belcorp.library.annotation.Country;
import biz.belcorp.library.annotation.HybrisOriginId;

public class Constant {



    private Constant(){
        throw new UnsupportedOperationException();
    }

    public static final @BrandFocus int BRAND_FOCUS = BrandFocus.ESIKA;
    public static final @BrandFocusName String BRAND_FOCUS_NAME = BrandFocusName.ESIKA;
    public static final @Country String BRAND_COUNTRY_DEFAULT = Country.PE;
    public static final @AppId int BRAND_APP_ID = AppId.ESIKA;
    public static final @HybrisOriginId String HYBRIS_ORIGIN_ID = HybrisOriginId.GENERAL;
}
