package biz.belcorp.consultoras.feature.client.registration;

import java.util.List;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.AnotacionModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.view.LoadingView;

public interface ClientRegistrationView extends View, LoadingView {

    void initScreenTrackAgregarGuardar(LoginModel loginModel);

    void saved(ClienteModel clienteModel, Boolean result);

    void onCountryObtained(ClienteModel clienteModel, String paisISO);

    void onError(Throwable exception);

    void initScreenTrack(LoginModel transform);

    void showAnotations(List<AnotacionModel> list, int maxNoteAmount);

    void showMaximumNoteAmount(int maxNoteAmount);

    void recordCanceled();

    void anotacionDeleted(AnotacionModel anotacionModel);
}
