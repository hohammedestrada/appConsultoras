package biz.belcorp.consultoras.feature.home.ganamas.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.feature.home.BaseHomeFragment
import biz.belcorp.consultoras.feature.home.di.HomeComponent
import com.uxcam.UXCam
import kotlinx.android.synthetic.main.fragment_ganamas_error.*

class GanaMasErrorFragment : BaseHomeFragment() {

    var listener: GanaMasErrorFragment.Listener? = null
    var typeError: Int? = null

    override fun onInjectView(): Boolean {
        getComponent(HomeComponent::class.java).inject(this)
        return true
    }

    // Overrides Fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ganamas_error, container, false)
    }

    // Override BaseFragment
    override fun context(): Context {
        return context?.let { it } ?: throw NullPointerException("No hay contexto")
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

    /**
     * Public Functions
     */

    fun init() {

        typeError = arguments?.getInt(FRAGMENT_ERROR_TYPE_KEY)

        btnRefresh.setOnClickListener {
            listener?.refresh()
        }

        showMessage(typeError)
    }

    fun showMessage(type: Int?){
        when(type){
            ERROR_MESSAGE_NETWORK -> {
                txtTitle.text = resources.getString(R.string.ganamas_empty_network_title)
                txtSubtitle.visibility = View.GONE
                imgEmpty.setImageResource(R.drawable.negative_notification)
                btnRefresh.visibility = View.VISIBLE
            }
            ERROR_MESSAGE_EMPTY_CONTAINER ->{
                txtTitle.text = resources.getString(R.string.ganamas_empty_container_title)
                txtSubtitle.visibility = View.VISIBLE
                txtSubtitle.text = resources.getString(R.string.ganamas_empty_container_subtitle)
                imgEmpty.setImageResource(R.drawable.surprise_notification)
                btnRefresh.visibility = View.GONE
            }
            ERROR_MESSAGE_EMPTY_CATEGORY ->{
                txtTitle.text = resources.getString(R.string.ganamas_empty_category_title)
                txtSubtitle.visibility = View.VISIBLE
                txtSubtitle.text = resources.getString(R.string.ganamas_empty_category_subtitle)
                imgEmpty.setImageResource(R.drawable.negative_notification)
                btnRefresh.visibility = View.GONE
            }
            ERROR_MESSAGE_EMPTY_FILTER ->{
                txtTitle.text = resources.getString(R.string.ganamas_empty_filter_offer)
                txtSubtitle.visibility = View.GONE
                imgEmpty.setImageResource(R.drawable.negative_notification)
                btnRefresh.visibility = View.GONE
            }
            else ->{
                txtTitle.text = resources.getString(R.string.ganamas_empty_network_title)
                txtSubtitle.visibility = View.VISIBLE
                txtSubtitle.text = resources.getString(R.string.ganamas_empty_category_subtitle)
                imgEmpty.setImageResource(R.drawable.negative_notification)
                btnRefresh.visibility = View.GONE
            }
        }
    }

    /**
     * Interface
     */

    interface Listener {
        fun refresh()
    }

    /**
     * Constants
     */
    companion object {
        val TAG: String = GanaMasErrorFragment::class.java.simpleName

        val FRAGMENT_ERROR_TYPE_KEY = "FRAGMENT_ERROR_TYPE_KEY"

        const val ERROR_MESSAGE_NETWORK = 1
        const val ERROR_MESSAGE_EMPTY_CONTAINER = 2
        const val ERROR_MESSAGE_EMPTY_CATEGORY = 3
        const val ERROR_MESSAGE_EMPTY_FILTER = 4

        fun newInstance(): GanaMasErrorFragment{
            return GanaMasErrorFragment()
        }
    }

}
