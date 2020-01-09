package biz.belcorp.consultoras.feature.home.clients.porcobrar;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.client.ClienteModelDataMapper;
import biz.belcorp.consultoras.common.model.user.UserModel;
import biz.belcorp.consultoras.common.model.user.UserModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Cliente;
import biz.belcorp.consultoras.domain.entity.Country;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.ClienteUseCase;
import biz.belcorp.consultoras.domain.interactor.CountryUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.exception.BusinessErrorFactory;
import biz.belcorp.consultoras.exception.ErrorFactory;

@PerActivity
public class PorCobrarClientsPresenter implements Presenter<PorCobrarClientsView> {

    private PorCobrarClientsView porCobrarClientsView;

    private final UserUseCase userUseCase;
    private final UserModelDataMapper userModelDataMapper;
    private final CountryUseCase countryUseCase;
    private final ClienteUseCase clienteUseCase;
    private final ClienteModelDataMapper clienteModelDataMapper;

    private UserModel userModel;

    @Inject
    PorCobrarClientsPresenter(ClienteUseCase clienteUseCase,
                                     UserModelDataMapper userModelDataMapper,
                                     CountryUseCase countryUseCase,
                                     ClienteModelDataMapper clienteModelDataMapper,
                                     UserUseCase userUseCase) {

        this.clienteUseCase = clienteUseCase;
        this.userModelDataMapper = userModelDataMapper;
        this.countryUseCase = countryUseCase;
        this.clienteModelDataMapper = clienteModelDataMapper;
        this.userUseCase = userUseCase;
    }

    @Override
    public void attachView(@NonNull PorCobrarClientsView view) {
        porCobrarClientsView = view;
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
//        userUseCase.dispose();
//        clienteUseCase.dispose();
//        countryUseCase.dispose();
        this.porCobrarClientsView = null;
    }

    void updateMaterialTap() {
        userUseCase.updateMaterialTapDeuda(new UpdateMaterialObserver());
    }

    void getClientesPorCobrar() {
        this.userUseCase.get(new GetUser());
    }

    void loadMaterialTap() {
        userUseCase.showMaterialTapDeuda(new ShowMaterialObserver());
    }

    /**********************************************************/

    private final class GetUser extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (null != user && countryUseCase != null) {
                String iso = user.getCountryISO();
                countryUseCase.find(iso, new GetCountryUser(user));
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (porCobrarClientsView != null) {
                porCobrarClientsView.hideLoading();
                porCobrarClientsView.onError(ErrorFactory.INSTANCE.create(exception));
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    porCobrarClientsView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    porCobrarClientsView.onError(ErrorFactory.INSTANCE.create(exception));
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
            if (null != country && clienteUseCase != null) {
                user.setCountryShowDecimal(country.isShowDecimals() ? 1 : 0);
                userModel = userModelDataMapper.transform(user);
                clienteUseCase.getDeudores(new GetClientesPorCobrarObserver());
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (porCobrarClientsView != null) {
                porCobrarClientsView.hideLoading();
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    porCobrarClientsView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    porCobrarClientsView.onError(ErrorFactory.INSTANCE.create(exception));
                }
            }
        }
    }

    private final class GetClientesPorCobrarObserver extends BaseObserver<Collection<Cliente>> {
        @Override
        public void onNext(Collection<Cliente> clients) {
            if (porCobrarClientsView != null) {
                List<ClienteModel> clienteModelList = clienteModelDataMapper.transform(clients);

                Collections.sort(clienteModelList, new Comparator<ClienteModel>() {
                    @Override
                    public int compare(ClienteModel c1, ClienteModel c2) {
                        String date1 = c1.getRecordatorioModels().isEmpty() ? "2099-11-30T09:35:00" : c1.getRecordatorioModels().get(0).getFecha();
                        String date2 = c2.getRecordatorioModels().isEmpty() ? "2099-11-30T09:35:00" : c2.getRecordatorioModels().get(0).getFecha();

                        return date1.compareTo(date2);
                    }
                });

                porCobrarClientsView.showClients(clienteModelList, userModel);
                if (clienteModelList.isEmpty())
                    loadMaterialTap();
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (porCobrarClientsView != null) {
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    porCobrarClientsView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    porCobrarClientsView.onBusinessError(BusinessErrorFactory.INSTANCE.create(exception));
                }
            }
        }
    }

    private final class ShowMaterialObserver extends BaseObserver<Boolean> {
        @Override
        public void onNext(Boolean show) {
            if (porCobrarClientsView != null && show) {
                porCobrarClientsView.showMaterialTap();
            }
        }
    }

    private final class UpdateMaterialObserver extends BaseObserver<Boolean> {
    }
}
