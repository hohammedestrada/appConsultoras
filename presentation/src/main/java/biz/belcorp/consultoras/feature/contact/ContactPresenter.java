package biz.belcorp.consultoras.feature.contact;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.inject.Inject;

import biz.belcorp.consultoras.base.Presenter;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.client.ClienteModelDataMapper;
import biz.belcorp.consultoras.common.model.client.ClienteValidator;
import biz.belcorp.consultoras.common.model.client.ContactoModel;
import biz.belcorp.consultoras.di.PerActivity;
import biz.belcorp.consultoras.domain.entity.Cliente;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.exception.VersionException;
import biz.belcorp.consultoras.domain.interactor.BaseObserver;
import biz.belcorp.consultoras.domain.interactor.ClienteUseCase;
import biz.belcorp.consultoras.domain.interactor.UserUseCase;
import biz.belcorp.consultoras.util.anotation.ClientRegisterType;
import biz.belcorp.consultoras.util.anotation.ClientStateType;
import biz.belcorp.consultoras.util.anotation.ContactStateType;
import biz.belcorp.consultoras.util.anotation.ContactType;
import biz.belcorp.consultoras.util.anotation.UserType;
import biz.belcorp.library.net.exception.BadRequestException;
import biz.belcorp.library.util.CountryUtil;
import biz.belcorp.library.util.StringUtil;

@PerActivity
public class ContactPresenter implements Presenter<ContactView> {

    private ContactView contactView;

    private final UserUseCase userUseCase;
    private final ClienteUseCase clienteUseCase;
    private final ClienteModelDataMapper clientModelDataMapper;
    private final LoginModelDataMapper loginModelDataMapper;

    private int contactCount = 0;

    @Inject
    ContactPresenter(UserUseCase userUseCase, ClienteUseCase clienteUseCase, ClienteModelDataMapper clientModelDataMapper, LoginModelDataMapper loginModelDataMapper) {
        this.userUseCase = userUseCase;
        this.clienteUseCase = clienteUseCase;
        this.clientModelDataMapper = clientModelDataMapper;
        this.loginModelDataMapper = loginModelDataMapper;
    }

    @Override
    public void attachView(@NonNull ContactView view) {
        if (view instanceof ContactListFragment) {
            contactView = view;
        }
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
        this.clienteUseCase.dispose();
        this.userUseCase.dispose();
        this.contactView = null;
    }

    /******************************************************/

    public void save(List<ClienteModel> clienteModels, LoginModel loginModel) {
        contactCount = clienteModels.size();
        contactView.showLoading();

        if (loginModel.getUserType() == UserType.CONSULTORA) {
            clienteUseCase.subida((List<Cliente>) clientModelDataMapper.transform(clienteModels), new SaveObserver());
        } else {
            clienteUseCase.guardar((List<Cliente>) clientModelDataMapper.transform(clienteModels), new SaveObserver());
        }
    }

    void readContacts() {
        this.userUseCase.get(new GetUser());
    }

    private void processContactList(String iso, List<ClienteModel> listClients) {
        if (contactView == null) return;

        HashMap<String, ClienteModel> clientMap = null;
        HashMap<String, Void> phoneclientMap = new HashMap<>();

        if (listClients != null) {
            clientMap = new HashMap<>();
            for (ClienteModel client : listClients) {
                String name = client.getNombres();

                for (ContactoModel i : client.getContactoModels()) {
                    if (i.getTipoContactoID() == ContactType.MOBILE || i.getTipoContactoID() == ContactType.PHONE)
                        phoneclientMap.put(i.getValor(), null);
                }

                clientMap.put(name, client);
            }
        }

        HashMap<String, ClienteModel> contactMap = null;
        HashMap<String, Void> phoneMap;

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER};
        String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1";

