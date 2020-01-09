package biz.belcorp.consultoras.feature.client.edit;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.client.AnotacionModel;
import biz.belcorp.consultoras.common.model.client.AnotacionModelDataMapper;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.client.ClienteModelDataMapper;
import biz.belcorp.consultoras.common.model.user.UserModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Cliente;
import biz.belcorp.consultoras.domain.entity.Country;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.SincronizacionClienteNoRegistradoException;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.ClienteUseCase;
import biz.belcorp.consultoras.domain.interactor.CountryUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.util.anotation.UserType;
import biz.belcorp.library.log.BelcorpLogger;

@PerActivity
public class ClientEditPresenter implements Presenter<ClientEditView> {

    public static final String TAG = "ClientEditPresenter";
    private static final String ERROR = "Error";
    private ClientEditView clientEditView;

    private final UserUseCase userUseCase;
    private final CountryUseCase countryUseCase;
    private final ClienteUseCase clienteUseCase;
    private final ClienteModelDataMapper clientModelDataMapper;
    private final UserModelDataMapper userModelDataMapper;
    private final AnotacionModelDataMapper anotacionModelDataMapper;
    private final LoginModelDataMapper loginModelDataMapper;

    @Inject
    ClientEditPresenter(ClienteUseCase clienteUseCase,
                        CountryUseCase countryUseCase,
                        ClienteModelDataMapper clientModelDataMapper,
                        UserUseCase userUseCase,
                        UserModelDataMapper userModelDataMapper,
                        AnotacionModelDataMapper anotacionModelDataMapper,
                        LoginModelDataMapper loginModelDataMapper) {
        this.clienteUseCase = clienteUseCase;
        this.countryUseCase = countryUseCase;
        this.clientModelDataMapper = clientModelDataMapper;
        this.userUseCase = userUseCase;
        this.userModelDataMapper = userModelDataMapper;
        this.anotacionModelDataMapper = anotacionModelDataMapper;
        this.loginModelDataMapper = loginModelDataMapper;
    }

    @Override
    public void attachView(@NonNull ClientEditView view) {
        clientEditView = view;
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
        this.clienteUseCase.dispose();
        this.clientEditView = null;
    }

    /**********************************************************/

    void loadDataEdit(int id) {
        userUseCase.get(new GetUser(id));
    }

    void loadNotas(int id) {
        clienteUseCase.findClienteById(id, new GetNotesObserver());
    }

    void update(ClienteModel model, LoginModel loginModel) {
        this.clientEditView.showLoading();
        this.clienteUseCase.validateClient(this.clientModelDataMapper.transform(model), new ValidateObserver(model, loginModel));
    }

    void delete(ClienteModel model) {
        List<ClienteModel> clienteModels = new ArrayList<>();
        clienteModels.add(model);
        clienteUseCase.eliminar((List<Cliente>) clientModelDataMapper.transform(clienteModels), new DeleteObserver());
    }

    void getCountryForEdit(ClienteModel model) {
        this.clientEditView.showLoading();
        userUseCase.get(new GetUserForEditObserver(model));
    }


    void deleteAnotacion(AnotacionModel anotacionModel) {
        clientEditView.showLoading();
        clienteUseCase.deleteAnotacion(anotacionModelDataMapper.transform(anotacionModel), new DeleteAnotacionObserver(anotacionModel));
    }

    void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    /**********************************************************/

    private final class GetUser extends BaseObserver<User> {

        private int id;

        public GetUser(int id) {
            this.id = id;
        }

        @Override
        public void onNext(User user) {

            if (null != user) {

                String symbol = user.getCountryMoneySymbol();
                String iso = user.getCountryISO();

                countryUseCase.find(iso, new CountryObserver(id, iso, symbol));
            }
        }

        @Override
        public void onError(Throwable exception) {
            BelcorpLogger.w(ERROR, exception);
            clientEditView.hideLoading();
        }
    }

    private final class CountryObserver extends BaseObserver<Country> {
        private final int id;
        private final String iso;
        private final String symbol;

        private CountryObserver(int id, String iso, String symbol) {
            this.id = id;
            this.iso = iso;
            this.symbol = symbol;
        }

        @Override
        public void onNext(Country country) {
            clientEditView.showMaximumNoteAmount(country.getMaxNoteAmount());
            clienteUseCase.findClienteById(id, new GetDataEditObserver(iso, symbol));
        }

        @Override
        public void onError(Throwable exception) {
            BelcorpLogger.w(ERROR, exception);
        }
    }

    private final class GetUserForEditObserver extends BaseObserver<User> {
        private ClienteModel model;

        private GetUserForEditObserver(ClienteModel model) {
            this.model = model;
        }

