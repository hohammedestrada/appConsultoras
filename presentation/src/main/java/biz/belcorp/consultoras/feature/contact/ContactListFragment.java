package biz.belcorp.consultoras.feature.contact;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.dialog.MessageDialog;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.client.ClienteModel;
import biz.belcorp.consultoras.common.tracking.Tracker;
import biz.belcorp.consultoras.feature.contact.di.ContactComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.CommunicationUtils;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.analytics.annotation.AnalyticEvent;
import biz.belcorp.library.analytics.annotation.AnalyticScreen;
import biz.belcorp.library.log.BelcorpLogger;
import biz.belcorp.library.util.KeyboardUtil;
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

@AnalyticScreen(name = "ContactListScreen")
@RuntimePermissions
public class ContactListFragment extends BaseFragment implements ContactView, ContactListAdapter.OnItemSelectedListener {

    @Inject
    ContactPresenter presenter;

    @BindView(R.id.ivw_back)
    ImageView ivwBack;
    @BindView(R.id.tvw_counter)
    TextView tvwCounter;
    @BindView(R.id.tvw_select_all)
    TextView tvwSelectAll;
    @BindView(R.id.ivw_all)
    ImageView ivwAll;
    @BindView(R.id.btn_sync)
    Button btnSync;
    @BindView(R.id.edt_filter)
    EditText edtFilter;
    @BindView(R.id.ivw_search)
    ImageView ivwSearch;
    @BindView(R.id.rvw_contacts)
    RecyclerView rvwContacts;

    ContactListAdapter adapter;

    private Unbinder bind;
    private LoginModel loginModel;

    private Boolean selectAll = false;
    private Integer counterAllContacts = 0;

    /**********************************************************/

