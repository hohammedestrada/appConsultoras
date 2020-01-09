package biz.belcorp.consultoras.feature.caminobrillante.feature.home

import android.animation.ObjectAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PorterDuff
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.dialog.FullScreenDialog
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.caminobrillante.Carousel
import biz.belcorp.consultoras.domain.entity.caminobrillante.NivelCaminoBrillante
import biz.belcorp.consultoras.domain.entity.caminobrillante.NivelConsultoraCaminoBrillante
import biz.belcorp.consultoras.feature.caminobrillante.CaminoBrillanteActivity
import biz.belcorp.consultoras.feature.caminobrillante.di.CaminoBrillanteComponent
import biz.belcorp.consultoras.feature.caminobrillante.feature.home.adapter.BeneficiosAdapter
import biz.belcorp.consultoras.feature.caminobrillante.feature.home.adapter.BeneficiosNewAdapter
import biz.belcorp.consultoras.feature.caminobrillante.feature.home.viewmodels.BarraMontoAcumuladoViewModel
import biz.belcorp.consultoras.feature.embedded.academia.AcademiaActivity
import biz.belcorp.consultoras.feature.ficha.common.BaseFichaActivity
import biz.belcorp.consultoras.util.anotation.MenuCodeTop
import biz.belcorp.consultoras.util.anotation.WizardLevelType
import biz.belcorp.consultoras.util.toHtml
import biz.belcorp.library.util.DeviceUtil
import biz.belcorp.mobile.components.charts.wizard.Step
import biz.belcorp.mobile.components.core.adapters.WizardAdapter
import biz.belcorp.mobile.components.design.button.Button
import biz.belcorp.mobile.components.design.counter.Counter
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog
import biz.belcorp.mobile.components.offers.Multi
import biz.belcorp.mobile.components.offers.Offer
import biz.belcorp.mobile.components.offers.model.OfferModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.fragment_camino_brillante_constraint.*
import javax.inject.Inject

class CaminoBrillanteFragment : BaseFragment(), CaminoBrillanteView {

    @Inject
    lateinit var presenter: CaminoBrillantePresenter

    private lateinit var listener: Listener

    //adapters
    private lateinit var wizardAdapter: WizardAdapter
    private lateinit var adapterBeneficios: BeneficiosAdapter
    private lateinit var newAdapterBeneficios: BeneficiosNewAdapter

    //badge
    private var ordersCount: Int? = null
    private var cartBadge: TextView? = null
    private var animationDrawable: AnimationDrawable? = null

    //Flags
    private var onGananciaAnin = false
    private var onShowNivelActual = true

    companion object {
        fun newInstance(): CaminoBrillanteFragment {
            return CaminoBrillanteFragment()
        }
    }

    override fun context(): Context? {
        return activity?.applicationContext
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as CaminoBrillanteActivity
    }

    @Throws(IllegalStateException::class)
    override fun onInjectView(): Boolean {
        getComponent(CaminoBrillanteComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        presenter.attachView(this)
        init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(mBadgeReceiver, IntentFilter(CaminoBrillanteActivity.BROADCAST_COUNT_ACTION))
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.caminobrillante_menu, menu)
        menu?.let {
            val actionView = MenuItemCompat.getActionView(it.findItem(R.id.item_cart))
            cartBadge = actionView.findViewById(R.id.tvi_cart)

            (actionView.findViewById(R.id.imgCart) as ImageView).setOnClickListener {
                presenter.getMenuActive2(MenuCodeTop.ORDERS, MenuCodeTop.ORDERS_NATIVE)
            }

            updateBadge()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            animationDrawable?.start()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(mBadgeReceiver)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_camino_brillante_constraint, container, false)
    }

