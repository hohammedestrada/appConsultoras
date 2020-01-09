package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    SearchOriginType.ORIGEN_DESPLEGABLE,
    SearchOriginType.ORIGEN_DESPLEGABLE_FICHA,
    SearchOriginType.ORIGEN_DESPLEGABLE_FICHA_CARRUSEL,
    SearchOriginType.ORIGEN_LANDING,
    SearchOriginType.ORIGEN_LANDING_FICHA,
    SearchOriginType.ORIGEN_LANDING_FICHA_CARRUSEL
})
public @interface SearchOriginType {
    String ORIGEN_DESPLEGABLE = "OrigenPedidoWebDesplegable"; // Seccion desplegable del buscador
    String ORIGEN_DESPLEGABLE_FICHA = "OrigenPedidoWebDesplegableFicha";   // Seccion ficha desde el desplegable del buscador
    String ORIGEN_DESPLEGABLE_FICHA_CARRUSEL = "OrigenPedidoWebDesplegableFichaCarrusel";   // Seccion desde el desplegable ficha del carrusel
    String ORIGEN_LANDING = "OrigenPedidoWebLanding"; // Seccion desde el landing del buscador
    String ORIGEN_LANDING_FICHA = "OrigenPedidoWebLandingFicha"; // Sección de la ficha desde el landing del buscador
    String ORIGEN_LANDING_FICHA_CARRUSEL = "OrigenPedidoWebLandingFichaCarrusel"; // Sección desde el carrusel de la ficha del landing del buscador

    String ORIGEN_BUSCADOR_DESPLEGABLE = "OrigenBuscadorDesplegable";
    String ORIGEN_BUSCADOR_LANDING = "OrigenBuscadorLanding";
    String ORIGEN_BUSCADOR_DESPLEGABLE_FICHA = "OrigenBuscadorDesplegableFicha";
    String ORIGEN_BUSCADOR_LANDING_FICHA = "OrigenBuscadorLandingFicha";
    String ORIGEN_BUSCADOR_DESPLEGABLE_FICHA_CARRUSEL = "OrigenBuscadorDesplegableFichaCarrusel";
    String ORIGEN_BUSCADOR_LANDING_FICHA_CARRUSEL = "OrigenBuscadorLandingFichaCarrusel";

    String ORIGEN_BUSCADOR_FICHA = "OrigenBuscadorFicha";
    String ORIGEN_LANDING_FESTIVAL = "OrigenFestivalLandingFicha";
    String ORIGEN_LANDING_SUBCAMPANIA = "OrigenSubCampaniaLandingCarrusel";

    String ORIGEN_LANDING_FEST_FICHA = "OrigenLandingFicha";
    String ORIGEN_LANDING_FEST_CARRUSEL_PREMIO = "OrigenLandingCarrusel";
    String ORIGEN_LANDING_FEST_CARRUSEL_CONDICION = "OrigenFestivalLandingCarrusel";

    String ORIGEN_LANDING_OFERTA_FINAL = "OrigenLanding";
    String ORIGEN_LANDING_FICHA_OFERTA_FINAL = "OrigenLandingFicha";

}
