package biz.belcorp.consultoras.feature.ofertafinal

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.feature.home.BaseHomeFragment
import biz.belcorp.consultoras.feature.home.fest.FestErrorFragment
import biz.belcorp.consultoras.feature.ofertafinal.di.OfertaFinalComponent
import biz.belcorp.consultoras.util.GlobalConstant.NO_CONTEXT
import kotlinx.android.synthetic.main.fragment_ganamas_error.*

class OfertaFinalErrorFragment : BaseHomeFragment() {

    var listener: Listener? = null
    var typeError: Int? = null

    override fun onInjectView(): Boolean {
        getComponent(OfertaFinalComponent::class.java).inject(this)
        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_oferta_final_error, container, false)
    }

    // Override BaseFragment
    override fun context(): Context {
        return context?.let { it } ?: throw NullPointerException(NO_CONTEXT)
    }

    // Overrides Fragment
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            this.listener = context
        }
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        init()
    }

    fun init() {

        typeError = arguments?.getInt(FRAGMENT_ERROR_TYPE_KEY)

        btnRefresh.setOnClickListener {
            listener?.refreshFromError()
        }

        lltContainer.setBackgroundColor(ContextCompat.getColor(context(), R.color.white))

        centerView()
        showMessage(typeError)
    }

    private fun centerView() {
        lltContainer.gravity = Gravity.CENTER

        val parameter = imgEmpty.layoutParams as LinearLayout.LayoutParams
        parameter.setMargins(0, 0, 0, 0)
        imgEmpty.layoutParams = parameter
    }

    fun showMessage(type: Int?){
        when(type){
            ERROR_MESSAGE_NETWORK -> {
                txtTitle.text = resources.getString(R.string.ganamas_empty_network_title)
                txtSubtitle.visibility = View.GONE
                imgEmpty.setImageResource(R.drawable.negative_notification)
                btnRefresh.visibility = View.VISIBLE
            }
            ERROR_MESSAGE_OFERTA_FINAL -> {
                txtTitle.text = resources.getString(R.string.oferta_final_error_load_data)
                txtSubtitle.visibility = View.GONE
                imgEmpty.setImageResource(R.drawable.negative_notification)
                btnRefresh.visibility = View.VISIBLE
            }

            else -> {
                txtTitle.text = resources.getString(R.string.ganamas_empty_network_title)
                txtSubtitle.visibility = View.VISIBLE
                txtSubtitle.text = resources.getString(R.string.ganamas_empty_category_subtitle)
                imgEmpty.setImageResource(R.drawable.negative_notification)
                btnRefresh.visibility = View.GONE
            }
        }
    }

    interface Listener {
        fun refreshFromError()
    }

    companion object {
        val TAG: String = OfertaFinalErrorFragment::class.java.simpleName
        const val FRAGMENT_ERROR_TYPE_KEY = "FRAGMENT_ERROR_TYPE_KEY"

        const val ERROR_MESSAGE_NETWORK = 1
        const val ERROR_MESSAGE_OFERTA_FINAL = 2

        fun newInstance(): OfertaFinalErrorFragment {
            return OfertaFinalErrorFragment()
        }
    }
}
