package biz.belcorp.consultoras.feature.home.addorders.choosegift

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.EstrategiaCarrusel
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.feature.home.addorders.choosegift.di.GiftComponent
import biz.belcorp.consultoras.util.GlobalConstant
import kotlinx.android.synthetic.main.fragment_elige_regalo.*
import java.math.BigDecimal
import javax.inject.Inject

class GiftFragment : BaseFragment(), GiftView, GiftAdapter.GiftListenerProduct {

    private var adapter: GiftAdapter? = null
    private var hayElegido: Boolean = false
    private var countElegido = 0
    private var showMessage = false
    private var usuario: User? = null
    private var regalo : Boolean = false
    @Inject
    lateinit var presenter: GiftPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_elige_regalo, container, false)
    }

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(GiftComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        this.presenter.getListGift()

        arguments?.getString(GlobalConstant.EXTRA_MONTO).let {

            if (it.isNullOrBlank() || it.isNullOrEmpty()) { //significa que llego al monto
                showMessage = false
                tvwSeraTuyo.text = context?.getText(R.string.regaloPuedeCambiar)
                tvwBtnSeguirComprando.text = context?.getText(R.string.sigueComprando)
            } else {
                showMessage = true
                tvwSeraTuyo.text = context?.getText(R.string.seraTuyoSi).toString().plus(" ".plus(it))
                tvwBtnSeguirComprando.text = context?.getText(R.string.sigueComprandoLlevartelo)
            }

        }

        initEvents()

    }

    override fun onListGiftRecived(gift: Collection<EstrategiaCarrusel?>) {

        gift.filterNotNull()
            .forEach {
                if (it.precioValorizado?.compareTo(BigDecimal.ZERO) == 0)
                    regalo = true
                return@forEach
            }

        adapter = GiftAdapter(gift.toList(), this, arguments?.getBoolean(GlobalConstant.EXTRA_BOOLEANO_GIFT) == true, usuario?.countryISO, regalo)
        if (recvGift != null) {
            checkIfHasSelected(gift)
            tvwmsgStatusEleccion.visibility = View.VISIBLE
            tvwSeraTuyo.visibility = View.VISIBLE
            recvGift.setHasFixedSize(true)
            recvGift.adapter = adapter
        }

    }

    override fun assignUser(usuario: User?) {
        this@GiftFragment.usuario = usuario
    }

    fun initEvents() {
        ivwCloseGeneral.setOnClickListener {
            returnToBackResult()

        }
        lnrSeguirComprando.setOnClickListener {
            returnToBackResult()
        }
    }

    override fun agregarRegalo(regalo: EstrategiaCarrusel, identificador: String) {
        presenter.saveGift(regalo, identificador)
    }

    override fun onGiftAdded(cuv: String) {
        if (recvGift != null) {
            adapter?.updateStatusGift(cuv)
        }
    }

    override fun changeMessage(esEscogido: Boolean, flagPulsaste: Boolean) {

        if (esEscogido)
            countElegido++

        if (countElegido >= 1) {
            tvwmsgStatusEleccion.text =
                if (regalo) {
                    context?.getText(R.string.yaElegisteRegalo).toString()
                } else {
                    context?.getText(R.string.yaElegisteProducto).toString()
                }
            hayElegido = true
        } else {
            tvwmsgStatusEleccion.text = if (regalo) {
                context?.getText(R.string.puedesElegirRegalo)
            } else {
                context?.getText(R.string.puedesElegirProducto)
            }
            hayElegido = false
        }

        tvwmsgStatusEleccion.text =
            if (arguments?.getBoolean(GlobalConstant.EXTRA_BOOLEANO_GIFT) == true || flagPulsaste) {
                if (regalo){
                    context?.getText(R.string.yaElegisteRegalo)
                } else {
                    context?.getText(R.string.yaElegisteProducto)
                }
            }
            else
                if (regalo){
                    context?.getText(R.string.puedesElegirRegalo)
                } else {
                    context?.getText(R.string.puedesElegirProducto)
                }

    }

    private fun checkIfHasSelected(gift: Collection<EstrategiaCarrusel?>) {
        gift.filterNotNull()
            .forEach {
                if (it.flagSeleccionado == 1)
                    changeMessage(true, false)
            }
    }

    private fun returnToBackResult() {
        Tracker.EligeTuRegalo.confirmSelectGift(tvwBtnSeguirComprando.text.toString())

        if (hayElegido) {
            activity?.setResult(Activity.RESULT_OK)
        } else
            activity?.setResult(Activity.RESULT_CANCELED)

        activity?.finish()
    }

}
