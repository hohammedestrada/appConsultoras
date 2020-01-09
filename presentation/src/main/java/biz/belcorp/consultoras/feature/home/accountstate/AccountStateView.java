package biz.belcorp.consultoras.feature.home.accountstate;

import java.util.List;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.accountState.AccountStateModel;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.view.LoadingView;
import biz.belcorp.consultoras.domain.entity.LoginDetail;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.entity.UserConfigData;

interface AccountStateView extends View, LoadingView {

    /**
     * Muestra el historial de deudas y pagos.
     */
    void showResumen(LoginDetail loginDetail, boolean isPagoOnline);

    /**
     * Muestra el historial de deudas y pagos.
     */
    void showMovements(List<AccountStateModel> accountStates);

    /**
     * Setea el símbolo de la moneda de la consultora.
     */
    void setData(String iso, String moneySymbol, String name);

    /**
     * Setea si se muestra o no decimales.
     */
    void setShowDecimals(int show);

    void onError(Throwable throwable);

    void initScreenTrack(LoginModel transform);

    void trackBackPressed(LoginModel transform);

    void sendToPay(User tansform);

    void gotToMethodPay(UserConfigData item);
}
