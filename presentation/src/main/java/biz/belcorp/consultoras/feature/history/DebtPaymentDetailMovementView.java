package biz.belcorp.consultoras.feature.history;

import android.content.Intent;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClientMovementModel;
import biz.belcorp.consultoras.common.view.LoadingView;

/**
 *
 */
interface DebtPaymentDetailMovementView extends View, LoadingView {

    void showMovement(ClientMovementModel movementModel);

    void onEditNote(Intent intent);

    void onEditNote(ClientMovementModel movementModel);

    void onError(Throwable throwable);

    void setData(String iso, String symbol);

    void initScreenTrack(LoginModel transform);

    void trackBackPressed(LoginModel transform);
}
