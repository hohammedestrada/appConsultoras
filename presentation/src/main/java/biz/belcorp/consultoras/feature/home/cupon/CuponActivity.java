package biz.belcorp.consultoras.feature.home.cupon;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseActivity;
import biz.belcorp.consultoras.di.HasComponent;
import biz.belcorp.consultoras.feature.home.cupon.di.CuponComponent;
import biz.belcorp.consultoras.feature.home.cupon.di.DaggerCuponComponent;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CuponActivity extends BaseActivity implements HasComponent<CuponComponent>,
    CuponFragment.CuponListener {

    private CuponComponent component;
    CuponFragment fragment;
    private Unbinder unbinder;

    /**********************************************************/

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cupon);

        unbinder = ButterKnife.bind(this);

        init(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (unbinder != null)
            unbinder.unbind();
    }

    /**********************************************************/

    @Override
    protected void init(Bundle savedInstanceState) {
        initializeInjector();

        if (savedInstanceState == null) {
            fragment = new CuponFragment();
            fragment.setArguments(getIntent().getExtras());
            addFragment(R.id.fltContainer, fragment);
        }
    }

    @Override
    protected void initializeInjector() {
        this.component = DaggerCuponComponent.builder()
            .appComponent(getAppComponent())
            .activityModule(getActivityModule())
            .build();
    }

    @Override
    protected void initControls() {
        // Empty
    }

    @Override
    protected void initEvents() {
        // Empty
    }

    @Override
    public CuponComponent getComponent() {
        return component;
    }

    @Override
    public void onBackFromFragment() {
        onBackPressed();
    }
}
