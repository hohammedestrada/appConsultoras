package biz.belcorp.consultoras.feature.legal;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.interactor.AccountUseCase;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.feature.terms.TermsPresenter;
import biz.belcorp.library.mobile.analytics.core.Analytics;

@PerActivity
public class LegalPresenter implements Presenter<LegalView> {

    private LegalView legalView;
    private AccountUseCase accountUseCase;

    private final UserUseCase userUseCase;
    private final LoginModelDataMapper loginModelDataMapper;

    @Inject
    public LegalPresenter(AccountUseCase accountUseCase,
                          UserUseCase userUseCase,
                          LoginModelDataMapper loginModelDataMapper) {
        this.accountUseCase = accountUseCase;
        this.userUseCase = userUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
    }

    @Override
    public void attachView(@NonNull LegalView view) {
        legalView = view;
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
        this.legalView = null;
    }

    void load() {
        userUseCase.get(new UserObserver());
    }

    void acceptTerms(Boolean acceptedPrivacy) {
        legalView.showLoading();
        accountUseCase.terms(true, acceptedPrivacy, new PrivacyObserver());
    }

    void getPdfTermsUrl() {
        accountUseCase.getPdfTermsUrl(new GetPdfObserver());
    }

    void getPdfPrivacyUrl() {
        this.accountUseCase.getPdfPrivacyUrl(new GetPdfObserver());
    }

    void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void trackBackPressed() {
        userUseCase.get(new UserBackPressedObserver());
    }

    /**********************************************************/

    private final class UserObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            if (legalView == null) return;
            legalView.setData(loginModelDataMapper.transform(user));
        }
    }

    private final class PrivacyObserver extends BaseObserver<Boolean> {

        @Override
        public void onNext(Boolean result) {
            if (legalView == null) return;
            legalView.stopSDK();
            legalView.hideLoading();
            legalView.onPrivacyAccepted();
        }

        @Override
        public void onError(Throwable exception) {
            if (legalView == null) return;
            legalView.hideLoading();
            legalView.onError(exception);
        }
    }

    private final class GetPdfObserver extends BaseObserver<String> {

        @Override
        public void onNext(String result) {
            if (legalView == null) return;
            legalView.onUrlLegalGot(result);
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (legalView == null) return;
            legalView.onError(exception);
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            if (legalView == null) return;
            legalView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            if (legalView == null) return;
            legalView.trackBackPressed(loginModelDataMapper.transform(user));
        }
    }
}
