package biz.belcorp.consultoras.feature.home.accountstate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.model.accountState.AccountStateModel;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.domain.entity.LoginDetail;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.domain.entity.UserConfigData;
import biz.belcorp.consultoras.feature.client.di.ClientComponent;
import biz.belcorp.consultoras.feature.payment.online.PayOnlineActivity;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.PagoEnLineaConfigCode;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.CountryUtil;
import biz.belcorp.library.util.DateUtil;
import biz.belcorp.library.util.NetworkUtil;
import biz.belcorp.library.util.StringUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AccountStateFragment extends BaseFragment implements AccountStateView {

    @Inject
    AccountStatePresenter presenter;

    @BindView(R.id.tvw_deuda)
    TextView tvwDeuda;
    @BindView(R.id.tvw_vencimiento)
    TextView tvwVencimiento;
    @BindView(R.id.tvw_user)
    TextView tvwUser;
    @BindView(R.id.tvw_user_valoracion)
    TextView tvwUserValoracion;
    @BindView(R.id.ivw_status)
    ImageView ivwStatus;
    @BindView(R.id.rvw_deuda)
    RecyclerView rvwDeuda;
    @BindView(R.id.tvw_message)
    TextView tvwMessage;
    @BindView(R.id.btn_paid_online)
    Button btnPaid;
    @BindView(R.id.lnlEmpty)
    LinearLayout lnlEmpty;

    private String name;
    private String moneySymbol;
    private DecimalFormat decimalFormat;
    private Unbinder bind;

    AccountStateListAdapter adapter;

    AccountStateFragmentListener listener;

    boolean reloadDeuda = false;
    /**********************************************************/

    public AccountStateFragment() {
        super();
    }

    public static AccountStateFragment newInstance() {
        return new AccountStateFragment();
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

        final Bundle b = getArguments();
        if( b != null){
            reloadDeuda = b.getBoolean("RELOAD_DEUDA",reloadDeuda);
        }

        presenter.data(reloadDeuda);
    }

    @Override
    public void onAttach(@org.jetbrains.annotations.Nullable Context context) {
        super.onAttach(context);
        if(context instanceof AccountStateFragmentListener){
            listener = (AccountStateFragmentListener) context;
        }

    }

    /**********************************************************/
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_estado_cuenta, container, false);
        bind = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(presenter != null) presenter.initScreenTrack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bind != null) bind.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != presenter) presenter.destroy();
    }


    /**********************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {

        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_ESTADO_CUENTA);
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

    @Override
    public void showResumen(LoginDetail loginDetail, boolean isPagoOnline) {
        calculateDebts(loginDetail,isPagoOnline);
    }

    @Override
    public void showMovements(List<AccountStateModel> accountStates) {
        if (accountStates != null && !accountStates.isEmpty()) {
            lnlEmpty.setVisibility(View.GONE);
            rvwDeuda.setVisibility(View.VISIBLE);
            adapter = new AccountStateListAdapter(context(), accountStates, moneySymbol, decimalFormat);
            rvwDeuda.setAdapter(adapter);
            rvwDeuda.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            //rvwDeuda.setHasFixedSize(true);
            rvwDeuda.setNestedScrollingEnabled(false);
            rvwDeuda.setVisibility(View.VISIBLE);
        } else {
            rvwDeuda.setVisibility(View.GONE);
            lnlEmpty.setVisibility(View.VISIBLE);
            if (NetworkUtil.isThereInternetConnection(context())){
                tvwMessage.setText(R.string.estado_cuenta_message_no_items);
            }
            else{
                tvwMessage.setText(R.string.estado_cuenta_message_no_items_conexion);
            }
        }
    }

    @Override
    public void setData(String iso, String moneySymbol, String name) {
        this.decimalFormat = CountryUtil.getDecimalFormatByISO(iso, true);
        this.moneySymbol = moneySymbol;
        this.name = name;
    }

    @Override
    public void setShowDecimals(int show) {
        // EMPTY
    }

    @Override
    public void onError(Throwable throwable) {
        processError(throwable);
    }


    @OnClick(R.id.btn_paid_online)
    public void btnPaid(){
        presenter.goToPaidOnline();
    }

    /****************************************************************/

    private void calculateDebts(LoginDetail loginDetail, boolean isPagoOnline) {
        String date = "";
        Calendar actualDate = Calendar.getInstance();
        Calendar deudaCalender = Calendar.getInstance();

        try {
            date = StringUtil.capitalize(DateUtil.convertFechaToString(DateUtil.convertirDDMMAAAAtoDate(loginDetail.getDetailDescription()), "dd MMMM"));
            deudaCalender.setTime(DateUtil.convertirDDMMAAAAtoDate(loginDetail.getDetailDescription()));
        } catch (ParseException e) {
            BelcorpLogger.w("AccountStateFragment", "calculateDebts", e.getMessage());
        }

        if(null != name && !name.isEmpty()) name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        tvwDeuda.setText(moneySymbol + " " + decimalFormat.format(loginDetail.getValue()));

        if (date == null || date.isEmpty()) date = "--/--";
        tvwVencimiento.setText(String.format(getString(R.string.estado_cuenta_vencimiento), date));


        if (loginDetail.getValue() <= 0 || date.equals("--/--")) {
            ivwStatus.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_cheers));
            tvwUser.setText(String.format(getString(R.string.estado_cuenta_user_valoracion), name));
            tvwUserValoracion.setText(getString(R.string.estado_cuenta_status_valoracion));
        } else {
            if(isPagoOnline)
                btnPaid.setVisibility(View.VISIBLE);
            else
                btnPaid.setVisibility(View.GONE);

            if (deudaCalender.equals(actualDate) || deudaCalender.after(actualDate)) {
                ivwStatus.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_force));
                tvwUser.setText(String.format(getString(R.string.estado_cuenta_user_valoracion2), name));
                tvwUserValoracion.setText(getString(R.string.estado_cuenta_status_valoracion2));

            } else {
                ivwStatus.setVisibility(View.GONE);
                tvwUser.setText(String.format(getString(R.string.estado_cuenta_user_valoracion3), name));
                tvwUserValoracion.setText(getString(R.string.estado_cuenta_status_valoracion3));
            }
        }
    }

    @Override
    public void trackBackPressed(LoginModel loginModel) {
        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_ESTADO_CUENTA);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);
        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);
    }

    @Override
    public void sendToPay(User user) {
        if(listener!=null){
            Tracker.trackEvent(
                GlobalConstant.SCREEN_ESTADO_CUENTA,
                GlobalConstant.EVENT_LABEL_ESTADO_CUENTA,
                GlobalConstant.EVENT_ACTION_CLICK_BOTON,
                getString(R.string.paga_en_linea),
                GlobalConstant.EVENT_NAME_VIRTUAL_EVENT,
                user);
            presenter.gotToMethodPay();
        }
    }

    @Override
    public void gotToMethodPay(UserConfigData item){
        if(item == null) return;
        if (PagoEnLineaConfigCode.FlujoCode.INTERNO.compareTo(item.getValue1()) == 0){
            Intent intent = new Intent(getContext(), PayOnlineActivity.class);
            startActivity(intent);
        }else if (PagoEnLineaConfigCode.FlujoCode.EXTERNO.compareTo(item.getValue1()) == 0 && item.getValue2() != null ){
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getValue2()));
            startActivity(i);
        }
    }

    public void trackBackPressed() {
        presenter.trackBackPressed();
    }

    interface AccountStateFragmentListener{

    }

}

