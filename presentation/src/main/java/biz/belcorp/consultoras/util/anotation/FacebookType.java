package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    FacebookType.ID,
    FacebookType.NOMBRE,
    FacebookType.CORREO,
    FacebookType.IMAGEN,
    FacebookType.TELEFONO
})
public @interface FacebookType {
    String ID = "facebookId";
    String NOMBRE = "facebookNombre";
    String CORREO = "facebookCorreo";
    String IMAGEN = "facebookImagen";
    String TELEFONO = "facebookTelefono";
}
