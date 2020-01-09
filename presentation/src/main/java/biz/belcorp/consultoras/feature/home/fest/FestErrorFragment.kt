package biz.belcorp.consultoras.feature.home.fest

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.feature.home.BaseHomeFragment
import biz.belcorp.consultoras.feature.home.fest.di.FestComponent
import kotlinx.android.synthetic.main.fragment_ganamas_error.*

class FestErrorFragment : BaseHomeFragment() {

    var listener: Listener? = null
    var typeError: Int? = null

    override fun onInjectView(): Boolean {
        getComponent(FestComponent::class.java).inject(this)
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
            listener?.refreshFromError()
        }

        lltContainer.setBackgroundColor(ContextCompat.getColor(context(), R.color.white))

        centerView()
        showMessage(typeError)
    }

    private fun centerView() {
        lltContainer.gravity = Gravity.CENTER

        val parameter = imgEmpty.layoutParams as LinearLayout.LayoutParams
        parameter.setMargins(0, 0, 0, 0) // left, top, right, bottom
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
            ERROR_MESSAGE_FEST -> {
                txtTitle.text = resources.getString(R.string.fest_error_load_data)
                txtSubtitle.visibility = View.GONE
                imgEmpty.setImageResource(R.drawable.negative_notification)
                btnRefresh.visibility = View.VISIBLE
            }
            ERROR_MESSAGE_EMPTY_FEST -> {
                txtTitle.text = resources.getString(R.string.fest_empty_product)
                txtSubtitle.visibility = View.GONE
                imgEmpty.setImageResource(R.drawable.negative_notification)
                btnRefresh.visibility = View.GONE
            }
            ERROR_MESSAGE_EMPTY_FILTER -> {
                txtTitle.text = resources.getString(R.string.fest_empty_product_filter)
                txtSubtitle.visibility = View.GONE
                imgEmpty.setImageResource(R.drawable.negative_notification)
                btnRefresh.visibility = View.GONE
            }
            ERROR_MESSAGE_EMPTY_PROGRESS -> {

                // OJO CON ESTO

                txtTitle.text = resources.getString(R.string.fest_empty_product)
                txtSubtitle.visibility = View.VISIBLE
                txtSubtitle.text = resources.getString(R.string.ganamas_empty_category_subtitle)
                imgEmpty.setImageResource(R.drawable.negative_notification)
                btnRefresh.visibility = View.GONE
            }

            ERROR_MESSAGE_EMPTY_FEST_SUBCAMPAING -> {
                txtTitle.text = resources.getString(R.string.fest_empty_sub_campaing_product)
                txtSubtitle.visibility = View.VISIBLE
                txtSubtitle.text = resources.getString(R.string.ganamas_empty_category_subtitle)
                imgEmpty.setImageResource(R.drawable.negative_notification)
                btnRefresh.visibility = View.GONE
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

    /**
     * Interface
     */

    interface Listener {
        fun refreshFromError()
    }

    /**
     * Constants
     */
    companion object {
        val TAG: String = FestErrorFragment::class.java.simpleName

        const val FRAGMENT_ERROR_TYPE_KEY = "FRAGMENT_ERROR_TYPE_KEY"

        const val ERROR_MESSAGE_NETWORK = 1
        const val ERROR_MESSAGE_FEST = 2
        const val ERROR_MESSAGE_EMPTY_FEST = 3
        const val ERROR_MESSAGE_EMPTY_FILTER = 4
        const val ERROR_MESSAGE_EMPTY_PROGRESS = 5
        const val ERROR_MESSAGE_EMPTY_FEST_SUBCAMPAING = 6

        fun newInstance(): FestErrorFragment {
            return FestErrorFragment()
        }
    }

}
