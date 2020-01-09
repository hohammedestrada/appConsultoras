package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    MetaType.MM,
    MetaType.ME,
    MetaType.GM
})
public @interface MetaType {
    String MM = "MM";           // Monto Mínimo
    String ME = "ME";           // Monto Escala
    String GM = "GM";           // Gana Más
}
