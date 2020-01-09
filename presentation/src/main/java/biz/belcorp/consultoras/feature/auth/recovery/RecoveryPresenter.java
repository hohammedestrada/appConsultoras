package biz.belcorp.consultoras.feature.auth.recovery;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.country.CountryModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Country;
import biz.belcorp.consultoras.domain.interactor.AccountUseCase;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.CountryUseCase;
import biz.belcorp.consultoras.domain.interactor.SessionUseCase;
import biz.belcorp.consultoras.exception.ErrorFactory;
import biz.belcorp.consultoras.util.Constant;

@PerActivity
public class RecoveryPresenter implements Presenter<RecoveryView> {

    private RecoveryView recoveryView;

    private final SessionUseCase sessionUseCase;

    private final CountryUseCase countryUseCase;
    private final CountryModelDataMapper countryModelDataMapper;

    private final AccountUseCase accountUseCase;
    private final RecoveryModelMapper recoveryModelDataMapper;

    @Inject
    RecoveryPresenter(SessionUseCase sessionUseCase,
                             CountryUseCase countryUseCase,
                             CountryModelDataMapper countryModelDataMapper,
                             AccountUseCase accountUseCase,
                             RecoveryModelMapper recoveryModelDataMapper) {
        this.sessionUseCase = sessionUseCase;
        this.countryUseCase = countryUseCase;
        this.countryModelDataMapper = countryModelDataMapper;
        this.accountUseCase = accountUseCase;
        this.recoveryModelDataMapper = recoveryModelDataMapper;
    }

    @Override
    public void attachView(@NonNull RecoveryView view) {
        recoveryView = view;
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
        this.sessionUseCase.dispose();
        this.countryUseCase.dispose();
        this.accountUseCase.dispose();
        this.recoveryView = null;
    }

    /**********************************************************/

    public void data() {
        this.sessionUseCase.getCountrySIM(new GetCountrySIM());
    }

    public void recovery(int countryId, String mail) {
        recoveryView.showLoading();

        this.accountUseCase.recovery(recoveryModelDataMapper.transform(
            new RecoveryModel(countryId, mail)), new RecoveryObserver());

    }

    /**********************************************************/

    private final class GetCountrySIM extends BaseObserver<String> {
        @Override
        public void onNext(String iso) {
            countryUseCase.getByBrand(Constant.BRAND_FOCUS, new GetCountry(iso));
        }
    }

    private final class GetCountry extends BaseObserver<List<Country>> {

        private final String iso;

        GetCountry(String iso) {
            this.iso = iso;
        }

        @Override
        public void onNext(List<Country> countries) {
            if (recoveryView == null) return;
            recoveryView.renderData(iso, countryModelDataMapper.transformToDataModel(countries));
        }
    }

    private final class RecoveryObserver extends BaseObserver<String> {
        @Override
        public void onNext(String result) {
            if (recoveryView == null) return;
            recoveryView.success(result);
        }

        @Override
        public void onComplete() {
            if (recoveryView == null) return;
            recoveryView.hideLoading();
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (recoveryView == null) return;
            recoveryView.hideLoading();
            recoveryView.failed(ErrorFactory.INSTANCE.create(exception));
        }
    }
}
