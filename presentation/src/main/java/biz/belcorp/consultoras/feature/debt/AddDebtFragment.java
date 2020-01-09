package biz.belcorp.consultoras.feature.debt;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.component.CurrencyEditText;
import biz.belcorp.consultoras.common.component.EmptyCurrencyTextWatcher;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.debt.CampaniaModel;
import biz.belcorp.consultoras.common.model.debt.DebtModel;
import biz.belcorp.consultoras.common.model.user.UserModel;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.feature.debt.client.ClientFilterFragment;
import biz.belcorp.consultoras.feature.debt.di.DebtComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.ToastUtil;
import biz.belcorp.consultoras.util.anotation.MovementType;
import biz.belcorp.consultoras.util.anotation.StatusType;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.annotation.DatetimeFormat;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.CountryUtil;
import biz.belcorp.library.util.DateUtil;
import biz.belcorp.library.util.KeyboardUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 */
public class AddDebtFragment extends BaseFragment implements AddDebtView, ClientFilterFragment.OnFilterCompleteListener {

    @Inject
    protected AddDebtPresenter addDebtPresenter;

    @BindView(R.id.tvw_client_filter)
    protected TextView tvwClientFilter;
    @BindView(R.id.ivw_select)
    protected ImageView imvSelect;
    @BindView(R.id.tvw_currency)
    protected TextView tvwCurrency;
    @BindView(R.id.edt_amount)
    protected CurrencyEditText edtAmount;
    @BindView(R.id.spr_campaign)
    protected Spinner sprCampaign;
    @BindView(R.id.edt_note)
    protected EditText edtNote;
    @BindView(R.id.llt_helper)
    LinearLayout llHelper;

    private OnClientFilterClick onClientFilterClick;
    private ArrayAdapter<CampaniaModel> campaniaArrayAdapter;
    private LoginModel loginModel;

    private DecimalFormat decimalFormat;
    private boolean shouldFilter = true;
    private String moneySymbol;
    private int showDecimal = 1;

    /******************************************************/

    public AddDebtFragment() {
        // EMPTY
    }

    /******************************************************/

