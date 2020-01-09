package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    ContactType.DEFAULT,
    ContactType.MOBILE,
    ContactType.PHONE,
    ContactType.EMAIL,
    ContactType.ADDRESS,
    ContactType.REFERENCE
})
public @interface ContactType {
    int DEFAULT = 0;     // Default
    int MOBILE = 1;     // Principal
    int PHONE = 2;      // Casa
    int EMAIL = 3;      // Correo
    int ADDRESS = 4;    //  Direccion
    int REFERENCE = 5;    //  Referencia
}
