package biz.belcorp.consultoras.feature.contest.news;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.dialog.MessageDialog;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.contest.OrderModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.model.error.BooleanDtoModel;
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModel;
import biz.belcorp.consultoras.common.model.incentivos.CuponModel;
import biz.belcorp.consultoras.common.model.incentivos.NivelProgramaNuevaModel;
import biz.belcorp.consultoras.feature.contest.news.di.NewProgramComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.anotation.OrderResultCode;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.CountryUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NewProgramFragment extends BaseFragment implements NewProgramView,
    NewProgramCuponAdapter.NewProgramCuponListener, NewProgramAdapter.NewProgramListener {

    @Inject
    NewProgramPresenter presenter;

    @BindView(R.id.tvw_current_title)
    TextView tvwCurrentTitle;

    @BindView(R.id.nsv_content)
    NestedScrollView nsvContent;

    @BindView(R.id.rvw_contest)
    RecyclerView rvwContest;

    private Unbinder bind;

    private String codigoConcurso = "";

    private DecimalFormat decimalFormat;

    /**********************************************************/

    public NewProgramFragment() {
        super();
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() {
        getComponent(NewProgramComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);

        Bundle bundle = getArguments();

        if (savedInstanceState == null && bundle != null) {
            codigoConcurso = bundle.getString(GlobalConstant.CODE_CONCURSO, "");
            init();
        }
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contest_new_program, container, false);
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
        if (bind != null)
            bind.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    /**********************************************************/

    private void init() {
        presenter.get(codigoConcurso);
    }


    /**********************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {

        Bundle bundle = new Bundle();

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_PROGRAM_FOR_NEWS);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    @Override
    public void trackBackPressed(LoginModel loginModel) {

        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_PROGRAM_FOR_NEWS);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);

        getActivity().finish();
    }

    @Override
    public void initializeAdapter(String countryISO) {
        decimalFormat = CountryUtil.getDecimalFormatByISO(countryISO, true);
    }


    @Override
    public void showContest(ConcursoModel model, String campaingCurrent, String countryMoneySymbol) {

        Integer level = 0;
        if (null != model.getCodigoNivelProgramaNuevas() && !model.getCodigoNivelProgramaNuevas().isEmpty())
            level = Integer.parseInt(model.getCodigoNivelProgramaNuevas());

        String importe = countryMoneySymbol + " " + decimalFormat.format(model.getImportePedido());

        tvwCurrentTitle.setText(String.format(context().getString(R.string.incentives_nuevas_title), level, importe));

        List<NivelProgramaNuevaModel> filterListPN = new ArrayList<>();

        for (NivelProgramaNuevaModel nivelNueva : model.getNivelProgramaNuevas()) {

            int levelNueva = 0;
            if (null != nivelNueva.getCodigoNivel() && !nivelNueva.getCodigoNivel().isEmpty())
                levelNueva = Integer.parseInt(nivelNueva.getCodigoNivel());

            if (levelNueva >= level)
                filterListPN.add(nivelNueva);
        }

        NewProgramAdapter newProgramAdapter = new NewProgramAdapter(getActivity(),
            model.getImportePedido(), filterListPN,
            model.getCodigoNivelProgramaNuevas(), countryMoneySymbol, decimalFormat, model.getTextoCupon(),
            model.getTextoCuponIndependiente());

        newProgramAdapter.setNewProgramCuponListener(this);
        newProgramAdapter.setListener(this);

        rvwContest.setAdapter(newProgramAdapter);
        rvwContest.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvwContest.setHasFixedSize(true);
        rvwContest.setNestedScrollingEnabled(false);
    }

    @Override
    public void orderAdded() {
        Toast.makeText(getActivity(), R.string.incentives_add_order, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(ErrorModel errorModel) {
        processGeneralError(errorModel);
    }

    @Override
    public void onOrderError(BooleanDtoModel errorModel) {
        switch (errorModel.getCode()) {
            case OrderResultCode.FACTURADO_1:
            case OrderResultCode.FACTURADO_2:
                showError(getString(R.string.dg_pedido_facturado_title),
                    getString(R.string.dg_pedido_facturado_message));
                break;
            case OrderResultCode.RESERVADO:
                showOrderErrorReservado();
                break;
            case OrderResultCode.NO_EXISTE:
                showError(getString(R.string.dg_no_existe_title),
                    getString(R.string.dg_no_existe_message));
                break;
            case OrderResultCode.MAXIMO_EXCEDIDO:
                showError(getString(R.string.dg_maximo_excedido_title),
                    getString(R.string.dg_maximo_excedido_message));
                break;
            default:
                break;
        }
    }

    @Override
    public void onReservationError(BooleanDtoModel errorModel) {
        if (null != errorModel && null != errorModel.getMessage())
            showError(getString(R.string.dg_no_reserva_title), errorModel.getMessage());
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
    public void addCupon(CuponModel cuponModel) {

        OrderModel orderModel = new OrderModel();
        orderModel.setCantidad(1);
        orderModel.setCuv(cuponModel.getCodigoCupon());
        orderModel.setOrigenPedidoWeb(GlobalConstant.WEB_ORDER_ORIGIN);
        presenter.addCupon(orderModel);
    }

    @Override
    public void trackEvent(String label) {
        presenter.trackEvent(
            GlobalConstant.SCREEN_PROGRAM_FOR_NEWS,
            GlobalConstant.EVENT_CAT_PROGRAM_FOR_NEWS,
            GlobalConstant.ACTION_AGREGAR_PRODUCTO,
            label,
            GlobalConstant.EVENT_NAME_PROGRAM_FOR_NEWS);
    }

    @Override
    public void removeReservation() {
        Toast.makeText(getActivity(), R.string.incentives_remove_reservation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void movePos(final int pos, final int countSize) {

        rvwContest.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pos > 0) {
                    View firstView = rvwContest.getChildAt(0);
                    int toY = firstView.getTop() - 150;
                    int firstPosition = rvwContest.getChildAdapterPosition(firstView);
                    View thisView = rvwContest.getChildAt(pos - firstPosition);
                    int fromY = thisView.getTop();
                    nsvContent.scrollTo(0, fromY - toY);
                }
            }
        }, 250);
    }

    private void showOrderErrorReservado() {

        try {
            new MessageDialog()
                .setIcon(R.drawable.ic_alert, 0)
                .setStringTitle(R.string.dg_pedido_reservado_title)
                .setStringMessage(R.string.dg_pedido_reservado_message)
                .setStringAceptar(R.string.dg_pedido_reservado_accept)
                .setStringCancelar(R.string.dg_pedido_reservado_cancel)
                .showIcon(true)
                .showClose(false)
                .showCancel(true)
                .setListener(reservaListener)
                .show(getFragmentManager(), "modalSuccess");
        } catch (IllegalStateException e) {
            BelcorpLogger.w("modalSuccess", e);
        }
    }

    /****************************************************************/

    final MessageDialog.MessageDialogListener reservaListener = new MessageDialog.MessageDialogListener() {
        @Override
        public void aceptar() {
            presenter.removeReservation();
        }

        @Override
        public void cancelar() {
            // EMPTY
        }
    };

}