        Cursor people = contactView.context().getContentResolver().query(uri, projection, selection, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if (people != null) {

            int indexId = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
            int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            if (people.moveToFirst()) {
                contactMap = new HashMap<>();
                phoneMap = new HashMap<>();

                do {
                    String name = people.getString(indexName);
                    int id = people.getInt(indexId);

                    boolean repeatedClient = false;
                    if (clientMap != null) repeatedClient = clientMap.containsKey(name);

                    if (!repeatedClient) {

                        repeatedClient = contactMap.containsKey(name);

                        ClienteModel contact = repeatedClient ? contactMap.get(name) : new ClienteModel(id, name);

                        List<ContactoModel> contactModelList = repeatedClient ? contact.getContactoModels() : new ArrayList<>();

                        String data = people.getString(indexNumber);

                        if (null != data && !data.isEmpty()) {

                            String contactNumber = removePrefix(data.replaceAll("[^0-9+]", ""), iso);

                            if (!"".equals(contactNumber)) {
                                if (!phoneMap.containsKey(contactNumber) && !phoneclientMap.containsKey(contactNumber)) {
                                    ContactoModel model = generateContactModel(contactNumber, iso);
                                    if (model.getTipoContactoID() != ContactType.DEFAULT) {
                                        contactModelList.add(model);
                                        phoneMap.put(contactNumber, null);
                                    } else
                                        continue;
                                } else
                                    continue;
                            }
                        }

                        contact.setContactoModels(contactModelList);
                        contact.setEstado(ClientStateType.INSERT_UPDATE);
                        contact.setTipoRegistro(ClientRegisterType.ALL);

                        contactMap.put(name, contact);
                    }

                } while (people.moveToNext());

                people.close();
            }
        }

        contactView.showContacts((List<ClienteModel>) clientModelDataMapper.transform(contactMap));
    }

    private String removePrefix(String number, String iso) {
        String newNumber = number;

        String[] prefix = CountryUtil.getPhonePrefix().get(iso);
        if (prefix != null)
            for (String aPrefix : prefix) {
                newNumber = StringUtil.removePrefix(number, aPrefix);
            }

        return newNumber;
    }

    private ContactoModel generateContactModel(String contactNumber, String iso) {

        int contactType = ClienteValidator.validateMobilePattern(contactNumber, iso) ?
            ContactType.MOBILE : ClienteValidator.validatePhonePattern(contactNumber, iso) ?
            ContactType.PHONE : ContactType.DEFAULT;

        ContactoModel contactoModel = new ContactoModel();
        contactoModel.setContactoClienteID(0);
        contactoModel.setClienteID(0);
        contactoModel.setTipoContactoID(contactType);
        contactoModel.setValor(contactNumber);
        contactoModel.setEstado(ContactStateType.INSERT_UPDATE);

        return contactoModel;
    }

    public void initScreenTrack() {
        userUseCase.get(new UserPropertyObserver());
    }

    public void trackBackPressed() {
        userUseCase.get(new UserBackPressedObserver());
    }

    public void initScreenTrackAgregar() {
        userUseCase.get(new UserPropertyAgregarObserver());
    }

    /******************************************************/

    private final class GetUser extends BaseObserver<User> {
        @Override
        public void onNext(User user) {
            if (null != user) {
                String iso = user.getCountryISO();
                clienteUseCase.getClientes(new GetAllClientsObserver(iso));
            }
        }
    }

    private final class GetAllClientsObserver extends BaseObserver<Collection<Cliente>> {

        private String iso;

        private GetAllClientsObserver(String iso) {
            this.iso = iso;
        }

        @Override
        public void onNext(Collection<Cliente> clients) {
            if (null != clients)
                processContactList(iso, clientModelDataMapper.transform(clients));
        }

        @Override
        public void onError(Throwable exception) {
            if (contactView == null) return;
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                contactView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else {
                contactView.onError(exception);
            }
        }
    }

    private final class SaveObserver extends BaseObserver<List<Cliente>> {

        @Override
        public void onNext(List<Cliente> result) {
            if (contactView == null) return;

            contactView.hideLoading();
            contactView.saved(true);
        }

        @Override
        public void onError(Throwable exception) {
            if (contactView == null) return;
            contactView.hideLoading();
            if (exception instanceof VersionException) {
                VersionException vE = (VersionException) exception;
                contactView.onVersionError(vE.isRequiredUpdate(), vE.getUrl());
            } else if (exception.getMessage() != null) {
                userUseCase.updateScheduler(new UpdateObserver());
                contactView.onError(new Throwable(String.format(Locale.getDefault(), "Se agregaron %1$d contacto(s) de un total de %2$d. Confirma que la información de contacto sea correcta en tu dispositivo.",
                    contactCount - exception.getMessage().split(Pattern.quote("\n")).length, contactCount)));
            } else {
                contactView.onError(new BadRequestException());
            }
        }
    }

    private final class UserPropertyObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            contactView.initScreenTrack(loginModelDataMapper.transform(user));
        }
    }

    private final class UserPropertyAgregarObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            contactView.initScreenTrackAgregar(loginModelDataMapper.transform(user));
        }
    }

    private final class UserBackPressedObserver extends BaseObserver<User> {

        @Override
        public void onNext(User user) {
            contactView.trackBackPressed(loginModelDataMapper.transform(user));
        }
    }

    private final class UpdateObserver extends BaseObserver<Boolean> { }
}
