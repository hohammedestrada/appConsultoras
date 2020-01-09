package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    StorieType.IMAGEN,
    StorieType.VIDEO,
    StorieType.TEXTO
})
public @interface StorieType {
    String IMAGEN = "IMG";
    String VIDEO = "VIDEO";
    String TEXTO = "TXT";

}
