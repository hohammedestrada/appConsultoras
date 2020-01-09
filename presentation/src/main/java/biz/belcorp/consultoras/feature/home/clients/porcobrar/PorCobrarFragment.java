package biz.belcorp.consultoras.feature.home.clients.porcobrar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.error.BusinessErrorModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.model.user.UserModel;
import biz.belcorp.consultoras.feature.history.DebtPaymentHistoryActivity;
import biz.belcorp.consultoras.feature.home.clients.BaseClientsFragment;
import biz.belcorp.consultoras.feature.home.clients.ClientsListEvent;
import biz.belcorp.consultoras.feature.home.di.HomeComponent;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.util.CountryUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 */
public class PorCobrarFragment extends BaseClientsFragment implements PorCobrarClientsView, PorCobrarListAdapter.OnItemSelectedListener {

    @Inject
    PorCobrarClientsPresenter clientsPresenter;

    @BindView(R.id.rvw_clients)
    RecyclerView rvwClients;

    @BindView(R.id.rlt_content_list_null)
    RelativeLayout rltNoClients;

    @BindView(R.id.rlt_content)
    RelativeLayout rltContentList;

    @BindView(R.id.nsv_content)
    NestedScrollView nsvContent;

    @BindView(R.id.tvw_total)
    TextView tvwtotal;

    @BindView(R.id.tvw_total2)
    TextView tvwtotal2;

    @BindView(R.id.tvw_clients_counter)
    TextView tvwClientsCounter;

    @BindView(R.id.layoutFooter)
    LinearLayout layoutFooter;

    private Unbinder unbinder;

    private ClientListListener listListener;

    /**********************************************************/

    public PorCobrarFragment() {
        super();
    }

    public static PorCobrarFragment getInstance() {
        return new PorCobrarFragment();
    }

    public void setListListener(ClientListListener listListener) {
        this.listListener = listListener;
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() throws IllegalStateException {
        getComponent(HomeComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.clientsPresenter.attachView(this);
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clients_list_porcobrar, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (clientsPresenter != null)
            clientsPresenter.destroy();
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
    public void showClients(List<ClienteModel> clientModelList, UserModel model) {

        if (!isVisible()) return;

        String moneySymbol = model.getCountryMoneySymbol();
        DecimalFormat decimalFormat = CountryUtil.getDecimalFormatByISO(model.getCountryISO(), true);

        if (null != clientModelList && !clientModelList.isEmpty()) {
            String counter = getString(R.string.clients_porcobrar_title) + " : ";
            tvwClientsCounter.setText(counter);

            PorCobrarListAdapter adapter = new PorCobrarListAdapter(clientModelList, this, moneySymbol, decimalFormat);
            rvwClients.setAdapter(adapter);
            rvwClients.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            rvwClients.setHasFixedSize(true);
            rvwClients.setNestedScrollingEnabled(false);
            layoutFooter.setVisibility(View.VISIBLE);
            rltContentList.setVisibility(View.VISIBLE);
            rltNoClients.setVisibility(View.GONE);

            BigDecimal total = BigDecimal.ZERO;

            for (ClienteModel oClienteModel : clientModelList) {
                if (oClienteModel.getTotalDeuda() != null)
                    total = total.add(oClienteModel.getTotalDeuda());
            }

            tvwtotal.setText(moneySymbol + " " + decimalFormat.format(total));
            tvwtotal2.setText(moneySymbol + " " + decimalFormat.format(total));

            if (listListener != null) {
                listListener.setPorCobrarClientsCount(clientModelList.size());
            }
        } else {
            rvwClients.setAdapter(null);
            layoutFooter.setVisibility(View.GONE);
            rltContentList.setVisibility(View.GONE);
            rltNoClients.setVisibility(View.VISIBLE);

            if (listListener != null) {
                listListener.checkMaterialTapDeuda();
            }
        }
    }

    @Override
    public void onBusinessError(BusinessErrorModel errorModel) {
        showError("Error", errorModel.getParams().toString());
    }

    @Override
    public void onError(ErrorModel errorModel) {
        processError(errorModel);
    }

    /**********************************************************/

    @Override
    public void showMaterialTap() {
        //
    }

    /****************************************************************/

    @Override
    public void onLongClick(ClienteModel clienteModel, View v) {
        // EMPTY
    }

    /*****************************************************************/

    @Override
    public void onClienteModelClick(ClienteModel clienteModel) {

        String clientTotalDebt = "0";

        if (clienteModel.getTotalDeuda() != null)
            clientTotalDebt = "" + clienteModel.getTotalDeuda().toString();

        Intent intent = new Intent(getContext(), DebtPaymentHistoryActivity.class);
        intent.putExtra(GlobalConstant.CLIENTE_LOCAL_ID, clienteModel.getId());
        intent.putExtra(GlobalConstant.CLIENTE_ID, clienteModel.getClienteID());
        intent.putExtra(GlobalConstant.CLIENT_NAME, clienteModel.getNombres());
        intent.putExtra(GlobalConstant.CLIENT_TOTAL_DEBT, clientTotalDebt);
        startActivity(intent);
    }

    /****************************************************************/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClientListDownloaded(ClientsListEvent clientsListEvent) {
        clientsPresenter.getClientesPorCobrar();
    }

    @Override
    public void setCountryMoneySymbol(String countryMoneySymbol) {
        // EMPTY
    }

    public interface ClientListListener {

        void checkMaterialTapDeuda();

        void setPorCobrarClientsCount(int size);
    }

}
