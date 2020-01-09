package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    OffersOriginType.ORIGEN_CONTENEDOR,
    OffersOriginType.ORIGEN_LANDING,
    OffersOriginType.ORIGEN_LANDING_CATEGORIA,
    OffersOriginType.ORIGEN_CONTENEDOR_FICHA,
    OffersOriginType.ORIGEN_LANDING_FICHA,
    OffersOriginType.ORIGEN_LANDING_CATEGORIA_FICHA,
    OffersOriginType.ORIGEN_CONTENEDOR_FICHA_CARRUSEL,
    OffersOriginType.ORIGEN_LANDING_FICHA_CARRUSEL,
    OffersOriginType.ORIGEN_LANDING_CATEGORIA_FICHA_CARRUSEL,
    OffersOriginType.ORIGEN_UPSELLING_CARRUSEL,
    OffersOriginType.ORIGEN_UPSELLING_FICHA,
    OffersOriginType.ORIGEN_CROSSSELLING_CARRUSEL,
    OffersOriginType.ORIGEN_CROSSSELLING_FICHA,
    OffersOriginType.ORIGEN_PEDIDO,
    OffersOriginType.ORIGEN_SUBCAMPANIA_FICHA
})
public @interface OffersOriginType {
    String ORIGEN_CONTENEDOR = "OrigenContenedor"; // Sección contenedor
    String ORIGEN_LANDING = "OrigenLanding";   // Sección Landing
    String ORIGEN_LANDING_CATEGORIA = "OrigenLandingCategoria";   // Sección Landing Categoría
    String ORIGEN_CONTENEDOR_FICHA = "OrigenContenedorFicha"; // Sección Ficha desde contenedor
    String ORIGEN_LANDING_FICHA = "OrigenLandingFicha"; // Sección Ficha desde Landing
    String ORIGEN_LANDING_CATEGORIA_FICHA = "OrigenLandingCategoriaFicha"; // Sección Ficha desde Landing categoría
    String ORIGEN_CONTENEDOR_FICHA_CARRUSEL = "OrigenContenedorFichaCarrusel"; // Sección Carrusel ficha desde contenedor ficha
    String ORIGEN_LANDING_FICHA_CARRUSEL = "OrigenLandingFichaCarrusel"; // Sección Carrusel ficha desde Landing
    String ORIGEN_LANDING_CATEGORIA_FICHA_CARRUSEL = "OrigenLandingCategoriaFichaCarrusel"; // Sección Carrusel ficha desde Landing Categoria
    String ORIGEN_UPSELLING_CARRUSEL = "OrigenUpsellingCarrusel";
    String ORIGEN_UPSELLING_FICHA = "OrigenUpsellingFicha";
    String ORIGEN_SUGERIDOS_CARRUSEL = "OrigenSugeridoCarrusel";
    String ORIGEN_SUGERIDOS_FICHA = "OrigenSugeridoFicha";
    String ORIGEN_CROSSSELLING_CARRUSEL = "OrigenCrossSellingCarrusel";
    String ORIGEN_CROSSSELLING_FICHA = "OrigenCrossSellingFicha";
    String ORIGEN_RECOMENDADO_CARRUSEL = "OrigenRecomendadoCarrusel";
    String ORIGEN_RECOMENDADO_FICHA = "OrigenRecomendadoFicha";
    String ORIGEN_PEDIDO = "OrigenPedido";
    String ORIGEN_SUBCAMPANIA_FICHA = "OrigenSubCampaniaLandingFicha";


    String ORIGEN_CONDICION_PROMOCION_FICHA_CARRUSEL = "OrigenCondicionPromocionFichaCarrusel";
    String ORIGEN_CONTENEDOR_PROMOCION_FICHA = "OrigenContenedorPromocionFicha";
}
