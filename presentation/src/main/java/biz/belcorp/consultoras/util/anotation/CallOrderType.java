package biz.belcorp.consultoras.util.anotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
    CallOrderType.FROM_INIT,
    CallOrderType.FROM_ADD_ITEM,
    CallOrderType.FROM_ADD_OFFER,
    CallOrderType.FROM_UPDATE_ITEM,
    CallOrderType.FROM_DELETE_ITEM,
    CallOrderType.FROM_RESERVE,
    CallOrderType.FROM_UNDO_RESERVE,
    CallOrderType.FROM_FINAL_OFFER
})
public @interface CallOrderType {
    int FROM_INIT = 0;
    int FROM_ADD_ITEM = 1;
    int FROM_ADD_OFFER = 2;
    int FROM_UPDATE_ITEM = 3;
    int FROM_DELETE_ITEM = 4;
    int FROM_RESERVE = 5;
    int FROM_UNDO_RESERVE = 6;
    int FROM_FINAL_OFFER = 7;

}
