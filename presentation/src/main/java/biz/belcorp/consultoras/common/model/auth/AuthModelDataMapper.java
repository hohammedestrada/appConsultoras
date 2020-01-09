package biz.belcorp.consultoras.common.model.auth;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Auth;

@PerActivity
public class AuthModelDataMapper {

    @Inject
    AuthModelDataMapper() {
    }

    public Auth transform(AuthModel model) {
        Auth auth = null;

        if (null != model) {
            auth = new Auth(model.isLogged(), model.isTutorial(), model.isFacebook(), model.isHasUser());
        }
        return auth;
    }

    public AuthModel transform(Auth entity) {
        AuthModel auth = null;

        if (null != entity) {
            auth = new AuthModel();
            auth.setLogged(entity.isLogged());
            auth.setTutorial(entity.isTutorial());
            auth.setFacebook(entity.isFacebook());
            auth.setHasUser(entity.isHasUser());
        }
        return auth;
    }

}
