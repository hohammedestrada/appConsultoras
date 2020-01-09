package biz.belcorp.consultoras.feature.auth.login.facebook;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.country.CountryModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.model.facebook.FacebookProfileModel;
import biz.belcorp.consultoras.common.model.kinesis.KinesisModel;
import biz.belcorp.consultoras.common.view.LoadingView;
import biz.belcorp.consultoras.domain.entity.Login;
import biz.belcorp.consultoras.domain.entity.Verificacion;

public interface LoginFacebookView extends View, LoadingView {

    void renderData(CountryModel country, FacebookProfileModel facebookProfile);

    void success(Login login, LoginModel model, Verificacion verificacion);

    void failed(ErrorModel error);

    void setLogAccess(KinesisModel kinesisModel, Login login);

}
