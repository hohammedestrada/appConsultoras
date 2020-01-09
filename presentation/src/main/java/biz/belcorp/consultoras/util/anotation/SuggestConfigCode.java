package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    SuggestConfigCode.ACTIVAR_RECOMENDACIONES,
    SuggestConfigCode.MAXIMO_RESULTADOS,
    SuggestConfigCode.MINIMO_RESULTADOS,
    SuggestConfigCode.CARACTERES_DESCRIPCION,
    SuggestConfigCode.CODIGO_CATALOGO

})
public @interface SuggestConfigCode {
    String ACTIVAR_RECOMENDACIONES = "ActivarRecomendaciones";              // Activar Recomendaciones
    String MAXIMO_RESULTADOS = "MaximoResultados";                          // Cantidad Maxima de resultados
    String MINIMO_RESULTADOS = "MinimoResultados";                          // Cantidad Minima de resultados
    String CARACTERES_DESCRIPCION = "CaracteresDescripcion";                // Caracteres maximo de la descripcion
    String CODIGO_CATALOGO = "CodigoCatalogo";                              // Codigo del catologo
}
