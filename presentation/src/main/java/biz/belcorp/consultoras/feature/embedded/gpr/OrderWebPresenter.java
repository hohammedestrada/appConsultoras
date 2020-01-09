package biz.belcorp.consultoras.feature.embedded.gpr;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.CaminoBrillanteUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.PageUrlType;
import biz.belcorp.consultoras.util.anotation.UserType;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.security.JwtEncryption;

/**
 *
 */
class OrderWebPresenter implements Presenter<OrderWebView> {

    private OrderWebView orderWebView;

    private final UserUseCase userUseCase;
    private final LoginModelDataMapper loginModelDataMapper;

    private final CaminoBrillanteUseCase caminoBrillanteUseCase;

    @Inject
    OrderWebPresenter(UserUseCase userUseCase,
                      CaminoBrillanteUseCase caminoBrillanteUseCase,
                      LoginModelDataMapper loginModelDataMapper) {
        this.userUseCase = userUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
        this.caminoBrillanteUseCase = caminoBrillanteUseCase;
    }

    @Override
    public void attachView(@NonNull OrderWebView view) {
        orderWebView = view;
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
        this.orderWebView = null;
    }

    /**********************************************************/

    public void load(String deviceId) {
        orderWebView.showLoading();
        this.caminoBrillanteUseCase.getNivelConsultoraAsObserver(new GetNivelConsultoraObserver(deviceId));
    }

    void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void trackBack() {
        userUseCase.get(new UserBackPressedObserver());
    }

    /**********************************************************/

    private final class GetUser extends BaseObserver<User> {

        String deviceId;
        Integer idNivel;

        public GetUser(Integer idNivel, String deviceId) {
            this.idNivel = idNivel;
            this.deviceId = deviceId;
        }

        @Override
        public void onNext(User user) {

            String url = "";

            if (null != user) {
                String token = "";
                String countryISO = user.getCountryISO();
                String consultantId = user.getUserCode();

                if (!"".equals(countryISO) && !"".equals(consultantId)) {
                    if (null == deviceId || deviceId.isEmpty())
                        deviceId = consultantId;

                    JSONObject jsonPayLoad = new JSONObject();

                    try {
                        jsonPayLoad.put("Pais", countryISO);
                        jsonPayLoad.put("Pagina", PageUrlType.ORDER);
                        jsonPayLoad.put("CodigoUsuario", consultantId);
                        jsonPayLoad.put("EsAppMobile", "True");
                        jsonPayLoad.put("ClienteId", "0");
                        jsonPayLoad.put("Identifier", deviceId);
                        jsonPayLoad.put("NivelCaminoBrillante", idNivel);

                        String payload = jsonPayLoad.toString();
                        token = JwtEncryption.newInstance().encrypt(GlobalConstant.SECRET_PASE_PEDIDO, payload);

                    } catch (Exception e) {
                        BelcorpLogger.w("GetUser.onNext", e);
                    }
                }

                if (!"".equals(token))
                    url = GlobalConstant.URL_PASE_PEDIDO + token;
            }

            if (null == orderWebView) return;

            if (user.getUserType() == UserType.POSTULANTE) {
                orderWebView.showPostulant();
            }

            if(user.isMostrarBuscador()){
                orderWebView.showSearchOption();
            }

            orderWebView.showUrl(url);
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == orderWebView) return;
            orderWebView.hideLoading();
            orderWebView.showError();
        }
    }

    private final class UpdateObserver extends BaseObserver<Boolean> {
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            if (null == orderWebView) return;
            orderWebView.setCampaign(user.getCampaing());
            orderWebView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (null == orderWebView) return;
            orderWebView.trackBack(loginModelDataMapper.transform(user));
        }
    }

    private final class GetNivelConsultoraObserver extends BaseObserver<Integer> {

        String deviceId;

        public GetNivelConsultoraObserver(String deviceId) {
            this.deviceId = deviceId;
        }

        @Override
        public void onNext(Integer nivel) {
            userUseCase.get(new GetUser(nivel, deviceId));
            userUseCase.updateScheduler(new UpdateObserver());
        }
    }

}
