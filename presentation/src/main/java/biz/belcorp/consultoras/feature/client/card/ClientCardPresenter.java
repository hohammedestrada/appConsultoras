package biz.belcorp.consultoras.feature.client.card;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.client.AnotacionModel;
import biz.belcorp.consultoras.common.model.client.AnotacionModelDataMapper;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.client.ClienteModelDataMapper;
import biz.belcorp.consultoras.common.model.menu.MenuModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Cliente;
import biz.belcorp.consultoras.domain.entity.Country;
import biz.belcorp.consultoras.domain.entity.Menu;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.ClienteUseCase;
import biz.belcorp.consultoras.domain.interactor.CountryUseCase;
import biz.belcorp.consultoras.domain.interactor.MenuUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.library.log.BelcorpLogger;

@PerActivity
public class ClientCardPresenter implements Presenter<ClientCardView> {

    public static final String TAG = "ClientCardPresenter";
    private static final String ERROR = "Error";

    private ClientCardView clientCardView;

    private final ClienteUseCase clienteUseCase;
    private final CountryUseCase countryUseCase;
    private final UserUseCase userUseCase;
    private final MenuUseCase menuUseCase;

    private final LoginModelDataMapper loginModelDataMapper;
    private final ClienteModelDataMapper clientModelDataMapper;
    private final AnotacionModelDataMapper anotacionModelDataMapper;
    private final MenuModelDataMapper menuModelDataMapper;

    @Inject
    ClientCardPresenter(UserUseCase userUseCase,
                        ClienteUseCase clienteUseCase,
                        CountryUseCase countryUseCase,
                        MenuUseCase menuUseCase,
                        ClienteModelDataMapper clientModelDataMapper,
                        AnotacionModelDataMapper anotacionModelDataMapper,
                        LoginModelDataMapper loginModelDataMapper,
                        MenuModelDataMapper menuModelDataMapper) {
        this.userUseCase = userUseCase;
        this.clienteUseCase = clienteUseCase;
        this.countryUseCase = countryUseCase;
        this.menuUseCase = menuUseCase;
        this.clientModelDataMapper = clientModelDataMapper;
        this.anotacionModelDataMapper = anotacionModelDataMapper;
        this.loginModelDataMapper = loginModelDataMapper;
        this.menuModelDataMapper = menuModelDataMapper;
    }

    @Override
    public void attachView(@NonNull ClientCardView view) {
        clientCardView = view;
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
        this.countryUseCase.dispose();
        this.menuUseCase.dispose();
        this.clientCardView = null;
    }

    /**********************************************************/

    void load(int id) {
        userUseCase.get(new GetUser(id));
    }

    void loadOnline(Integer id, Integer clientLocalId) {
        this.clientCardView.showLoading();
        userUseCase.get(new GetCampaingCode(id, clientLocalId));
    }

    void save(ClienteModel model) {
        List<ClienteModel> clienteModels = new ArrayList<>();
        clienteModels.add(model);
        clienteUseCase.subida((List<Cliente>) clientModelDataMapper.transform(clienteModels), new SaveObserver());
    }

    void saveFavorite(ClienteModel model) {
        this.clientCardView.showLoading();

        List<ClienteModel> clienteModels = new ArrayList<>();
        clienteModels.add(model);

        clienteUseCase.subida((List<Cliente>) clientModelDataMapper.transform(clienteModels), new SaveFavoriteObserver());
    }

    void deleteAnotacion(AnotacionModel anotacionModel) {
        clientCardView.showLoading();
        clienteUseCase.deleteAnotacion(anotacionModelDataMapper.transform(anotacionModel), new DeleteAnotacionObserver(anotacionModel));
    }

    void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void initScreenTrackGestionarDeuda() {
        userUseCase.get(new UserPropertyObserverGestionarDeuda());
    }

    void initScreenTrackIngresarPedido() {
        userUseCase.get(new UserPropertyObserverIngresarPedido());
    }

    void initScreenTrackRevisarPedidos() {
        userUseCase.get(new UserPropertyObserverRevisarPedidos());
    }

    void trackBackPressed() {
        userUseCase.get(new UserBackPressedObserver());
    }

    void getMenuActive(String code1, String code2) {
        this.menuUseCase.getActive(code1, code2, new GetMenuObserver());
    }


    /**********************************************************/

    private void processError(Throwable exception) {
        clientCardView.hideLoading();
        if (exception instanceof VersionException) {
            VersionException vE = (VersionException) exception;
            clientCardView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
        } else {
            clientCardView.onError(exception);
        }
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
        public void onError(@NonNull Throwable exception) {
            BelcorpLogger.w(ERROR, exception);
            if (null == clientCardView) return;
            clientCardView.hideLoading();
            processError(exception);
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
            if (null == clientCardView) return;
            clientCardView.showMaximumNoteAmount(country.getMaxNoteAmount());
            clienteUseCase.findClienteById(id, new GetObserver(iso, symbol, country.getMaxNoteAmount()));
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            BelcorpLogger.w(ERROR, exception);
            if (null == clientCardView) return;
            processError(exception);
        }
    }

