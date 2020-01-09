package biz.belcorp.consultoras.feature.history;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.recordatorio.RecordatorioModel;
import biz.belcorp.consultoras.common.recordatory.RecordatoryJobService;
import biz.belcorp.consultoras.feature.client.di.ClientComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.StatusType;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.annotation.DatetimeFormat;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.DateUtil;
import biz.belcorp.library.util.PickerUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.Nullable;

public class AddRecordatoryFragment extends BaseFragment implements AddRecordatoryView {

    private static final String TAG = "AddRecordatoryFragment";

    @Inject
    protected AddRecordatoryPresenter addRecordatoryPresenter;

    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.npk_dia)
    NumberPicker npkDia;
    @BindView(R.id.npk_mes)
    NumberPicker npkMes;
    @BindView(R.id.npk_anio)
    NumberPicker npkAnio;
    @BindView(R.id.npk_hora)
    NumberPicker npkHora;
    @BindView(R.id.npk_minuto)
    NumberPicker npkMinuto;
    @BindView(R.id.npk_turno)
    NumberPicker npkTurno;

    private int localID;
    private int recordatoryID;
    private int clientId;
    private String clientName = "";
    private String clientName2 = "";
    private String fechaRecordatorio = "";

    static final int MINUTE_MINIMAL = 1;
    static final int MILISECONDS_IN_ONE_MINUTE = 60000;

    private AddRecordatoryFragmentListener listener;
    private BigDecimal totalDebt;
    private int clienteLocalID;
    private LoginModel loginModel;

    /******************************************************/

    public static AddRecordatoryFragment newInstance() {
        return new AddRecordatoryFragment();
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
        this.addRecordatoryPresenter.attachView(this);

        initBundle();
        init();
    }

    /******************************************************/

    interface AddRecordatoryFragmentListener {

        void setResult(boolean result);
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_recordatory, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(addRecordatoryPresenter != null) addRecordatoryPresenter.trackScreen();
    }

    /******************************************************/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddRecordatoryFragmentListener) {
            this.listener = (AddRecordatoryFragmentListener) context;
        }
    }

    /******************************************************/

    public void init() {

        setDividerColor(npkDia, Color.BLACK);
        npkDia.setMinValue(1);
        npkDia.setMaxValue(31);
        npkDia.setFormatter(i -> String.format(Locale.getDefault(), "%02d", i));

        setDividerColor(npkMes, Color.BLACK);
        npkMes.setMinValue(1);
        npkMes.setMaxValue(12);
        npkMes.setDisplayedValues(PickerUtil.getMonthsShortNames());
        npkMes.setOnValueChangedListener((picker, oldMonth, newMonth) -> {
            Calendar calendar = new GregorianCalendar(npkAnio.getValue(), newMonth - 1, 1);
            int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            npkDia.setMaxValue(daysInMonth);
        });

        setDividerColor(npkAnio, Color.BLACK);
        npkAnio.setMinValue(2000);
        npkAnio.setMaxValue(2050);
        npkAnio.setOnValueChangedListener((picker, oldYear, newYear) -> {
            Calendar calendar = new GregorianCalendar(newYear, npkMes.getValue() - 1, 1);
            int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            npkDia.setMaxValue(daysInMonth);
        });

        npkHora.setMinValue(1);
        npkHora.setMaxValue(12);
        npkHora.setFormatter(i -> String.format(Locale.getDefault(), "%02d", i));
        setDividerColor(npkHora, Color.BLACK);

        npkMinuto.setMinValue(0);
        npkMinuto.setMaxValue(59);
        npkMinuto.setFormatter(i -> String.format(Locale.getDefault(), "%02d", i));
        setDividerColor(npkMinuto, Color.BLACK);

        npkTurno.setMinValue(0);
        npkTurno.setMaxValue(1);
        npkTurno.setDisplayedValues(PickerUtil.getTurno());
        setDividerColor(npkTurno, Color.BLACK);

        try {

            Calendar cal = Calendar.getInstance();

            if (!TextUtils.isEmpty(fechaRecordatorio)) {
                btnSend.setText(getString(R.string.debt_update_recordatory));

                Date date = DateUtil.convertirISODatetoDate(fechaRecordatorio);
                cal.setTime(date);

                npkHora.setValue(cal.get(Calendar.HOUR));
                npkMinuto.setValue(cal.get(Calendar.MINUTE));

                if (cal.get(Calendar.AM_PM) == Calendar.AM) {
                    npkTurno.setValue(0);
                } else {
                    npkTurno.setValue(1);
                }
            } else {
                npkHora.setValue(4);
                npkMinuto.setValue(0);
                npkTurno.setValue(1);
            }

            npkDia.setMaxValue(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            npkDia.setValue(cal.get(Calendar.DAY_OF_MONTH));
            npkMes.setValue(cal.get(Calendar.MONTH) + 1);
            npkAnio.setValue(cal.get(Calendar.YEAR));
        } catch (ParseException e) {
            Log.e(TAG, "ParseException", e);
        }
    }

    /**********************************************************/

    private void initBundle() {

        Bundle extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            localID = extras.getInt(GlobalConstant.CLIENT_LOCAL_ID_RECORDATORY, -1);
            recordatoryID = extras.getInt(GlobalConstant.CLIENT_ID_RECORDATORY, -1);
            clientId = extras.getInt(GlobalConstant.CLIENTE_ID, -1);
            clientName = extras.getString(GlobalConstant.CLIENT_NAME, "");
            fechaRecordatorio = extras.getString(GlobalConstant.CLIENT_DATE_RECORDATORY, "");
            clienteLocalID = extras.getInt(GlobalConstant.CLIENTE_LOCAL_ID, -1);
            clientName2 = extras.getString(GlobalConstant.CLIENT_NAME, "");
            totalDebt = new BigDecimal(extras.getString(GlobalConstant.CLIENT_TOTAL_DEBT, "0"));
        }
    }

    /**********************************************************/

    @OnClick(R.id.btn_send)
    public void saveRecordatory() {
        RecordatorioModel recordatorioModel = new RecordatorioModel();
        recordatorioModel.setId(localID);
        recordatorioModel.setRecordatorioID(recordatoryID);
        recordatorioModel.setClienteID(clientId);
        recordatorioModel.setFecha(returnDate());
        recordatorioModel.setDescripcion(".");
        recordatorioModel.setEstado(StatusType.CREATE);

        if (recordatoryID != 0 && recordatoryID != -1)
            recordatorioModel.setEstado(StatusType.UPDATE);

        Date date;

        try {
            date = DateUtil.convertEngFechaToDate(returnDate(), DatetimeFormat.ISO_8601);

            Calendar cal = Calendar.getInstance();
            if (date.after(cal.getTime())) {
                if (TextUtils.isEmpty(fechaRecordatorio)) {
                    addRecordatoryPresenter.saveRecordatory(recordatorioModel, loginModel, clienteLocalID);
                } else {
                    addRecordatoryPresenter.updateRecordatory(recordatorioModel, loginModel, clienteLocalID);
                    addRecordatoryPresenter.trackEvent(
                        GlobalConstant.SCREEN_RECORDATORY,
                        GlobalConstant.EVENT_CAT_UPDATE_RECORDATORY,
                        GlobalConstant.EVENT_ACTION_UPDATE_RECORDATORY,
                        GlobalConstant.EVENT_LABEL_NOT_AVAILABLE,
                        GlobalConstant.EVENT_NAME_UPDATE_RECORDATORY
                    );
                }
            } else {
                Toast.makeText(context(), R.string.reminder_alert_date, Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            BelcorpLogger.w(TAG, e.getMessage());
            Toast.makeText(context(), R.string.reminder_alert_date_error, Toast.LENGTH_SHORT).show();
        }

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

    @Override
    public void saveData() {
        initJob();
        hideLoading();

        if (listener != null) {
            listener.setResult(true);
        }
    }

    @Override
    public void deleteData() {
        cancelJob();
        hideLoading();

        if (listener != null) {
            listener.setResult(true);
        }
    }

    public void eliminar() {
        addRecordatoryPresenter.eliminarRecordatory(localID, clientId, clienteLocalID);
        addRecordatoryPresenter.trackEvent(
            GlobalConstant.SCREEN_RECORDATORY,
            GlobalConstant.EVENT_CAT_DELETE_RECORDATORY,
            GlobalConstant.EVENT_ACTION_DELETE_RECORDATORY,
            GlobalConstant.EVENT_LABEL_NOT_AVAILABLE,
            GlobalConstant.EVENT_NAME_DELETE_RECORDATORY
        );
    }

    private void cancelJob() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getActivity()));
        dispatcher.cancel(getString(R.string.reminder_key) + clientId);
    }

    private void initJob() {
        Date date = null;

        try {
            date = DateUtil.convertEngFechaToDate(returnDate(), DatetimeFormat.ISO_8601);
        } catch (ParseException e) {
            Log.e(TAG, "ParseException", e);
        }

        Calendar now = Calendar.getInstance();
        Calendar calendarAlarm = Calendar.getInstance();
        calendarAlarm.setTime(date);

        long diff = calendarAlarm.getTimeInMillis() - now.getTimeInMillis();
        int startSeconds = (int) (diff / 1000) < 0 ? 0 : (int) (diff / 1000);
        int endSeconds = startSeconds + 10;

        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.CLIENT_MESSAGE_RECORDATORY, String.format(getString(R.string.debt_recordatory_alarm), clientName, retornaHora()));

        bundle.putInt(GlobalConstant.CLIENTE_LOCAL_ID, clienteLocalID);
        bundle.putInt(GlobalConstant.CLIENTE_ID, clientId);
        bundle.putString(GlobalConstant.CLIENT_NAME, clientName2);
        bundle.putString(GlobalConstant.CLIENT_TOTAL_DEBT, totalDebt.toString());

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getActivity()));
        Job.Builder builder = dispatcher.newJobBuilder();
        builder.setService(RecordatoryJobService.class);
        builder.setTag(getString(R.string.reminder_key) + clientId);
        builder.setRecurring(false);
        builder.setReplaceCurrent(true);
        builder.setLifetime(Lifetime.FOREVER);
        builder.setTrigger(Trigger.executionWindow(startSeconds, endSeconds));
        builder.setExtras(bundle);
        dispatcher.schedule(builder.build());
    }

    public String returnDate() {

        int h = npkHora.getValue();

        if (h == 12)
            h = 0;

        if (npkTurno.getValue() == 1)
            h = 12 + h;

        String hour = String.format(Locale.getDefault(), "%02d", h);

        return npkAnio.getValue() + "-"
            + String.format(Locale.getDefault(), "%02d", npkMes.getValue()) + "-"
            + String.format(Locale.getDefault(), "%02d", npkDia.getValue()) + "T"
            + hour + ":"
            + String.format(Locale.getDefault(), "%02d", npkMinuto.getValue()) + ":00";
    }

    public String retornaHora() {
        return " " + String.format(Locale.getDefault(), "%02d", npkHora.getValue()) + ":"
            + String.format(Locale.getDefault(), "%02d", npkMinuto.getValue()) + " "
            + PickerUtil.turnos.get(npkTurno.getValue());
    }

    private void setDividerColor(NumberPicker picker, int color) {
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException | IllegalAccessException | Resources.NotFoundException e) {
                    Log.e(TAG, e.getMessage());
                }
                break;
            }
        }
    }

    @Override
    public void onError(Throwable exception) {
        processError(exception);
    }

    @Override
    public void initScreenTrack(LoginModel loginModel) {
        this.loginModel = loginModel;

        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_RECORDATORY);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);
        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }
}
