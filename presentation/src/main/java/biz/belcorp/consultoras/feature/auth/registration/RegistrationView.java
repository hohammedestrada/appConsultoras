package biz.belcorp.consultoras.feature.auth.registration;

import java.util.List;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.country.CountryModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.model.kinesis.KinesisModel;
import biz.belcorp.consultoras.common.view.LoadingView;
import biz.belcorp.consultoras.domain.entity.Login;
import biz.belcorp.consultoras.domain.entity.Verificacion;

public interface RegistrationView extends View, LoadingView {

    void renderData(String countrySIM, List<CountryModel> countries);

    void success(Login login, LoginModel model, Verificacion verificacion);

    void failed(ErrorModel error);

    void setLogAccess(KinesisModel kinesisModel, Login login);

}
