package biz.belcorp.consultoras.util.anotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import android.support.annotation.StringDef;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    MedallaCaminoBrilanteType.CIRCULAR,
    MedallaCaminoBrilanteType.NIVEL,
    MedallaCaminoBrilanteType.PIE,
    MedallaCaminoBrilanteType.TIME,
    MedallaCaminoBrilanteType.PEDIDO
})
public @interface MedallaCaminoBrilanteType {
    String CIRCULAR = "CIRC";
    String NIVEL = "NIV";
    String PIE = "PIE";
    String TIME = "TIME";
    String PEDIDO = "PED";
}
