package biz.belcorp.consultoras.feature.contest.order.previous;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModel;
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Concurso;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.IncentivesUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.exception.ErrorFactory;

@PerActivity
class PerPreviousOrderPresenter implements Presenter<PerPreviousOrderView> {

    private final UserUseCase userUseCase;
    private final LoginModelDataMapper loginModelDataMapper;

    private final IncentivesUseCase incentivesUseCase;
    private final ConcursoModelDataMapper concursoModelDataMapper;

    private PerPreviousOrderView perPreviousOrderView;


    @Inject
    PerPreviousOrderPresenter(UserUseCase userUseCase,
                              LoginModelDataMapper loginModelDataMapper,
                              IncentivesUseCase incentivesUseCase,
                              ConcursoModelDataMapper concursoModelDataMapper){
        this.userUseCase = userUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
        this.incentivesUseCase = incentivesUseCase;
        this.concursoModelDataMapper = concursoModelDataMapper;
    }

    @Override
    public void attachView(@NonNull PerPreviousOrderView view) {
        if (view instanceof PerPreviousOrderFragment) {
            perPreviousOrderView = view;
        }
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
        this.incentivesUseCase.dispose();
        this.perPreviousOrderView = null;
    }

    /******************************************************/

    public void get(String contestCode) {
        perPreviousOrderView.showLoading();
        userUseCase.get(new GetUser(contestCode));
    }

    public void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    /**********************************************************/

    private final class GetUser extends BaseObserver<User> {

        private String contestCode;

        GetUser(String contestCode) {
            this.contestCode = contestCode;
        }

        @Override
        public void onNext(User user) {
            incentivesUseCase.getContest(contestCode, new GetContestObserver(user));
        }
    }

    private final class GetContestObserver extends BaseObserver<Concurso> {

        private String campaingCurrent;
        private String countryMoneySymbol;
        private String countryISO;

        private GetContestObserver(User user) {
            this.campaingCurrent = user.getCampaing();
            this.countryMoneySymbol = user.getCountryMoneySymbol();
            this.countryISO = user.getCountryISO();
        }

        @Override
        public void onNext(Concurso data) {

            if (null == perPreviousOrderView) return;

            ConcursoModel model = concursoModelDataMapper.transform(data);
            perPreviousOrderView.showContest(model, campaingCurrent, countryMoneySymbol);
            perPreviousOrderView.initializeAdapter(countryISO);
            perPreviousOrderView.hideLoading();
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == perPreviousOrderView) return;
            perPreviousOrderView.initializeAdapter(countryISO);
            perPreviousOrderView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                perPreviousOrderView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                perPreviousOrderView.onError(ErrorFactory.INSTANCE.create(exception));
            }
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            perPreviousOrderView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }
}
