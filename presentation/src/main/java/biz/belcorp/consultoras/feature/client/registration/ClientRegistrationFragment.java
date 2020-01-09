package biz.belcorp.consultoras.feature.client.registration;

import android.Manifest;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.component.DatePickerPopupWindow;
import biz.belcorp.consultoras.common.dialog.CustomDialog;
import biz.belcorp.consultoras.common.dialog.MessageDialog;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.AnotacionModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.client.ClienteValidator;
import biz.belcorp.consultoras.common.model.client.ContactoModel;
import biz.belcorp.consultoras.common.recordatory.BirthdayJobService;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.feature.client.di.ClientComponent;
import biz.belcorp.consultoras.feature.client.note.NoteActivity;
import biz.belcorp.consultoras.feature.client.note.NotesAdapter;
import biz.belcorp.consultoras.feature.client.note.NotesView;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.CommunicationUtils;
import biz.belcorp.consultoras.util.anotation.ClientRegisterType;
import biz.belcorp.consultoras.util.anotation.ClientStateType;
import biz.belcorp.consultoras.util.anotation.ContactStateType;
import biz.belcorp.consultoras.util.anotation.ContactType;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.StatusType;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.DateUtil;
import biz.belcorp.library.util.StringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ClientRegistrationFragment extends BaseFragment implements ClientRegistrationView, NotesAdapter.OnItemSelectedListener {

    private static final String TAG = "ClientRegistrationFragment";

    @Inject
    ClientRegistrationPresenter presenter;

    @BindView(R.id.llt_personal_data)
    LinearLayout lltPersonalData;
    @BindView(R.id.llt_content_personal_data)
    LinearLayout lltContentPersonalData;
    @BindView(R.id.tvw_personal_data)
    TextView tvwPersonalData;
    @BindView(R.id.llt_annotations)
    LinearLayout lltAnnotations;
    @BindView(R.id.llt_content_annotations)
    LinearLayout lltContentAnnotations;
    @BindView(R.id.tvw_annotations)
    TextView tvwAnnotations;
    @BindView(R.id.ted_name)
    EditText tedName;
    @BindView(R.id.ted_last)
    TextInputEditText tedLastName;
    @BindView(R.id.ted_mobile)
    TextInputEditText tedMobile;
    @BindView(R.id.ted_phone)
    TextInputEditText tedPhone;
    @BindView(R.id.ted_email)
    TextInputEditText tedEmail;
    @BindView(R.id.ted_birthday)
    TextInputEditText tedBirthday;
    @BindView(R.id.ted_address)
    TextInputEditText tedAddress;
    @BindView(R.id.ted_reference)
    TextInputEditText tedReference;
    @BindView(R.id.ivw_arrow_data)
    ImageView ivwArrowData;
    @BindView(R.id.ivw_arrow_annotations)
    ImageView ivwArrowAnnotations;
    @BindView(R.id.chk_favorite)
    ImageView chkFavorite;
    @BindView(R.id.tvw_note_label)
    TextView tvwNoteLabel;
    @BindView(R.id.chk_mobile_favorite)
    ImageView chkMobileFavorite;
    @BindView(R.id.chk_phone_favorite)
    ImageView chkPhoneFavorite;
    @BindView(R.id.tvw_error_mobile)
    TextView tvwErrorMobile;
    @BindView(R.id.tvw_error_phone)
    TextView tvwErrorPhone;
    @BindView(R.id.tvw_error_name)
    TextView tvwErrorName;
    @BindView(R.id.tvw_error_email)
    TextView tvwErrorEmail;
    @BindView(R.id.llt_nueva_nota)
    LinearLayout lltNuevaNota;
    @BindView(R.id.vw_notes)
    NotesView notesView;

    List<AnotacionModel> anotacionModels;
    LoginModel loginModel;

    CustomDialog customDialog;
    DatePickerPopupWindow datePicker;
    String birthday = "";
    @BindView(R.id.rlt_container_correo)
    RelativeLayout rltContainerCorreo;
    @BindView(R.id.llt_direccion_container)
    LinearLayout lltDireccionContainer;
    @BindView(R.id.llt_agregar_correo)
    LinearLayout lltAgregarCorreo;
    @BindView(R.id.llt_agregar_direccion)
    LinearLayout lltAgregarDireccion;

    private ClientRegistrationFragmentListener listener;
    private Unbinder bind;

    private Drawable starOn;
    private Drawable starOff;

    private ClienteModel model = new ClienteModel();

    /**********************************************************/

    interface ClientRegistrationFragmentListener {

        void onRegister(boolean result, ClienteModel client);

        void onHome();
    }

    /**********************************************************/

    public ClientRegistrationFragment() {
        super();
    }

    /**********************************************************/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ClientRegistrationFragmentListener) {
            this.listener = (ClientRegistrationFragmentListener) context;
        }
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() {
        getComponent(ClientComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);

        starOn = ContextCompat.getDrawable(getContext(), R.drawable.ic_star_filled);
        starOff = ContextCompat.getDrawable(getContext(), R.drawable.ic_star);

        initControls();
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_registration, container, false);
        bind = ButterKnife.bind(this, view);
        rltContainerCorreo.setVisibility(View.GONE);
        lltDireccionContainer.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.initScreenTrack();
            presenter.getAnotations(0);
        }
    }

    /**********************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {
        this.loginModel = loginModel;

        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_CLIENT_REGISTRATION);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);
        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    @Override
    public void initScreenTrackAgregarGuardar(LoginModel loginModel) {
        Tracker.Clientes.trackAgregarGuardar(loginModel);
    }

    /**********************************************************/

    @Override
    public Context context() {
        FragmentActivity activity = getActivity();
        if (activity != null)
            return activity.getApplicationContext();
        else
            return null;
    }

    /**********************************************************/

    private void initControls() {

        Drawable icCalendar = ContextCompat.getDrawable(getContext(), R.drawable.ic_calendar);
        Bitmap bitmap = ((BitmapDrawable) icCalendar).getBitmap();
        Drawable newIcCalendar = new BitmapDrawable(getResources(),
            Bitmap.createScaledBitmap(bitmap, getResources().getDimensionPixelSize(R.dimen.size_icon), getResources().getDimensionPixelSize(R.dimen.size_icon), true)
        );

        tedBirthday.setCompoundDrawablesWithIntrinsicBounds(
            newIcCalendar, null, null, null
        );

        datePicker = new DatePickerPopupWindow.Builder(context(), (year, month, day, dateDesc) -> {

            birthday = dateDesc;
            try {
                String birthdayFormat = StringUtil.capitalize(DateUtil.convertFechaToString(
                    DateUtil.convertirDDMMAAAAtoDate(dateDesc), "dd MMM"));
                tedBirthday.setText(birthdayFormat);
            } catch (ParseException e) {
                Log.e("ClientRegistrationFrag", "ClientList", e);
            }

        }).setTypeDatePicker(2)
            .colorCancel(ContextCompat.getColor(context(), R.color.datepicker_text))
            .colorConfirm(ContextCompat.getColor(context(), R.color.datepicker_text))
            .viewTextSize(20)
            .showMonthOrdinal(false)
            .showYear(false)
            .build();

        chkFavorite.setImageDrawable(starOff);
        chkMobileFavorite.setImageDrawable(starOff);
        chkPhoneFavorite.setImageDrawable(starOff);

        chkFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkFavorite.setImageDrawable(chkFavorite.getDrawable().equals(starOff) ? starOn : starOff);
            }
        });
        chkMobileFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkMobileFavorite.getDrawable().equals(starOff) && !tedMobile.getText().toString().equals("")) {
                    chkMobileFavorite.setImageDrawable(starOn);
                    chkPhoneFavorite.setImageDrawable(starOff);
                } else {
                    chkMobileFavorite.setImageDrawable(starOff);
                }
            }
        });
        chkPhoneFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkPhoneFavorite.getDrawable().equals(starOff) && !tedPhone.getText().toString().equals("")) {
                    chkMobileFavorite.setImageDrawable(starOff);
                    chkPhoneFavorite.setImageDrawable(starOn);
                } else {
                    chkPhoneFavorite.setImageDrawable(starOff);
                }
            }
        });
        notesView.setListener(this);
    }

    @Override
    public void saved(ClienteModel client, Boolean result) {

        if (result) {
            initJob(client);

            if (listener != null) {
                listener.onRegister(true, client);
            }
        } else {
            try {
                customDialog = new CustomDialog(getContext());
                customDialog.setIconDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_update_contact));
                customDialog.setTitle("Error al crear Contacto");
                customDialog.setMessage("Los datos de contacto no fueron actualizados.");
                customDialog.setCloseListener(v -> {
                    if (customDialog.isShowing()) customDialog.dismiss();
                    listener.onRegister(false, client);
                });
                customDialog.setCancelable(false);
                customDialog.show();
            } catch (IllegalStateException e) {
                BelcorpLogger.w("saved", e);
            }
        }
    }

    private void initJob(ClienteModel client) {
        DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        if (client != null && client.getFechaNacimiento() != null) {
            String fechaNacimiento = client.getFechaNacimiento();
            try {
                date = simpleDateFormat.parse(fechaNacimiento);
            } catch (ParseException e) {
                BelcorpLogger.w(TAG, "ParseException", e);
            }

            if (date != null) {
                Calendar now = Calendar.getInstance();
                Calendar calendarAlarm = Calendar.getInstance();
                calendarAlarm.setTime(date);
                calendarAlarm.set(Calendar.YEAR, DateUtil.getCurrentYear());
                calendarAlarm.set(Calendar.HOUR_OF_DAY, 8);
                calendarAlarm.set(Calendar.MINUTE, 0);

                long diff = calendarAlarm.getTimeInMillis() - now.getTimeInMillis();
                int startSeconds = (int) (diff / 1000) < 0 ? 0 : (int) (diff / 1000);
                int endSeconds = startSeconds + 10;
                int dayDiff = calculateDaysLeft(calendarAlarm);

                if (dayDiff >= 0) {
                    String alias = client.getAlias();
                    if (alias == null || alias.isEmpty()) {
                        alias = client.getNombres() + (TextUtils.isEmpty(client.getApellidos()) ? "" : " " + client.getApellidos());
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString(GlobalConstant.CLIENT_MESSAGE_RECORDATORY, String.format(getString(R.string.debt_birthday_alarm), alias));
                    bundle.putInt(GlobalConstant.CLIENTE_LOCAL_ID, client.getId());

                    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getActivity()));
                    Job.Builder builder = dispatcher.newJobBuilder();
                    builder.setService(BirthdayJobService.class);
                    builder.setTag(getString(R.string.reminder_birthday_key) + client.getClienteID());
                    builder.setRecurring(false);
                    builder.setReplaceCurrent(true);
                    builder.setLifetime(Lifetime.FOREVER);
                    builder.setTrigger(Trigger.executionWindow(startSeconds, endSeconds));
                    builder.setExtras(bundle);
                    dispatcher.schedule(builder.build());
                }
            }
        }
    }

    private int calculateDaysLeft(Calendar reminderCalender) {
        Calendar actualDate = Calendar.getInstance();

        long diff = reminderCalender.getTimeInMillis() - actualDate.getTimeInMillis();
        long days = diff / (24 * 60 * 60 * 1000);

        return (int) days;
    }

    @Override
    public void onError(Throwable exception) {
        processError(exception);
    }


    @Override
    public void onAnotacionItemSelected(AnotacionModel anotacionModel, int position) {
        Intent intent = new Intent(getContext(), NoteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(NoteActivity.ACCION, NoteActivity.NUEVO_CLIENTE);
        intent.putExtra(NoteActivity.NOTA_ID, anotacionModel.getId());
        intent.putExtra(NoteActivity.CLIENT_ID, 0);
        intent.putExtra(NoteActivity.CLIENT_LOCAL_ID, 0);
        startActivityForResult(intent, NoteActivity.REQUEST_CODE_EDIT);
    }

    @Override
    public void onEliminarSelected(AnotacionModel anotacionModel, int adapterPosition) {
        anotacionModel.setEstado(StatusType.DELETE);
        presenter.deleteAnotacion(anotacionModel);
    }

    @Override
    public void anotacionDeleted(AnotacionModel anotacionModel) {
        notesView.removeNote(anotacionModel);
        presenter.getAnotations(0);
    }

    @Override
    public void showMaximumNoteAmount(int maxNoteAmount) {

        if (isVisible())
            tvwNoteLabel.setText(String.format(getString(R.string.client_card_annotations_message), maxNoteAmount));
    }

    @Override
    public void showAnotations(List<AnotacionModel> list, int maxNoteAmount) {

        if (null != list) {
            anotacionModels = new ArrayList<>();
            anotacionModels.addAll(list);
        }

        lltNuevaNota.setVisibility((null != list && list.size() >= maxNoteAmount) ? View.GONE : View.VISIBLE);
        notesView.setNestedScrollingEnabled(false);
        notesView.refreshNotes(list);
        notesView.setListener(this);
    }

    @Override
    public void recordCanceled() {
        if (null != listener)
            listener.onHome();
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) context().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        if (view == null) view = new View(getActivity());
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**********************************************************/

    @OnClick(R.id.llt_nueva_nota)
    public void newAnnotation() {
        Intent intent = new Intent(getContext(), NoteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(NoteActivity.ACCION, NoteActivity.NUEVO_CLIENTE);
        intent.putExtra(NoteActivity.NOTA_ID, 0);
        intent.putExtra(NoteActivity.CLIENT_ID, 0);
        intent.putExtra(NoteActivity.CLIENT_LOCAL_ID, 0);
        startActivityForResult(intent, NoteActivity.REQUEST_CODE);
    }


    @OnClick(R.id.llt_toolbar_option_1)
    public void onCancel() {
        hideKeyboard();
        if ((anotacionModels == null || anotacionModels.isEmpty()) && null != listener)
            listener.onHome();
        else {

            try {
                new MessageDialog()
                    .setIcon(R.drawable.ic_alerta, 0)
                    .setStringTitle(R.string.client_dg_cancel_register_title)
                    .setStringMessage(R.string.client_dg_cancel_register_message)
                    .setStringAceptar(R.string.button_aceptar)
                    .setStringCancelar(R.string.button_cancelar)
                    .showIcon(true)
                    .showClose(true)
                    .showCancel(true)
                    .setListener(cancelListener)
                    .show(getFragmentManager(), "modalCancel");
            } catch (IllegalStateException e) {
                BelcorpLogger.w("modalCancel", e);
            }
        }
    }

    @OnClick(R.id.llt_toolbar_option_2)
    public void onSave() {
        ClientRegistrationFragmentPermissionsDispatcher.saveContactWithPermissionCheck(this);
    }

    @NeedsPermission(Manifest.permission.WRITE_CONTACTS)
    public void saveContact(){
        disableAllErrors();
        hideKeyboard();

        String name = tedName.getText().toString().trim();
        String mobile = tedMobile.getText().toString().trim();
        String phone = tedPhone.getText().toString().trim();
        String email = tedEmail.getText().toString().trim();
        String address = tedAddress.getText().toString().trim();
        String reference = tedReference.getText().toString().trim();

        String lastName = tedLastName.getText().toString().trim();

        if ("".equals(name)) {
            String message = getString(R.string.client_registration_validation_0);
            showNameError(message);
            return;
        }

        if ("".equals(mobile) && "".equals(phone)) {
            String message = getString(R.string.client_registration_validation_1);
            showMobileError(message);
            showPhoneError(message);
            Toast.makeText(getActivity(), getString(R.string.client_registration_validation_5), Toast.LENGTH_LONG).show();
            return;
        }

        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        int rawContactID = ops.size();

        model.setClienteID(0);
        model.setId(0);
        model.setAnotacionModels(notesView.getNotes());
        model.setOrigen(GlobalConstant.APP_CODE);
        model.setNombres(name);
        model.setFechaNacimiento(birthday);
        model.setFavorito(chkFavorite.getDrawable().equals(starOn) ? 1 : 0);
        model.setEstado(ClientStateType.INSERT_UPDATE);
        model.setTipoRegistro(ClientRegisterType.ALL);

        model.setApellidos(lastName);

        if (chkMobileFavorite.getDrawable().equals(starOn)) {
            model.setTipoContactoFavorito(ContactType.MOBILE);
        } else {
            model.setTipoContactoFavorito(chkPhoneFavorite.getDrawable().equals(starOn) ? ContactType.PHONE : ContactType.DEFAULT);
        }

        // Adding insert operation to operations list
        // to insert a new raw contact in the table ContactsContract.RawContacts
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
            .build());

        // Adding insert operation to operations list
        // to insert display name in the table ContactsContract.Data
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
            .build());

        List<ContactoModel> contactoModelList = new ArrayList<>();

        ContactoModel contactoModel;

        if (!"".equals(mobile)) {
            contactoModel = new ContactoModel();
            contactoModel.setContactoClienteID(0);
            contactoModel.setClienteID(0);
            contactoModel.setTipoContactoID(ContactType.MOBILE);
            contactoModel.setValor(mobile);
            contactoModel.setEstado(ContactStateType.INSERT_UPDATE);
            contactoModelList.add(contactoModel);

            // Adding insert operation to operations list
            // to insert Mobile Number in the table ContactsContract.Data
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobile)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());
        }

        if (!"".equals(phone)) {
            contactoModel = new ContactoModel();
            contactoModel.setContactoClienteID(0);
            contactoModel.setClienteID(0);
            contactoModel.setTipoContactoID(ContactType.PHONE);
            contactoModel.setValor(phone);
            contactoModel.setEstado(ContactStateType.INSERT_UPDATE);
            contactoModelList.add(contactoModel);

            // Adding insert operation to operations list
            // to  insert Home Phone Number in the table ContactsContract.Data
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                .build());
        }

        if (!"".equals(email)) {

            if (!email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {

                String message = getString(R.string.client_registration_validation_2);
                showEmailError(message);
                return;
            }

            contactoModel = new ContactoModel();
            contactoModel.setContactoClienteID(0);
            contactoModel.setClienteID(0);
            contactoModel.setTipoContactoID(ContactType.EMAIL);
            contactoModel.setValor(email);
            contactoModel.setEstado(ContactStateType.INSERT_UPDATE);
            contactoModelList.add(contactoModel);

            // Adding insert operation to operations list
            // to insert Home Email in the table ContactsContract.Data
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, email)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                .build());
        }

        if (!"".equals(address)) {
            contactoModel = new ContactoModel();
            contactoModel.setContactoClienteID(0);
            contactoModel.setClienteID(0);
            contactoModel.setTipoContactoID(ContactType.ADDRESS);
            contactoModel.setValor(address);
            contactoModel.setEstado(ContactStateType.INSERT_UPDATE);
            contactoModelList.add(contactoModel);
        }

        if (!"".equals(reference)) {
            contactoModel = new ContactoModel();
            contactoModel.setContactoClienteID(0);
            contactoModel.setClienteID(0);
            contactoModel.setTipoContactoID(ContactType.REFERENCE);
            contactoModel.setValor(reference);
            contactoModel.setEstado(ContactStateType.INSERT_UPDATE);
            contactoModelList.add(contactoModel);
        }

        addContact(ops);

        model.setContactoModels(contactoModelList);
        presenter.getCountryForRegister(model);
    }

    @OnShowRationale(Manifest.permission.WRITE_CONTACTS)
    void showRationaleForWriteContacts(final PermissionRequest request) {
        new AlertDialog.Builder(getContext())
            .setMessage(R.string.permission_write_contacts_rationale)
            .setPositiveButton(R.string.button_aceptar, (dialog, button) -> request.proceed())
            .setNegativeButton(R.string.button_cancelar, (dialog, button) -> request.cancel())
            .show();
    }

    @OnPermissionDenied(Manifest.permission.WRITE_CONTACTS)
    void showDeniedForWriteContacts() {
        Toast.makeText(getContext(), R.string.permission_write_contacts_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_CONTACTS)
    void showNeverAskForWriteContacts() {
        Toast.makeText(getContext(), R.string.permission_write_contacts_neverask, Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(getContext())
            .setMessage(R.string.permission_write_contacts_denied)
            .setPositiveButton(R.string.button_go_to_settings, (dialog, button) -> CommunicationUtils.goToSettings(context()))
            .setNegativeButton(R.string.button_cancelar, (dialog, button) -> dialog.dismiss())
            .show();

    }

    @Override
    public void onCountryObtained(ClienteModel clienteModel, String country) {
        ContactoModel mobile = clienteModel.getContactoModelMap().get(ContactType.MOBILE);
        ContactoModel phone = clienteModel.getContactoModelMap().get(ContactType.PHONE);

        if ((mobile != null && !TextUtils.isEmpty(mobile.getValor())) ||
            (phone != null && !TextUtils.isEmpty(phone.getValor()))) {
            presenter.save(clienteModel, loginModel);
            presenter.initScreenTrackAgregarGuardar();
        } else {
            hideLoading();
            ContactoModel mobileModel = clienteModel.getContactoModelMap().get(ContactType.MOBILE);
            if (mobileModel != null && mobileModel.getValor() != null &&
                !ClienteValidator.validateMobilePattern(mobileModel.getValor(), country)) {
                String message = getString(R.string.client_registration_validation_3);
                showMobileError(message);
            }

            ContactoModel phoneModel = clienteModel.getContactoModelMap().get(ContactType.PHONE);
            if (phoneModel != null && phoneModel.getValor() != null &&
                !ClienteValidator.validatePhonePattern(phoneModel.getValor(), country)) {
                String message = getString(R.string.client_registration_validation_4);
                showPhoneError(message);
            }
        }
    }

    @OnClick(R.id.tvw_delete_contact)
    public void onDelete() {
        hideKeyboard();
        if (null != listener)
            listener.onHome();
    }

    @OnClick(R.id.rlt_annotations)
    public void showHideAnnotations() {

        if (lltContentAnnotations.getVisibility() == View.VISIBLE) {
            lltContentAnnotations.setVisibility(View.GONE);
            tvwAnnotations.setTextColor(ContextCompat.getColor(context(), R.color.card_view_text_title_inactive));
            lltAnnotations.setBackground(ContextCompat.getDrawable(context(), R.drawable.shape_box_card_view_inactive));
            ivwArrowAnnotations.setImageDrawable(VectorDrawableCompat.create(getContext().getResources()
                , R.drawable.ic_arrow_down_black, null));
        } else {
            lltContentAnnotations.setVisibility(View.VISIBLE);
            tvwAnnotations.setTextColor(ContextCompat.getColor(context(), R.color.card_view_text_title_active));
            lltAnnotations.setBackground(ContextCompat.getDrawable(context(), R.drawable.shape_box_card_view));
            ivwArrowAnnotations.setImageDrawable(VectorDrawableCompat.create(getContext().getResources()
                , R.drawable.ic_arrow_up_black, null));
        }

    }

    @OnClick(R.id.rlt_personal_data)
    public void showHidePersonalData() {

        if (lltContentPersonalData.getVisibility() == View.VISIBLE) {
            lltContentPersonalData.setVisibility(View.GONE);
            tvwPersonalData.setTextColor(ContextCompat.getColor(context(), R.color.card_view_text_title_inactive));
            lltPersonalData.setBackground(ContextCompat.getDrawable(context(), R.drawable.shape_box_card_view_inactive));
            ivwArrowData.setImageDrawable(VectorDrawableCompat.create(getContext().getResources()
                , R.drawable.ic_arrow_down_black, null));
        } else {
            lltContentPersonalData.setVisibility(View.VISIBLE);
            tvwPersonalData.setTextColor(ContextCompat.getColor(context(), R.color.card_view_text_title_active));
            lltPersonalData.setBackground(ContextCompat.getDrawable(context(), R.drawable.shape_box_card_view));
            ivwArrowData.setImageDrawable(VectorDrawableCompat.create(getContext().getResources()
                , R.drawable.ic_arrow_up_black, null));
        }

    }

    @OnClick(R.id.ted_birthday)
    public void selectDate() {
        hideKeyboard();
        datePicker.showPopWin(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (bind != null)
            bind.unbind();
        if (presenter != null)
            presenter.destroy();
    }

    @OnClick(R.id.llt_agregar_correo)
    public void onLltAgregarCorreoClicked() {
        rltContainerCorreo.setVisibility(View.VISIBLE);
        lltAgregarCorreo.setVisibility(View.GONE);
    }

    @OnClick(R.id.llt_agregar_direccion)
    public void onLltAgregarDireccionClicked() {
        lltDireccionContainer.setVisibility(View.VISIBLE);
        lltAgregarDireccion.setVisibility(View.GONE);
    }

    public boolean onBackFragment() {
        hideKeyboard();
        if ((anotacionModels == null || anotacionModels.isEmpty()) && null != listener) {
            listener.onHome();
            return true;
        } else {
            try {
                new MessageDialog()
                    .setIcon(R.drawable.ic_alerta, 0)
                    .setStringTitle(R.string.client_dg_cancel_register_title)
                    .setStringMessage(R.string.client_dg_cancel_register_message)
                    .setStringAceptar(R.string.button_aceptar)
                    .setStringCancelar(R.string.button_cancelar)
                    .showIcon(true)
                    .showClose(true)
                    .showCancel(true)
                    .setListener(cancelListener)
                    .show(getFragmentManager(), "modalCancel");
            } catch (IllegalStateException e) {
                BelcorpLogger.w("modalCancel", e);
            }
            return false;
        }
    }

    /**********************************************************************************************/

    private void showNameError(String message) {
        tvwErrorName.setText(message);
        tvwErrorName.setVisibility(View.VISIBLE);
        tedName.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edt_bg_third_error_selector));
    }

    private void showMobileError(String message) {
        tvwErrorMobile.setText(message);
        tvwErrorMobile.setVisibility(View.VISIBLE);
        tedMobile.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edt_bg_white_error_selector));
    }

    private void showPhoneError(String message) {
        tvwErrorPhone.setText(message);
        tvwErrorPhone.setVisibility(View.VISIBLE);
        tedPhone.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edt_bg_white_error_selector));
    }

    private void showEmailError(String message) {
        tvwErrorEmail.setText(message);
        tvwErrorEmail.setVisibility(View.VISIBLE);
        tedEmail.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edt_bg_white_error_selector));
    }

    public void disableAllErrors() {
        tvwErrorName.setVisibility(View.GONE);
        tvwErrorMobile.setVisibility(View.GONE);
        tvwErrorPhone.setVisibility(View.GONE);
        tvwErrorEmail.setVisibility(View.GONE);

        tedName.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edt_bg_third_selector));
        tedMobile.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edt_bg_white_selector));
        tedPhone.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edt_bg_white_selector));
        tedEmail.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edt_bg_white_selector));
    }

    /*************************************************/

    public void addContact(ArrayList<ContentProviderOperation> ops) {
        try {
            context().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException | OperationApplicationException e) {
            BelcorpLogger.w("addContact", e);
        }
    }

    /**********************************************************/

    final MessageDialog.MessageDialogListener cancelListener = new MessageDialog.MessageDialogListener() {
        @Override
        public void aceptar() {
            presenter.deleteAnotations();
        }

        @Override
        public void cancelar() {
            // EMPTY
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ClientRegistrationFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
