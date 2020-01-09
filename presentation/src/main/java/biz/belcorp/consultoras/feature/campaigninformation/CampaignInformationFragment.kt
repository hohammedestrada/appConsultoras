package biz.belcorp.consultoras.feature.campaigninformation

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.campaignInformation.InfoCampaignDataMapper
import biz.belcorp.consultoras.common.model.error.ErrorModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.campaignInformation.InfoCampaign
import biz.belcorp.consultoras.feature.campaigninformation.di.CampaignInformationComponent
import biz.belcorp.consultoras.util.GlobalConstant
import kotlinx.android.synthetic.main.fragment_campaign_information.*
import javax.inject.Inject

class CampaignInformationFragment : BaseFragment(), CampaignInformationView {

    @Inject
    lateinit var presenter: CampaignInformationPresenter

    companion object {
        val PARAM_NAME_CAMPANIAS_ANTERIORES = "anteriores"
        val PARAM_NAME_CAMPANIAS_SIGUIENTE = "siguientes"
    }

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_campaign_information, container, false)
    }

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(CampaignInformationComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        init()
    }

    override fun onError(errorModel: ErrorModel) {

    }

    override fun onInfoCampania(info: InfoCampaign?) {
        info?.let {
            tvwCampania.text = it.Campania
            tvwFechaFacturacion.text = it.FechaFacturacion
            tvwInfoFechaPago.text = "${resources.getString(R.string.fecha_pago_pedido)}:"
            tvwFecha.text = it.FechaPago

            val extrasPasadas = Bundle()
            val extrasSiguiente = Bundle()

            it.CampaniaAnterior?.let{ant ->
                extrasPasadas.putParcelableArrayList(PARAM_NAME_CAMPANIAS_ANTERIORES,
                    InfoCampaignDataMapper().transform(ant))
            }

            it.CampaniaSiguiente?.let {sig ->
                extrasSiguiente.putParcelableArrayList(PARAM_NAME_CAMPANIAS_SIGUIENTE,
                    InfoCampaignDataMapper().transform(sig))
            }

            val adapter = CampaignInformationAdapter(childFragmentManager, arrayListOf(resources.getString(R.string.anteriores), resources.getString(R.string.proximas)))
            adapter.setArguments(extrasPasadas, extrasSiguiente)

            view_pager_content.offscreenPageLimit = 2
            view_pager_content.adapter = adapter

            view_pager_content.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    Tracker.InformacionCampanias.pantallaVista(adapter.getPageTitle(position).toString(), presenter.user)

                    var currentTitle : String= adapter.getPageTitle(position).toString()
                    var passTitle :String = adapter.getPageTitle(0).toString()

                    if(position == 0){
                        passTitle = adapter.getPageTitle(1).toString()
                    }

                    Tracker.InformacionCampanias.OnClickTab(currentTitle, passTitle, presenter.user)
                }
            })

            tabs.setActivityContainer(activity)
            tabs.setViewPager(view_pager_content)
            tabs.setTypeface(Typeface.createFromAsset(activity?.assets, GlobalConstant.LATO_REGULAR_SOURCE), Typeface.NORMAL)
        }
    }

    private fun init() {

    }

}
