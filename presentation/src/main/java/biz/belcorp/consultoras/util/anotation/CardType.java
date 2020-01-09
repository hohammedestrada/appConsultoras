package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({
    CardType.INCENTIVES,
    CardType.CLIENTS,
    CardType.ORDERS,
    CardType.OFFERS,
    CardType.GANANCIAS,
    CardType.CATALOGO,
    CardType.CHAT
})
public @interface CardType {
    int INCENTIVES = 0;
    int CLIENTS = 1;
    int ORDERS = 2;
    int OFFERS = 3;
    int GANANCIAS = 4;
    int CATALOGO = 6;
    int CHAT = 7;
}
