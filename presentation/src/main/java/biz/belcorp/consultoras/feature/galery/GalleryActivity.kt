package biz.belcorp.consultoras.feature.galery

import android.os.Bundle
import android.view.Window
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.galery.di.DaggerGaleryComponent
import biz.belcorp.consultoras.feature.galery.di.GaleryComponent
import kotlinx.android.synthetic.main.app_bar_main.*

class GalleryActivity : BaseActivity() , HasComponent<GaleryComponent> {

    private var containerFragment: GalleryContainerFragment? = null
    private var galeryComponent : GaleryComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_gallery)

        initializeInjector()
        init(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {

        tvw_toolbar_title.setText(R.string.downloadable_content_title)

        toolbar.setNavigationOnClickListener { _ ->
            onBackPressed()
        }

        if (savedInstanceState == null) {
            containerFragment = GalleryContainerFragment()
            containerFragment?.arguments = intent.extras

            addFragment(R.id.fltContainer, containerFragment,true)
        }
    }

    override fun initializeInjector() {
        this.galeryComponent = DaggerGaleryComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    override fun initControls() {

    }

    override fun initEvents() {

    }

    override fun getComponent(): GaleryComponent? {
        return galeryComponent
    }

}
