package biz.belcorp.consultoras.feature.contest.rewards

import android.content.Context
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.animation.carousel.CarouselPicker
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.incentivos.OpcionModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.feature.contest.rewards.di.RewardsComponent
import biz.belcorp.consultoras.util.GlobalConstant
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_rewards.*

class RewardsFragment : BaseFragment(), RewardsView {

    @Inject
    lateinit var presenter: RewardsPresenter

    private var listener: Listener? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is Listener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rewards, container, false)
    }

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(RewardsComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        init()
    }

    override fun onResume() {
        super.onResume()
        presenter.trackScreen()
    }

    override fun context(): Context {
        return activity as Context
    }


    /** RewardsView  */

    override fun trackScreen(model: LoginModel) {
        Tracker.trackScreen(GlobalConstant.SCREEN_INCENTIVES_GIFT_ORDER, model)
    }

    override fun trackBack(model: LoginModel) {
        Tracker.trackBackEvent(GlobalConstant.SCREEN_INCENTIVES_GIFT_ORDER, model)
        listener?.onBackFromFragment()
    }


    /** Functions */

    private fun init() {

        val imageItems = mutableListOf<CarouselPicker.ImageItem>()

        var opcion: OpcionModel? = null
        val bundle = arguments

        if (bundle != null) {
            opcion = bundle.getParcelable("opcion")
        }

        val contenido = StringBuilder()

        opcion?.let {

            val title = "OPCIÓN ${it.opcion} (Cód: ${it.premios[0].codigoPremio}): Conjunto de productos"
            val titleToolbar = "OPCIÓN ${it.opcion} (${it.premios[0].codigoPremio})"

            listener?.setTitle(titleToolbar)
            tvwTitulo.text = title

            it.premios?.forEach { pr ->
                imageItems.add(CarouselPicker.ImageItem(pr.imagenPremio))
                pr.descripcionPremio?.let { desc ->
                    contenido.append("- ${getDescription(desc)}\n")
                }
            }

            it.premios[0].descripcionPremio?.let { d ->
                tvwNombreRegalo.text = getDescription(d)
            }

        }

        tvwRegalos.text = contenido

        val imageAdapter = CarouselPicker.CarouselViewAdapter(activity, imageItems.toList(), 0)
        carousel.adapter = imageAdapter

        carousel.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float,
                                        positionOffsetPixels: Int) {
                // EMPTY
            }

            override fun onPageSelected(position: Int) {
                opcion?.let {
                    it.premios[position].descripcionPremio?.let { d ->
                        tvwNombreRegalo.text = getDescription(d)
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                // EMPTY
            }
        })

    }

    fun getDescription(name: String?) : String {
        var desc = ""

        name?.let {
            val descTrim = name.trim()
            desc = "${descTrim.substring(0, 1).toUpperCase()}${descTrim.substring(1).toLowerCase()}"
        }

        return desc
    }

    fun trackBack() {
        presenter.trackBack()
    }

    /** Interfaces */

    internal interface Listener {
        fun onBackFromFragment()
        fun setTitle(title: String)
    }

    companion object {
        fun newInstance(): RewardsFragment {
            return RewardsFragment()
        }
    }

}
