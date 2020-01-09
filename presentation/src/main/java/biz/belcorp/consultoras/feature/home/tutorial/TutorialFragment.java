package biz.belcorp.consultoras.feature.home.tutorial;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.BuildConfig;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseFragment;
import biz.belcorp.consultoras.common.component.ViewPagerIndicator;
import biz.belcorp.consultoras.common.dialog.IntrigueDialog;
import biz.belcorp.consultoras.common.dialog.RenewDialog;
import biz.belcorp.consultoras.common.model.auth.LoginModel;
import biz.belcorp.consultoras.common.model.tutorial.TutorialModel;
import biz.belcorp.consultoras.data.manager.SessionManager;
import biz.belcorp.consultoras.feature.home.tutorial.di.TutorialComponent;
import biz.belcorp.consultoras.util.AnalyticsUtil;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.analytics.BelcorpAnalytics;
import biz.belcorp.library.annotation.Country;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *
 */
public class TutorialFragment extends BaseFragment implements TutorialView {

    public static final String USERCODE = "userCode";
    public static final String PAIS = "pais";
    public static final String CR = "CR";

    private String selectedCountry = "PE";

    @Inject
    TutorialPresenter presenter;

    @BindView(R.id.vwp_tutorial)
    ViewPager vwpTutorial;
    @BindView(R.id.vpi_tutorial)
    ViewPagerIndicator vpiTutorial;
    @BindView(R.id.btn_tutorial_siguiente)
    TextView tvwSiguiente;

    private TutorialPagerAdapter adapter;
    private TutorialFragmentListener listener;
    private Unbinder unbinder;

    private Boolean firstImgLoaded = false;
    private Boolean secondImgLoaded = false;

