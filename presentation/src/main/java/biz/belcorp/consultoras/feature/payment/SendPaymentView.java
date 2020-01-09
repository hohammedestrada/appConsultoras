package biz.belcorp.consultoras.feature.payment;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.view.LoadingView;

public interface SendPaymentView extends View, LoadingView {

    /**
     * Comparte el pago.
     */
    void sharePayment(String text);

    /**
     * Seteo de datos.
     */
    void setData(ClienteModel cliente);

    /**
     * Muestra el mensaje.
     */
    void showMessage(String consultantName);


    void initScreenTrack(LoginModel transform);

    void trackBackPressed(LoginModel transform);

    void initScreenTrackEnviarMensajePago(LoginModel loginModel);
}
