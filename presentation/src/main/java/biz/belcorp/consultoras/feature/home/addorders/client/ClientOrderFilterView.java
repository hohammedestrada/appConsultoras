package biz.belcorp.consultoras.feature.home.addorders.client;

import java.util.List;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.view.LoadingView;
import biz.belcorp.consultoras.domain.entity.User;

/**
 *
 */
interface ClientOrderFilterView extends View, LoadingView {

    void showClients(User user, List<ClienteModel> list);

    void trackBackPressed(LoginModel transform);
}