    private final class GetObserver extends BaseObserver<Cliente> {

        private final String iso;
        private final String symbol;
        private final int maxNoteAmount;

        private GetObserver(String iso, String symbol, int maxNoteAmount) {
            this.iso = iso;
            this.symbol = symbol;
            this.maxNoteAmount = maxNoteAmount;
        }

        @Override
        public void onNext(final Cliente client) {
            if (null == clientCardView) return;
            clientCardView.showClient(clientModelDataMapper.transform(client), iso, symbol, maxNoteAmount);
        }

        @Override
        public void onComplete() {
            if (null == clientCardView) return;
            clientCardView.hideLoading();
        }
    }

    private final class SaveObserver extends BaseObserver<List<Cliente>> {

        @Override
        public void onNext(List<Cliente> result) {
            if (null == clientCardView) return;
            clientCardView.saved(true);
            clientCardView.hideLoading();
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            BelcorpLogger.w(ERROR, exception);
            if (null == clientCardView) return;
            processError(exception);
        }
    }

    private final class SaveFavoriteObserver extends BaseObserver<List<Cliente>> {

        @Override
        public void onNext(List<Cliente> result) {
            if (null == clientCardView) return;
            clientCardView.hideLoading();
            clientCardView.saved(true);
            clienteUseCase.guardar(result, new SaveLocalObserver());
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            BelcorpLogger.w(ERROR, exception);
            if (null == clientCardView) return;
            processError(exception);
        }
    }

    private final class SaveLocalObserver extends BaseObserver<List<Cliente>> {

        @Override
        public void onNext(List<Cliente> result) {
            BelcorpLogger.d(TAG, "Datos actualizados.");
        }
    }

    private final class DeleteAnotacionObserver extends BaseObserver<Boolean> {

        AnotacionModel anotacionModel;

        private DeleteAnotacionObserver(AnotacionModel anotacionModel) {
            this.anotacionModel = anotacionModel;
        }

        @Override
        public void onNext(Boolean result) {
            if (null == clientCardView) return;
            clientCardView.hideLoading();
            clientCardView.anotacionDeleted(anotacionModel);
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            BelcorpLogger.w(ERROR, exception);
            if (null == clientCardView) return;
            processError(exception);
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (null == clientCardView) return;
            clientCardView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserPropertyObserverGestionarDeuda extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (null == clientCardView) return;
            clientCardView.initScreenTrackGestionarDeuda(loginModelDataMapper.transform(user));
        }
    }

    private final class UserPropertyObserverIngresarPedido extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (null == clientCardView) return;
            clientCardView.initScreenTrackIngresarPedido(loginModelDataMapper.transform(user));
        }
    }

    private final class UserPropertyObserverRevisarPedidos extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (null == clientCardView) return;
            clientCardView.initScreenTrackRevisarPedidos(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (null == clientCardView) return;
            clientCardView.trackBackPressed(loginModelDataMapper.transform(user));
        }
    }

    private final class GetCampaingCode extends BaseObserver<User> {

        private int id;
        private int clientLocalId;

        GetCampaingCode(int id, int clientLocalId) {
            this.id = id;
            this.clientLocalId = clientLocalId;
        }

        @Override
        public void onNext(User user) {

            if (null != user) {
                String campaing = user.getCampaing();
                String symbol = user.getCountryMoneySymbol();
                String iso = user.getCountryISO();
                clienteUseCase.get(id, campaing, new GetOnlineObserver(clientLocalId, iso, symbol));
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            BelcorpLogger.w(ERROR, exception);
            if (null == clientCardView) return;
            clientCardView.hideLoading();
            processError(exception);
        }
    }

    private final class GetOnlineObserver extends BaseObserver<List<Cliente>> {

        private final int id;
        private final String iso;
        private final String symbol;

        GetOnlineObserver(int id, String iso, String symbol) {
            this.id = id;
            this.iso = iso;
            this.symbol = symbol;
        }

        @Override
        public void onNext(List<Cliente> saved) {
            if (null == clientCardView) return;
            countryUseCase.find(iso, new CountryObserver(id, iso, symbol));
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            BelcorpLogger.w(ERROR, exception);
            if (null == clientCardView) return;
            processError(exception);
        }
    }

    private final class GetMenuObserver extends BaseObserver<Menu> {

        @Override
        public void onNext(Menu menu) {
            if (null == clientCardView) return;
            clientCardView.setMenuModel(menuModelDataMapper.transform(menu));
        }
    }

}
