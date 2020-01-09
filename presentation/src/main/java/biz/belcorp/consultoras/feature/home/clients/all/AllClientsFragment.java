package biz.belcorp.consultoras.feature.home.clients.all;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.dialog.MessageDialog;
import biz.belcorp.consultoras.common.dialog.Tooltip;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.error.BusinessErrorModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.feature.client.card.ClientCardActivity;
import biz.belcorp.consultoras.feature.home.clients.BaseClientsFragment;
import biz.belcorp.consultoras.feature.home.clients.ClientsListAdapter;
import biz.belcorp.consultoras.feature.home.clients.ClientsListEvent;
import biz.belcorp.consultoras.feature.home.clients.ClientsListFragment;
import biz.belcorp.consultoras.feature.home.di.HomeComponent;
import biz.belcorp.consultoras.util.CommunicationUtils;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.KeyboardUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 */
public class AllClientsFragment extends BaseClientsFragment implements AllClientsView, ClientsListAdapter.OnItemSelectedListener {

    @Inject
    AllClientsPresenter presenter;

    @BindView(R.id.rlt_content_list_null)
    RelativeLayout rltNoClients;

    @BindView(R.id.rlt_content_list)
    RelativeLayout rltContentList;

    @BindView(R.id.edt_filter)
    EditText edtFilter;

    @BindView(R.id.ivw_search)
    ImageView ivwSearch;

    @BindView(R.id.rvw_clients)
    RecyclerView rvwClients;

    @BindView(R.id.tvw_clients_counter)
    TextView tvwClientsCounter;

    private ClientsListAdapter adapter;
    private ClientsListFragment.ClientListFragmentListener listener;

    private ClientListListener listListener;

    LoginModel loginModel;

    /**********************************************************/


    public AllClientsFragment() {
        super();
    }

