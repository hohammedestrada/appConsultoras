package biz.belcorp.consultoras.feature.dreammeter.feature.detail

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.dreammeter.DreamMeter
import biz.belcorp.consultoras.feature.dreammeter.DreamMeterActivity
import biz.belcorp.consultoras.feature.dreammeter.component.TermometroView
import biz.belcorp.consultoras.feature.dreammeter.di.DreamMeterComponent
import biz.belcorp.consultoras.feature.dreammeter.feature.detail.adapter.RvCampaniasAdapter
import biz.belcorp.library.util.CountryUtil
import kotlinx.android.synthetic.main.fragment_detail_dream.*
import java.math.BigDecimal
import java.text.DecimalFormat
import javax.inject.Inject

class DetailFragment : BaseFragment(), DetailView {

    @Inject
    lateinit var presenter: DetailPresenter

    private var listener: Listener? = null

    private lateinit var rvCampaniasAdapter: RvCampaniasAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_dream, container, false)
    }

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(DreamMeterComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        presenter.attachView(this)
        init()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DreamMeterActivity) {
            listener = context
        }
    }

    private fun init() {
        setupListener()
        initViews()
        presenter.getConfiguracion()
    }

    private fun initViews() {
        ivwStar.colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0.0f) })

        rvCampaniasAdapter = RvCampaniasAdapter()
        rvwCampanias.apply {
            layoutManager = LinearLayoutManager(context(), RecyclerView.VERTICAL, false)
            adapter = rvCampaniasAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupListener() {
        tvwEditar.setOnClickListener {
            listener?.goToSave(getExtraDreamMeter(), false)
        }

        ivwTermometro.setListener(object : TermometroView.Listener {
            override fun onFinishedProgress(isComplete: Boolean) {
                if (isComplete) {
                    ivwStar.colorFilter = null
                    listener?.showConfeti()
                    presenter.updateStatus()
                }
            }
        })
    }

    override fun reloadDreamMeter(dreamMeter: DreamMeter){
        arguments = Bundle().apply {
            putSerializable(ARG_DREAM, dreamMeter)
        }
        init()
    }

    override fun getExtraDreamMeter(): DreamMeter? {
        return arguments?.get(ARG_DREAM) as DreamMeter?
    }

    override fun onLoadConfiguracion(it: User) {
        tvwMoneySymbol.text = it.countryMoneySymbol
        tvwMoneySymbolAmountNeeded.text = it.countryMoneySymbol

        decimalFormat = CountryUtil.getDecimalFormatByISO(it.countryISO, true)
        moneySymbol = it.countryMoneySymbol ?: ""

        presenter.getDreamMeter(getExtraDreamMeter())
    }

    override fun showDreamMeter(user: User?, consultantDream: DreamMeter.ConsultantDream, totalProgress: BigDecimal, showAnimation: Boolean) {
        tvwNameDream.text = consultantDream.description ?: ""
        tvwDreamAmount.text = formatWithOutMoneySymbol(consultantDream.dreamAmount ?: 0.toBigDecimal())
        tvwAmountNeeded.text = formatWithOutMoneySymbol(consultantDream.saleAmount ?: 0.toBigDecimal())
        consultantDream.details?.let {
            rvCampaniasAdapter.setCampaigns(user, it.reversed())
        }
        ivwTermometro.setTotalProgress(showAnimation, totalProgress)
        if(!showAnimation){
            ivwStar.colorFilter = null
        }
    }

    override fun setMensajeDream(mensaje: Int) {
        tvwMensaje.setText(mensaje)
    }

    override fun setMensajeDream(mensaje: String) {
        tvwMensaje.text = mensaje
    }

    override fun onErrorDreamMeter() {
        listener?.goToSave(null, true)
    }

    override fun context(): Context {
        return requireContext()
    }

    override fun setCanBack(canBack: Boolean) {
        listener?.setCanBack(canBack)
    }

    override fun showEditar() {
        grpEditar.visibility = View.VISIBLE
    }

    override fun hideEditar() {
        grpEditar.visibility = View.GONE
    }

    override fun getStringByIdRes(idRes: Int, vararg params: String): String = getString(idRes, *params)

    companion object {
        private const val ARG_DREAM = "ARG_DREAM"

        var decimalFormat = DecimalFormat()
        var moneySymbol: String = ""

        @JvmStatic
        fun newInstance(dreamMeter: DreamMeter? = null) = DetailFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_DREAM, dreamMeter)
            }
        }

        fun formatWithMoneySymbol(precio: BigDecimal): String {
            return "$moneySymbol ${formatWithOutMoneySymbol(precio)}"
        }

        fun formatWithOutMoneySymbol(precio: BigDecimal): String {
            return decimalFormat.format(precio)
        }
    }

    interface Listener {
        fun goToSave(dreamMeter: DreamMeter?, isReplace: Boolean)
        fun showConfeti()
        fun setCanBack(isCanBack: Boolean)
    }

}
