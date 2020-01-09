package biz.belcorp.consultoras.feature.embedded.changes;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.interactor.AccountUseCase;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.security.JwtEncryption;

@PerActivity
public class ChangesPresenter implements Presenter<ChangesView> {

    private ChangesView changesView;

    private final AccountUseCase accountUseCase;
    private final UserUseCase userUseCase;
    private final LoginModelDataMapper loginModelDataMapper;

    @Inject
    ChangesPresenter(AccountUseCase accountUseCase,
                            UserUseCase userUseCase,
                            LoginModelDataMapper loginModelDataMapper) {
        this.accountUseCase = accountUseCase;
        this.userUseCase = userUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
    }

    @Override
    public void attachView(@NonNull ChangesView view) {
        changesView = view;
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
        this.accountUseCase.dispose();
        this.userUseCase.dispose();
        this.changesView = null;
    }

    /**********************************************************/

    public void load(String deviceId, String page) {
        changesView.showLoading();
        this.userUseCase.get(new GetUser(deviceId, page));
        this.userUseCase.updateScheduler(new UpdateObserver());
    }

    void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void trackBack() {
        this.userUseCase.get(new UserBackPressedObserver());
    }

    /**********************************************************/

    private final class GetUser extends BaseObserver<User> {

        String deviceId;
        String page;

        public GetUser(String deviceId, String page) {
            this.deviceId = deviceId;
            this.page = page;
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
                        jsonPayLoad.put("Pagina", page);
                        jsonPayLoad.put("CodigoUsuario", consultantId);
                        jsonPayLoad.put("EsAppMobile", "True");
                        jsonPayLoad.put("ClienteId", "0");
                        jsonPayLoad.put("Identifier", deviceId);

                        String payload = jsonPayLoad.toString();
                        token = JwtEncryption.newInstance().encrypt(GlobalConstant.SECRET_PASE_PEDIDO, payload);

                    } catch (Exception e) {
                        BelcorpLogger.w("GetUser.onNext", e);
                    }

                }

                if (!"".equals(token))
                    url = BuildConfig.PASE_OFERTA + token;
            }

            changesView.showUrl(url);
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            super.onError(exception);

            changesView.hideLoading();
            changesView.showError();
        }
    }

    private final class UpdateObserver extends BaseObserver<Boolean> {
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            changesView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (null == changesView) return;
            changesView.trackBack(loginModelDataMapper.transform(user));
        }
    }

}