    @Override
    protected boolean onInjectView() {
        getComponent(DebtComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.addDebtPresenter.attachView(this);
        init();
    }

    /******************************************************/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnClientFilterClick)
            onClientFilterClick = (OnClientFilterClick) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_debt, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(addDebtPresenter != null) addDebtPresenter.trackScreen();
    }

    /**********************************************************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {
        this.loginModel = loginModel;

        Bundle bundle = new Bundle();

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_DEBT_ADD);
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

    /******************************************************/

    private void init() {

        if (addDebtPresenter == null) getActivity().onBackPressed();

        addDebtPresenter.getCurrentCampaign();

        Bundle bundle = getArguments();

        if (bundle != null) {
            int clientId = bundle.getInt(GlobalConstant.CLIENTE_ID);
            int clientLocalId = bundle.getInt(GlobalConstant.CLIENTE_LOCAL_ID);
            String clientName = bundle.getString(GlobalConstant.CLIENT_NAME);

            if (clientLocalId != 0 && clientName != null) {
                shouldFilter = false;
                tvwClientFilter.setText(clientName);
                imvSelect.setVisibility(View.GONE);

                addDebtPresenter.setSelectedClient(clientLocalId, clientId, clientName);
            }
        }

        campaniaArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_campaign_spinner);
        campaniaArrayAdapter.setDropDownViewResource(R.layout.item_campaign_spinner);
        sprCampaign.setAdapter(campaniaArrayAdapter);

        llHelper.setOnClickListener(v -> {
            if (isVisible())
                hideKeyboard();
        });
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view == null) view = new View(getActivity());
        KeyboardUtil.dismissKeyboard(getActivity(), view);
    }

    /******************************************************/

    @OnClick(R.id.tvw_client_filter)
    protected void onFilterClick() {
        if (shouldFilter && onClientFilterClick != null)
            onClientFilterClick.onFilter();
    }

    @OnClick(R.id.tvw_add_debt)
    protected void onAddDebtClick() {

        int position = sprCampaign.getSelectedItemPosition();
        String amount = edtAmount.getText().toString().trim();

        amount = amount.replace(moneySymbol, "").replace(" ", "").trim();

        String note = edtNote.getText().toString().trim();
        String campaign = "";

        Number number = BigDecimal.ZERO;

        try {
            if (showDecimal == 0) amount = amount.replace(".", "");
            number = decimalFormat.parse(amount);
        } catch (ParseException e) {
            BelcorpLogger.w("onAddDebtClick", e);
        }

        if (position >= 0)
            campaign = campaniaArrayAdapter.getItem(sprCampaign.getSelectedItemPosition()).getCampaniaId();

        if (tvwClientFilter.getText().toString().equals(getString(R.string.debt_pick_client_label))) {
            ToastUtil.INSTANCE.show(context(), R.string.debt_client_filter_error, Toast.LENGTH_SHORT);
            return;
        }

        if (TextUtils.isEmpty(amount) || number.doubleValue() < 0.01) {
            ToastUtil.INSTANCE.show(context(), R.string.debt_amount_error, Toast.LENGTH_SHORT);
            return;
        }

        DebtModel debtModel = new DebtModel();
        debtModel.setMonto(BigDecimal.valueOf(number.doubleValue()));
        debtModel.setCodigoCampania(campaign);
        debtModel.setNote(note);
        debtModel.setTipoMovimiento(MovementType.CARGO);
        debtModel.setEstado(StatusType.CREATE);
        debtModel.setDesc("DEUDA AGREGADA");

        try {
            debtModel.setFecha(DateUtil.convertFechaToString(new Date(), DatetimeFormat.ISO_8601));
        } catch (ParseException e) {
            BelcorpLogger.w("onAddDebtClick", e);
        }

        addDebtPresenter.uploadDebt(debtModel, loginModel);
    }

    /******************************************************/

    @Override
    public void onComplete(ClienteModel clienteModel) {
        addDebtPresenter.setSelectedClient(clienteModel);
        tvwClientFilter.setText(clienteModel.getNombres() + (clienteModel.getApellidos() == null ? "" : " " + clienteModel.getApellidos()));
    }

    /******************************************************/

    @Override
    public void onUploadDebtComplete() {
        addDebtPresenter.initScreenTrackAgregarDeuda();
    }

    @Override
    public void setData(UserModel model) {
        decimalFormat = CountryUtil.getDecimalFormatByISO(model.getCountryISO(), false);
        DecimalFormat decimalFormatConstancia = CountryUtil.getDecimalFormatConstanciaByISO(model.getCountryISO(), false);
        showDecimal = model.getCountryShowDecimal();
        moneySymbol = model.getCountryMoneySymbol();

        tvwCurrency.setText(model.getCountryMoneySymbol());
        edtAmount.addTextChangedListener(new EmptyCurrencyTextWatcher(edtAmount, decimalFormat, showDecimal));
        edtAmount.setSelection(edtAmount.getText().length());
        edtAmount.setHint(decimalFormatConstancia.format(0.0));
        edtAmount.requestFocus();
    }

    @Override
    public void setUpCampaign(List<CampaniaModel> campaigns) {
        campaniaArrayAdapter.addAll(campaigns);
        campaniaArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(Throwable exception) {
        processError(exception);
    }

    @Override
    public void trackBackPressed(LoginModel loginModel) {
        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_DEBT_ADD);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);
    }

    @Override
    public void initScreenTrackAgregarDeuda(LoginModel loginModel) {
        Tracker.Deudas.trackAgregarDeudaExitoso(loginModel);
        if (getActivity() != null) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().onBackPressed();
        }
    }

    public void trackBackPressed() {
        addDebtPresenter.trackBackPressed();
    }

    /******************************************************/

    interface OnClientFilterClick {
        void onFilter();
    }
}
