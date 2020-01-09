package biz.belcorp.consultoras.common.notification.home;

/**
 *
 */

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
    OrderNotificationState.NONE,
    OrderNotificationState.DEBT,
    OrderNotificationState.MODIFY
})
@interface OrderNotificationState {
    String NONE = "Ninguna";
    String DEBT = "Deuda";
    String MODIFY = "ModificaPedido";
}
