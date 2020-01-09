package biz.belcorp.consultoras.common.model.session;

import javax.inject.Inject;

import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Session;

@PerActivity
public class SessionModelDataMapper {

    @Inject
    SessionModelDataMapper() {
    }

    public Session transform(SessionModel model) {
        Session session = null;

        if (null != model) {
            session = new Session();
            session.setCountrySIM(model.getCountrySIM());
            session.setAuthType(model.getAuthType());
            session.setUsername(model.getUsername());
            session.setEmail(model.getEmail());
            session.setPassword(model.getPassword());
            session.setCountry(model.getCountry());
            session.setTokenType(model.getTokenType());
            session.setAccessToken(model.getAccessToken());
            session.setRefreshToken(model.getRefreshToken());
            session.setExpiresIn(model.getExpiresIn());
            session.setIssued(model.getIssued());
            session.setExpires(model.getExpires());
            session.setTutorial(model.isTutorial());
            session.setLogged(model.isLogged());
            session.setStarted(model.getStarted());
            session.setUpdated(model.getUpdated());
            session.setAceptaTerminosCondiciones(session.isAceptaTerminosCondiciones());
            session.setOrdersCount(model.getOrdersCount());
        }
        return session;
    }

    public SessionModel transform(Session model) {
        SessionModel session = null;

        if (null != model) {
            session = new SessionModel();
            session.setCountrySIM(model.getCountrySIM());
            session.setAuthType(model.getAuthType());
            session.setUsername(model.getUsername());
            session.setPassword(model.getPassword());
            session.setEmail(model.getEmail());
            session.setCountry(model.getCountry());
            session.setTokenType(model.getTokenType());
            session.setAccessToken(model.getAccessToken());
            session.setRefreshToken(model.getRefreshToken());
            session.setExpiresIn(model.getExpiresIn());
            session.setIssued(model.getIssued());
            session.setExpires(model.getExpires());
            session.setTutorial(model.isTutorial());
            session.setLogged(model.isLogged());
            session.setStarted(model.getStarted());
            session.setUpdated(model.getUpdated());
            session.setAceptaTerminosCondiciones(session.isAceptaTerminosCondiciones());
            session.setOrdersCount(model.getOrdersCount());
        }
        return session;
    }

}
