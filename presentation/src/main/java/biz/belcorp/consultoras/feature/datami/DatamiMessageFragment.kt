package biz.belcorp.consultoras.feature.datami

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import javax.inject.Inject

import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.feature.datami.di.DatamiMessageComponent
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.util.StringUtil
import kotlinx.android.synthetic.main.fragment_datami_message.*


class DatamiMessageFragment : BaseFragment(), DatamiMessageView {

    @Inject
    lateinit var presenter: DatamiMessagePresenter

    internal var listener: Listener? = null

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(DatamiMessageComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        init()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_datami_message, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.initScreenTrack()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    /**  functions **/

    fun init() {
        ivwClose.setOnClickListener { presenter.updateStatusDatamiMessage() }

        tvwMessage.text = "${getString(R.string.datami_message_description)} ${StringUtil.getEmojiByUnicode(0x1F600)}"
    }

    fun showClose() {
        ivwClose.visibility = View.VISIBLE
    }

    /**  view functions **/

    override fun initScreenTrack(model: LoginModel) {
        Tracker.trackScreen(GlobalConstant.SCREEN_DATAMI_MESSAGE, model)
    }

    override fun closeMessageResult() {
        listener?.onBackFromFragment()
    }

    /** listeners */

    internal interface Listener {
        fun onBackFromFragment()
    }

    companion object {

        fun newInstance(): DatamiMessageFragment {
            return DatamiMessageFragment()
        }
    }

}
