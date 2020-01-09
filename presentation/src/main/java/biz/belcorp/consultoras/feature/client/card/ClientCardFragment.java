package biz.belcorp.consultoras.feature.client.card;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Map;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.dialog.MessageDialog;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.AnotacionModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.model.client.ContactoModel;
import biz.belcorp.consultoras.common.model.menu.MenuModel;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.feature.client.di.ClientComponent;
import biz.belcorp.consultoras.feature.client.note.NoteActivity;
import biz.belcorp.consultoras.feature.client.note.NotesAdapter;
import biz.belcorp.consultoras.feature.client.note.NotesView;
import biz.belcorp.consultoras.feature.client.order.ClientOrderWebActivity;
import biz.belcorp.consultoras.feature.client.order.history.ClientOrderHistoryWebActivity;
import biz.belcorp.consultoras.feature.history.DebtPaymentHistoryActivity;
import biz.belcorp.consultoras.feature.home.addorders.AddOrdersActivity;
import biz.belcorp.consultoras.feature.search.single.SearchProductActivity;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.consultoras.util.ViewUtils;
import biz.belcorp.consultoras.util.anotation.ContactType;
import biz.belcorp.consultoras.util.anotation.MenuCodeTop;
import biz.belcorp.consultoras.util.anotation.StatusType;
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

public class ClientCardFragment extends BaseFragment implements ClientCardView, NotesAdapter.OnItemSelectedListener {

    private static final String TAG = "ClientCardFragment";

    @Inject
    ClientCardPresenter presenter;

    @BindView(R.id.llt_client_card)
    LinearLayout lltClientCard;
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
    @BindView(R.id.tvw_mobile)
    TextView tvwMobile;
    @BindView(R.id.tvw_phone)
    TextView tvwPhone;
    @BindView(R.id.tvw_email)
    TextView tvwEmail;
    @BindView(R.id.tvw_birthday)
    TextView tvwBirthday;
    @BindView(R.id.tvw_personal_address)
    TextView tvwPersonalAddress;
    @BindView(R.id.tvw_address)
    TextView tvwAddress;
    @BindView(R.id.tvw_reference)
    TextView tvwReference;
    @BindView(R.id.tvw_new_data)
    TextView tvwNewData;
    @BindView(R.id.ivw_arrow_data)
    ImageView ivwArrowData;
    @BindView(R.id.ivw_arrow_annotations)
    ImageView ivwArrowAnnotations;
    @BindView(R.id.tvw_item_detail_deuda)
    TextView tvwDetalleDeuda;
    @BindView(R.id.tvw_item_detail_pedido)
    TextView tvwDetallePedido;
    @BindView(R.id.vw_notes)
    NotesView notesView;
    @BindView(R.id.llt_nueva_nota)
    LinearLayout lltNuevaNota;
    @BindView(R.id.tvw_note_label)
    TextView tvwNoteLabel;

    private Unbinder bind;

    private ClientCardFragmentListener listener;
    private ClienteModel clientModel;
    private boolean showByDefault;
    private boolean isViewOrder;
    private int clientID = -1;

    private static final int ORDER_RESULT = 1000;

    /**********************************************************/

    public ClientCardFragment() {
        super();
    }

