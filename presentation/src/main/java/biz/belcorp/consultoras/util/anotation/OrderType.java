package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    OrderType.INGRESADO,
    OrderType.FACTURADO
})
public @interface OrderType {
    String INGRESADO = "INGRESADO";
    String FACTURADO = "FACTURADO";
}
