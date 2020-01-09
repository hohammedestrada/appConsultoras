package biz.belcorp.consultoras.feature.home.clients.all;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.client.ClienteModelDataMapper;
import biz.belcorp.consultoras.common.model.client.ContactoModel;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Cliente;
import biz.belcorp.consultoras.domain.entity.NotificacionCliente;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.SincronizacionClienteYaExistenteException;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.ClienteUseCase;
import biz.belcorp.consultoras.domain.interactor.CountryUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.exception.BusinessErrorFactory;
import biz.belcorp.consultoras.exception.ErrorFactory;
import biz.belcorp.consultoras.util.CommunicationUtils;
import biz.belcorp.consultoras.util.anotation.ContactType;
import biz.belcorp.consultoras.util.anotation.UserType;
import biz.belcorp.library.util.CountryUtil;
import biz.belcorp.library.util.NetworkUtil;

@PerActivity
public class AllClientsPresenter implements Presenter<AllClientsView> {

    private AllClientsView allClientsView;

    private final UserUseCase userUseCase;
    private final CountryUseCase countryUseCase;
    private final ClienteUseCase clienteUseCase;
    private final ClienteModelDataMapper clienteModelDataMapper;
    private final LoginModelDataMapper loginModelDataMapper;

    @Inject
    AllClientsPresenter(ClienteUseCase clienteUseCase,
                        CountryUseCase countryUseCase,
                        ClienteModelDataMapper clienteModelDataMapper,
                        UserUseCase userUseCase,
                        LoginModelDataMapper loginModelDataMapper) {

        this.clienteUseCase = clienteUseCase;
        this.countryUseCase = countryUseCase;
        this.clienteModelDataMapper = clienteModelDataMapper;
        this.userUseCase = userUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
    }

    @Override
    public void attachView(@NonNull AllClientsView view) {
        allClientsView = view;
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
        this.allClientsView = null;
    }

    /**********************************************************/

    void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void getAllClientes() {
        this.clienteUseCase.getClientes(new GetAllClientesObserver());
    }

    void call(ClienteModel clienteModel, Activity activity) {
        Map<Integer, ContactoModel> contactoModelMap = clienteModel.getContactoModelMap();
        if (contactoModelMap != null && (contactoModelMap.get(ContactType.MOBILE) != null || contactoModelMap.get(ContactType.PHONE) != null)) {

            String phone;

            if (clienteModel.getTipoContactoFavorito() == null || clienteModel.getTipoContactoFavorito().equals(0))
                phone = contactoModelMap.containsKey(ContactType.MOBILE) ? contactoModelMap.get(ContactType.MOBILE).getValor() : contactoModelMap.get(ContactType.PHONE).getValor();
            else
                phone = contactoModelMap.get(clienteModel.getTipoContactoFavorito().equals(ContactType.PHONE) ? ContactType.PHONE : ContactType.MOBILE).getValor();

            CommunicationUtils.llamar(activity, phone);
        }
    }

    void sendWhatsapp(ClienteModel clienteModel, Context context) {
        Map<Integer, ContactoModel> contactoModelMap = clienteModel.getContactoModelMap();
        if (contactoModelMap.get(ContactType.MOBILE) != null) {
            String number = contactoModelMap.get(ContactType.MOBILE).getValor();
            CommunicationUtils.enviarWhatsapp(context, CountryUtil.getDialCode(context) + number, "");
        }
    }

    void eliminar(ClienteModel clienteModel, LoginModel loginModel, Fragment fragment) {
        boolean internet = NetworkUtil.isThereInternetConnection(fragment.getContext());

        clienteModel.setEstado(0);
        clienteModel.setSincronizado(internet ? 1 : 0);

        List<Cliente> clientes = new ArrayList<>();
        clientes.add(clienteModelDataMapper.transform(clienteModel));

        if (fragment instanceof AllClientsFragment) {
            allClientsView.showLoading();

            if (loginModel.getUserType() == UserType.CONSULTORA) {
                clienteUseCase.eliminar(clientes, new EliminarObserver());
            } else {
                clienteUseCase.deleteOfflinePostulant(clientes, new EliminarObserver());
            }
        }
    }

