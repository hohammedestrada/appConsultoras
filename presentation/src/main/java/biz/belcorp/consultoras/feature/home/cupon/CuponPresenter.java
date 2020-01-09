package biz.belcorp.consultoras.feature.home.cupon;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Login;
import biz.belcorp.consultoras.domain.interactor.AccountUseCase;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;

@PerActivity
public class CuponPresenter implements Presenter<CuponView> {

    private CuponView cuponView;

    private final AccountUseCase accountUseCase;
    private final UserUseCase userUseCase;

    @Inject
    CuponPresenter(AccountUseCase accountUseCase,
                   UserUseCase userUseCase) {
        this.accountUseCase = accountUseCase;
        this.userUseCase = userUseCase;
    }

    @Override
    public void attachView(@NonNull CuponView view) {
        cuponView = view;
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
        this.cuponView = null;
    }

    /**********************************************************/

    void data() {
        this.userUseCase.getLogin(new GetData());
    }

    /**********************************************************/

    private final class GetData extends BaseObserver<Login> {
        @Override
        public void onNext(Login login) {
            if (login != null) {
                cuponView.setData(login);
                userUseCase.updateCupon(login.getCampaing(), new BaseObserver<>());
            }
        }
    }
}
