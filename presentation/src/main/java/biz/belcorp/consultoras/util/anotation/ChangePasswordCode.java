package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    ChangePasswordCode.OK,
    ChangePasswordCode.ERROR
})
public @interface ChangePasswordCode {

    String OK = "0000";
    String ERROR = "1101";

}
