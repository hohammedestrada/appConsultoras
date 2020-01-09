package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    CaminoBrillanteType.ENABLED_GANANCIA,
    CaminoBrillanteType.ENABLED_CARRUSEL,
    CaminoBrillanteType.ENABLED_BARRA_MONTO,
    CaminoBrillanteType.ENABLED_ENTERATE_MAS,
    CaminoBrillanteType.APP_GRAN_BRILALNTE,
    CaminoBrillanteType.FLAG_ONBOARDING,
    CaminoBrillanteType.FLAG_DEDO_GANANCIA,
    CaminoBrillanteType.FLAG_CAMBIO_NIVEL_ANIM,
    CaminoBrillanteType.FLAG_CAMBIO_NIVEL_VALOR,
    CaminoBrillanteType.FLAG_CB_MONTO_INCENTIVO
})

public @interface CaminoBrillanteType {
    String ENABLED_GANANCIA = "app_ganancias";
    String ENABLED_CARRUSEL = "app_carrusel";
    String ENABLED_BARRA_MONTO = "app_barraMonto";
    String ENABLED_ENTERATE_MAS = "app_enterateMas";

    String APP_GRAN_BRILALNTE = "app_granBrillante"; //Puntaje Gran Brillante;

    String FLAG_ONBOARDING = "CB_CON_ONBOARDING_ANIM";
    String FLAG_DEDO_GANANCIA = "CB_CON_GANANCIA_ANIM";
    String FLAG_CAMBIO_NIVEL_ANIM = "CB_CON_CAMB_NIVEL_ANIM";
    String FLAG_CAMBIO_NIVEL_VALOR = "CB_CON_CAMB_NIVEL_VAL";
    String FLAG_CB_MONTO_INCENTIVO = "CB_MONTO_INCENTIVO";


}
