package biz.belcorp.consultoras.feature.home.clients.pedido;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uxcam.UXCam;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.dialog.MessageDialog;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.error.BusinessErrorModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.model.user.UserModel;
import biz.belcorp.consultoras.feature.client.card.ClientCardActivity;
import biz.belcorp.consultoras.feature.home.clients.BaseClientsFragment;
import biz.belcorp.consultoras.feature.home.clients.ClientsListEvent;
import biz.belcorp.consultoras.feature.home.clients.ClientsListFragment;
import biz.belcorp.consultoras.feature.home.di.HomeComponent;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.UXCamUtils;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.CountryUtil;
import biz.belcorp.library.util.NetworkUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */

public class PedidoClientsFragment extends BaseClientsFragment implements PedidoClientsView, PedidoClientsListAdapter.OnItemSelectedListener {

    @Inject
    PedidoClientsPresenter presenter;

    @BindView(R.id.rvw_clients)
    RecyclerView rvwClients;

    @BindView(R.id.rlt_content_list_null)
    RelativeLayout rltNoClients;

    @BindView(R.id.rlt_content)
    RelativeLayout rltContentList;

    @BindView(R.id.tvw_clients_counter)
    TextView tvwClientsCounter;

    private PedidoClientsListAdapter adapter;
    private ClientsListFragment.ClientListFragmentListener listener;

    /**********************************************************/

    public PedidoClientsFragment() {
        super();
    }

    public static PedidoClientsFragment newInstance() {
        return new PedidoClientsFragment();
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
        this.presenter.attachView(this);
    }

    /**********************************************************/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ClientsListFragment.ClientListFragmentListener) {
            this.listener = (ClientsListFragment.ClientListFragmentListener) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        UXCam.tagScreenName(UXCamUtils.PedidoClientsFragmentName);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clients_list_pedido, container, false);
        ButterKnife.bind(this, view);
        return view;
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

            //Clientes con pedido en C16
            String counter = getString(R.string.clients_list_pedido) + " (" + clientModelList.size() + ")";

            if (null != model.getCampaing() && model.getCampaing().length() == 6)
                counter = String.format(getString(R.string.clients_list_order_title_campaing), model.getCampaing().substring(4));

            if (isVisible()) {
                tvwClientsCounter.setText(counter);
            }

            adapter = new PedidoClientsListAdapter(getContext(), clientModelList, this, moneySymbol, decimalFormat);
            rvwClients.setAdapter(adapter);
            rvwClients.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            rvwClients.setHasFixedSize(true);

            rvwClients.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    hideKeyboard();
                    return false;
                }
            });

            rltContentList.setVisibility(View.VISIBLE);
            rltNoClients.setVisibility(View.GONE);
        } else {
            rltContentList.setVisibility(View.GONE);
            rltNoClients.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBusinessError(BusinessErrorModel errorModel) {
        showError("Error", errorModel.getParams().toString());
    }

    @Override
    public void onError(ErrorModel errorModel) {
        processGeneralError(errorModel);
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

    /****************************************************************/

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) context().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        if (view == null) view = new View(getActivity());
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClienteModelClick(ClienteModel clienteModel) {

        if (NetworkUtil.isThereInternetConnection(context())) {
            Intent intent = new Intent(getContext(), ClientCardActivity.class);
            intent.putExtra(GlobalConstant.CLIENTE_ID, clienteModel.getId());
            intent.putExtra(GlobalConstant.CLIENTE_LOCAL_ID, clienteModel.getClienteID());
            intent.putExtra(GlobalConstant.CLIENT_PEDIDO, true);
            startActivity(intent);
        } else {
            showErrorNetwork();
        }
    }

    @Override
    public void onLongClick(ClienteModel clienteModel, View v) {
        // EMPTY
    }

    private void showErrorNetwork() {
        try {
            new MessageDialog()
                .setIcon(R.drawable.ic_network_error, 1)
                .setStringTitle(R.string.error_network_title)
                .setStringMessage(R.string.error_network_message)
                .setStringAceptar(R.string.button_aceptar)
                .showIcon(true)
                .showClose(false)
                .show(getFragmentManager(), "modalError");
        } catch (IllegalStateException e) {
            BelcorpLogger.w("modalError", e);
        }
    }

    /****************************************************************/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClientListDownloaded(ClientsListEvent clientsListEvent) {
        presenter.getClientesPedidos();
    }
}