        @Override
        public void onNext(User user) {
            if (clientEditView != null) {
                clientEditView.onCountryObtained(model, userModelDataMapper.transform(user).getCountryISO());
            }
        }

        @Override
        public void onError(Throwable exception) {
            BelcorpLogger.w(ERROR, exception);
            if (null == clientEditView) return;

            clientEditView.hideLoading();

            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                clientEditView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            }
        }
    }

    private final class GetDataEditObserver extends BaseObserver<Cliente> {

        private final String iso;
        private final String symbol;

        public GetDataEditObserver(String iso, String symbol) {
            this.iso = iso;
            this.symbol = symbol;
        }

        @Override
        public void onNext(Cliente client) {
            if (clientEditView != null) {
                clientEditView.showClient(clientModelDataMapper.transform(client), iso, symbol);
            }
        }

        @Override
        public void onComplete() {
            if (clientEditView != null) {
                clientEditView.hideLoading();
            }
        }
    }

    private final class GetNotesObserver extends BaseObserver<Cliente> {

        @Override
        public void onNext(Cliente client) {
            if (clientEditView != null) {
                clientEditView.showNotes(clientModelDataMapper.transform(client));
            }
        }

        @Override
        public void onComplete() {
            if (clientEditView != null) {
                clientEditView.hideLoading();
            }
        }
    }

    private final class ValidateObserver extends BaseObserver<Integer> {

        private ClienteModel cliente;
        private LoginModel loginModel;

        private ValidateObserver(ClienteModel cliente, LoginModel loginModel) {
            this.cliente = cliente;
            this.loginModel = loginModel;
        }

        @Override
        public void onNext(Integer validate) {

            switch (validate) {
                case 0:
                    List<ClienteModel> clienteModels = new ArrayList<>();
                    clienteModels.add(cliente);

                    if (loginModel.getUserType() == UserType.CONSULTORA) {
                        clienteUseCase.subida((List<Cliente>) clientModelDataMapper.transform(clienteModels), new UpdateObserver());
                    } else {
                        clienteUseCase.guardar((List<Cliente>) clientModelDataMapper.transform(clienteModels), new UpdateObserver());
                    }

                    break;
                case 1:
                    if (clientEditView == null) return;
                    clientEditView.hideLoading();
                    clientEditView.onError(new SincronizacionClienteNoRegistradoException("El Nombre ya existe. No se puede registrar un mismo cliente con un nombre registrado anteriormente."));

                    break;
                case 2:
                    if (clientEditView == null) return;
                    clientEditView.hideLoading();
                    clientEditView.onError(new SincronizacionClienteNoRegistradoException("El Télefono/Celular ya existe. No se puede registrar un mismo cliente con un número registrado anteriormente."));

                    break;
                default:
                    if (clientEditView == null) return;
                    clientEditView.hideLoading();

                    break;
            }
        }

        @Override
        public void onError(Throwable exception) {
            if (clientEditView == null) return;
            clientEditView.hideLoading();
        }
    }

    private final class UpdateObserver extends BaseObserver<List<Cliente>> {

        @Override
        public void onNext(List<Cliente> result) {
            if (clientEditView != null) {
                clientEditView.saved(true);
            }
        }

        @Override
        public void onError(Throwable exception) {
            if (clientEditView != null) {
                clientEditView.hideLoading();
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    clientEditView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    clientEditView.onError(exception);
                }
            }
            BelcorpLogger.w(ERROR, exception);
        }
    }

    private final class DeleteObserver extends BaseObserver<String> {

        @Override
        public void onNext(String result) {
            if (clientEditView != null) {
                clientEditView.deleted(true);
            }
        }

        @Override
        public void onError(Throwable exception) {
            if (clientEditView != null) {
                clientEditView.hideLoading();
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    clientEditView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    clientEditView.onError(exception);
                }
            }
            BelcorpLogger.w(ERROR, exception);
        }
    }

    private final class DeleteAnotacionObserver extends BaseObserver<Boolean> {

        AnotacionModel anotacionModel;

        private DeleteAnotacionObserver(AnotacionModel anotacionModel) {
            this.anotacionModel = anotacionModel;
        }

        @Override
        public void onNext(Boolean result) {
            if (clientEditView != null) {
                clientEditView.hideLoading();
                clientEditView.anotacionDeleted(anotacionModel);
            }
        }

        @Override
        public void onError(Throwable exception) {
            if (clientEditView != null) {
                clientEditView.hideLoading();
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    clientEditView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    clientEditView.onError(exception);
                }
            }
            BelcorpLogger.w(ERROR, exception);
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            clientEditView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }
}
