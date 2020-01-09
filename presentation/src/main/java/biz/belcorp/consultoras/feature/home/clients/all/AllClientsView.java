package biz.belcorp.consultoras.feature.home.clients.all;

import java.util.List;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.error.BusinessErrorModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.view.LoadingView;

interface AllClientsView extends View, LoadingView {

    void showClients(List<ClienteModel> clientModelList);

    void onError(ErrorModel exception);

    void onDataUpdated(String mensaje);

    void onBusinessError(BusinessErrorModel errorModel);

    void initScreenTrack(LoginModel loginModel);
}
