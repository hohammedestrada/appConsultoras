package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    StatusType.DELETE,
    StatusType.CREATE,
    StatusType.UPDATE
})
public @interface StatusType {
    int DELETE = -1;    // Borrar
    int CREATE = 0;     // Nuevo
    int UPDATE = 1;     // Casa
}
