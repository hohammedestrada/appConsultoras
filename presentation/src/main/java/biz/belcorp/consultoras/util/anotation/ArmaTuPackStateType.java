package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    ArmaTuPackStateType.INSERT_UPDATE,
    ArmaTuPackStateType.DELETE
})
public @interface ArmaTuPackStateType {
    int INSERT_UPDATE = 1;
    int DELETE = 0;
}