    public static AllClientsFragment getInstance() {
        return new AllClientsFragment();
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clients_list_all, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.initScreenTrack();
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

    /**********************************************************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {
        this.loginModel = loginModel;
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
    public void showClients(List<ClienteModel> clientModelList) {
        if (!isVisible()) return;

        if (null != clientModelList && !clientModelList.isEmpty()) {

            String counter = getString(R.string.clients_list_all_clients) + " (" + clientModelList.size() + ")";
            tvwClientsCounter.setText(counter);

            adapter = new ClientsListAdapter(clientModelList, this);
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

            if (listListener != null) {
                listListener.setAllClientsCount(clientModelList.size());
            }

            textChange();
        } else {
            if (listListener != null) {
                listListener.setRippleState(true);
                listListener.checkMaterialTap();
            }

            rltContentList.setVisibility(View.GONE);
            rltNoClients.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDataUpdated(String mensaje) {
        if (listener != null) {
            listener.refreshClientsList();
        }
    }

    @Override
    public void onBusinessError(BusinessErrorModel errorModel) {
        showError("Error", errorModel.getParams().toString());
    }

    @Override
    public void onError(ErrorModel exception) {
        processError(exception);
    }

    /*****************************************************************/

    private void textChange() {
        edtFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                boolean edtState = cs.toString().trim().length() > 0;
                ivwSearch.setImageResource(edtState ? R.drawable.ic_close_black : R.drawable.ic_lupe_black);
                ivwSearch.setTag(edtState ? "1" : "0");

                AllClientsFragment.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // EMPTY
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // EMPTY
            }
        });
    }

    /****************************************************************/

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view == null) view = new View(getActivity());
        KeyboardUtil.dismissKeyboard(getActivity(), view);
    }

    @Override
    public void onClienteModelClick(ClienteModel clienteModel) {
        Intent intent = new Intent(getContext(), ClientCardActivity.class);
        intent.putExtra(GlobalConstant.CLIENTE_ID, clienteModel.getId());
        startActivity(intent);
    }

    @Override
    public void refreshClientCounter() {
        if (!isVisible()) return;

        if (adapter != null) {
            String counter = getString(R.string.clients_list_all_clients) + " (" + adapter.getItemCount() + ")";
            tvwClientsCounter.setText(counter);
        }
    }

    /****************************************************************/

    @Override
    public void onPhoneClick(ClienteModel clienteModel) {
        presenter.call(clienteModel, getActivity());
    }

    @Override
    public void onWhatsappClick(ClienteModel clienteModel) {
        if (!isVisible()) return;

        if (CommunicationUtils.isWhatsappInstalled(getContext())) {
            presenter.sendWhatsapp(clienteModel, getContext());
        } else {
            new AlertDialog.Builder(getContext())
                .setMessage(R.string.whatsapp_not_installed)
                .setPositiveButton(R.string.button_aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        CommunicationUtils.openWhatsappPlayStore(getContext());
                    }
                })
                .setNegativeButton(R.string.button_cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        dialog.dismiss();
                    }
                })
                .show();
        }
    }

    @Override
    public void onLongClick(ClienteModel clienteModel, View v) {
        if (!isVisible()) return;

        presenter.trackEvent(
            GlobalConstant.SCREEN_CLIENTS_ALL,
            GlobalConstant.EVENT_CAT_CLIENTS,
            GlobalConstant.EVENT_ACTION_CLICK_CLIENTS,
            GlobalConstant.EVENT_LABEL_CLIENT_LONG_CLICK,
            GlobalConstant.EVENT_NAME_CLIENT_LONG_CLICK);

        drawTooltipOptions(v, Tooltip.GRAVITY_TOP, 1, clienteModel, new OnPopupOptionSelectedListener() {
            @Override
            public void editar(ClienteModel clientemodel) {

                presenter.trackEvent(
                    GlobalConstant.SCREEN_CLIENTS_ALL,
                    GlobalConstant.EVENT_CAT_CLIENTS,
                    GlobalConstant.EVENT_ACTION_CLICK_CLIENTS,
                    GlobalConstant.EVENT_LABEL_CLIENT_EDIT,
                    GlobalConstant.EVENT_NAME_CLIENT_EDIT);

                if (listener != null) {
                    listener.editar(clientemodel);
                }
            }

            @Override
            public void eliminar(final ClienteModel clienteModel) {
                if (clienteModel.getCantidadPedido() == 0) {
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
                            .setListener(new MessageDialog.MessageDialogListener() {
                                @Override
                                public void aceptar() {
                                    presenter.trackEvent(
                                        GlobalConstant.SCREEN_CLIENTS_ALL,
                                        GlobalConstant.EVENT_CAT_CLIENTS,
                                        GlobalConstant.EVENT_ACTION_CLICK_CLIENTS,
                                        GlobalConstant.EVENT_LABEL_CLIENT_DELETE,
                                        GlobalConstant.EVENT_NAME_CLIENT_DELETE);

                                    presenter.eliminar(clienteModel, loginModel, AllClientsFragment.this);
                                }

                                @Override
                                public void cancelar() {
                                    // EMPTY
                                }
                            })
                            .show(getFragmentManager(), "modalDelete");
                    } catch (IllegalStateException e) {
                        BelcorpLogger.w("modalDelete", e);
                    }
                } else {
                    Toast.makeText(getContext(), R.string.clients_list_delete_with_order, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @OnClick(R.id.ivw_search)
    protected void onFilterClick(View v) {

        if (v.getTag().toString().equals("1")) {
            edtFilter.getText().clear();
            ((ImageView) v).setImageResource(R.drawable.ic_lupe_black);
        }
    }

    /****************************************************************/
    public void setListListener(ClientListListener listListener) {
        this.listListener = listListener;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClientListDownloaded(ClientsListEvent event) {
        if (presenter != null)
            presenter.getAllClientes();
    }

    public interface ClientListListener {
        void setRippleState(boolean state);

        void checkMaterialTap();

        int getCurrentPage();

        void setAllClientsCount(int size);
    }
}
