package biz.belcorp.consultoras.feature.auth.login.form;

import java.util.List;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.country.CountryModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.kinesis.KinesisModel;
import biz.belcorp.consultoras.common.model.kinesis.KinesisModel;
import biz.belcorp.consultoras.common.view.LoadingView;
import biz.belcorp.consultoras.domain.entity.Login;
import biz.belcorp.consultoras.domain.entity.Verificacion;

public interface LoginFormView extends View, LoadingView {

    void renderData(String countrySIM, List<CountryModel> countries);

    void successOnline(Login login, LoginModel model);

    void successOffline();

    void failedOnline(ErrorModel error);

    void failedOffline();

    void onVerificacionResponse(Verificacion verificacion, LoginModel loginModel);

    void setLogAccess(KinesisModel kinesisModel, Login login);
}
