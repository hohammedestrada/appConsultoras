package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    BrandCode.LBEL,
    BrandCode.ESIKA,
    BrandCode.CYZONE,
    BrandCode.FINART
})
public @interface BrandCode {

    int LBEL = 1;
    int ESIKA  = 2;
    int CYZONE = 3;
    int FINART = 4;

}
