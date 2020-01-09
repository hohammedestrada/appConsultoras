package biz.belcorp.consultoras.feature.debt;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.debt.CampaniaModel;
import biz.belcorp.consultoras.common.model.debt.DebtModel;
import biz.belcorp.consultoras.common.model.debt.DebtModelMapper;
import biz.belcorp.consultoras.common.model.user.UserModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Country;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.CountryUseCase;
import biz.belcorp.consultoras.domain.interactor.DebtUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.util.anotation.UserType;
import biz.belcorp.library.util.CountryUtil;

/**
 *
 */
@PerActivity
class AddDebtPresenter implements Presenter<AddDebtView> {

    private AddDebtView addDebtView;

    private ClienteModel selectedClient;

    private final DebtUseCase debtUseCase;
    private final UserUseCase userUseCase;
    private final CountryUseCase countryUseCase;
    private final DebtModelMapper debtModelMapper;
    private final UserModelDataMapper userModelDataMapper;
    private final LoginModelDataMapper loginModelDataMapper;

    @Inject
    AddDebtPresenter(DebtUseCase debtUseCase, UserUseCase userUseCase, CountryUseCase countryUseCase, DebtModelMapper debtModelMapper, UserModelDataMapper userModelDataMapper, LoginModelDataMapper loginModelDataMapper) {
        this.debtUseCase = debtUseCase;
        this.userUseCase = userUseCase;
        this.countryUseCase = countryUseCase;
        this.debtModelMapper = debtModelMapper;
        this.userModelDataMapper = userModelDataMapper;
        this.loginModelDataMapper = loginModelDataMapper;
    }

    /**********************************************************/

    @Override
    public void attachView(@NonNull AddDebtView view) {
        addDebtView = view;
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
        this.addDebtView = null;
    }

    /**********************************************************/

    void getCurrentCampaign() {
        if (addDebtView == null) return;
        addDebtView.showLoading();
        userUseCase.get(new UserObserver());
    }

    void setSelectedClient(ClienteModel selectedClient) {
        this.selectedClient = selectedClient;
    }

    void setSelectedClient(int clienteLocalID, int clienteID, String clientName) {
        this.selectedClient = new ClienteModel(clienteLocalID, clienteID, clientName);
    }

    void uploadDebt(DebtModel debtModel, LoginModel loginModel) {
        if (addDebtView == null) return;
        addDebtView.showLoading();

        debtModel.setClienteID(selectedClient.getClienteID());
        debtModel.setClienteLocalID(selectedClient.getId());

        if (loginModel.getUserType() == UserType.CONSULTORA) {
            debtUseCase.updateNewDebt(debtModelMapper.transform(debtModel), new UpdateDebtObserver());
        } else {
            debtUseCase.updateNewDebtOfflinePostulant(debtModelMapper.transform(debtModel), new UpdateDebtObserver());
        }
    }

    void trackScreen() {
        userUseCase.get(new UserPropertyObserver());
    }

    void initScreenTrackAgregarDeuda() {
        userUseCase.get(new UserPropertyAgregarDeudaObserver());
    }

    void trackBackPressed() {
        userUseCase.get(new UserBackPressedObserver());
    }

    /**********************************************************/

    private final class UpdateDebtObserver extends BaseObserver<Boolean> {
        @Override
        public void onNext(Boolean aBoolean) {
            super.onNext(aBoolean);

            userUseCase.updateScheduler(new UpdateObserver());
            if (null != addDebtView) {
                addDebtView.hideLoading();
                addDebtView.onUploadDebtComplete();
            }
        }

        @Override
        public void onError(Throwable exception) {
            super.onError(exception);

            if (null != addDebtView) {
                addDebtView.hideLoading();
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    addDebtView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    addDebtView.onError(exception);
                }
            }

        }
    }

    private final class UserObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            super.onNext(user);

            if (null != user) {
                String iso = user.getCountryISO();
                countryUseCase.find(iso, new GetCountryUser(user));
            }
        }

        @Override
        public void onError(Throwable exception) {
            super.onError(exception);

            if (null != addDebtView) {
                addDebtView.hideLoading();
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    addDebtView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    addDebtView.onError(exception);
                }
            }

        }
    }

    private final class GetCountryUser extends BaseObserver<Country> {

        private User user;

        private GetCountryUser(User user) {
            this.user = user;
        }

        @Override
        public void onNext(Country country) {
            if (null != addDebtView) {
                user.setCountryShowDecimal(country.isShowDecimals() ? 1 : 0);

                int campaignNumber = Integer.parseInt(user.getCampaing().substring(4));
                int campaignYearNumber = Integer.parseInt(user.getCampaing().substring(0, 4));

                List<CampaniaModel> campanias = new ArrayList<>();

                for (int i = 0; i < 6; i++) {
                    if (campaignNumber == 0) {
                        campaignYearNumber--;
                        campaignNumber = CountryUtil.getMaximumCampaign(country.getIso());
                    }

                    CampaniaModel campaniaModel = new CampaniaModel();
                    campaniaModel.setCampaniaId(String.format(Locale.getDefault(), "%1$d%2$02d", campaignYearNumber, campaignNumber));
                    campaniaModel.setCampaniaName(String.format(Locale.getDefault(), "C-%02d", campaignNumber));
                    campaignNumber--;

                    campanias.add(campaniaModel);
                }

                addDebtView.setData(userModelDataMapper.transform(user));
                addDebtView.setUpCampaign(campanias);
                addDebtView.hideLoading();
            }
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            addDebtView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserPropertyAgregarDeudaObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            addDebtView.initScreenTrackAgregarDeuda(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            addDebtView.trackBackPressed(loginModelDataMapper.transform(user));
        }
    }

    private final class UpdateObserver extends BaseObserver<Boolean> { }

}
