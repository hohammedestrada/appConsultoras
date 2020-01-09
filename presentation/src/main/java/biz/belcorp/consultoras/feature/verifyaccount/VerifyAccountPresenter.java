package biz.belcorp.consultoras.feature.verifyaccount;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.interactor.AccountUseCase;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;

/**
 * @author andres.escobar on 4/08/2017.
 */
@PerActivity
public class VerifyAccountPresenter implements Presenter<VerifyAccountView> {

    private VerifyAccountView verifyAccountView;
    private AccountUseCase accountUseCase;

    private final UserUseCase userUseCase;
    private final LoginModelDataMapper loginModelDataMapper;

    @Inject
    public VerifyAccountPresenter(AccountUseCase accountUseCase,
                          UserUseCase userUseCase,
                          LoginModelDataMapper loginModelDataMapper) {
        this.accountUseCase = accountUseCase;
        this.userUseCase = userUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
    }

    @Override
    public void attachView(@NonNull VerifyAccountView view) {
        verifyAccountView = view;
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
        this.verifyAccountView = null;
    }


    void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            verifyAccountView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }
}
