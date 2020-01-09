package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    PagoEnLineaConfigCode.FLUJO
})
public @interface PagoEnLineaConfigCode {
    String FLUJO = "FlujoPagoLinea";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
        FlujoCode.INTERNO,
        FlujoCode.EXTERNO
    })
    @interface FlujoCode {
        String INTERNO = "1";
        String EXTERNO = "2";
    }
}
