package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    ClientRegisterType.ALL,
    ClientRegisterType.DATA,
    ClientRegisterType.CONTACT,
    ClientRegisterType.ANNOTATIONS
})
public @interface ClientRegisterType {
    int ALL = 0;            // Todos
    int DATA = 1;           // Datos generales
    int CONTACT = 2;        // Tipo contacto
    int ANNOTATIONS = 3;    // Anotaciones
}
