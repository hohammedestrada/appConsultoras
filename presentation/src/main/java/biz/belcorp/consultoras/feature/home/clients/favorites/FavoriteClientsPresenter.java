package biz.belcorp.consultoras.feature.home.clients.favorites;

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
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.client.ClienteModelDataMapper;
import biz.belcorp.consultoras.common.model.client.ContactoModel;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Cliente;
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
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.CountryUtil;
import biz.belcorp.library.util.NetworkUtil;

@PerActivity
public class FavoriteClientsPresenter implements Presenter<FavoriteClientsView> {

    private FavoriteClientsView favoritesclientsView;

    private final UserUseCase userUseCase;
    private final CountryUseCase countryUseCase;
    private final ClienteUseCase clienteUseCase;
    private final ClienteModelDataMapper clienteModelDataMapper;

    @Inject
    FavoriteClientsPresenter(ClienteUseCase clienteUseCase,
                             CountryUseCase countryUseCase,
                             ClienteModelDataMapper clienteModelDataMapper,
                             UserUseCase userUseCase) {

        this.clienteUseCase = clienteUseCase;
        this.countryUseCase = countryUseCase;
        this.clienteModelDataMapper = clienteModelDataMapper;
        this.userUseCase = userUseCase;
    }

    @Override
    public void attachView(@NonNull FavoriteClientsView view) {
        favoritesclientsView = view;
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
        this.favoritesclientsView = null;
    }

    /**********************************************************/

    void getClientesFavoritos() {
        this.clienteUseCase.getClientesFavoritos(new GetClientesFavoritosObserver());
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
        } else {
            favoritesclientsView.onErrorComunication(ContactType.MOBILE);
        }
    }

    void eliminar(ClienteModel clienteModel, Fragment fragment) {
        boolean internet = NetworkUtil.isThereInternetConnection(fragment.getContext());

        clienteModel.setEstado(0);
        clienteModel.setSincronizado(internet ? 1 : 0);

        List<Cliente> clientes = new ArrayList<>();
        clientes.add(clienteModelDataMapper.transform(clienteModel));

        if (fragment instanceof FavoriteClientsFragment) {
            favoritesclientsView.showLoading();
            clienteUseCase.eliminar(clientes, new EliminarObserver());
        }
    }

    void favorito(ClienteModel clienteModel, Fragment fragment) {
        boolean internet = NetworkUtil.isThereInternetConnection(fragment.getContext());

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
        clienteModel.setSincronizado(internet ? 1 : 0);
        clientes.add(clienteModelDataMapper.transform(clienteModel));

        if (fragment instanceof FavoriteClientsFragment) {
            favoritesclientsView.showLoading();
            clienteUseCase.subida(clientes, new FavoritoObserver());
        }
    }

    /**********************************************************/

    private final class GetClientesFavoritosObserver extends BaseObserver<Collection<Cliente>> {
        @Override
        public void onNext(Collection<Cliente> clients) {
            if (favoritesclientsView != null) {
                favoritesclientsView.showClients(clienteModelDataMapper.transform(clients));
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            BelcorpLogger.w("GetClientesFavoritosObserver", exception);
            if (favoritesclientsView != null) {
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    favoritesclientsView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    favoritesclientsView.onBusinessError(BusinessErrorFactory.INSTANCE.create(exception));
                }
            }
        }
    }

    private final class EliminarObserver extends BaseObserver<String> {

        @Override
        public void onNext(String mensaje) {
            if (favoritesclientsView != null) {
                favoritesclientsView.onDataUpdated(mensaje);
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (favoritesclientsView != null) {
                favoritesclientsView.hideLoading();
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    favoritesclientsView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else if (exception instanceof SincronizacionClienteYaExistenteException) {
                    favoritesclientsView.onBusinessError(BusinessErrorFactory.INSTANCE.create(exception));
                } else {
                    favoritesclientsView.onError(ErrorFactory.INSTANCE.create(exception));
                }
            }
        }

    }

    private final class FavoritoObserver extends BaseObserver<List<Cliente>> {

        @Override
        public void onNext(List<Cliente> result) {
            if (favoritesclientsView != null) {
                favoritesclientsView.hideLoading();

                if (!result.isEmpty()) {
                    Cliente cliente = result.get(0);
                    if (cliente != null)
                        favoritesclientsView.onDataUpdated(cliente.getMensajeRespuesta());
                }
            }
        }

        @Override
        public void onError(@NonNull Throwable exception) {
            if (favoritesclientsView != null) {
                favoritesclientsView.hideLoading();
                if (exception instanceof VersionException) {
                    VersionException vE = (VersionException) exception;
                    favoritesclientsView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
                } else {
                    favoritesclientsView.onBusinessError(BusinessErrorFactory.INSTANCE.create(exception));
                }
            }
        }
    }

}
