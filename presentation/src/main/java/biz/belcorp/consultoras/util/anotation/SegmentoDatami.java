package biz.belcorp.consultoras.util.anotation;

    import android.support.annotation.StringDef;

    import java.lang.annotation.Retention;
    import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    SegmentoDatami.SEGMENTO_A,
    SegmentoDatami.SEGMENTO_B,
    SegmentoDatami.SEGMENTO_C,
    SegmentoDatami.SEGMENTO_D,
    SegmentoDatami.SEGMENTO_E,
    SegmentoDatami.SEGMENTO_F,
})
@interface SegmentoDatami {
    String SEGMENTO_A = "Segmento_A";
    String SEGMENTO_B = "Segmento_B";
    String SEGMENTO_C = "Segmento_C";
    String SEGMENTO_D = "Segmento_D";
    String SEGMENTO_E = "Segmento_E";
    String SEGMENTO_F = "Segmento_F";

}
