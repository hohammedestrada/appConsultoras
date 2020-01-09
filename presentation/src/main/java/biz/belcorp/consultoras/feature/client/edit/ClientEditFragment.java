package biz.belcorp.consultoras.feature.client.edit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import biz.belcorp.consultoras.feature.client.di.ClientComponent;
import biz.belcorp.consultoras.feature.client.note.NoteActivity;
import biz.belcorp.consultoras.feature.client.note.NotesAdapter;
import biz.belcorp.consultoras.feature.client.note.NotesView;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.anotation.ClientRegisterType;
import biz.belcorp.consultoras.util.anotation.ClientStateType;
import biz.belcorp.consultoras.util.anotation.ContactStateType;
import biz.belcorp.consultoras.util.anotation.ContactType;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.StatusType;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.DateUtil;
import biz.belcorp.library.util.KeyboardUtil;
import biz.belcorp.library.util.StringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *
 */

public class ClientEditFragment extends BaseFragment implements ClientEditView,
    NotesAdapter.OnItemSelectedListener {

    private static final String TAG = "ClientEditFragment";

    @Inject
    ClientEditPresenter presenter;

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

    @BindView(R.id.chk_favorite)
    ImageView chkFavorite;

    @BindView(R.id.ted_name)
    TextInputEditText tedName;

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

    @BindView(R.id.ted_last)
    TextInputEditText tedLastName;

    @BindView(R.id.ivw_arrow_data)
    ImageView ivwArrowData;

    @BindView(R.id.ivw_arrow_annotations)
    ImageView ivwArrowAnnotations;

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

    @BindView(R.id.vw_notes)
    NotesView notesView;

    @BindView(R.id.llt_nueva_nota)
    LinearLayout lltNuevaNota;

    @BindView(R.id.tvw_note_label)
    TextView tvwNoteLabel;
    @BindView(R.id.rlt_container_correo)
    RelativeLayout rltContainerCorreo;
    @BindView(R.id.llt_direccion_container)
    LinearLayout lltDireccionContainer;
    @BindView(R.id.llt_agregar_correo)
    LinearLayout lltAgregarCorreo;
    @BindView(R.id.llt_agregar_direccion)
    LinearLayout lltAgregarDireccion;

    private ClientEditFragmentListener listener;
    private Unbinder bind;

    private LoginModel loginModel;
    private ClienteModel client;
    private Map<Integer, ContactoModel> mapContactClient;
    private String birthday = "";
    private int maxNoteAmount;

    private DatePickerPopupWindow datePicker;
    CustomDialog customDialog;

    private View.OnClickListener onDialogCustomClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (customDialog.isShowing() && listener != null) {
                customDialog.dismiss();
                listener.onBack();
            }
        }
    };

    private Drawable starOn;
    private Drawable starOff;

    private int clientID = -1;

    /**********************************************************/

    interface ClientEditFragmentListener {

        void onBack();

        void onChange(int result);
    }

    /**********************************************************/

    public ClientEditFragment() {
        super();
    }

    /**********************************************************/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ClientEditFragmentListener) {
            this.listener = (ClientEditFragmentListener) context;
        }
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() throws IllegalStateException {
        getComponent(ClientComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);

        if (getArguments() != null)
            clientID = getArguments().getInt(GlobalConstant.CLIENTE_ID, -1);

        initControls();
        presenter.loadDataEdit(clientID);
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_edit, container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.initScreenTrack();
            presenter.loadNotas(clientID);
        }
    }

    @Override
    public void onDestroyView() {
        if (bind != null) bind.unbind();

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.destroy();
    }

    /**********************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {
        this.loginModel = loginModel;

        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_CLIENT_EDIT);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);
        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
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

    private void initJob() {
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
    public void saved(Boolean result) {

        if (result) {
            initJob();

            if (listener != null) {
                listener.onChange(1);
            }
        } else {
            hideLoading();

            try {
                customDialog = new CustomDialog(getContext());
                customDialog.setIconDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_update_contact));
                customDialog.setTitle("Error en la actualización");
                customDialog.setMessage("Los datos de contacto no fueron actualizados.");
                customDialog.setCloseListener(onDialogCustomClickListener);
                customDialog.setCancelable(false);
                customDialog.show();
            } catch (IllegalStateException e) {
                BelcorpLogger.w("saved", e);
            }
        }
    }

    @Override
    public void deleted(Boolean result) {

        if (result) {
            if (listener != null) {
                listener.onChange(2);
            }
        } else {
            hideLoading();

            try {
                customDialog = new CustomDialog(getContext());
                customDialog.setIconDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_update_contact));
                customDialog.setTitle("Error en la eliminación");
                customDialog.setMessage("El cliente no pude ser eliminado.");
                customDialog.setCloseListener(onDialogCustomClickListener);
                customDialog.setCancelable(false);
                customDialog.show();
            } catch (IllegalStateException e) {
                BelcorpLogger.w("deleted", e);
            }
        }
    }

    @Override
    public void showMaximumNoteAmount(int maxNoteAmount) {
        if (isVisible()) {
            this.maxNoteAmount = maxNoteAmount;
            tvwNoteLabel.setText(String.format(getString(R.string.client_card_annotations_message), maxNoteAmount));
        }
    }

    @Override
    @SuppressLint("UseSparseArrays")
    public void showClient(ClienteModel model, String iso, String moneySymbol) {
        client = model;
        if (null == model) return;

        lltAgregarCorreo.setVisibility(View.GONE);
        lltAgregarDireccion.setVisibility(View.GONE);

        chkFavorite.setImageDrawable(model.getFavorito() == 1 ? starOn : starOff);
        tedName.setText(model.getNombres());
        tedName.setSelection(model.getNombres().length());
        tedLastName.setText(model.getApellidos());
        tedBirthday.setText(model.getFechaNacimiento());

        if (!TextUtils.isEmpty(model.getFechaNacimiento())) {
            birthday = model.getFechaNacimiento();
            datePicker.setSelectedDateddMMyyyy(birthday);

            try {
                String birthdayFormat = StringUtil.capitalize(DateUtil.convertFechaToString(DateUtil.convertirDDMMAAAAtoDate(birthday), "dd MMM"));
                tedBirthday.setText(birthdayFormat);
            } catch (ParseException e) {
                Log.e("ClienEditFrag", "ShowClient", e);
            }
        }

        Integer favContactType = model.getTipoContactoFavorito();

        if (null != favContactType) {
            switch (favContactType) {
                case ContactType.MOBILE:
                    chkMobileFavorite.setImageDrawable(starOn);
                    chkPhoneFavorite.setImageDrawable(starOff);
                    break;
                case ContactType.PHONE:
                    chkPhoneFavorite.setImageDrawable(starOn);
                    chkMobileFavorite.setImageDrawable(starOff);
                    break;
                case ContactType.DEFAULT:
                default:
                    chkMobileFavorite.setImageDrawable(starOff);
                    chkPhoneFavorite.setImageDrawable(starOff);
            }
        }

        mapContactClient = new HashMap<>();
        for (ContactoModel i : model.getContactoModels())
            mapContactClient.put(i.getTipoContactoID(), i);

        if (mapContactClient.get(ContactType.MOBILE) != null)
            tedMobile.setText(mapContactClient.get(ContactType.MOBILE).getValor());

        if (mapContactClient.get(ContactType.PHONE) != null)
            tedPhone.setText(mapContactClient.get(ContactType.PHONE).getValor());

        if (mapContactClient.get(ContactType.EMAIL) != null)
            tedEmail.setText(mapContactClient.get(ContactType.EMAIL).getValor());
        else {
            rltContainerCorreo.setVisibility(View.GONE);
            lltAgregarCorreo.setVisibility(View.VISIBLE);
        }

        if (mapContactClient.get(ContactType.ADDRESS) != null)
            tedAddress.setText(mapContactClient.get(ContactType.ADDRESS).getValor());
        else {
            lltDireccionContainer.setVisibility(View.GONE);
            lltAgregarDireccion.setVisibility(View.VISIBLE);
        }

        if (mapContactClient.get(ContactType.REFERENCE) != null)
            tedReference.setText(mapContactClient.get(ContactType.REFERENCE).getValor());

        lltNuevaNota.setVisibility(model.getAnotacionModels().size() >= maxNoteAmount ? View.GONE : View.VISIBLE);
        notesView.refreshNotes(model.getAnotacionModels());

    }

    @Override
    @SuppressLint("UseSparseArrays")
    public void showNotes(ClienteModel model) {
        if (null == client || null == client.getAnotacionModels()) return;
        client.getAnotacionModels().addAll(model.getAnotacionModels());
        lltNuevaNota.setVisibility(model.getAnotacionModels().size() >= maxNoteAmount ? View.GONE : View.VISIBLE);
        notesView.refreshNotes(model.getAnotacionModels());
    }

    @Override
    public void onError(Throwable exception) {
        processError(exception);
    }

    @Override
    public void anotacionDeleted(AnotacionModel anotacionModel) {
        presenter.loadDataEdit(clientID);
    }

    @Override
    public void onAnotacionItemSelected(AnotacionModel anotacionModel, int position) {

        String name = client.getNombres() + (client.getApellidos() != null ? " " + client.getApellidos() : "");

        Intent intent = new Intent(getContext(), NoteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(NoteActivity.ACCION, NoteActivity.CLIENTE_EXISTENTE);
        intent.putExtra(NoteActivity.NOTA_ID, anotacionModel.getId());
        intent.putExtra(NoteActivity.CLIENT_ID, client.getClienteID());
        intent.putExtra(NoteActivity.CLIENT_LOCAL_ID, client.getId());
        intent.putExtra(NoteActivity.CLIENT_NAME, name);

        startActivityForResult(intent, NoteActivity.REQUEST_CODE_EDIT);
    }

    @Override
    public void onEliminarSelected(AnotacionModel anotacionModel, int adapterPosition) {
        anotacionModel.setEstado(StatusType.DELETE);
        presenter.deleteAnotacion(anotacionModel);
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view == null) view = new View(getActivity());
        KeyboardUtil.dismissKeyboard(getActivity(), view);
    }


    @OnClick(R.id.tvw_delete_contact)
    public void onDeleteClient() {
        if (null != client) {
            if (client.getCantidadPedido() == 0) {
                try {
                    new MessageDialog()
                        .setIcon(R.drawable.ic_alerta, 0)
                        .setStringTitle(R.string.client_dg_delete_title)
                        .setStringMessage(R.string.client_dg_delete_message)
                        .setStringAceptar(R.string.button_aceptar)
                        .setStringCancelar(R.string.button_cancelar)
                        .showIcon(true)
                        .showClose(true)
                        .showCancel(true)
                        .setListener(deleteListener)
                        .show(getFragmentManager(), "modalDelete");
                } catch (IllegalStateException e) {
                    BelcorpLogger.w("onDeleteClient", e);
                }
            } else {
                Toast.makeText(getContext(), R.string.clients_list_delete_with_order, Toast.LENGTH_LONG).show();
            }
        }
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


    /**********************************************************/

    private void initControls() {

        starOn = ContextCompat.getDrawable(getContext(), R.drawable.ic_star_filled);
        starOff = ContextCompat.getDrawable(getContext(), R.drawable.ic_star);

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
                BelcorpLogger.w("initControls", e);
            }

        }).setTypeDatePicker(2)
            .colorCancel(ContextCompat.getColor(context(), R.color.datepicker_text))
            .colorConfirm(ContextCompat.getColor(context(), R.color.datepicker_text))
            .viewTextSize(20)
            .showMonthOrdinal(false)
            .showYear(false)
            .build();

        chkFavorite.setOnClickListener(v -> chkFavorite.setImageDrawable(chkFavorite.getDrawable().equals(starOff) ? starOn : starOff));

        chkMobileFavorite.setOnClickListener(v -> {
            boolean isMobileEmpty = tedMobile.getText().toString().trim().isEmpty();
            boolean isStarChecked = chkMobileFavorite.getDrawable().equals(starOn);

            chkMobileFavorite.setImageDrawable(isMobileEmpty || isStarChecked ? starOff : starOn);
            chkPhoneFavorite.setImageDrawable(!isStarChecked ? starOff : chkPhoneFavorite.getDrawable());
        });

        chkPhoneFavorite.setOnClickListener(v -> {
            boolean isPhoneEmpty = tedPhone.getText().toString().trim().isEmpty();
            boolean isStarChecked = chkPhoneFavorite.getDrawable().equals(starOn);

            chkPhoneFavorite.setImageDrawable(isPhoneEmpty || isStarChecked ? starOff : starOn);
            chkMobileFavorite.setImageDrawable(!isStarChecked ? starOff : chkPhoneFavorite.getDrawable());
        });

        tedMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // EMPTY
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // EMPTY
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                chkMobileFavorite.setImageDrawable(s.length() == 0 ? starOff : chkMobileFavorite.getDrawable());
            }
        });

        tedPhone.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // EMPTY
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // EMPTY
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                chkPhoneFavorite.setImageDrawable(s.length() == 0 ? starOff : chkPhoneFavorite.getDrawable());
            }
        });
        notesView.setListener(this);
    }

    /*************************************************************/

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

    @OnClick(R.id.llt_nueva_nota)
    public void newAnnotation() {
        String name = client.getNombres() + (client.getApellidos() != null ? " " + client.getApellidos() : "");

        Intent intent = new Intent(getContext(), NoteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(NoteActivity.ACCION, NoteActivity.CLIENTE_EXISTENTE);
        intent.putExtra(NoteActivity.NOTA_ID, 0);
        intent.putExtra(NoteActivity.CLIENT_ID, client.getClienteID());
        intent.putExtra(NoteActivity.CLIENT_LOCAL_ID, client.getId());
        intent.putExtra(NoteActivity.CLIENT_NAME, name);
        startActivityForResult(intent, NoteActivity.REQUEST_CODE);
    }

    @OnClick(R.id.llt_toolbar_option_1)
    public void onCancel() {
        if (null != listener)
            listener.onBack();
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

    @OnClick(R.id.llt_toolbar_option_2)
    public void onSave() {
        hideKeyboard();
        disableAllErrors();

        String name = tedName.getText().toString().trim();
        String mobile = tedMobile.getText().toString().trim();
        String phone = tedPhone.getText().toString().trim();
        String email = tedEmail.getText().toString().trim();
        String address = tedAddress.getText().toString().trim();
        String reference = tedReference.getText().toString().trim();
        String lastName = tedLastName.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            String message = getString(R.string.client_registration_validation_0);
            showNameError(message);
            return;
        }
        if (TextUtils.isEmpty(mobile) && TextUtils.isEmpty(phone)) {
            String message = getString(R.string.client_registration_validation_1);
            showMobileError(message);
            showPhoneError(message);
            Toast.makeText(getActivity(), getString(R.string.client_registration_validation_5), Toast.LENGTH_LONG).show();
            return;
        }

        if (!TextUtils.isEmpty(email) && !email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            String message = getString(R.string.client_registration_validation_2);
            showEmailError(message);
            return;
        }

        client.setClienteID(client.getClienteID());
        client.setOrigen(GlobalConstant.APP_CODE);
        client.setNombres(name);
        client.setFechaNacimiento(birthday);
        client.setFavorito(chkFavorite.getDrawable().equals(starOn) ? 1 : 0);
        client.setEstado(ClientStateType.INSERT_UPDATE);
        client.setTipoRegistro(ClientRegisterType.ALL);
        client.setAnotacionModels(notesView.getNotes());

        client.setApellidos(lastName);
        if (chkMobileFavorite.getDrawable().equals(starOn)) {
            client.setTipoContactoFavorito(ContactType.MOBILE);
        } else {
            if (chkPhoneFavorite.getDrawable().equals(starOn)) {
                client.setTipoContactoFavorito(ContactType.PHONE);
            } else {
                client.setTipoContactoFavorito(ContactType.DEFAULT);
            }
        }

        int contactType = chkPhoneFavorite.getDrawable().equals(starOn) ? ContactType.PHONE : ContactType.DEFAULT;
        client.setTipoContactoFavorito(chkMobileFavorite.getDrawable().equals(starOn) ? ContactType.MOBILE : contactType);

        List<ContactoModel> contactoModelList = new ArrayList<>();

        ContactoModel contactoModel;

        int mobileState = mapContactClient.get(ContactType.MOBILE) != null ? mapContactClient.get(ContactType.MOBILE).getContactoClienteID() : 0;
        if (!TextUtils.isEmpty(mobile)) {
            contactoModel = new ContactoModel();
            contactoModel.setContactoClienteID(mobileState);
            contactoModel.setClienteID(client.getClienteID());
            contactoModel.setTipoContactoID(ContactType.MOBILE);
            contactoModel.setValor(mobile);
            contactoModel.setEstado(ContactStateType.INSERT_UPDATE);
            contactoModelList.add(contactoModel);
        } else if (mapContactClient.get(ContactType.MOBILE) != null) {
            contactoModel = new ContactoModel();
            contactoModel.setContactoClienteID(mapContactClient.get(ContactType.MOBILE).getContactoClienteID());
            contactoModel.setClienteID(client.getClienteID());
            contactoModel.setTipoContactoID(ContactType.MOBILE);
            contactoModel.setValor("");
            contactoModel.setEstado(ContactStateType.DELETE);
            contactoModelList.add(contactoModel);
        }

        int phoneState = mapContactClient.get(ContactType.PHONE) != null ? mapContactClient.get(ContactType.PHONE).getContactoClienteID() : 0;
        if (!TextUtils.isEmpty(phone)) {
            contactoModel = new ContactoModel();
            contactoModel.setContactoClienteID(phoneState);
            contactoModel.setClienteID(client.getClienteID());
            contactoModel.setTipoContactoID(ContactType.PHONE);
            contactoModel.setValor(phone);
            contactoModel.setEstado(ContactStateType.INSERT_UPDATE);
            contactoModelList.add(contactoModel);
        } else if (mapContactClient.get(ContactType.PHONE) != null) {
            contactoModel = new ContactoModel();
            contactoModel.setContactoClienteID(mapContactClient.get(ContactType.PHONE).getContactoClienteID());
            contactoModel.setClienteID(client.getClienteID());
            contactoModel.setTipoContactoID(ContactType.PHONE);
            contactoModel.setValor("");
            contactoModel.setEstado(ContactStateType.DELETE);
            contactoModelList.add(contactoModel);
        }

        int emailState = mapContactClient.get(ContactType.EMAIL) != null ? mapContactClient.get(ContactType.EMAIL).getContactoClienteID() : 0;
        if (!TextUtils.isEmpty(email)) {
            contactoModel = new ContactoModel();
            contactoModel.setContactoClienteID(emailState);
            contactoModel.setClienteID(client.getClienteID());
            contactoModel.setTipoContactoID(ContactType.EMAIL);
            contactoModel.setValor(email);
            contactoModel.setEstado(ContactStateType.INSERT_UPDATE);
            contactoModelList.add(contactoModel);
        } else if (mapContactClient.get(ContactType.EMAIL) != null) {
            contactoModel = new ContactoModel();
            contactoModel.setContactoClienteID(mapContactClient.get(ContactType.EMAIL).getContactoClienteID());
            contactoModel.setClienteID(client.getClienteID());
            contactoModel.setTipoContactoID(ContactType.EMAIL);
            contactoModel.setValor("");
            contactoModel.setEstado(ContactStateType.DELETE);
            contactoModelList.add(contactoModel);
        }

        int addressState = mapContactClient.get(ContactType.ADDRESS) != null ? mapContactClient.get(ContactType.ADDRESS).getContactoClienteID() : 0;
        if (!TextUtils.isEmpty(address)) {
            contactoModel = new ContactoModel();
            contactoModel.setContactoClienteID(addressState);
            contactoModel.setClienteID(client.getClienteID());
            contactoModel.setTipoContactoID(ContactType.ADDRESS);
            contactoModel.setValor(address);
            contactoModel.setEstado(ContactStateType.INSERT_UPDATE);
            contactoModelList.add(contactoModel);
        } else if (mapContactClient.get(ContactType.ADDRESS) != null) {
            contactoModel = new ContactoModel();
            contactoModel.setContactoClienteID(mapContactClient.get(ContactType.ADDRESS).getContactoClienteID());
            contactoModel.setClienteID(client.getClienteID());
            contactoModel.setTipoContactoID(ContactType.ADDRESS);
            contactoModel.setValor("");
            contactoModel.setEstado(ContactStateType.DELETE);
            contactoModelList.add(contactoModel);
        }

        int referenceState = mapContactClient.get(ContactType.REFERENCE) != null ? mapContactClient.get(ContactType.REFERENCE).getContactoClienteID() : 0;
        if (!TextUtils.isEmpty(reference)) {
            contactoModel = new ContactoModel();
            contactoModel.setContactoClienteID(referenceState);
            contactoModel.setClienteID(client.getClienteID());
            contactoModel.setTipoContactoID(ContactType.REFERENCE);
            contactoModel.setValor(reference);
            contactoModel.setEstado(ContactStateType.INSERT_UPDATE);
            contactoModelList.add(contactoModel);
        } else if (mapContactClient.get(ContactType.REFERENCE) != null) {
            contactoModel = new ContactoModel();
            contactoModel.setContactoClienteID(mapContactClient.get(ContactType.REFERENCE).getContactoClienteID());
            contactoModel.setClienteID(client.getClienteID());
            contactoModel.setTipoContactoID(ContactType.REFERENCE);
            contactoModel.setValor("");
            contactoModel.setEstado(ContactStateType.DELETE);
            contactoModelList.add(contactoModel);
        }

        client.setContactoModels(contactoModelList);
        presenter.getCountryForEdit(client);
    }

    @Override
    public void onCountryObtained(ClienteModel clienteModel, String country) {

        ContactoModel mobile = clienteModel.getContactoModelMap().get(ContactType.MOBILE);
        ContactoModel phone = clienteModel.getContactoModelMap().get(ContactType.PHONE);

        if ((mobile != null && !TextUtils.isEmpty(mobile.getValor())) ||
            (phone != null && !TextUtils.isEmpty(phone.getValor()))) {
            presenter.update(transformClientModel(clienteModel), loginModel);
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

    @OnClick(R.id.ted_birthday)
    public void selectDate() {
        hideKeyboard();
        datePicker.showPopWin(getActivity());
    }

    /**********************************************************/

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

    private ClienteModel transformClientModel(ClienteModel client) {

        for (ContactoModel contactoModel : client.getContactoModels()) {
            int code = -1;
            switch (contactoModel.getTipoContactoID()) {
                case ContactType.MOBILE:
                    code = -1;
                    break;
                case ContactType.PHONE:
                    code = -2;
                    break;
                case ContactType.EMAIL:
                    code = -3;
                    break;
                case ContactType.ADDRESS:
                    code = -4;
                    break;
                case ContactType.REFERENCE:
                    code = -5;
                    break;
                default:
                    break;
            }

            if (contactoModel.getContactoClienteID() == 0)
                contactoModel.setContactoClienteID(code);
        }

        return client;
    }

    /**********************************************************/

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

    public void refreshData() {
        presenter.loadDataEdit(clientID);
    }

    /**********************************************************/

    final MessageDialog.MessageDialogListener deleteListener = new MessageDialog.MessageDialogListener() {
        @Override
        public void aceptar() {
            client.setEstado(ClientStateType.DELETE);
            presenter.delete(client);
        }

        @Override
        public void cancelar() {
            // EMPTY
        }
    };

}
