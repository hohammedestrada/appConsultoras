package biz.belcorp.consultoras.feature.datami

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.view.Window

import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseActivity
import biz.belcorp.consultoras.di.HasComponent
import biz.belcorp.consultoras.feature.datami.di.DaggerDatamiMessageComponent
import biz.belcorp.consultoras.feature.datami.di.DatamiMessageComponent
import biz.belcorp.consultoras.util.FestivityAnimationUtil


import kotlinx.android.synthetic.main.activity_datami_message.*

class DatamiMessageActivity : BaseActivity(),
    HasComponent<DatamiMessageComponent>,
    DatamiMessageFragment.Listener {


    private var component: DatamiMessageComponent? = null
    private var fragment: DatamiMessageFragment? = null

    private var backLocked = false

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val window = window
        window.setFormat(PixelFormat.RGBA_8888)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_datami_message)

        initializeInjector()
        init(savedInstanceState)
    }

    override fun onBackPressed() {
        if (!backLocked) super.onBackPressed()
    }

    override fun init(savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            fragment = DatamiMessageFragment()
            fragment?.arguments = intent.extras
            addFragment(R.id.fltContainer, fragment)
        }

        startHolidayAnimation()
    }

    override fun initializeInjector() {
        this.component = DaggerDatamiMessageComponent.builder()
                .appComponent(appComponent)
                .activityModule(activityModule)
                .build()
    }

    override fun initControls() {
        // Empty
    }

    override fun initEvents() {
        // Empty
    }

    private fun startHolidayAnimation() {
        backLocked = true

        rltActivity!!.postDelayed({

            FestivityAnimationUtil.getCommonConfetti(
                    ContextCompat.getColor(this@DatamiMessageActivity, R.color.dorado),
                    ContextCompat.getColor(this@DatamiMessageActivity, R.color.primary),
                    resources, rltActivity)

            Handler().postDelayed({
                backLocked = false
                if (null != fragment) fragment?.showClose()
            }, 1000)
        }, 100)
    }


    /** listener functions */

    override fun onBackFromFragment() {
        onBackPressed()
    }


    override fun getComponent(): DatamiMessageComponent? {
        return component
    }

    companion object {

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, DatamiMessageActivity::class.java)
        }
    }

}
