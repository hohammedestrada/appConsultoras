package biz.belcorp.consultoras.feature.debt.client;

import android.support.annotation.NonNull;

import java.util.Collection;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.client.ClienteModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Cliente;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.ClienteUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;

/**
 *
 */
@PerActivity
public class ClientFilterPresenter implements Presenter<ClientFilterView> {

    private ClientFilterView clientFilterView;

    private final UserUseCase userUseCase;
    private final ClienteUseCase clientUseCase;
    private final LoginModelDataMapper loginModelDataMapper;
    private final ClienteModelDataMapper clientModelDataMapper;

    @Inject
    ClientFilterPresenter(UserUseCase userUseCase,
                          ClienteUseCase clientUseCase,
                          LoginModelDataMapper loginModelDataMapper,
                          ClienteModelDataMapper clientModelDataMapper) {
        this.userUseCase = userUseCase;
        this.clientUseCase = clientUseCase;
        this.loginModelDataMapper = loginModelDataMapper;
        this.clientModelDataMapper = clientModelDataMapper;
    }

    /**********************************************************/

    @Override
    public void attachView(@NonNull ClientFilterView view) {
        clientFilterView = view;
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
        this.clientUseCase.dispose();
        this.clientFilterView = null;
    }

    /**********************************************************/

    void getClients() {
        clientFilterView.showLoading();
        clientUseCase.bajada("0", new BajadaObserver());
    }

    void getClientsOffline() {
        clientFilterView.showLoading();
        clientUseCase.getClientes(new ClientsObserver());
    }

    void trackBackPressed() {
        userUseCase.get(new UserBackPressedObserver());
    }

    /**********************************************************/

    private class BajadaObserver extends BaseObserver<Collection<Cliente>> {

        @Override
        public void onNext(Collection<Cliente> response) {
            clientUseCase.getClientes(new ClientsObserver());
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == clientFilterView) return;
            clientFilterView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                clientFilterView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            }
        }
    }

    private class ClientsObserver extends BaseObserver<Collection<Cliente>> {

        @Override
        public void onNext(Collection<Cliente> clients) {
            if (null == clientFilterView) return;
            clientFilterView.showClients(clientModelDataMapper.transform(clients));
            clientFilterView.hideLoading();
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (null == clientFilterView) return;
            clientFilterView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                clientFilterView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            }
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (null == clientFilterView) return;
            clientFilterView.trackBackPressed(loginModelDataMapper.transform(user));
        }
    }
}
