package biz.belcorp.consultoras.feature.verifyaccount;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.dialog.MessageDialog;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.feature.terms.di.TermsComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.analytics.BelcorpAnalytics;

/**
 * @author andres.escobar on 3/08/2017.
 */
public class VerifyAccountFragment extends BaseFragment implements VerifyAccountView {

    @Inject
    VerifyAccountPresenter presenter;

    private VerifyAccountFragmentListener listener;

    @Override
    public Context context() {
        return null;
    }

    /**********************************************************/

    public VerifyAccountFragment() {
        super();
    }

    public static VerifyAccountFragment newInstance() {
        return new VerifyAccountFragment();
    }

    /**********************************************************************************************/

    @Override
    protected boolean onInjectView() {
        getComponent(TermsComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);
    }

    /**********************************************************************************************/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof VerifyAccountFragmentListener) {
            this.listener = (VerifyAccountFragmentListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public String applicationLabel(Context con, ApplicationInfo info) {
        PackageManager p = con.getPackageManager();
        return p.getApplicationLabel(info).toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(presenter != null) presenter.initScreenTrack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**********************************************************/

    /*********************************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {
        Bundle bundle = new Bundle();

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_TEMS);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    /*********************************************************************/

    interface VerifyAccountFragmentListener {

        void onHome();
    }

    /**********************************************************/

    private final MessageDialog.MessageDialogListener aceptarListener = new MessageDialog.MessageDialogListener() {
        @Override
        public void aceptar() {
            // EMPTY
        }

        @Override
        public void cancelar() {
            // EMPTY
        }
    };
}
