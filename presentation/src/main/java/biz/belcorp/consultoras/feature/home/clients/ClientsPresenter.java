package biz.belcorp.consultoras.feature.home.clients;

import android.support.annotation.NonNull;

import java.util.Collection;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
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
public class ClientsPresenter implements Presenter<ClientsView> {

    private ClientsView clientsView;

    private final UserUseCase userUseCase;
    private final CountryUseCase countryUseCase;
    private final ClienteUseCase clienteUseCase;
    private final LoginModelDataMapper loginModelDataMapper;

    @Inject
    ClientsPresenter(ClienteUseCase clienteUseCase,
                     CountryUseCase countryUseCase,
                     UserUseCase userUseCase,
                     LoginModelDataMapper loginModelDataMapper) {

        this.clienteUseCase = clienteUseCase;
        this.countryUseCase = countryUseCase;
        this.userUseCase = userUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
    }

    @Override
    public void attachView(@NonNull ClientsView view) {
        clientsView = view;
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
        this.clientsView = null;
    }

    /**********************************************************/

    void bajada() {
        this.clientsView.showLoading();
        this.userUseCase.get(new GetUser());
    }

    void initScreenTrack(int type) {
        userUseCase.get(new UserPropertyObserver(type));
    }

    void initScreenTrackBotonAgregarCliente() {
        userUseCase.get(new UserPropertyObserverBotonAgregarCliente());
    }

    void initScreenTrackNuevoCliente() {
        userUseCase.get(new UserPropertyObserverNuevoCliente());
    }

    void initScreenTrackAgregarDesdeContacto() {
        userUseCase.get(new UserPropertyObserverAgregarDesdeContacto());
    }

    void initScreenTrackAgregarDeuda() {
        userUseCase.get(new UserPropertyObserverAgregarDeuda());
    }

    void trackEvent(String screenName,
                    String eventCat,
                    String eventAction,
                    String eventLabel,
                    String eventName) {
        userUseCase.get(new EventPropertyObserver(screenName, eventCat, eventAction, eventLabel, eventName));
    }

    void showMaterialTap() {
        userUseCase.showMaterialTapCliente(new ShowMaterialObserver());
    }

    void showMaterialTapDeuda() {
        userUseCase.showMaterialTapDeuda(new ShowMaterialDeudaObserver());
    }

    void updateMaterialTap() {
        userUseCase.updateMaterialTapCliente(new UpdateMaterialObserver());
    }

    void updateMaterialTapDeuda() {
        userUseCase.updateMaterialTapDeuda(new UpdateMaterialObserver());
    }

    /**********************************************************/

    private void processError(Throwable exception) {
        clientsView.hideLoading();
        if (exception instanceof VersionException) {
            VersionException vE = (VersionException) exception;
            clientsView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
        } else {
            clientsView.onError(ErrorFactory.INSTANCE.create(exception));
        }
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
            processError(exception);
        }
    }

    private final class GetCountryUser extends BaseObserver<Country> {

        private User user;

        GetCountryUser(User user) {
            this.user = user;
        }

        @Override
        public void onNext(Country country) {
            if (null != country && clienteUseCase != null) {
                user.setCountryShowDecimal(country.isShowDecimals() ? 1 : 0);
                clienteUseCase.bajada(user.getCampaing(), new GetObserver());
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            processError(exception);
        }
    }

    private final class GetObserver extends BaseObserver<Collection<Cliente>> {

        @Override
        public void onNext(Collection<Cliente> saved) {
            if (clientsView != null) {
                clientsView.onClientsSaved();
                clientsView.hideLoading();
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            processError(exception);
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        private final int type;

        private UserPropertyObserver(int type) {
            this.type = type;
        }

        @Override
        public void onNext(User user) {
            clientsView.initScreenTrack(loginModelDataMapper.transform(user), type);
        }
    }

    private final class UserPropertyObserverBotonAgregarCliente extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            clientsView.initScreenTrackBotonAgregarCliente(loginModelDataMapper.transform(user));
        }
    }


    private final class UserPropertyObserverNuevoCliente extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            clientsView.initScreenTrackNuevoCliente(loginModelDataMapper.transform(user));
        }
    }

    private final class UserPropertyObserverAgregarDesdeContacto extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            clientsView.initScreenTrackAgregarDesdeContacto(loginModelDataMapper.transform(user));
        }
    }

    private final class UserPropertyObserverAgregarDeuda extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            clientsView.initScreenTrackAgregarDeuda(loginModelDataMapper.transform(user));
        }
    }

    private final class ShowMaterialObserver extends BaseObserver<Boolean> {
        @Override
        public void onNext(Boolean show) {
            if (clientsView != null && show) {
                clientsView.showMaterialTap();
            }
        }
    }

    private final class ShowMaterialDeudaObserver extends BaseObserver<Boolean> {
        @Override
        public void onNext(Boolean show) {
            if (clientsView != null && show) {
                clientsView.showMaterialTapDeuda();
            }
        }
    }

    private final class UpdateMaterialObserver extends BaseObserver<Boolean> {
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
            clientsView.trackEvent(screenHome,
                eventCat,
                eventAction,
                eventLabel,
                eventName,
                loginModelDataMapper.transform(user));
        }
    }
}
