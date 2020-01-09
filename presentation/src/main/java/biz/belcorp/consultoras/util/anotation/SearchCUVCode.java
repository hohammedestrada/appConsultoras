package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import biz.belcorp.consultoras.util.GlobalConstant;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    SearchCUVCode.OK,
    SearchCUVCode.ERROR_PRODUCTO_NOEXISTE,
    SearchCUVCode.ERROR_PRODUCTO_AGOTADO,
    SearchCUVCode.ERROR_PRODUCTO_LIQUIDACION,
    SearchCUVCode.ERROR_PRODUCTO_OFERTAREVISTA_ESIKA,
    SearchCUVCode.ERROR_PRODUCTO_OFERTAREVISTA_LBEL,
    SearchCUVCode.ERROR_PRODUCTO_ESTRATEGIA,
    SearchCUVCode.ERROR_PRODUCTO_SUGERIDO,
    SearchCUVCode.ERROR_CANTIDAD_EXCEDIDA,
    SearchCUVCode.ERROR_PRODUCTO_ALCANCE_PREMIO_FEST,
    SearchCUVCode.ERROR_PRODUCTO_CANTIDAD_LIMITE
})
public @interface SearchCUVCode {

    String OK = "0000";
    String ERROR_PRODUCTO_NOEXISTE = "1101";
    String ERROR_PRODUCTO_AGOTADO = "1102";
    String ERROR_PRODUCTO_LIQUIDACION = "1103";
    String ERROR_PRODUCTO_OFERTAREVISTA_ESIKA = "1106";
    String ERROR_PRODUCTO_OFERTAREVISTA_LBEL = "1107";
    String ERROR_PRODUCTO_ESTRATEGIA = "1108";
    String ERROR_PRODUCTO_SUGERIDO = "1109";
    String ERROR_PRODUCTO_DUOPERFECTO_LIMITE = "1115";
    String ERROR_PRODUCTO_FESTIVAL_ALCANZADO = GlobalConstant.CODIGO_PREMIO_FESTIVAL_ALCANZADO;
    String ERROR_PRODUCTO_CANTIDAD_LIMITE = "2111";
    String ERROR_PRODUCTO_ALCANCE_PREMIO_FEST = "4003";
    String ERROR_CANTIDAD_EXCEDIDA = GlobalConstant.CODIGO_CANTIDAD_EXCEDIDA;

}
