package biz.belcorp.consultoras.feature.home.tutorial;

import android.os.Bundle;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.base.BaseActivity;
import biz.belcorp.consultoras.di.HasComponent;
import biz.belcorp.consultoras.feature.home.tutorial.di.DaggerTutorialComponent;
import biz.belcorp.consultoras.feature.home.tutorial.di.TutorialComponent;

public class TutorialActivity extends BaseActivity implements HasComponent<TutorialComponent>,
    TutorialFragment.TutorialFragmentListener {

    private TutorialComponent component;
    private TutorialFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        initializeInjector();

        if (savedInstanceState == null && getIntent() != null) {
            fragment = TutorialFragment.newInstance(getIntent().getExtras());
            addFragment(R.id.fltContainer, fragment);
        }
    }

    @Override
    public void onBackPressed() {
        if (null != fragment) fragment.hideTutorial();
        setResult(RESULT_OK);
        finish();
    }

    /**********************************************************************************************/

    @Override
    protected void init(Bundle savedInstanceState) {
        // EMPTY
    }

    @Override
    protected void initializeInjector() {
        this.component = DaggerTutorialComponent.builder()
            .appComponent(getAppComponent())
            .activityModule(getActivityModule())
            .build();
    }

    @Override
    protected void initControls() {
        // EMPTY
    }

    @Override
    protected void initEvents() {
        // EMPTY
    }

    /***************************************************************/

    @Override
    public void onHome() {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    /***************************************************************/

    @Override
    public TutorialComponent getComponent() {
        return component;
    }
}
