package biz.belcorp.consultoras.feature.client.registration;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
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
import biz.belcorp.consultoras.domain.entity.Anotacion;
import biz.belcorp.consultoras.domain.entity.Cliente;
import biz.belcorp.consultoras.domain.entity.Country;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.SincronizacionClienteNoRegistradoException;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.ClienteUseCase;
import biz.belcorp.consultoras.domain.interactor.CountryUseCase;
import biz.belcorp.consultoras.domain.interactor.NoteUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.util.anotation.UserType;
import biz.belcorp.library.log.BelcorpLogger;

@PerActivity
public class ClientRegistrationPresenter implements Presenter<ClientRegistrationView> {

    public static final String TAG = "ClientRegistrationPresenter";
    private static final String ERROR = "Error";
    private ClientRegistrationView clientRegistrationView;

    private final ClienteUseCase clienteUseCase;
    private final CountryUseCase countryUseCase;
    private final ClienteModelDataMapper clientModelDataMapper;
    private final UserUseCase userUseCase;
    private final UserModelDataMapper userModelDataMapper;
    private LoginModelDataMapper loginModelDataMapper;
    private final NoteUseCase noteUseCase;
    private AnotacionModelDataMapper anotacionModelDataMapper;

    @Inject
    ClientRegistrationPresenter(ClienteUseCase clienteUseCase,
                                CountryUseCase countryUseCase,
                                ClienteModelDataMapper clientModelDataMapper,
                                UserUseCase userUseCase,
                                UserModelDataMapper userModelDataMapper,
                                LoginModelDataMapper loginModelDataMapper,
                                NoteUseCase noteUseCase,
                                AnotacionModelDataMapper anotacionModelDataMapper) {
        this.clienteUseCase = clienteUseCase;
        this.countryUseCase = countryUseCase;
        this.clientModelDataMapper = clientModelDataMapper;
        this.userUseCase = userUseCase;
        this.userModelDataMapper = userModelDataMapper;
        this.loginModelDataMapper = loginModelDataMapper;
        this.noteUseCase = noteUseCase;
        this.anotacionModelDataMapper = anotacionModelDataMapper;
    }

    @Override
    public void attachView(@NonNull ClientRegistrationView view) {
        clientRegistrationView = view;
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
        this.clientRegistrationView = null;
    }

    /**********************************************************/

    void getAnotations(Integer id) {
        this.clientRegistrationView.showLoading();
        userUseCase.get(new UserCountryObserver(id));
    }

    void deleteAnotations() {
        noteUseCase.deleteByClientLocalID(0, new DeleteAnotationsObserver(0));
    }

    void deleteAnotacion(AnotacionModel anotacionModel) {
        clienteUseCase.deleteAnotacion(anotacionModelDataMapper.transform(anotacionModel), new DeleteAnotacionObserver(anotacionModel));
    }

    void save(ClienteModel model, LoginModel loginModel) {
        this.clientRegistrationView.showLoading();
        this.clienteUseCase.validateClient(this.clientModelDataMapper.transform(model), new ValidateObserver(model, loginModel));
    }

    void getCountryForRegister(ClienteModel model) {
        this.clientRegistrationView.showLoading();
        userUseCase.get(new GetUserorRegisterObserver(model));
    }

    void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    public void initScreenTrackAgregarGuardar() {
        userUseCase.get(new UserPropertyAgregarGuardarObserver());
    }

    /**********************************************************/

    private final class UserCountryObserver extends BaseObserver<User> {

        private final int id;

        private UserCountryObserver(int id) {
            this.id = id;
        }

        @Override
        public void onNext(User user) {
            countryUseCase.find(user.getCountryISO(), new CountryObserver(id));
        }

        @Override
        public void onError(Throwable exception) {
            clientRegistrationView.hideLoading();
            clientRegistrationView.onError(exception);
        }
    }

    private final class CountryObserver extends BaseObserver<Country> {

        private final int id;

        private CountryObserver(int id) {
            this.id = id;
        }

        @Override
        public void onNext(Country country) {
            clientRegistrationView.showMaximumNoteAmount(country.getMaxNoteAmount());
            noteUseCase.listByClientLocalID(id, new GetAnotationsObserver(country.getMaxNoteAmount()));
        }

        @Override
        public void onError(Throwable exception) {
            BelcorpLogger.w(ERROR, exception);
        }
    }

    private final class GetAnotationsObserver extends BaseObserver<Collection<Anotacion>> {

        private final int maxNoteAmount;

        public GetAnotationsObserver(int maxNoteAmount) {
            this.maxNoteAmount = maxNoteAmount;
        }