    private fun init() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            initViews()
        }

        setupListener()
        presenter.init()
    }

    fun initViews() {
        fingerBack.setBackgroundResource(R.drawable.animation)
        animationDrawable = fingerBack.background as AnimationDrawable
    }

    private fun initBchtGanancias() {
        bchtGanancias.isDoubleTapToZoomEnabled = false
        bchtGanancias.isDragEnabled = false
        bchtGanancias.setScaleEnabled(false)
        bchtGanancias.setTouchEnabled(true)
        bchtGanancias.setPinchZoom(false)
        bchtGanancias.animateY(3000)
        bchtGanancias.setFitBars(true)
        bchtGanancias.setDrawBorders(false)
        bchtGanancias.axisRight.setDrawGridLines(false)
        bchtGanancias.axisLeft.setDrawGridLines(false)
        bchtGanancias.xAxis.setDrawGridLines(false)
        bchtGanancias.xAxis.position = XAxis.XAxisPosition.BOTTOM
        bchtGanancias.xAxis.isEnabled = true
        bchtGanancias.rendererLeftYAxis
        bchtGanancias.legend.isEnabled = false
        bchtGanancias.description.isEnabled = false
        bchtGanancias.setDrawBarShadow(false)
        bchtGanancias.axisLeft.setDrawAxisLine(false)
        bchtGanancias.axisRight.setDrawAxisLine(false)
        bchtGanancias.axisLeft.setDrawLabels(false)
        bchtGanancias.axisRight.setDrawLabels(false)
        bchtGanancias.axisLeft.axisMinimum = 0.0f
        bchtGanancias.axisRight.axisMinimum = 0.0f
        bchtGanancias.setVisibleXRangeMinimum(0f)
        bchtGanancias.setVisibleYRangeMinimum(0f, YAxis.AxisDependency.LEFT)
        bchtGanancias.setVisibleYRangeMinimum(0f, YAxis.AxisDependency.RIGHT)
    }

    fun setupListener() {


        fingerBack.setOnClickListener {
            it.visibility = View.GONE
            hideFinder()
        }

        btnVerLogros.buttonClickListener = object : Button.OnClickListener {
            override fun onClick(view: View) {
                listener.goToDetalleLogro()
            }
        }

        btnVerOfertas.buttonClickListener = object : Button.OnClickListener {
            override fun onClick(view: View) {
                listener.goToOfertasEspeciales()
            }
        }

        btnEnterateMas.buttonClickListener = object : Button.OnClickListener {
            override fun onClick(view: View) {
                presenter.loadEnterateMas()
            }
        }

        offerKitsDemostradores.leverListener = object : Offer.OfferListener {
            override fun bindMulti(multiItem: Multi, position: Int) {
                presenter.onBindMulti(multiItem, position)
            }

            override fun didPressedPackBanner(typeLever: String, isUpdate: Boolean) {
                Log.d("LeverListener", "didPressedPackBanner")
            }

            override fun didPressedPackButton(typeLever: String, isUpdate: Boolean) {
                Log.d("LeverListener", "didPressedPackButton")
            }

            override fun didPressedBanner(typeLever: String, pos: Int) {
                Log.d("LeverListener", "didPressedBanner")
            }

            override fun didPressedItem(typeLever: String, keyItem: String, marcaID: Int, marca: String, pos: Int) {
                presenter.onClickItemCarousel(keyItem, marcaID, marca, pos)
            }

            override fun didPressedItemButtonAdd(typeLever: String, keyItem: String, quantity: Int, counterView: Counter, pos: Int, multi: Multi?) {
                presenter.addOffer(keyItem, quantity, pos, counterView, multi, DeviceUtil.getId(context))
            }


            override fun didPressedItemButtonSelection(typeLever: String, keyItem: String, marcaID: Int, marca: String, pos: Int) {
                Log.d("LeverListener", "didPressedPackBanner")
            }

            override fun didPressedItemButtonShowOffer(typeLever: String, keyItem: String, marcaID: Int, marca: String, pos: Int) {
                Log.d("LeverListener", "didPressedItemButtonShowOffer")
            }

            override fun didPressedButtonMore(typeLever: String, pos: Int) {
                Log.d("LeverListener", "didPressedButtonMore")
            }

            override fun didPressedButtonDuo(typeLever: String, pos: Int) {
                Log.d("LeverListener", "didPressedButtonDuo")
            }

            override fun didPressedAppBarOption(typeLever: String) {
                goToOfertasEspeciales()
            }

            override fun viewBanner(typeLever: String, pos: Int) {
                Log.d("LeverListener", "viewBanner")
            }

            override fun impressionItems(typeLever: String, list: ArrayList<OfferModel>) {
                Log.d("LeverListener", "impressionItems")
            }

            override fun finishedTimer(typeLever: String) {
                Log.d("LeverListener", "finishedTimer")
            }
        }

        btnSeeLessMore.buttonClickListener = object : Button.OnClickListener {
            override fun onClick(view: View) {
                presenter.onClickBtnSeeLessMore()
            }
        }

        arrow.setOnClickListener {
            presenter.onClickBtnSeeLessMore()
        }

    }

    override fun collapseBeneficios() {
        btnSeeLessMore.setText(getString(R.string.ver_mas))

        AnimationColapse.collapse(rvwBeneficios, object : AnimationColapse.onFinishAnimator {
            override fun onFinished() {
                adapterBeneficios.explaList = false
                adapterBeneficios.notifyDataSetChanged()
            }
        })

        rotateOriginal()

    }

    override fun expandBeneficios() {
        btnSeeLessMore.setText(getString(R.string.ver_menos))

        adapterBeneficios.explaList = true
        adapterBeneficios.notifyDataSetChanged()

        AnimationColapse.expand(rvwBeneficios)

        rotate()
    }

    //region barra niveles
    override fun onLoadBarraNiveles(niveles: List<NivelCaminoBrillante>, resumenConsultora: NivelConsultoraCaminoBrillante) {
        if (niveles.isNotEmpty()) {
            val layoutManager = loadLayoutManager(niveles.size)
            val adapter = loadAdapter(niveles.size, niveles)

            wzdNiveles.setAdapter(adapter, layoutManager)
            wzdNiveles.showAnimationBar = true

            resumenConsultora.nivel?.let { it ->
                adapter.setCurrentStep(it.toInt())
            }
        }
    }

    private fun loadLayoutManager(size: Int): RecyclerView.LayoutManager {
        val layoutManager = object : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
                val result = super.checkLayoutParams(lp)
                lp.apply { this!!.width = wzdNiveles.width / size }
                return result
            }
        }
        return layoutManager
    }

    private fun loadAdapter(size: Int, niveles: List<NivelCaminoBrillante>): WizardAdapter {
        wizardAdapter = object : WizardAdapter(null, size) {

            private val colorTitle = ContextCompat.getColor(context!!, R.color.gray_4)
            private val colorTitleCurrent = ContextCompat.getColor(context!!, R.color.black)
            private var flagCenterProgress = false

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Int> {
                val view = LayoutInflater.from(context).inflate(R.layout.wizard_step, parent, false)
                return object : BaseViewHolder<Int>(view) {
                    private val wizardStep: Step = itemView.findViewById(R.id.wizard_step)

                    init {
                        wizardStep.titleSize = resources.getDimension(R.dimen.wizard_step_title_size)
                    }

                    override fun bind(item: Int, type: Int, itemListener: WizardListener?) {
                        val nivelConsultora = if ((item - 1) < size) niveles[(item - 1)] else null
                        wizardStep.setTitle(nivelConsultora?.descripcionNivel ?: "")
                        wizardStep.setTitleAppareance(setStyle(type))
                        wizardStep.setTitleColor(if (type == TYPE_CURRENT) colorTitleCurrent else colorTitle)

                        wizardStep.setImageResource(
                            when (type) {
                                TYPE_DISABLE -> when (item) {
                                    WizardLevelType.WIZARD_NIVEL_1 -> R.drawable.ic_coral_color_deshabilitado
                                    WizardLevelType.WIZARD_NIVEL_2 -> R.drawable.ic_coral_color_deshabilitado
                                    WizardLevelType.WIZARD_NIVEL_3 -> R.drawable.ic_ambar_color_deshabilitado
                                    WizardLevelType.WIZARD_NIVEL_4 -> R.drawable.ic_perla_color_deshabilitado
                                    WizardLevelType.WIZARD_NIVEL_5 -> R.drawable.ic_topacio_color_deshabilitado
                                    WizardLevelType.WIZARD_NIVEL_6 -> R.drawable.ic_brillante_color_deshabilitado
                                    else -> R.drawable.ic_brillante_color_deshabilitado
                                }
                                else -> when (item) {
                                    WizardLevelType.WIZARD_NIVEL_1 -> R.drawable.ic_nivel_consultora
                                    WizardLevelType.WIZARD_NIVEL_2 -> R.drawable.ic_nivel_coral
                                    WizardLevelType.WIZARD_NIVEL_3 -> R.drawable.ic_nivel_ambar
                                    WizardLevelType.WIZARD_NIVEL_4 -> R.drawable.ic_nivel_perla
                                    WizardLevelType.WIZARD_NIVEL_5 -> R.drawable.ic_nivel_topacio
                                    WizardLevelType.WIZARD_NIVEL_6 -> R.drawable.ic_nivel_brillante
                                    else -> R.drawable.ic_nivel_consultora
                                }
                            }
                        )

                        when (type) {
                            TYPE_CURRENT -> {
                                wizardStep.elevation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) 10F else 0F
                                wizardStep.setMostrarCircle(true)
                            }
                            else -> {
                                wizardStep.elevation = 0F
                                wizardStep.setMostrarCircle(false)
                            }
                        }

                        wizardStep.setOnClickListener { step ->
                            nivelConsultora?.let {
                                presenter.onSelectedNivel(step as Step, it)
                                //Tracker.CaminoBrillante.selectNivel(user, it.descripcionNivel)
                                //goToDetailBenefit(it.codigoNivel ?: "")
                            }
                        }

                        when {
                            !flagCenterProgress -> {
                                val progress = wzdNiveles.getPgWizard()
                                progress.postDelayed({
                                    when {
                                        progress.layoutParams is FrameLayout.LayoutParams -> {
                                            val params = progress.layoutParams as FrameLayout.LayoutParams
                                            params.bottomMargin = wizardStep.getTitleHeigth() / 2
                                            progress.layoutParams = params
                                            progress.invalidate()
                                        }
                                    }
                                }, 50)
                                flagCenterProgress = true
                            }
                        }
                    }
                }
            }
        }
        return wizardAdapter
    }

    private fun setStyle(type: Int): Int {
        return if (type == WizardAdapter.TYPE_CURRENT) R.style.FontLatoBold else R.style.FontLatoRegular
    }
    //endregion

    override fun onLoadBarraMontoAcumulado(bma: BarraMontoAcumuladoViewModel) {
        barraMontoAcumulado.isVisibleIndicador(bma.isVisibleIndicador)
        barraMontoAcumulado.isVisibleTextMontoAcumulado(bma.isVisibleAccumulatedAmount)
        barraMontoAcumulado.visibilityIcon2(bma.visibilityIcon2)
        barraMontoAcumulado.visibilityIcon1(bma.visibilityIcon1)
        barraMontoAcumulado.isVisibleFlag(bma.isVisibleFlag)
        barraMontoAcumulado.isVisibleIndicador(bma.isVisibleIndicador)
        barraMontoAcumulado.isVisibleText1(bma.isVisibleText1)
        barraMontoAcumulado.isVisibleText2(bma.isVisibleText2)
        barraMontoAcumulado.isVisibleMontoSiguienteNivel(bma.isVisibleAmountNextLevel)

        barraMontoAcumulado.setTextSiguienteNivel(bma.textNameNextLevel)
        barraMontoAcumulado.setTextMontoSiguienteNivel(bma.textAmountNextLevel)
        barraMontoAcumulado.setTextMontoAcumulado(bma.textAccumulatedAmount)

        barraMontoAcumulado.setText1(bma.text1)
        barraMontoAcumulado.setAlignementText1(bma.text1Alignment)
        barraMontoAcumulado.setText1Bold(bma.text1IsBold)

        barraMontoAcumulado.setText2(bma.text2)
        barraMontoAcumulado.setAlignementText2(bma.text2Alignment)

        barraMontoAcumulado.setText3(bma.text3)

        barraMontoAcumulado.setColorIndicador(bma.idResColorIndicador)

        barraMontoAcumulado.setPorcentajeIndicadorMontoMinimo(bma.montoMinimoPorcentaje)
        barraMontoAcumulado.setPorcentajeIndicadorMontoAcumulado(bma.montoAcumuladoPorcentaje)

        // barraMontoAcumulado.setSizeText1(bma.text1SizeDp)
        // barraMontoAcumulado.setSizeText2(bma.text2SizeDp)

        Handler().postDelayed({
            barraMontoAcumulado.setProgressBarra(bma.montoAcumuladoPorcentaje)
        }, 500)
    }

    override fun onLoadBeneficios(nivel: NivelCaminoBrillante?, showAll: Boolean) {
        fingerBack.visibility = if (onGananciaAnin && onShowNivelActual) View.VISIBLE else View.GONE
        ivwNivelBeneficio.setImageResource(presenter.getIconNivelById(nivel?.codigoNivel
            ?: "", true))
        tvwBeneficios.text = "${getString(R.string.beneficios_de)} ${nivel?.descripcionNivel}"
        nivel?.beneficios?.let {
            if (showAll) {
                newAdapterBeneficios = BeneficiosNewAdapter(it.toMutableList())
                rvwBeneficios.layoutManager = LinearLayoutManager(context)
                rvwBeneficios.adapter = newAdapterBeneficios
                rvwBeneficios.isNestedScrollingEnabled = false
                btnSeeLessMore.visibility = View.GONE
                arrow.visibility = View.GONE
            } else {
                adapterBeneficios = BeneficiosAdapter(it.toMutableList(), 3)
                rvwBeneficios.layoutManager = LinearLayoutManager(context)
                rvwBeneficios.adapter = adapterBeneficios
                rvwBeneficios.isNestedScrollingEnabled = false

                btnSeeLessMore.setText(getString(R.string.ver_mas))
                arrow.rotation = 360f

                btnSeeLessMore.visibility = View.VISIBLE
                arrow.visibility = View.VISIBLE
            }
        }
    }

    //region carrusel
    override fun onLoadOfertasCarousel(verMas: Boolean, offers: ArrayList<OfferModel>) {
        offerKitsDemostradores?.setIsCustomView(true)
        offerKitsDemostradores?.updateCarouselItems(offers.size)
        offerKitsDemostradores?.setProducts(offers, null, null)
        offerKitsDemostradores?.showSeeMore(verMas)
    }

    override fun onBindMultiKit(oferta: Carousel.OfertaCarousel, multi: Multi, codigoNivel: String) {
        multi.setImageTag(presenter.getTagNivel(codigoNivel, true))
        multi.setTacharLabelYou(false)
        multi.setMultiImageMargin(null, 24F, null, 8F)
        multi.setVisibilityCounter(View.GONE)
        multi.setButtonMargin(null, 33F, null, null)
        multi.setTitleLabelClient(getString(R.string.camino_brillante_earn_up))
        multi.setTitleLabelYou(getString(R.string.camino_brillante_price_for_you))
        multi.setValues(oferta.descripcionMarca ?: "", oferta.descripcionCortaCUV
            ?: oferta.descripcionCUV ?: "",
            presenter.formatWithMoneySymbol(oferta.precioCatalogo
                ?: 0.0), presenter.formatWithMoneySymbol(oferta.ganancia ?: 0.0),
            oferta.getFotoProducto() ?: "")

    }

    override fun onBindMultiDemostrador(oferta: Carousel.OfertaCarousel, multi: Multi) {
        multi.setImageTag(null)
        multi.setTacharLabelYou(true)
        multi.setMultiImageMargin(null, 8F, null, 0F)
        multi.setVisibilityCounter(View.VISIBLE)
        multi.setButtonMargin(null, 8F, null, null)
        multi.setTitleLabelClient(getString(R.string.camino_brillante_price_for_you))
        multi.setTitleLabelYou(getString(R.string.camino_brillante_price))
        multi.setValues(oferta.descripcionMarca ?: "", oferta.descripcionCortaCUV
            ?: oferta.descripcionCUV ?: "",
            presenter.formatWithMoneySymbol(oferta.precioValorizado
                ?: 0.0), presenter.formatWithMoneySymbol(oferta.precioCatalogo ?: 0.0),
            oferta.getFotoProducto() ?: "")

    }
    //endregion carrusel

    override fun onLoadGanancias(historicoConsultora: List<NivelConsultoraCaminoBrillante>) {
        val barEntries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        var idx = 1.0f

        historicoConsultora.forEach {
            val valueCamp = it.campania!!.substring(4, 6)
            barEntries.add(BarEntry(idx++, it.montoPedido!!.toFloat(), it))
            labels.add("C$valueCamp")
        }

        if (labels.size < 6) {
            var idxFix = idx;
            for (i in labels.size..6) {
                barEntries.add(BarEntry(idxFix++, 0.0f, null))
                labels.add("")
            }
        }

        val iSizeValid = historicoConsultora.size

        val barDataSet = BarDataSet(barEntries, getString(R.string.camino_brillante_campana))
        barDataSet.valueFormatter = IValueFormatter { value, entry, dataSetIndex, _ ->
            var result = ""
            val idx = entry.x
            if (iSizeValid >= idx)
                result = presenter.format(value.toDouble())
            result
        }
        barDataSet.color = resources.getColor(R.color.section_pink)
        barDataSet.highLightAlpha = 200
        barDataSet.highLightColor = resources.getColor(R.color.select_bar)

        val theData = BarData(barDataSet)
        bchtGanancias.data = theData
        bchtGanancias.xAxis.valueFormatter = IAxisValueFormatter { value, _ ->
            var text = ""
            if (historicoConsultora.size >= value.toInt() && value.toInt() > 0) {
                text = labels[value.toInt() - 1]
            }
            text
        }

        initBchtGanancias()

        var indice = -1
        var _indice = 0

        historicoConsultora.forEach { it ->
            it.isFlagSeleccionMisGanancias?.let {
                if (it) {
                    indice = _indice
                }
            }
            _indice++
        }
        when {
            historicoConsultora.size > 1 -> lblTituloMonto.text = "${getString(R.string.camino_brillante_monto_pedido)} ${labels[0]} a ${labels[historicoConsultora.size - 1]}"
            else -> lblTituloMonto.text = "${getString(R.string.camino_brillante_monto_pedido)} ${labels[0]}"
        }

        val listener = object : OnChartValueSelectedListener {
            var highlight: Highlight? = null

            fun autoSelected() {
                when {
                    indice > -1 -> {
                        val x = indice
                        val nivelConsultoraCaminoBrillante = historicoConsultora[x]
                        lblGananciaCampania.text = "${getString(R.string.camino_brillante_ganancia)} ${labels[x]}"
                        tvwGananciaCampania.text = presenter.formatWithMoneySymbol(nivelConsultoraCaminoBrillante.gananciaCampania!!)
                        tvwGananciaPeriodo.text = presenter.formatWithMoneySymbol(nivelConsultoraCaminoBrillante.gananciaPeriodo!!)
                        bchtGanancias.highlightValue((indice + 1).toFloat(), nivelConsultoraCaminoBrillante.montoPedido!!.toFloat(), 0)
                    }
                }
            }

            override fun onNothingSelected() {
                highlight?.let {
                    bchtGanancias.highlightValue(highlight, false)
                    return
                }
                autoSelected()
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                e?.let {
                    val nivelConsultoraCaminoBrillante = e.data as NivelConsultoraCaminoBrillante?
                    nivelConsultoraCaminoBrillante?.let {
                        if (it.montoPedido!!.toFloat() > 0) {
                            lblGananciaCampania.text = "${getString(R.string.camino_brillante_ganancia)} ${labels[e.x.toInt() - 1]}"
                            tvwGananciaCampania.text = presenter.formatWithMoneySymbol(it.gananciaCampania!!)
                            tvwGananciaPeriodo.text = presenter.formatWithMoneySymbol(it.gananciaPeriodo!!)
                            h?.let {
                                highlight = h
                            }
                            return
                        }
                    }
                }
                onNothingSelected()
            }
        }

        //fingerBack.visibility = View.VISIBLE
        bchtGanancias.setOnChartValueSelectedListener(listener)
        listener.autoSelected()
    }

    override fun onSelectNivel(isNivelActualConsultora: Boolean) {
        nsvMain.scrollTo(0, 0)
        listener.onSelectNivel(isNivelActualConsultora)
    }

    fun autoSelectedCurrentLevel() {
        presenter.autoSelectNivelActual()
    }

    //region show/hide views
    override fun showCaminoBrillante() {
        ctlMain.visibility = View.VISIBLE
    }

    override fun hideCaminoBrillante() {
        ctlMain.visibility = View.GONE
    }

    override fun showBarraNiveles() {
        wzdNiveles.visibility = View.VISIBLE
    }

    override fun hideBarraNiveles() {
        wzdNiveles.visibility = View.GONE
    }

    override fun showBarraMonto(isAvaibleBarraMonto: Boolean) {
        if (isAvaibleBarraMonto) {
            grpBarraMontoAcumulado.visibility = View.VISIBLE
        }
    }

    override fun hideBarraMonto() {
        grpBarraMontoAcumulado.visibility = View.GONE
    }

    override fun showNivelesSuperiores(isAvaibleEnterateMas: Boolean, resIdColor: Int, text1: String, text2: String?) {
        tvw1NivelesSuperiores.text = text1

        text2?.let {
            tvw2NivelesSuperiores.text = it
            tvw2NivelesSuperiores.visibility = View.VISIBLE
        } ?: run {
            tvw2NivelesSuperiores.visibility = View.GONE
        }

        context?.let {
            ctlNivelesSuperiores.background.setColorFilter(ContextCompat.getColor(it, resIdColor), PorterDuff.Mode.SRC_IN)
        }
        btnEnterateMas.visibility = if (isAvaibleEnterateMas) View.VISIBLE else View.GONE
        grpNivelesSuperiores.visibility = View.VISIBLE
        onShowNivelActual = false
    }

    override fun hideNivelesSuperiores() {
        onShowNivelActual = true
        if (onGananciaAnin && onShowNivelActual) {
            fingerBack.visibility = View.VISIBLE
        }
        grpNivelesSuperiores.visibility = View.GONE
    }

    override fun showBeneficios() {
        fingerBack.visibility = if (onGananciaAnin && onShowNivelActual) View.VISIBLE else View.GONE
        cvwBeneficios.visibility = View.VISIBLE
    }

    override fun hideBeneficios() {
        cvwBeneficios.visibility = View.GONE
    }

    override fun showOffers(isTieneOfertas: Boolean, isAvaibleKitsYDemostradores: Boolean, showLoading: Boolean, isCargoAnteriormente: Boolean) {
        if (isTieneOfertas) {
            if (isAvaibleKitsYDemostradores) {
                if (isCargoAnteriormente) {
                    if (showLoading) {
                        offerKitsDemostradores.startLoading()
                    }
                    btnVerOfertas.visibility = View.GONE
                    offerKitsDemostradores.visibility = View.VISIBLE
                } else {
                    btnVerOfertas.visibility = View.GONE
                    offerKitsDemostradores.visibility = View.GONE
                }
            } else {
                btnVerOfertas.visibility = View.VISIBLE
                offerKitsDemostradores.visibility = View.GONE
            }
        } else {
            btnVerOfertas.visibility = View.GONE
            offerKitsDemostradores.visibility = View.GONE
        }
    }

    override fun hideOffers() {
        if (btnVerOfertas == null || offerKitsDemostradores == null) return
        btnVerOfertas.visibility = View.GONE
        offerKitsDemostradores.visibility = View.GONE
    }

    override fun showLogros() {
        cvwLogros.visibility = View.VISIBLE
    }

    override fun hideLogros() {
        cvwLogros.visibility = View.GONE
    }

    override fun showGanancias(isAvaibleGanancias: Boolean, onGananciaAnin: Boolean) {
        if (isAvaibleGanancias) {
            grpMisGanancias.visibility = View.VISIBLE
            if (onGananciaAnin) {
                this.onGananciaAnin = onGananciaAnin
                fingerBack.visibility = View.VISIBLE
            }
        }
    }

    override fun showGananciasAfter(isAvaibleGanancias: Boolean) {
        if (isAvaibleGanancias) {
            grpMisGanancias.visibility = View.VISIBLE
        }
    }

    override fun hideGanancias() {
        grpMisGanancias.visibility = View.GONE
        fingerBack.visibility = View.GONE
    }


    //endregion

    override fun goToAcademia(enterateMasParam: String?) {
        val intent = Intent(context, AcademiaActivity::class.java)
        intent.putExtra(AcademiaActivity.EXTRA_VALUE, enterateMasParam ?: "")
        startActivity(intent)
    }

    override fun goToIssuu(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    override fun onErrorLoadCaminoBrillante() {
        activity?.onBackPressed()
    }

    fun onFragmentVisible(isAutoSelecteNivelActual: Boolean) {
        updateBadge()
        presenter.reloadCarrusel()
        if (isAutoSelecteNivelActual) {
            presenter.autoSelectNivelActual()
        }
    }

    override fun onCarouselItemClick(keyItem: String, marcaID: Int, marca: String, origenPedidoWeb: String) {
        listener.onClickItem(keyItem, marcaID, marca, origenPedidoWeb)
    }

    private fun goToOfertasEspeciales() {
        listener.goToOfertasEspeciales()
    }

    override fun showMessage(message: String) {
        //showSnackBarMessage(layMain, message)
    }

    override fun showMessage(resIdMessage: Int) {
        //showSnackBarMessage(layMain, getString(resIdMessage))
    }

    override fun showBottomDialog(messageRes: Int, imageLink: String?, messageColorRes: Int) {
        context?.let {
            showBottomDialog(it, it.getString(messageRes), imageLink, ContextCompat.getColor(it, messageColorRes))
        }
    }

    override fun showError(message: String) {
        context?.let {
            BottomDialog.Builder(it)
                .setIcon(R.drawable.ic_mano_error)
                .setContent(message)
                .setNeutralText(getString(R.string.msj_entendido))
                .onNeutral(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                    }
                })
                .setNeutralBackgroundColor(R.color.magenta)
                .show()
        }
    }

    override fun showError(resIdMessage: Int) {
        context?.let {
            BottomDialog.Builder(it)
                .setIcon(R.drawable.ic_mano_error)
                .setContent(resIdMessage)
                .setNeutralText(getString(R.string.msj_entendido))
                .onNeutral(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                    }
                })
                .setNeutralBackgroundColor(R.color.magenta)
                .show()
        }
    }

    override fun onDemostradorAdded(counter: Counter) {
        counter.changeQuantity(1)
    }

    override fun onKitAdded(multi: Multi?, codigoNivel: String) {
        multi?.setEnabledMulti(false)
        multi?.setImageTag(presenter.getTagNivel(codigoNivel, false))
    }

    override fun updateOffersCount(count: Int) {
        context?.let {
            LocalBroadcastManager.getInstance(it).sendBroadcast(Intent(CaminoBrillanteActivity.BROADCAST_COUNT_ACTION).putExtra(CaminoBrillanteActivity.EXTRA_COUNT_BADGE, count))
        }
    }

    //region AnimationRotation
    private fun rotate() {
        val rotate = ObjectAnimator.ofFloat(arrow, View.ROTATION, 180f)
        rotate.duration = 1000
        rotate.interpolator = AccelerateInterpolator()
        rotate.start()
    }

    private fun rotateOriginal() {
        val rotate = ObjectAnimator.ofFloat(arrow, View.ROTATION, 360f)
        rotate.duration = 1000
        rotate.interpolator = AccelerateInterpolator()
        rotate.start()
    }

    //endregion

    //region badge
    private fun formatCount(count: Int): String {
        return if (count <= 99)
            count.toString()
        else
            "99+"
    }

    fun updateBadge() {
        context?.let {
            ordersCount = SessionManager.getInstance(it).getOrdersCount()

            if (ordersCount != null)
                cartBadge?.text = formatCount(ordersCount ?: 0)
            else {
                cartBadge?.text = "0"
            }
        }
    }

    fun updateCount(count: Int) {
        context?.let {
            ordersCount = if (ordersCount == null)
                count
            else
                ordersCount!! + count

            SessionManager.getInstance(it).saveOffersCount(ordersCount ?: 0)
            updateBadge()
            sendCountBroadcast()
        }
    }

    private val mBadgeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val count = intent.getIntExtra(CaminoBrillanteActivity.EXTRA_COUNT_BADGE, 0)
            updateCount(count)
        }
    }

    private fun sendCountBroadcast() {
        val countIntent = Intent(BaseFichaActivity.BROADCAST_COUNT_ACTION)
        activity?.sendBroadcast(countIntent)
    }
    //endregion

    interface Listener {
        fun goToDetalleLogro()
        fun goToOfertasEspeciales()
        fun onClickItem(key: String, marcaId: Int, marcaName: String, origenPedidoWeb: String)
        fun onSelectNivel(isNivelActualConsultora: Boolean)
        fun goToOrders(menu: Menu)
        fun goToOnBoarding()
        fun setCanBack(canBack: Boolean)
    }

    //utils
    override fun getTextByIdRes(id: Int, vararg params: String): String = getString(id, *params)

    override fun getDimensionByIdRes(id: Int): Float = context?.resources?.getDimension(id) ?: 0f

    override fun updateImageStep(isNivelAnteriorActual: Boolean, resIdAnterior: Int, wizardStepAnterior: Step?, resIdNuevo: Int, wizardStepNuevo: Step?) {
        context?.let {
            if (!isNivelAnteriorActual) {
                wizardStepAnterior?.setImageResource(resIdAnterior)
                wizardStepAnterior?.setTitleColor(ContextCompat.getColor(it, R.color.gray_disabled_text))
            }
            wizardStepNuevo?.setTitleColor(ContextCompat.getColor(it, R.color.black))
            wizardStepNuevo?.setImageResource(resIdNuevo)
        }
    }

    override fun onGetMenu(menu: Menu) {
        listener.goToOrders(menu)
    }

    override fun goToOnBoarding() {
        listener.goToOnBoarding()
    }

    override fun showConfirmDialog() {
        BottomDialog.Builder(requireContext())
            .setTitle(getString(R.string.titulo_popup_onboarding))
            .setTitleBold()
            .setContent(R.string.content_popup_onboarding)
            .setNeutralText(R.string.button_aceptar)
            .withConfirmation(true)
            .setCancelable(false)
            .setNeutralBackgroundColor(R.color.magenta)
            .onNeutral(object : BottomDialog.ButtonCallback {
                override fun onClick(dialog: BottomDialog, chbxConfirmacion: CheckBox) {
                    presenter.saveConfirmacion(chbxConfirmacion)
                    dialog.dismiss()
                    presenter.onConfirmDialog()
                }
            }).show()
    }

    override fun showAnimationChangeLevel(showConfeti: Boolean, icono: Int, title: Spanned, mensaje: Spanned) {
        val dialogBuilder = FullScreenDialog.Builder(requireContext())
            .withTitleHtml(title, 18F)
            .withMessageHtml(mensaje, 16F)
            .withIcon(icono)
            .withScreenDismiss(true)
            .setTime(5000)

        if (showConfeti)
            dialogBuilder.withAnimation(resources, FullScreenDialog.SIMPLE_ANIMATION,
                ContextCompat.getColor(requireContext(), R.color.dorado),
                ContextCompat.getColor(requireContext(), R.color.primary))
        else
            dialogBuilder.withVanish(true)

        dialogBuilder.show()
    }

    override fun hideFinder() {
        presenter.saveFinger()
        onGananciaAnin = false
    }

    override fun setCanBack(canBack: Boolean) {
        listener.setCanBack(canBack)
    }

}
