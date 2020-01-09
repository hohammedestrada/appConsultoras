package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    BeneficioType.BENEFICIO_1,
    BeneficioType.BENEFICIO_2,
    BeneficioType.BENEFICIO_3,
    BeneficioType.BENEFICIO_4,
    BeneficioType.BENEFICIO_5,
    BeneficioType.BENEFICIO_6,
    BeneficioType.BENEFICIO_7,
    BeneficioType.BENEFICIO_8,
    BeneficioType.BENEFICIO_9,
    BeneficioType.BENEFICIO_10,
    BeneficioType.BENEFICIO_11,
    BeneficioType.BENEFICIO_12,
    BeneficioType.BENEFICIO_13,
    BeneficioType.BENEFICIO_14,
    BeneficioType.URL_ISSUU
})
public @interface BeneficioType {
    String BENEFICIO_1 = "01";
    String BENEFICIO_2 = "02";
    String BENEFICIO_3 = "03";
    String BENEFICIO_4 = "04";
    String BENEFICIO_5 = "05";
    String BENEFICIO_6 = "06";
    String BENEFICIO_7 = "07";
    String BENEFICIO_8 = "08";
    String BENEFICIO_9 = "09";
    String BENEFICIO_10 = "10";
    String BENEFICIO_11 = "11";
    String BENEFICIO_12 = "12";
    String BENEFICIO_13 = "13";
    String BENEFICIO_14 = "14";
    String URL_ISSUU = "https://issuu.com/somosbelcorp/docs/";
}
