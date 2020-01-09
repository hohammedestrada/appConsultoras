package biz.belcorp.consultoras.feature.announcement

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.View
import android.view.Window
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.announcement.di.AnnouncementComponent
import biz.belcorp.consultoras.feature.announcement.di.DaggerAnnouncementComponent
import kotlinx.android.synthetic.main.activity_academia.*

class AnnouncementActivity : BaseActivity(),
    HasComponent<AnnouncementComponent>,
    AnnouncementFragment.AnnouncementListener, LoadingView {

    companion object {
        const val OPTION = "opcion"
    }

    private var component: AnnouncementComponent? = null
    private var fragment: AnnouncementFragment? = null

    override fun getComponent(): AnnouncementComponent? {
        return component
    }

    fun getCallingIntent(context: Context): Intent {
        return Intent(context, AnnouncementActivity::class.java)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val window = window
        window.setFormat(PixelFormat.RGBA_8888)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_academia)
        init(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {
        initializeInjector()
        if (savedInstanceState == null) {
            fragment = AnnouncementFragment()
            addFragment(R.id.fltContainer, fragment)
        }
    }

    override fun initializeInjector() {
        component = DaggerAnnouncementComponent.builder()
            .appComponent(appComponent)
            .activityModule(activityModule)
            .build()
    }

    override fun initControls() {
        // EMPTY
    }

    override fun initEvents() {
        // EMPTY
    }

    override fun showLoading() {
        if (null != view_loading) view_loading?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        if (null != view_loading) view_loading?.visibility = View.GONE
    }

    override fun onBackFromFragment() {
        onBackPressed()
    }

}
