package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    MovementType.CARGO,
    MovementType.CARGO_BELCORP,
    MovementType.ABONO,
    MovementType.HISTORICO,
})
public @interface MovementType {
    String CARGO = "C";
    String CARGO_BELCORP = "CB";
    String ABONO = "A";
    String HISTORICO = "H";
}
