package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    MenuType.SPLASH,
    MenuType.LOGIN,
    MenuType.HOME,
    MenuType.TASK
})
public @interface MenuType {
    int SPLASH = 0;
    int LOGIN = 1;
    int HOME = 2;
    int TASK = 3;
}
