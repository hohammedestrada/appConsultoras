package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    GuiaNegocioDigitalType.SIN_GND,
    GuiaNegocioDigitalType.CON_GND
})
public @interface GuiaNegocioDigitalType {
    int SIN_GND = 0;
    int CON_GND = 1;
}