    public ContactListFragment() {
        super();
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() {
        getComponent(ContactComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);
        ContactListFragmentPermissionsDispatcher.readContactsWithPermissionCheck(this);
    }

    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    public void readContacts(){
        this.presenter.readContacts();
    }

    @OnShowRationale(Manifest.permission.READ_CONTACTS)
    void showRationaleForReadContacts(final PermissionRequest request) {
        new AlertDialog.Builder(getContext())
            .setMessage(R.string.permission_read_contacts_rationale)
            .setPositiveButton(R.string.button_aceptar, (dialog, button) -> request.proceed())
            .setNegativeButton(R.string.button_cancelar, (dialog, button) -> request.cancel())
            .show();
    }

    @OnPermissionDenied(Manifest.permission.READ_CONTACTS)
    void showDeniedForReadContacts() {
        Toast.makeText(getContext(), R.string.permission_read_contacts_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.READ_CONTACTS)
    void showNeverAskForReadContacts() {
        Toast.makeText(getContext(), R.string.permission_read_contacts_neverask, Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(getContext())
            .setMessage(R.string.permission_read_contacts_denied)
            .setPositiveButton(R.string.button_go_to_settings, (dialog, button) -> CommunicationUtils.goToSettings(getContext()))
            .setNegativeButton(R.string.button_cancelar, (dialog, button) -> dialog.dismiss())
            .show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ContactListFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**********************************************************/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact_list, container, false);
        bind = ButterKnife.bind(this, v);
        return v;
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

    @Override
    public void initScreenTrack(LoginModel loginModel) {
        this.loginModel = loginModel;

        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_CONTACT);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);
        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    @Override
    public void initScreenTrackAgregar(LoginModel loginModel) {
        Tracker.Clientes.trackImportarAgregar(loginModel);
    }

    @Override
    public void trackBackPressed(LoginModel loginModel) {

        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_CONTACT);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);

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

    /**********************************************************/

    @Override
    protected void showError(String title, String message) {
        try {
            new MessageDialog()
                .setIcon(R.drawable.ic_alerta, 0)
                .setResTitle(title)
                .setResMessage(message)
                .setStringAceptar(R.string.button_aceptar)
                .setListener(new MessageDialog.MessageDialogListener() {
                    @Override
                    public void aceptar() {
                        getActivity().onBackPressed();
                    }

                    @Override
                    public void cancelar() {
                        // EMPTY
                    }
                })
                .showIcon(true)
                .showClose(false)
                .show(getFragmentManager(), "modalError");
        } catch (Exception e) {
            BelcorpLogger.w("showError", e);
        }
    }

    /**********************************************************/

    @Override
    public void showContacts(List<ClienteModel> clientModelList) {

        if (null != clientModelList && !clientModelList.isEmpty()) {

            counterAllContacts = clientModelList.size();

            adapter = new ContactListAdapter(getContext(), clientModelList, this);
            rvwContacts.setAdapter(adapter);
            rvwContacts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            rvwContacts.setHasFixedSize(true);

            rvwContacts.setOnTouchListener((v, event) -> {
                View view = getActivity().getCurrentFocus();
                if (view == null)
                    view = new View(getActivity());

                KeyboardUtil.dismissKeyboard(context(), view);
                return false;
            });

            textChange();
        } else {

            tvwSelectAll.setEnabled(false);
            btnSync.setEnabled(false);
        }
    }


    @Override
    public void saved(Boolean result) {
        try {
            new MessageDialog()
                .setIcon(R.drawable.ic_add_from_contact, 0)
                .setStringTitle(R.string.contact_list_dg_add_title)
                .setStringMessage(R.string.contact_list_dg_add_message)
                .setStringAceptar(R.string.button_aceptar)
                .showIcon(true)
                .showClose(false)
                .showCancel(false)
                .setListener(dgNewClientsListener)
                .show(getFragmentManager(), "modalNewClients");
        } catch (IllegalStateException e) {
            BelcorpLogger.w("modalNewClients", e);
        }

    }

    @Override
    public void onError(Throwable exception) {
        processError(exception);
    }

    /*****************************************************************/

    @Override
    public void refreshContactCounter(int count) {
        String counter = Integer.toString(count);
        btnSync.setTextColor(ContextCompat.getColor(context(), R.color.primary));
        btnSync.setEnabled(true);
        if (count == 0) {
            counter = "";
            btnSync.setTextColor(ContextCompat.getColor(context(), R.color.button_disabled));
            btnSync.setEnabled(false);
        }

        if (count == counterAllContacts) {
            tvwSelectAll.setTextColor(ContextCompat.getColor(context(), R.color.primary));
            ivwAll.setImageDrawable(VectorDrawableCompat.create(getContext().getResources()
                , R.drawable.ic_check_selector, null));
            selectAll = true;
        } else {
            tvwSelectAll.setTextColor(ContextCompat.getColor(context(), R.color.button_disabled));
            ivwAll.setImageDrawable(VectorDrawableCompat.create(getContext().getResources()
                , R.drawable.ic_check_selector_gray, null));
            selectAll = false;
        }

        tvwCounter.setText(counter);
    }

    /*****************************************************************/

    private void textChange() {
        edtFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                boolean edtState = cs.toString().trim().length() > 0;
                ivwSearch.setImageResource(edtState ? R.drawable.ic_close_black : R.drawable.ic_lupe_black);
                ivwSearch.setTag(edtState ? "1" : "0");

                ContactListFragment.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // EMPTY
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // EMPTY
            }
        });
    }

    /****************************************************************/

    @OnClick(R.id.rlt_select_all)
    @AnalyticEvent(action = "OnSelectAllClick", category = "Click")
    public void unSelectAll() {
        if (adapter != null) {
            if (!selectAll) {
                selectAll = true;
                adapter.selectAll();
                int count = adapter.getNumberSelectedContacts();
                String counter = Integer.toString(count);
                tvwCounter.setText(counter);
                btnSync.setTextColor(ContextCompat.getColor(context(), R.color.primary));
                btnSync.setEnabled(true);
                tvwSelectAll.setTextColor(ContextCompat.getColor(context(), R.color.primary));
                ivwAll.setImageDrawable(VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_check_selector, null));
            } else {
                selectAll = false;
                tvwCounter.setText("");
                btnSync.setTextColor(ContextCompat.getColor(context(), R.color.button_disabled));
                btnSync.setEnabled(false);
                tvwSelectAll.setTextColor(ContextCompat.getColor(context(), R.color.button_disabled));
                ivwAll.setImageDrawable(VectorDrawableCompat.create(getContext().getResources(), R.drawable.ic_check_selector_gray, null));
                adapter.unselectAll();
            }
        }
    }

    @OnClick(R.id.ivw_back)
    @AnalyticEvent(action = "OnBackClick", category = "Click")
    public void back() {
        presenter.trackBackPressed();
    }

    @OnClick(R.id.btn_sync)
    @AnalyticEvent(action = "OnAddNewClientsClick", category = "Click")
    public void addNewClients() {
        List<ClienteModel> list = null;
        if (null != adapter) list = adapter.getContactsSelect();

        if (null != list && !list.isEmpty()) {
            presenter.save(list, loginModel);
            presenter.initScreenTrackAgregar();
        } else {
            Toast.makeText(context(), R.string.contact_list_no_contacts_selected, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.ivw_search)
    @AnalyticEvent(action = "OnFilterClick", category = "Click")
    protected void onFilterClick(View v) {

        if (v.getTag().toString().equals("1")) {
            edtFilter.getText().clear();
            ((ImageView) v).setImageResource(R.drawable.ic_lupe_black);
        }
    }

    /**********************************************************/

    final MessageDialog.MessageDialogListener dgNewClientsListener = new MessageDialog.MessageDialogListener() {
        @Override
        public void aceptar() {
            back();
        }

        @Override
        public void cancelar() {
            // EMPTY
        }
    };

}
