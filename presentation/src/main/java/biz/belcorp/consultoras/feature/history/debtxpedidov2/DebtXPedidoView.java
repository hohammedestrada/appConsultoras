package biz.belcorp.consultoras.feature.history.debtxpedidov2;


import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClientMovementModel;
import biz.belcorp.consultoras.common.model.user.UserModel;
import biz.belcorp.consultoras.common.view.LoadingView;

/**
 *
 */
interface DebtXPedidoView extends View, LoadingView {

    void showMovement(ClientMovementModel movementModel);

    void onEditNote(ClientMovementModel movementModel);

    void onError(Throwable throwable);

    void initScreenTrack(LoginModel transform);

    void trackBackPressed(LoginModel transform);

    void setData(UserModel userModel);

    void onProductosUpdated();

}
