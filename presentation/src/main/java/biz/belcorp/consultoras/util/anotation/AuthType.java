package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    AuthType.FORM,
    AuthType.FACEBOOK
})
public @interface AuthType {
    int FORM = 1;
    int FACEBOOK = 2;
}
