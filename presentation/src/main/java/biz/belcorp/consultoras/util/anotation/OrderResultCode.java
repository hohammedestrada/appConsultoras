package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    OrderResultCode.OK,
    OrderResultCode.FACTURADO_1,
    OrderResultCode.FACTURADO_2,
    OrderResultCode.RESERVADO,
    OrderResultCode.NO_EXISTE,
    OrderResultCode.MAXIMO_EXCEDIDO,
})
public @interface OrderResultCode {

    String OK = "0000";
    String FACTURADO_1 = "0002";
    String FACTURADO_2 = "0004";
    String RESERVADO = "0003";
    String NO_EXISTE = "0005";
    String MAXIMO_EXCEDIDO = "0006";

}
