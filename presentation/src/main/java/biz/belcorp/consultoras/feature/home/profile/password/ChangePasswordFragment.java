package biz.belcorp.consultoras.feature.home.profile.password;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.error.ErrorModel;
import biz.belcorp.consultoras.common.model.error.BooleanDtoModel;
import biz.belcorp.consultoras.domain.entity.Country;
import biz.belcorp.consultoras.domain.entity.PasswordUpdateRequest;
import biz.belcorp.consultoras.domain.entity.User;
import biz.belcorp.consultoras.feature.home.profile.MyProfilePresenter;
import biz.belcorp.consultoras.feature.home.profile.MyProfileView;
import biz.belcorp.consultoras.feature.home.profile.di.MyProfileComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChangePasswordFragment extends BaseFragment implements MyProfileView {

    public static final String ENABLE_OLD_PSW = "ENABLE_OLD_PSW";

    @Inject
    MyProfilePresenter presenter;

    @BindView(R.id.tvwTitle1)
    TextView tvwTitle1;
    @BindView(R.id.tvwTitle2)
    TextView tvwTitle2;
    @BindView(R.id.tvw_old_password)
    TextView tvwOldPassword;
    @BindView(R.id.edt_old_password)
    EditText edtOldPassword;
    @BindView(R.id.edt_new_password)
    EditText edtNewPassword;
    @BindView(R.id.edt_repeat_new_password)
    EditText edtRepeatNewPassword;
    @BindView(R.id.tvw_cancelar)
    TextView tvwCancelar;
    @BindView(R.id.tvw_separator)
    View separator;
    @BindView(R.id.btn_change_password)
    Button btnChangePassword;


    ChangePasswordListener listener;
    LoginModel loginModel;
    Unbinder unbinder;

    boolean visibleNewPassword = false;
    boolean visibleRepeatPassword = false;

    /**********************************************************/

    public ChangePasswordFragment() {
        super();
    }

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    /**********************************************************/

    @Override
    protected boolean onInjectView() {
         getComponent(MyProfileComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);
        init();
    }

    /**********************************************************/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChangePasswordListener) {
            listener = (ChangePasswordListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.initScreenTrack();
    }

    @Override
    public Context context() {
        FragmentActivity activity = getActivity();
        if (activity != null)
            return activity.getApplicationContext();
        else
            return null;
    }

    /** */

    private void init() {
        tvwCancelar.setPaintFlags(tvwCancelar.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if(getArguments() != null && !getArguments().getBoolean(ENABLE_OLD_PSW, true)){
            tvwTitle1.setVisibility(View.VISIBLE);
            tvwTitle2.setVisibility(View.VISIBLE);
            tvwOldPassword.setVisibility(View.GONE);
            edtOldPassword.setVisibility(View.GONE);
            tvwCancelar.setVisibility(View.GONE);
            separator.setVisibility(View.GONE);
            btnChangePassword.setText(getString(R.string.my_profile_cambiar));

            float w = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());

            btnChangePassword.setLayoutParams(new LinearLayout.LayoutParams((int) w, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    /** */

    @Override
    public void initScreenTrack(LoginModel loginModel) {
        this.loginModel = loginModel;

        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_MI_PERFIL_EDITAR);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);
        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    @Override
    public void showUserData(User user, int update) {
        // EMPTY
    }

    @Override
    public void saveUserData(Boolean status) {
        // EMPTY
    }

    @Override
    public void saveUpload(Boolean status) {
        // EMPTY
    }

    @Override
    public void saveDelete(Boolean status) {
        // EMPTY
    }

    @Override
    public void savePassword() {
        Toast.makeText(getActivity(), getString(R.string.my_profile_successful), Toast.LENGTH_SHORT).show();

        if (listener != null) {
            listener.onPasswordSaved();
        }
    }

    @Override
    public void onError(ErrorModel errorModel) {
        processGeneralError(errorModel);
    }

    @Override
    public void onError(BooleanDtoModel errorModel) {
        showError("ERROR", errorModel.getMessage());
    }

    @OnClick(R.id.ivw_new_password_show)
    void onShowNewPassword() {
        visibleNewPassword = !visibleNewPassword;

        if (visibleNewPassword) {
            edtNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            edtNewPassword.setInputType(129);
        }

        edtNewPassword.setSelection(edtNewPassword.getText().toString().length());
    }

    @OnClick(R.id.ivw_repeat_password_show)
    void onShowRepeatNewPassword() {
        visibleRepeatPassword = !visibleRepeatPassword;

        if (visibleRepeatPassword) {
            edtRepeatNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            edtRepeatNewPassword.setInputType(129);
        }

        edtRepeatNewPassword.setSelection(edtRepeatNewPassword.getText().toString().length());
    }

    @OnClick(R.id.tvw_cancelar)
    public void cancelar() {
        if (listener != null) {
            listener.onPasswordSaved();
        }
    }

    @OnClick(R.id.btn_change_password)
    public void changePassword() {
        hideKeyboard();

        String password = edtOldPassword.getText().toString();
        String newPassword = edtNewPassword.getText().toString();
        String repeatNewPassword = edtRepeatNewPassword.getText().toString();

        boolean enableOldPassword = true;

        if(getArguments() != null) {
            enableOldPassword = getArguments().getBoolean(ENABLE_OLD_PSW, true);
        }

        if(enableOldPassword) {
            if (password.isEmpty()) {
                Toast.makeText(getActivity(), getString(R.string.my_profile_empty_password), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (newPassword.isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.my_profile_empty_new_password), Toast.LENGTH_SHORT).show();
            return;
        }
        if (newPassword.length() < 7) {
            Toast.makeText(getActivity(), getString(R.string.my_profile_new_password_validation), Toast.LENGTH_SHORT).show();
            return;
        }

        if (repeatNewPassword.isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.my_profile_empty_repeat_new_password), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(repeatNewPassword)) {
            Toast.makeText(getActivity(), getString(R.string.my_profile_error_password), Toast.LENGTH_SHORT).show();
            return;
        }

        PasswordUpdateRequest request = new PasswordUpdateRequest();
        request.setAnteriorContrasenia(password);
        request.setNuevaContrasenia(newPassword);
        presenter.changePassword(request);
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) context().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        if (view == null) view = new View(getActivity());
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**********************************************************/

    @Override
    public void trackBackPressed(LoginModel loginModel) {
        Bundle analytics = new Bundle();
        analytics.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_MI_PERFIL_EDITAR);
        analytics.putString(GlobalConstant.EVENT_VAR_CATEGORY, GlobalConstant.EVENT_CAT_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_ACTION, GlobalConstant.EVENT_ACTION_BACK);
        analytics.putString(GlobalConstant.EVENT_VAR_LABEL, GlobalConstant.EVENT_LABEL_NOT_AVAILABLE);
        analytics.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);
        BelcorpAnalytics.trackEvent(GlobalConstant.EVENT_BACK, analytics, properties);
    }

    public void trackBackPressed() {
        presenter.trackBackPressed();
    }

    @Override
    public void saveUserCountry(Country country) {
        // EMPTY
    }

    @Override
    public void activeCellphone() {

    }

    @Override
    public void activateCheckWhatsapp(boolean mostrar) {

    }

    @Override
    public void goToVerifyEmail() {

    }

    /************************************************************************************/

    public interface ChangePasswordListener {
        void onPasswordSaved();
    }
}
