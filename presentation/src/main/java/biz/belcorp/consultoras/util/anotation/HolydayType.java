package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({
    HolydayType.BIRTHDAY,
    HolydayType.CONSULTANT_DAY,
    HolydayType.ANNIVERSARY,
    HolydayType.CHRISTMAS,
    HolydayType.NEW_YEAR,
    HolydayType.SIXTH,
    HolydayType.POSTULANT,
    HolydayType.BELCORP_FIFTY,
    HolydayType.CONSULTANT_APPROVED,
    HolydayType.BELCORP_ORDER_RESERVAR,
    HolydayType.BELCORP_ORDER_GUARDAR,
    HolydayType.NEW_CONSULTANT,
    HolydayType.NONE,
    HolydayType.CAMINO_BRILLANTE
})
public @interface HolydayType {
    int BIRTHDAY = 1;
    int CONSULTANT_DAY = 2;
    int ANNIVERSARY = 3;
    int CHRISTMAS = 4;
    int NEW_YEAR = 5;
    int SIXTH = 6;
    int POSTULANT = 7;
    int BELCORP_FIFTY = 8;
    int CONSULTANT_APPROVED = 9;
    int BELCORP_ORDER_RESERVAR = 10;
    int BELCORP_ORDER_GUARDAR = 11;
    int NEW_CONSULTANT = 12;
    int NONE = 0;
    int CAMINO_BRILLANTE = 13;
}
