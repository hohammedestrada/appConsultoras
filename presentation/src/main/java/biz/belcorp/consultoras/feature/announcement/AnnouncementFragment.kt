package biz.belcorp.consultoras.feature.announcement

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.feature.announcement.di.AnnouncementComponent
import biz.belcorp.consultoras.util.GlobalConstant
import javax.inject.Inject

class AnnouncementFragment : BaseFragment(), AnnouncementView {

    companion object {
        fun newInstance(): AnnouncementFragment {
            return AnnouncementFragment()
        }
    }

    @Inject
    lateinit var presenter: AnnouncementPresenter

    internal var listener: AnnouncementListener? = null
    private var refreshDataHandler: Handler? = null
    private var refreshDataRunnable: Runnable? = null

    override fun onInjectView(): Boolean {
        getComponent(AnnouncementComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)

        // Aqui iniciar lo que deba hacer
        presenter.startAnimation()
    }

    /** */

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is AnnouncementFragment.AnnouncementListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_announcement, container, false)
    }

    override fun onResume() {
        super.onResume()

        if (refreshDataHandler == null) initHandler()

        refreshDataHandler?.removeCallbacks(refreshDataRunnable)
        refreshDataHandler?.postDelayed(refreshDataRunnable, 200)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    internal interface AnnouncementListener {
        fun onBackFromFragment()
    }

    private fun initHandler() {
        refreshDataHandler = Handler()
        refreshDataRunnable = Runnable { presenter.initScreenTrack() }
    }


    // Analytics
    override fun initScreenTrack(model: LoginModel) {
        Tracker.trackScreen(GlobalConstant.SCREEN_ORDERS, model)
    }

    override fun trackBackPressed(model: LoginModel) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_ORDERS_GPR, model)
        listener?.onBackFromFragment()
    }

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }
}
