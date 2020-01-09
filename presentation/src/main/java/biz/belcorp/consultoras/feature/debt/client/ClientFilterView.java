package biz.belcorp.consultoras.feature.debt.client;

import java.util.List;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.view.LoadingView;

/**
 *
 */
interface ClientFilterView extends View, LoadingView {

    void showClients(List<ClienteModel> clienteModels);

    void trackBackPressed(LoginModel transform);
}
