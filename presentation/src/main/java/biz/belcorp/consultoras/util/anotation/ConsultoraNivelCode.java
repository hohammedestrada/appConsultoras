package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    ConsultoraNivelCode.NIVEL_1,
    ConsultoraNivelCode.NIVEL_2,
    ConsultoraNivelCode.NIVEL_3,
    ConsultoraNivelCode.NIVEL_4,
    ConsultoraNivelCode.NIVEL_5,
    ConsultoraNivelCode.NIVEL_6
})
public @interface ConsultoraNivelCode {
    String NIVEL_1 = "1";
    String NIVEL_2 = "2";
    String NIVEL_3 = "3";
    String NIVEL_4 = "4";
    String NIVEL_5 = "5";
    String NIVEL_6 = "6";
}
