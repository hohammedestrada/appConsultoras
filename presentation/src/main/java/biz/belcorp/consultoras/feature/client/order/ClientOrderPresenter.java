package biz.belcorp.consultoras.feature.client.order;


import android.support.annotation.NonNull;

import org.json.JSONObject;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.CaminoBrillanteUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.PageUrlType;
import biz.belcorp.consultoras.util.anotation.UserType;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.security.JwtEncryption;

@PerActivity
public class ClientOrderPresenter implements Presenter<ClientOrderView> {

    private ClientOrderView clientOrderView;

    private final UserUseCase userUseCase;
    private final LoginModelDataMapper loginModelDataMapper;
    private final CaminoBrillanteUseCase caminoBrillanteUseCase;

    @Inject
    public ClientOrderPresenter(UserUseCase userUseCase, LoginModelDataMapper loginModelDataMapper, CaminoBrillanteUseCase caminoBrillanteUseCase) {

        this.userUseCase = userUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
        this.caminoBrillanteUseCase = caminoBrillanteUseCase;
    }

    @Override
    public void attachView(@NonNull ClientOrderView view) {
        clientOrderView = view;
    }

    @Override
    public void resume() {
        // EMPTY
    }

    @Override
    public void pause() {
        // EMPTY
    }

    @Override
    public void destroy() {
        this.userUseCase.dispose();
        this.caminoBrillanteUseCase.dispose();
        this.clientOrderView = null;
    }

    /**********************************************************/

    public void load(int clientId, String deviceId) {
        clientOrderView.showLoading();
        this.caminoBrillanteUseCase.getNivelConsultoraAsObserver(new GetNivelConsultoraObserver(clientId, deviceId));
    }

    public void hideLoading() {
        clientOrderView.hideLoading();
    }

    public void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void trackBackPressed() {
        userUseCase.get(new UserBackPressedObserver());
    }

    /**********************************************************/

    private final class GetUser extends BaseObserver<User> {

        private int clientId;
        private String deviceId;
        private int nivelId;

        public GetUser(int clientId, String deviceId, int nivelId) {
            this.clientId = clientId;
            this.deviceId = deviceId;
            this.nivelId = nivelId;
        }

        @Override
        public void onNext(User user) {

            String url = "";

            if (null != user) {

                String token = "";
                String countryISO = user.getCountryISO();
                String consultantId = user.getUserCode();

                if (!"".equals(countryISO) && !"".equals(consultantId)) {

                    JSONObject jsonPayLoad = new JSONObject();

                    try {
                        jsonPayLoad.put("Pais", countryISO);
                        jsonPayLoad.put("Pagina", PageUrlType.ORDER);
                        jsonPayLoad.put("CodigoUsuario", consultantId);
                        jsonPayLoad.put("EsAppMobile", "True");
                        jsonPayLoad.put("ClienteId", clientId);
                        jsonPayLoad.put("Identifier", deviceId);
                        jsonPayLoad.put("NivelCaminoBrillante", nivelId);

                        String payload = jsonPayLoad.toString();
                        token = JwtEncryption.newInstance().encrypt(GlobalConstant.SECRET_PASE_PEDIDO, payload);

                    } catch (Exception e) {
                        BelcorpLogger.w("GetUser.onNext", e);
                    }

                }

                if (!"".equals(token))
                    url = GlobalConstant.URL_PASE_PEDIDO + token;

            }

            if (user.getUserType() == UserType.POSTULANTE) {
                clientOrderView.showPostulant();
            }

            clientOrderView.showUrl(url);
        }

        @Override
        public void onError(Throwable exception) {
            super.onError(exception);
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                clientOrderView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                clientOrderView.showUrl("");
            }
        }
    }

    private final class UpdateObserver extends BaseObserver<Boolean> {
        @Override
        public void onNext(Boolean aBoolean) {
            BelcorpLogger.d("Schedule save state: " + aBoolean);
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            clientOrderView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            clientOrderView.trackBackPressed(loginModelDataMapper.transform(user));
        }
    }

    private final class GetNivelConsultoraObserver extends BaseObserver<Integer> {

        String deviceId;
        int clienteId;

        public GetNivelConsultoraObserver(int clienteId, String deviceId) {
            this.deviceId = deviceId;
            this.clienteId = clienteId;
        }

        @Override
        public void onNext(Integer nivel) {
            userUseCase.get(new GetUser(clienteId, deviceId, nivel));
            userUseCase.updateScheduler(new UpdateObserver());
        }
    }

}
