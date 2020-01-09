package biz.belcorp.consultoras.feature.home.clients.pedido;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
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
import biz.belcorp.consultoras.exception.ErrorFactory;

@PerActivity
public class PedidoClientsPresenter implements Presenter<PedidoClientsView> {

    private PedidoClientsView pedidosclientsView;

    private final UserUseCase userUseCase;
    private final UserModelDataMapper userModelDataMapper;
    private final CountryUseCase countryUseCase;
    private final ClienteUseCase clienteUseCase;
    private final ClienteModelDataMapper clienteModelDataMapper;

    private UserModel userModel;

    @Inject
    PedidoClientsPresenter(ClienteUseCase clienteUseCase,
                           CountryUseCase countryUseCase,
                           ClienteModelDataMapper clienteModelDataMapper,
                           UserUseCase userUseCase,
                           UserModelDataMapper userModelDataMapper) {

        this.clienteUseCase = clienteUseCase;
        this.countryUseCase = countryUseCase;
        this.clienteModelDataMapper = clienteModelDataMapper;
        this.userUseCase = userUseCase;
        this.userModelDataMapper = userModelDataMapper;
    }

    @Override
    public void attachView(@NonNull PedidoClientsView view) {
        pedidosclientsView = view;
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
        userUseCase.dispose();
        clienteUseCase.dispose();
        countryUseCase.dispose();
        this.pedidosclientsView = null;
    }

    /**********************************************************/

    void getClientesPedidos() {
        this.userUseCase.get(new GetUser());
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
            if (pedidosclientsView != null) {
                pedidosclientsView.hideLoading();
                pedidosclientsView.onError(ErrorFactory.INSTANCE.create(exception));
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    pedidosclientsView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    pedidosclientsView.onError(ErrorFactory.INSTANCE.create(exception));
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
                clienteUseCase.getClientes(new GetAllClientesObserver());
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (pedidosclientsView != null) {
                pedidosclientsView.hideLoading();
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    pedidosclientsView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    pedidosclientsView.onError(ErrorFactory.INSTANCE.create(exception));
                }
            }
        }
    }

    private final class GetAllClientesObserver extends BaseObserver<Collection<Cliente>> {
        @Override
        public void onNext(Collection<Cliente> clients) {
            if (pedidosclientsView != null) {
                List<ClienteModel> newClienteModelList = new ArrayList<>();
                List<ClienteModel> clienteModelList = clienteModelDataMapper.transform(clients);

                for (ClienteModel clienteModel : clienteModelList) {
                    if (clienteModel.getCantidadProductos() > 0) {
                        newClienteModelList.add(clienteModel);
                    }
                }

                pedidosclientsView.showClients(newClienteModelList, userModel);
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (pedidosclientsView != null) {
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    pedidosclientsView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    pedidosclientsView.onError(ErrorFactory.INSTANCE.create(exception));
                }
            }
        }
    }
}