    /**********************************************************/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ClientCardFragmentListener) {
            this.listener = (ClientCardFragmentListener) context;
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

        int clientServerID = -1;

        if (getArguments() != null) {
            clientID = getArguments().getInt(GlobalConstant.CLIENTE_ID, -1);
            clientServerID = getArguments().getInt(GlobalConstant.CLIENTE_LOCAL_ID, -1);
            isViewOrder = getArguments().getBoolean(GlobalConstant.CLIENT_PEDIDO, false);
        }

        if (isViewOrder) {
            if (NetworkUtil.isThereInternetConnection(context())) {
                Intent intent = new Intent(getActivity(), ClientOrderHistoryWebActivity.class);
                intent.putExtra(GlobalConstant.CLIENTE_ID, clientServerID);
                startActivityForResult(intent, ORDER_RESULT);
            } else {
                showErrorNetwork();
            }
        }

        if (showByDefault) {
            showHidePersonalData();
            showHideAnnotations();
        }
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_card, container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(presenter != null) {
            presenter.load(clientID);
            presenter.initScreenTrack();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 150 && resultCode == Activity.RESULT_OK && data != null) {
            int change = data.getIntExtra("change", 0);
            if (change == 1) {
                Snackbar snackbar = Snackbar
                    .make(lltClientCard, R.string.client_update_message, Snackbar.LENGTH_LONG);
                snackbar.show();
            } else if (change == 2 && listener != null) {
                listener.onClientDeleted();
            }
        } else if (requestCode == ORDER_RESULT && null != clientModel)
            presenter.loadOnline(clientModel.getClienteID(), clientModel.getId());

    }

    /**********************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {

        Bundle bundle = new Bundle();

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_CLIENT_CARD);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    @Override
    public void initScreenTrackGestionarDeuda(LoginModel loginModel) {
        Tracker.Clientes.trackFichaGestionarDeuda(loginModel);
    }

    @Override
    public void initScreenTrackIngresarPedido(LoginModel loginModel) {
        Tracker.Clientes.trackFichaIngresarPedido(loginModel);
    }

    @Override
    public void initScreenTrackRevisarPedidos(LoginModel loginModel) {
        Tracker.Clientes.trackFichaRevisarPedidos(loginModel);
    }

    @Override
    public void trackBackPressed(LoginModel loginModel) {
        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_CLIENT_CARD);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);

        getActivity().finish();
    }

    @Override
    public void showMaximumNoteAmount(int maxNoteAmount) {
        if (isVisible())
            tvwNoteLabel.setText(String.format(getString(R.string.client_card_annotations_message), maxNoteAmount));
    }

    @Override
    public void setMenuModel(MenuModel menu) {
        if (null != menu) {
            if(menu.isVisible() && menu.getCodigo().equals(MenuCodeTop.ORDERS)) {
                Intent intent = new Intent(getActivity(), ClientOrderWebActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(GlobalConstant.CLIENTE_ID, clientModel.getClienteID());
                startActivityForResult(intent, ORDER_RESULT);
            } else {
                Intent intent = new Intent(getActivity(), AddOrdersActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putInt(GlobalConstant.CLIENTE_ID, clientModel.getClienteID());
                String nombres = clientModel.getNombres() != null ? clientModel.getNombres() : "";
                String apellidos = clientModel.getApellidos() != null ? clientModel.getApellidos() : "";
                bundle.putParcelable(SearchProductActivity.EXTRA_CLIENTEMODEL, clientModel);
                bundle.putString(GlobalConstant.CLIENT_NAME, (nombres+" "+apellidos).trim());
                bundle.putBoolean(GlobalConstant.FROM_CLIENT_CARD, true);
                intent.putExtras(bundle);
                startActivityForResult(intent, ORDER_RESULT);
            }
            presenter.initScreenTrackIngresarPedido();
        }
    }

    public void trackBackPressed() {
        if (presenter != null)
            presenter.trackBackPressed();
        else
            getActivity().finish();
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

    /**********************************************************/

    @Override
    public void showClient(ClienteModel model, String iso, String moneySymbol, int maxNoteAmount) {
        clientModel = model;
        Map<Integer, ContactoModel> map = model.getContactoModelMap();

        if (listener != null) {
            listener.onClientSet(model);
        }

        BigDecimal totalDeuda = BigDecimal.ZERO;
        DecimalFormat df = CountryUtil.getDecimalFormatByISO(iso, true);
        if (model.getTotalDeuda() != null) totalDeuda = model.getTotalDeuda();

        String debt = moneySymbol + " " + df.format(totalDeuda);
        tvwDetalleDeuda.setText(getString(R.string.client_te_debe) + "\n" + debt);
        tvwDetallePedido.setText(getResources().getQuantityString(R.plurals.client_ha_pedido, model.getCantidadPedido(), model.getCantidadPedido()));

        if (null != model.getFechaNacimiento() && !"".equals(model.getFechaNacimiento())) {

            try {
                String fechaNacimiento = model.getFechaNacimiento();

                String birthday = StringUtil.capitalize(DateUtil.convertFechaToString(
                    DateUtil.convertirDDMMAAAAtoDate(fechaNacimiento), "dd MMM"));
                tvwBirthday.setText(birthday);
            } catch (ParseException e) {
                BelcorpLogger.w(TAG, "Fecha de cumpleaños", e.getMessage());
            }
        }

        tvwMobile.setText("");
        tvwPhone.setText("");
        tvwEmail.setText("");
        tvwAddress.setText("");
        tvwReference.setText("");

        if (map.get(ContactType.MOBILE) != null)
            tvwMobile.setText(map.get(ContactType.MOBILE).getValor());
        if (map.get(ContactType.PHONE) != null)
            tvwPhone.setText(map.get(ContactType.PHONE).getValor());
        if (map.get(ContactType.EMAIL) != null)
            tvwEmail.setText(map.get(ContactType.EMAIL).getValor());
        if (map.get(ContactType.ADDRESS) != null)
            tvwAddress.setText(map.get(ContactType.ADDRESS).getValor());
        if (map.get(ContactType.REFERENCE) != null)
            tvwReference.setText(map.get(ContactType.REFERENCE).getValor());

        Drawable img = ContextCompat.getDrawable(getContext(), R.drawable.ic_star_filled);
        int newWidth = (int) ViewUtils.convertDpToPixel(20, getContext());
        int newHeight = (int) ViewUtils.convertDpToPixel(20, getContext());
        img = ViewUtils.changeDrawableSize(img, newWidth, newHeight, getContext());
        img.setBounds(60, 0, 0, 60);
        Integer tipoContactoFavorito = model.getTipoContactoFavorito();
        if (null != tipoContactoFavorito) {
            switch (tipoContactoFavorito) {
                case ContactType.MOBILE:
                    tvwMobile.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    tvwPhone.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    break;
                case ContactType.PHONE:
                    tvwMobile.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    tvwPhone.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    break;
                default:
                    tvwMobile.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    tvwPhone.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }

        lltNuevaNota.setVisibility(model.getAnotacionModels().size() >= maxNoteAmount ? View.GONE : View.VISIBLE);
        notesView.setNestedScrollingEnabled(false);
        notesView.refreshNotes(model.getAnotacionModels());
        notesView.setListener(this);
    }

    @Override
    public void onError(Throwable exception) {
        processError(exception);
    }

    @Override
    public void saved(Boolean result) {
        if (listener != null) {
            listener.onClientSaved(result);
        }
    }

    @Override
    public void anotacionDeleted(AnotacionModel anotacionModel) {
        presenter.load(clientID);
    }

    @Override
    public void onAnotacionItemSelected(AnotacionModel anotacionModel, int position) {
        String name = clientModel.getNombres() + (clientModel.getApellidos() != null ? " " + clientModel.getApellidos() : "");

        Intent intent = new Intent(getContext(), NoteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(NoteActivity.ACCION, NoteActivity.CLIENTE_EXISTENTE);
        intent.putExtra(NoteActivity.NOTA_ID, anotacionModel.getId());
        intent.putExtra(NoteActivity.CLIENT_ID, clientModel.getClienteID());
        intent.putExtra(NoteActivity.CLIENT_LOCAL_ID, clientModel.getId());
        intent.putExtra(NoteActivity.CLIENT_NAME, name);

        getActivity().startActivityForResult(intent, NoteActivity.REQUEST_CODE);
    }

    @Override
    public void onEliminarSelected(AnotacionModel anotacionModel, int adapterPosition) {
        anotacionModel.setEstado(StatusType.DELETE);
        presenter.deleteAnotacion(anotacionModel);

    }

    /**********************************************************/

    @OnClick(R.id.llt_nueva_nota)
    public void newAnnotation() {

        String name = clientModel.getNombres() + (clientModel.getApellidos() != null ? " " + clientModel.getApellidos() : "");
        Intent intent = new Intent(getContext(), NoteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(NoteActivity.ACCION, NoteActivity.CLIENTE_EXISTENTE);
        intent.putExtra(NoteActivity.NOTA_ID, 0);
        intent.putExtra(NoteActivity.CLIENT_ID, clientModel.getClienteID());
        intent.putExtra(NoteActivity.CLIENT_LOCAL_ID, clientModel.getId());
        intent.putExtra(NoteActivity.CLIENT_NAME, name);
        getActivity().startActivityForResult(intent, NoteActivity.REQUEST_CODE);
    }

    @OnClick(R.id.rlt_annotations)
    public void showHideAnnotations() {

        if (lltContentAnnotations.getVisibility() == View.VISIBLE) {
            lltContentAnnotations.setVisibility(View.GONE);
            ivwArrowAnnotations.setImageDrawable(VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_arrow_down_black, null));
            lltAnnotations.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_box_card_view_client_inactive));
        } else {
            lltContentAnnotations.setVisibility(View.VISIBLE);
            ivwArrowAnnotations.setImageDrawable(VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_arrow_up_black, null));
            lltAnnotations.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_box_card_view_client));
        }
    }

    @OnClick(R.id.rlt_personal_data)
    public void showHidePersonalData() {

        if (lltContentPersonalData.getVisibility() == View.VISIBLE) {
            lltContentPersonalData.setVisibility(View.GONE);
            ivwArrowData.setImageDrawable(VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_arrow_down_black, null));
            lltPersonalData.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_box_card_view_client_inactive));
        } else {
            lltContentPersonalData.setVisibility(View.VISIBLE);
            ivwArrowData.setImageDrawable(VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_arrow_up_black, null));
            lltPersonalData.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_box_card_view_client));
        }
    }

    @OnClick(R.id.layoutVerPedidos)
    public void showOrders() {

        if (NetworkUtil.isThereInternetConnection(context())) {
            Intent intent = new Intent(getActivity(), ClientOrderHistoryWebActivity.class);
            intent.putExtra(GlobalConstant.CLIENTE_ID, clientModel.getClienteID());
            startActivityForResult(intent, ORDER_RESULT);
            presenter.initScreenTrackRevisarPedidos();
        } else {
            showErrorNetwork();
        }
    }

    @OnClick(R.id.layoutIngresaPedido)
    public void newOrder() {
        if (clientModel == null)
            showReloadAlert();

        if (NetworkUtil.isThereInternetConnection(context()) && null != presenter) {
            presenter.getMenuActive(MenuCodeTop.ORDERS, MenuCodeTop.ORDERS_NATIVE);
        } else {
            showErrorNetwork();
        }
    }

    @OnClick(R.id.layoutCobraDeuda)
    public void transactions() {
        String clientTotalDebt = "0";

        if (clientModel.getTotalDeuda() != null)
            clientTotalDebt = "" + clientModel.getTotalDeuda().toString();

        Intent intent = new Intent(getActivity(), DebtPaymentHistoryActivity.class);
        intent.putExtra(GlobalConstant.CLIENTE_ID, clientModel.getClienteID());
        intent.putExtra(GlobalConstant.CLIENTE_LOCAL_ID, clientModel.getId());
        intent.putExtra(GlobalConstant.CLIENT_NAME, clientModel.getNombres());
        intent.putExtra(GlobalConstant.CLIENT_TOTAL_DEBT, clientTotalDebt);
        startActivity(intent);

        presenter.initScreenTrackGestionarDeuda();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (bind != null)
            bind.unbind();
        if (presenter != null)
            presenter.destroy();
    }

    public void saveFavorite(ClienteModel clienteModel) {
        presenter.saveFavorite(clienteModel);
    }

    public void showAnnotationsByDefault() {
        showByDefault = true;
    }

    public void refreshData() {
        presenter.load(clientID);
    }

    private void showReloadAlert() {
        MessageDialog dialog = new MessageDialog()
            .setIcon(R.drawable.ic_alerta, 0)
            .setStringTitle(R.string.client_dg_reload_title)
            .setStringMessage(R.string.client_dg_reload_message)
            .setStringAceptar(R.string.button_aceptar)
            .setStringCancelar(R.string.button_cancelar)
            .showIcon(true)
            .showClose(false)
            .showCancel(false)
            .setListener(new MessageDialog.MessageDialogListener() {
                @Override
                public void aceptar() {
                    presenter.load(clientID);
                }

                @Override
                public void cancelar() {
                    // EMPTY
                }
            });

        dialog.setCancelable(false);
        dialog.show(getFragmentManager(), "modalDelete");
    }

    /**********************************************************/

    interface ClientCardFragmentListener {

        void onClientSet(ClienteModel clienteModel);

        void onClientSaved(Boolean result);

        void onClientDeleted();
    }
}
