package biz.belcorp.consultoras.feature.payment;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.user.UserModel;
import biz.belcorp.consultoras.common.view.LoadingView;

public interface PaymentView extends View, LoadingView {

    /**
     * Agrega un pago.
     */
    void showResult(Boolean result);

    /**
     * Setea la campaña de la consultora.
     */
    void setData(UserModel model);

    void initScreenTrack(LoginModel transform);

    void trackBackPressed(LoginModel transform);

    void initScreenTrackRegistrarPagoExitoso(LoginModel transform);
}