    void favorito(ClienteModel clienteModel, Fragment fragment) {
        List<Cliente> clientes = new ArrayList<>();
        Integer favorito = clienteModel.getFavorito();
        if (favorito == null) {
            clienteModel.setFavorito(1);
        } else {
            if (favorito.equals(0)) {
                clienteModel.setFavorito(1);
            } else {
                clienteModel.setFavorito(0);
            }
        }
        boolean internet = NetworkUtil.isThereInternetConnection(fragment.getContext());
        clienteModel.setSincronizado(internet ? 1 : 0);
        clientes.add(clienteModelDataMapper.transform(clienteModel));

        if (fragment instanceof AllClientsFragment) {
            allClientsView.showLoading();
            if (internet) {
                clienteUseCase.subida(clientes, new FavoritoObserver());
            } else {
                clienteUseCase.guardar(clientes, new FavoritoObserver());
            }
        }
    }

    public void trackEvent(String screenName,
                           String eventCat,
                           String eventAction,
                           String eventLabel,
                           String eventName) {
        userUseCase.get(new EventPropertyObserver(screenName, eventCat, eventAction, eventLabel, eventName));
    }

    /**********************************************************/

    private final class GetAllClientesObserver extends BaseObserver<Collection<Cliente>> {
        @Override
        public void onNext(Collection<Cliente> clients) {
            if (allClientsView != null) {
                clienteUseCase.getNotificacionesCliente(new GetNotificacionesClienteObserver());
                allClientsView.showClients(clienteModelDataMapper.transform(clients));
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (allClientsView != null) {
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    allClientsView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    allClientsView.onError(ErrorFactory.INSTANCE.create(exception));
                }
            }
        }
    }

    private final class GetNotificacionesClienteObserver extends BaseObserver<List<NotificacionCliente>> {
    }

    private final class EliminarObserver extends BaseObserver<String> {

        @Override
        public void onNext(String mensaje) {
            if (allClientsView == null) return;
            allClientsView.hideLoading();
            allClientsView.onDataUpdated(mensaje);
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (allClientsView == null) return;
            allClientsView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                allClientsView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else if (exception instanceof SincronizacionClienteYaExistenteException) {
                allClientsView.onBusinessError(BusinessErrorFactory.INSTANCE.create(exception));
            } else {
                allClientsView.onError(ErrorFactory.INSTANCE.create(exception));
            }
        }

    }

    private final class FavoritoObserver extends BaseObserver<List<Cliente>> {

        @Override
        public void onNext(List<Cliente> response) {
            if (allClientsView != null) {
                allClientsView.hideLoading();

                if (!response.isEmpty()) {
                    Cliente cliente = response.get(0);
                    if (cliente != null)
                        allClientsView.onDataUpdated(cliente.getMensajeRespuesta());
                }
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (allClientsView != null) {
                allClientsView.hideLoading();
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    allClientsView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    allClientsView.onBusinessError(BusinessErrorFactory.INSTANCE.create(exception));
                }
            }
        }
    }

    private final class EventPropertyObserver extends BaseObserver<User> {

        final String screenHome;
        final String eventCat;
        final String eventAction;
        final String eventLabel;
        final String eventName;

        private EventPropertyObserver(String screenHome,
                                      String eventCat,
                                      String eventAction,
                                      String eventLabel,
                                      String eventName) {
            this.screenHome = screenHome;
            this.eventCat = eventCat;
            this.eventAction = eventAction;
            this.eventLabel = eventLabel;
            this.eventName = eventName;
        }

        @Override
        public void onNext(User user) {
            Tracker.trackEvent(screenHome,
                eventCat,
                eventAction,
                eventLabel,
                eventName,
                loginModelDataMapper.transform(user));
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            allClientsView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }
}
