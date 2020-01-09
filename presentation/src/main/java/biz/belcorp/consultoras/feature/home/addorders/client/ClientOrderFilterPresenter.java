package biz.belcorp.consultoras.feature.home.addorders.client;

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
public class ClientOrderFilterPresenter implements Presenter<ClientOrderFilterView> {

    private ClientOrderFilterView clientFilterView;

    private final UserUseCase userUseCase;
    private final ClienteUseCase clientUseCase;
    private final LoginModelDataMapper loginModelDataMapper;
    private final ClienteModelDataMapper clientModelDataMapper;

    @Inject
    ClientOrderFilterPresenter(UserUseCase userUseCase,
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
    public void attachView(@NonNull ClientOrderFilterView view) {
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

    void getUserAndClients() {
        clientFilterView.showLoading();
        userUseCase.get(new UserObserver());
    }

    void trackBackPressed() {
        userUseCase.get(new UserBackPressedObserver());
    }

    /**********************************************************/

    private final class UserObserver extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            clientUseCase.getClientes(new ClientsObserver(user));
        }
    }

    private class ClientsObserver extends BaseObserver<Collection<Cliente>> {

        private User user;

        ClientsObserver(User user){
            this.user = user;
        }

        @Override
        public void onNext(Collection<Cliente> clients) {
            if (null == clientFilterView) return;
            clientFilterView.showClients(user, clientModelDataMapper.transform(clients));
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
