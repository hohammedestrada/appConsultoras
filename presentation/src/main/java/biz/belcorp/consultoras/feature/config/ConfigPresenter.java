package biz.belcorp.consultoras.feature.config;

import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.config.ConfigAppModel;
import biz.belcorp.consultoras.common.model.config.ConfigAppModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.ConfigUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;

@PerActivity
public class ConfigPresenter implements Presenter<ConfigView> {

    private static final String TAG = "ConfigPresenter";

    private ConfigView configView;

    private final ConfigUseCase configUseCase;
    private final UserUseCase userUseCase;
    private final LoginModelDataMapper loginModelDataMapper;
    private final ConfigAppModelDataMapper configModelDataMapper;

    @Inject
    ConfigPresenter(ConfigUseCase configUseCase,
                    UserUseCase userUseCase,
                    LoginModelDataMapper loginModelDataMapper,
                    ConfigAppModelDataMapper configModelDataMapper) {
        this.configUseCase = configUseCase;
        this.userUseCase = userUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
        this.configModelDataMapper = configModelDataMapper;
    }

    @Override
    public void attachView(@NonNull ConfigView view) {
        configView = view;
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
        this.configUseCase.dispose();
        this.userUseCase.dispose();
        this.configView = null;
    }

    /**********************************************************/

    public void save(ConfigAppModel model) {
        this.configUseCase.save(this.configModelDataMapper.transform(model),
            new SavePreferenceObserver());
    }

    public void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    /**********************************************************/

    private final class SavePreferenceObserver extends BaseObserver<Boolean> {
        @Override
        public void onNext(Boolean result) {
            Log.d(TAG, "Preferencia register ");
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            configView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }
}
