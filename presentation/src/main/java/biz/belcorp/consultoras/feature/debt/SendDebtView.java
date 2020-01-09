package biz.belcorp.consultoras.feature.debt;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.debt.DebtResumeModel;
import biz.belcorp.consultoras.common.view.LoadingView;

/**
 *
 */
interface SendDebtView extends View, LoadingView {

    void onShareTypeClick(android.view.View view);

    void setUpShareType(boolean whatsApp, boolean sms, boolean email);

    void showMessage(DebtResumeModel debtResumeModel);

    void shareDebt(String contactNumber);

    void onError(Throwable exception);

    void onError(Integer type);

    void initScreenTrack(LoginModel transform);

    void trackBackPressed(LoginModel transform);

    void initScreenTrackEnviarMensajeDeuda(LoginModel loginModel);
}
