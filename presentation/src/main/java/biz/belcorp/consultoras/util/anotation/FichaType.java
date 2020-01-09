package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    FichaType.PRODUCT_SIMPLE,
    FichaType.PRODUCT_COMPONENT
})
public @interface FichaType {
    String PRODUCT_SIMPLE= "Producto Simple";
    String PRODUCT_COMPONENT = "Producto Componente";
}
