package biz.belcorp.consultoras.feature.payment;

import android.support.annotation.NonNull;

import java.util.regex.Pattern;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.client.ClienteModelDataMapper;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Cliente;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.ClienteUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;

@PerActivity
public class SendPaymentPresenter implements Presenter<SendPaymentView> {

    private SendPaymentView sendPaymentView;

    private final UserUseCase userUseCase;
    private final ClienteUseCase clientUseCase;
    private final ClienteModelDataMapper clientModelDataMapper;
    private final LoginModelDataMapper loginModelDataMapper;

    @Inject
     SendPaymentPresenter(UserUseCase userUseCase,
                                ClienteUseCase clientUseCase,
                                ClienteModelDataMapper clientModelDataMapper,
                                LoginModelDataMapper loginModelDataMapper) {
        this.userUseCase = userUseCase;
        this.clientUseCase = clientUseCase;
        this.clientModelDataMapper = clientModelDataMapper;
        this.loginModelDataMapper = loginModelDataMapper;
    }

    @Override
    public void attachView(@NonNull SendPaymentView view) {
        sendPaymentView = view;
    }

    @Override
    public void resume() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void pause() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void destroy() {
        this.clientUseCase.dispose();
        this.userUseCase.dispose();
        this.sendPaymentView = null;
    }

    /**********************************************************/

    public void load(int clientLocalId) {
        userUseCase.get(new GetUser());
        clientUseCase.findClienteById(clientLocalId, new GetClientObserver());
    }

    void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    void initScreenTrackEnviarMensajePago() {
        userUseCase.get(new UserPropertyEnviarMensajePagoObserver());
    }

    void trackBackPressed() {
        userUseCase.get(new UserBackPressedObserver());
    }

    /**********************************************************/

    private final class GetUser extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            if (null != sendPaymentView && null != user) {
                String name = user.getConsultantName();
                String[] parts = name.split(Pattern.quote(" "));
                sendPaymentView.showMessage(parts[0]);
            }
        }
    }

    private final class GetClientObserver extends BaseObserver<Cliente> {
        @Override
        public void onNext(Cliente client) {
            if (null != sendPaymentView)
                sendPaymentView.setData(clientModelDataMapper.transform(client));
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            sendPaymentView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserPropertyEnviarMensajePagoObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            sendPaymentView.initScreenTrackEnviarMensajePago(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            sendPaymentView.trackBackPressed(loginModelDataMapper.transform(user));
        }
    }
}
