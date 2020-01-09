package biz.belcorp.consultoras.feature.history;

import java.util.List;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClientMovementModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.view.LoadingView;

interface DebtPaymentHistoryView extends View, LoadingView {

    /**
     * Muestra el historial de deudas y pagos.
     */
    void showHistory(List<ClientMovementModel> movements, boolean recordatory);

    /**
     * Setea el símbolo de la moneda de la consultora.
     */
    void setData(String iso, String moneySymbol);

    /**
     * Setea si se muestra o no decimales.
     */
    void setShowDecimals(int show);

    /**
     * Muestra el objeto de cliente.
     */
    void showRecordatorio(ClienteModel clienteModel);

    /**
     * Elimina el recordatorio.
     */
    void deleteRecordatory();

    void onError(Throwable throwable);

    void refresh();

    void initScreenTrack(LoginModel transform);

    void trackBackPressed(LoginModel transform);

    void initScreenTrackAnadirRecordatorio(LoginModel loginModel);

    void initScreenTrackAnadirDeuda(LoginModel loginModel);

    void initScreenTrackRegistrarUnPago(LoginModel loginModel);

    void initScreenTrackEnviarDetalleDeuda(LoginModel loginModel);

}
