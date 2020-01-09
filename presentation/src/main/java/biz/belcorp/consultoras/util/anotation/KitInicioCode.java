package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    KitInicioCode.OK,
    KitInicioCode.ERROR_ADD_KIT
})
public @interface KitInicioCode {

    String OK = "0000";
    String ERROR_ADD_KIT = "1101";

}