        @Override
        public void onNext(Collection<Anotacion> anotacions) {
            if (null == clientRegistrationView) return;
            clientRegistrationView.showAnotations(anotacionModelDataMapper.transform(anotacions), maxNoteAmount);
        }

        @Override
        public void onError(Throwable exception) {
            if (null == clientRegistrationView) return;
            clientRegistrationView.hideLoading();
            clientRegistrationView.showAnotations(null, maxNoteAmount);
        }

        @Override
        public void onComplete() {
            if (null == clientRegistrationView) return;
            clientRegistrationView.hideLoading();
        }
    }

    private final class DeleteAnotationsObserver extends BaseObserver<Boolean> {

        private int type;

        DeleteAnotationsObserver(int type) {
            this.type = type;
        }

        @Override
        public void onNext(Boolean aBoolean) {
            BelcorpLogger.d(ERROR, "Result " + aBoolean);
        }

        @Override
        public void onError(Throwable exception) {
            if (type == 0) {
                if (clientRegistrationView == null) return;
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    clientRegistrationView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    clientRegistrationView.onError(exception);
                }
            }
            BelcorpLogger.w(ERROR, exception);
        }

        @Override
        public void onComplete() {
            if (type == 0) {
                if (clientRegistrationView == null) return;
                clientRegistrationView.recordCanceled();
            }
        }
    }

    private final class GetUserorRegisterObserver extends BaseObserver<User> {
        private ClienteModel model;

        private GetUserorRegisterObserver(ClienteModel model) {
            this.model = model;
        }

        @Override
        public void onNext(User user) {
            if (clientRegistrationView != null) {
                clientRegistrationView.onCountryObtained(model, userModelDataMapper.transform(user).getCountryISO());
            }
        }

        @Override
        public void onError(Throwable exception) {
            BelcorpLogger.w(ERROR, exception);
            if (null == clientRegistrationView) return;

            clientRegistrationView.hideLoading();

            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                clientRegistrationView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            }
        }
    }

    private final class DeleteAnotacionObserver extends BaseObserver<Boolean> {

        AnotacionModel anotacionModel;

        private DeleteAnotacionObserver(AnotacionModel anotacionModel) {
            this.anotacionModel = anotacionModel;
        }

        @Override
        public void onNext(Boolean result) {
            if (null == clientRegistrationView) return;
            clientRegistrationView.anotacionDeleted(anotacionModel);

        }

        @Override
        public void onError(Throwable exception) {
            BelcorpLogger.w(ERROR, exception);
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
                        clienteUseCase.subida((List<Cliente>) clientModelDataMapper.transform(clienteModels), new SaveObserver());
                    } else {
                        clienteUseCase.guardar((List<Cliente>) clientModelDataMapper.transform(clienteModels), new SaveObserver());
                    }

                    noteUseCase.deleteByClientLocalID(0, new DeleteAnotationsObserver(1));
                    break;
                case 1:
                    if (clientRegistrationView == null) return;
                    clientRegistrationView.hideLoading();
                    clientRegistrationView.onError(new SincronizacionClienteNoRegistradoException("El Nombre ya existe. No se puede registrar un mismo cliente con un nombre registrado anteriormente."));

                    break;
                case 2:
                    if (clientRegistrationView == null) return;
                    clientRegistrationView.hideLoading();
                    clientRegistrationView.onError(new SincronizacionClienteNoRegistradoException("El Télefono/Celular ya existe. No se puede registrar un mismo cliente con un número registrado anteriormente."));
                    break;
                default:
                    if (clientRegistrationView == null) return;
                    clientRegistrationView.hideLoading();
                    break;
            }

        }

        @Override
        public void onError(Throwable exception) {
            if (clientRegistrationView == null) return;
            clientRegistrationView.hideLoading();
        }
    }

    private final class SaveObserver extends BaseObserver<List<Cliente>> {

        @Override
        public void onNext(List<Cliente> result) {
            if (clientRegistrationView != null) {

                List<ClienteModel> clienteModels = clientModelDataMapper.transform(result);

                ClienteModel client = null;

                if (clienteModels != null && !clienteModels.isEmpty()) {
                    client = clienteModels.get(0);
                }

                clientRegistrationView.saved(client, true);
                clientRegistrationView.hideLoading();
            }
        }

        @Override
        public void onError(Throwable exception) {
            if (clientRegistrationView != null) {
                clientRegistrationView.hideLoading();
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    clientRegistrationView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    clientRegistrationView.onError(exception);
                }
            }

            BelcorpLogger.w(ERROR, exception);
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            clientRegistrationView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserPropertyAgregarGuardarObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            clientRegistrationView.initScreenTrackAgregarGuardar(loginModelDataMapper.transform(user));
        }
    }

}
