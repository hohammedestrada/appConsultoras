package biz.belcorp.consultoras.feature.galery

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.galery.di.DaggerGalleryDetailComponent
import biz.belcorp.consultoras.feature.galery.di.GalleryDetailComponent
import biz.belcorp.consultoras.util.GlobalConstant
import kotlinx.android.synthetic.main.activity_detail_gallery.*

class GalleryDetailActivity : BaseActivity(), HasComponent<GalleryDetailComponent> {

    private var galleryDetailComponent: GalleryDetailComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_gallery)
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

        initializeInjector()
        init(savedInstanceState)
    }

    override fun getComponent(): GalleryDetailComponent? {
        return this.galleryDetailComponent
    }

    override fun init(savedInstanceState: Bundle?) {
        ivwClose.setOnClickListener {
            this@GalleryDetailActivity.finish()
        }

        intent?.extras?.let {
            if(it.containsKey(GlobalConstant.ITEMS_GALLERY)){
                val extras = intent.extras

                val fragment = GalleryDetailFragment()
                fragment.arguments = extras

                addFragment(R.id.container, fragment, true, "")
            }
        }
    }

    override fun initializeInjector() {
        this.galleryDetailComponent = DaggerGalleryDetailComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    override fun initControls() {
        //Empty
    }

    override fun initEvents() {
        //Empty
    }
}