    public static TutorialFragment newInstance(Bundle bundle) {
        TutorialFragment fragment = new TutorialFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TutorialFragmentListener) {
            this.listener = (TutorialFragmentListener) context;
        }
    }

    /**********************************************************************************************/

    @Override
    protected boolean onInjectView() {
        getComponent(TutorialComponent.class).inject(this);
        return true;
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);
        this.presenter.attachView(this);
    }

    /**********************************************************************************************/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null)
            selectedCountry = getArguments().getString(PAIS, "PE");

        adapter = new TutorialPagerAdapter(getChildFragmentManager(), getTutorialFragmentList(getTutorialModelList()));
        vwpTutorial.setAdapter(adapter);

        vpiTutorial.setupWithViewPager(vwpTutorial);

        vwpTutorial.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // EMPTY
            }

            @Override
            public void onPageSelected(int position) {
                presenter.initScreenTrackPosition(position);
                if (vwpTutorial.getCurrentItem() + 1 == adapter.getCount()) {
                    tvwSiguiente.setText(R.string.tutorial_ingresar);
                } else {
                    tvwSiguiente.setText(R.string.tutorial_siguiente);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // EMPTY
            }
        });

        hideTutorial();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(presenter != null) presenter.initScreenTrack();
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null)
            unbinder.unbind();

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (presenter != null)
            presenter.destroy();

        super.onDestroy();
    }

    /**********************************************************************************************/

    @Override
    public Context context() {
        FragmentActivity activity = getActivity();
        if (activity != null)
            return activity.getApplicationContext();
        else
            return null;
    }

    /**********************************************************************************************/

    @Override
    public void initScreenTrack(LoginModel loginModel) {
        Bundle bundle = new Bundle();

        bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_TUTORIAL_WELCOME);
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    @Override
    public void initScreenTrackPosition(LoginModel loginModel, int position) {
        Bundle bundle = new Bundle();

        switch (position) {
            case 0:
                bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_TUTORIAL_WELCOME);
                break;
            case 1:
                bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_TUTORIAL_CIENTS);
                break;
            case 2:
                bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_TUTORIAL_ORDERS);
                break;
            case 3:
                bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_TUTORIAL_DEBTS);
                break;
            case 4:
                bundle.putString(GlobalConstant.EVENT_VAR_SCREEN, GlobalConstant.SCREEN_TUTORIAL_INCENTIVES);
                break;
            default:
                // EMPTY
                break;
        }
        bundle.putString(GlobalConstant.TRACK_VAR_ENVIROMENT, BuildConfig.ANALYTICS);

        Bundle properties = AnalyticsUtil.getUserProperties(loginModel);

        BelcorpAnalytics.trackScreenView(GlobalConstant.SCREEN_VIEW, bundle, properties);
    }

    @Override
    public void showIntrigueDialog(String image) {
        Glide.with(getContext())
            .applyDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .load(image).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if (listener != null) listener.onHome();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                new IntrigueDialog.Builder(getContext()).withImage(image).onEnd(() -> {
                    if (listener != null) listener.onHome();
                }).show();
                return false;
            }
        }).submit();
    }

    @Override
    public void showRenewDialog(String image, String imageLogo, String message) {
        Glide.with(getContext())
            .applyDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .load(image).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if (listener != null) listener.onHome();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (secondImgLoaded){
                    new RenewDialog.Builder(getContext()).withImage(image).withImageLogo(imageLogo).message(message).onEnd(() -> {
                        if (listener != null) listener.onHome();
                    }).show();
                }
                firstImgLoaded = true;
                return false;
            }
        }).submit();

        Glide.with(getContext())
            .applyDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .load(image).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if (listener != null) listener.onHome();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (firstImgLoaded){
                    new RenewDialog.Builder(getContext()).withImage(image).withImageLogo(imageLogo).message(message).onEnd(() -> {
                        if (listener != null) listener.onHome();
                    }).show();
                }
                secondImgLoaded = true;
                return false;
            }
        }).submit();
    }

    @Override
    public void onHome() {
        if (listener != null) listener.onHome();
    }

    @Override
    public void onVersionError(boolean required, String url) {
        // EMPTY
    }

    /**********************************************************************************************/

    @OnClick(R.id.btn_tutorial_omitir)
    public void onBtnTutorialSkipClicked() {
        presenter.trackEvent(
            getCurrentScreenName(),
            GlobalConstant.EVENT_CAT_TUTORIAL,
            GlobalConstant.EVENT_ACTION_TUTORIAL,
            GlobalConstant.EVENT_LABEL_TUTORIAL_SKIP,
            GlobalConstant.EVENT_NAME_TUTORIAL_SKIP);

       presenter.initIntrigue();

    }

    @OnClick(R.id.btn_tutorial_siguiente)
    public void onBtnTutorialNextClicked() {
        int currentItem = vwpTutorial.getCurrentItem();
        if (currentItem + 1 != adapter.getCount()) {
            presenter.trackEvent(
                getCurrentScreenName(),
                GlobalConstant.EVENT_CAT_TUTORIAL,
                GlobalConstant.EVENT_ACTION_TUTORIAL,
                GlobalConstant.EVENT_LABEL_TUTORIAL_NEXT,
                GlobalConstant.EVENT_NAME_TUTORIAL_NEXT);

            vwpTutorial.setCurrentItem(currentItem + 1);
        } else {
            presenter.trackEvent(
                getCurrentScreenName(),
                GlobalConstant.EVENT_CAT_TUTORIAL,
                GlobalConstant.EVENT_ACTION_TUTORIAL,
                GlobalConstant.EVENT_LABEL_TUTORIAL_ENTER,
                GlobalConstant.EVENT_NAME_TUTORIAL_ENTER);
            presenter.initIntrigue();
        }
    }

    /**********************************************************************************************/

    private List<Fragment> getTutorialFragmentList(List<TutorialModel> tutorialModelList) {
        List<Fragment> tutorialFragmentList = new ArrayList<>();

        TutorialCardFragment tutorialFragment;
        for (TutorialModel tutorialModel : tutorialModelList) {
            tutorialFragment = new TutorialCardFragment();
            tutorialFragment.setTutorialModel(tutorialModel);
            tutorialFragmentList.add(tutorialFragment);
        }
        return tutorialFragmentList;
    }

    private List<TutorialModel> getTutorialModelList() {
        List<TutorialModel> tutorialModelList = new ArrayList<>();
        TutorialModel tutorialModel = new TutorialModel();

        tutorialModel.setTitle1(getString(R.string.tutorial_bienvenida_titulo_1));
        tutorialModel.setTitle2(getString(R.string.tutorial_bienvenida_titulo_2));
        tutorialModel.setTitle3(getString(R.string.tutorial_bienvenida_titulo_3));

        if (selectedCountry != null && selectedCountry.equals(CR)) {
            tutorialModel.setDescripcion(getString(R.string.tutorial_bienvenida_descripcion_cr));
        } else {
            tutorialModel.setDescripcion(getString(R.string.tutorial_bienvenida_descripcion));
        }

        tutorialModel.setImagen(R.drawable.ic_tutorial_1);

        TutorialModel tutorialModel2 = new TutorialModel();
        tutorialModel2.setTitle1(getString(R.string.tutorial_clientes_titulo_1));
        tutorialModel2.setTitle2(getString(R.string.tutorial_clientes_titulo_2));
        tutorialModel2.setTitle3(getString(R.string.tutorial_clientes_titulo_3));
        tutorialModel2.setDescripcion(getString(R.string.tutorial_clientes_descripcion));
        tutorialModel2.setImagen(R.drawable.ic_tutorial_2);

        TutorialModel tutorialModel3 = new TutorialModel();
        tutorialModel3.setTitle1(getString(R.string.tutorial_pedidos_titulo_1));
        tutorialModel3.setTitle2(getString(R.string.tutorial_pedidos_titulo_2));
        tutorialModel3.setTitle3(getString(R.string.tutorial_pedidos_titulo_3));
        tutorialModel3.setDescripcion(getString(R.string.tutorial_pedidos_descripcion));
        tutorialModel3.setImagen(R.drawable.ic_tutorial_3);

        TutorialModel tutorialModel4 = new TutorialModel();
        tutorialModel4.setTitle1(getString(R.string.tutorial_deudas_titulo_1));
        tutorialModel4.setTitle2(getString(R.string.tutorial_deudas_titulo_2));
        tutorialModel4.setTitle3(getString(R.string.tutorial_deudas_titulo_3));
        tutorialModel4.setDescripcion(getString(R.string.tutorial_deudas_descripcion));
        tutorialModel4.setImagen(R.drawable.ic_tutorial_4);

        TutorialModel tutorialModel5 = new TutorialModel();
        tutorialModel5.setTitle1(getString(R.string.tutorial_incentivos_titulo_1));
        tutorialModel5.setTitle2(getString(R.string.tutorial_incentivos_titulo_2));

        if (Country.CO.equals(selectedCountry)) {
            tutorialModel5.setTitle3(getString(R.string.tutorial_incentivos_titulo_3));
            tutorialModel5.setDescripcion(getString(R.string.tutorial_incentivos_descripcion));
        } else {
            tutorialModel5.setTitle3(getString(R.string.tutorial_bonificaciones_titulo_3));
            tutorialModel5.setDescripcion(getString(R.string.tutorial_bonificaciones_descripcion));
        }

        tutorialModel5.setImagen(R.drawable.ic_tutorial_5);

        tutorialModelList.add(tutorialModel);
        tutorialModelList.add(tutorialModel2);
        tutorialModelList.add(tutorialModel3);
        tutorialModelList.add(tutorialModel4);
        tutorialModelList.add(tutorialModel5);

        return tutorialModelList;
    }

    public void hideTutorial() {
        if (getArguments() != null) {
            String userCode = getArguments().getString(USERCODE);
            if (userCode != null)
                SessionManager.Companion.getInstance(context()).saveTutorial(userCode);
        }
    }

    private String getCurrentScreenName() {
        switch (vwpTutorial.getCurrentItem()) {
            case 0:
                return GlobalConstant.SCREEN_TUTORIAL_WELCOME;
            case 1:
                return GlobalConstant.SCREEN_TUTORIAL_CIENTS;
            case 2:
                return GlobalConstant.SCREEN_TUTORIAL_ORDERS;
            case 3:
                return GlobalConstant.SCREEN_TUTORIAL_DEBTS;
            case 4:
                return GlobalConstant.SCREEN_TUTORIAL_INCENTIVES;
            default:
                return GlobalConstant.SCREEN_TUTORIAL_WELCOME;
        }
    }



    /*********************************************************************/

    interface TutorialFragmentListener {
        void onHome();
    }

}
