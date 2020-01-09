package biz.belcorp.consultoras.feature.client.edit;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.AnotacionModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.view.LoadingView;

interface ClientEditView extends View, LoadingView {

    void saved(Boolean result);

    void deleted(Boolean result);

    void showClient(ClienteModel model, String iso, String moneySymbol);

    void showNotes(ClienteModel model);

    void onCountryObtained(ClienteModel clienteModel, String paisISO);

    void onError(Throwable exception);

    void showMaximumNoteAmount(int maxNoteAmount);

    void anotacionDeleted(AnotacionModel anotacionModel);

    void initScreenTrack(LoginModel transform);
}
