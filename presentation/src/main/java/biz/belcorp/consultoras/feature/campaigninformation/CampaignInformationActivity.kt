package biz.belcorp.consultoras.feature.campaigninformation

import android.os.Bundle
import android.view.Window
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.campaigninformation.di.CampaignInformationComponent
import biz.belcorp.consultoras.feature.campaigninformation.di.DaggerCampaignInformationComponent
import kotlinx.android.synthetic.main.app_bar_main.*
import javax.inject.Inject

class CampaignInformationActivity : BaseActivity(), HasComponent<CampaignInformationComponent> {

    @Inject
    lateinit var presenter: CampaignInformationPresenter

    private var campainComponent: CampaignInformationComponent? = null

    private var fragment: CampaignInformationFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_campaign_information)

        initializeInjector()
        init(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {
        tvw_toolbar_title.setText(R.string.calendario_facturacion)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        if (savedInstanceState == null) {
            fragment = CampaignInformationFragment()
            fragment?.arguments = intent.extras
            supportFragmentManager.beginTransaction().add(R.id.fltContainer, fragment).commit()
        }
    }

    override fun initializeInjector() {
        this.campainComponent = DaggerCampaignInformationComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    override fun initControls() {
        //empty
    }

    override fun initEvents() {
        //empty
    }

    override fun getComponent(): CampaignInformationComponent? {
        return campainComponent
    }
}
