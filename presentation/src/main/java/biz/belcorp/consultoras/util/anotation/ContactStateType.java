package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    ContactStateType.INSERT_UPDATE,
    ContactStateType.DELETE
})
public @interface ContactStateType {
    int INSERT_UPDATE = 1;
    int DELETE = 0;
}
