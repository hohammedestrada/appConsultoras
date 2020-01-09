package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import biz.belcorp.consultoras.util.GlobalConstant;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    UpdateProductCode.OK,
    UpdateProductCode.ERROR_CANTIDAD_EXCEDIDA,
    UpdateProductCode.ERROR_MONTO_MINIMO_RESERVADO,
    UpdateProductCode.ERROR_CRITERIO_PREMIO_FESTIVAL,
    UpdateProductCode.ERROR_PRODUCTO_ASOCIADO,
    UpdateProductCode.ERROR_MONTO_MINIMO_RESERVADO
})
public @interface UpdateProductCode {
    String OK = "0000";
    String ERROR_MONTO_MINIMO_RESERVADO = "2009";
    String ERROR_PRODUCTO_ASOCIADO = "4001";
    String ERROR_CANTIDAD_EXCEDIDA = GlobalConstant.CODIGO_CANTIDAD_EXCEDIDA;
    String ERROR_CRITERIO_PREMIO_FESTIVAL = "4004";

}
