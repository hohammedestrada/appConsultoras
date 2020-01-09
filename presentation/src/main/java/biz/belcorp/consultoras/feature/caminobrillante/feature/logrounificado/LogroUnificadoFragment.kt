package biz.belcorp.consultoras.feature.caminobrillante.feature.logrounificado

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.domain.entity.caminobrillante.LogroCaminoBrillante
import biz.belcorp.consultoras.feature.caminobrillante.CaminoBrillanteActivity
import biz.belcorp.consultoras.feature.caminobrillante.di.CaminoBrillanteComponent
import biz.belcorp.consultoras.feature.caminobrillante.feature.logrounificado.adapters.RvAdapterLogro
import biz.belcorp.consultoras.util.anotation.MedallaCaminoBrilanteType
import biz.belcorp.mobile.components.charts.circle.Icon
import biz.belcorp.mobile.components.charts.circle.Indicador
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_logros_unificados.*
import javax.inject.Inject

class LogroUnificadoFragment : BaseFragment(), LogroUnificadoView, RvAdapterLogro.Listener {

    @Inject
    lateinit var presenter: LogroUnificadoPresenter

    private lateinit var rvAdapterLogro: RvAdapterLogro

    private lateinit var listener: Listener

    companion object {
        fun newInstance(): LogroUnificadoFragment {
            return LogroUnificadoFragment()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as CaminoBrillanteActivity
    }

    override fun context(): Context? {
        return activity?.applicationContext
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_logros_unificados, container, false)
    }

    fun init() {
        initViews()
        Handler().postDelayed({
            presenter.init()
        }, 400)
    }

    private fun initViews() {
        rvAdapterLogro = RvAdapterLogro(this)

        rvwLogros.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvwLogros.isNestedScrollingEnabled = false

        rvwLogros.setHasFixedSize(true)
        rvwLogros.adapter = rvAdapterLogro
    }

    override fun setPhotoUser(urlImagen: String) {
        Glide.with(this).load(urlImagen).apply(RequestOptions.noTransformation()
            .placeholder(R.drawable.ic_contact_default)
            .error(R.drawable.ic_contact_default)
            .priority(Priority.HIGH))
            .into(ivwAvatar)
    }

    override fun setResumenLogro(tiempo: String, mensaje: String, lblIndicador1: String, indicador1: String, lblIndicador2: String, indicador2: String, lblIndicador3: String, indicador3: String) {
        tvwTiempo.text = tiempo
        tvwMensaje.text = mensaje

        tvwlblIndicador1.text = lblIndicador1
        tvwlblIndicador2.text = lblIndicador2
        tvwlblIndicador3.text = lblIndicador3

        tvwIndicador1.text = indicador1
        tvwIndicador2.text = indicador2
        tvwIndicador3.text = indicador3
    }

    override fun setDataIndicadores(logros: List<LogroCaminoBrillante>) {
        val mLogros = ArrayList<LogroCaminoBrillante>()
        var logroEscala = logros.filter { e -> e.id == "CRECIMIENTO" }.firstOrNull()
        if (logroEscala != null) {
            if (logroEscala.indicadores?.size == 2) {
                var indIncPedido = logroEscala!!.indicadores!!.filter { e -> e.codigo == "INCREMENTO_PEDIDO" }.firstOrNull()
                if (indIncPedido != null) {
                    (logroEscala!!.indicadores!! as ArrayList).remove(indIncPedido)
                    var logroVirtual = LogroCaminoBrillante()
                    logroVirtual.let {
                        it.id = logroEscala.id
                        it.indicadores = ArrayList()
                        it.descripcion = logroEscala.descripcion
                        it.titulo = logroEscala.titulo
                        (it.indicadores!! as ArrayList).add(indIncPedido)
                    }
                    mLogros!!.addAll(logros)
                    mLogros!!.add(logroVirtual)

                    rvAdapterLogro.updateData(mLogros)
                }
            }
        } else {
            rvAdapterLogro.updateData(logros)
        }

        rvwLogros.visibility = View.VISIBLE
        progressLogros.visibility = View.GONE
    }

    override fun onItemClickBefore(view: View, position: Int, rvwLogro: RecyclerView) {
        rvwLogro.smoothScrollToPosition(position - 1)
    }

    override fun onItemClickNext(view: View, position: Int, rvwLogro: RecyclerView) {
        rvwLogro.smoothScrollToPosition(position + 1)
    }

    override fun onItemMedallaClick(view: View, position: Int, medalla: LogroCaminoBrillante.Indicador.Medalla) {
        context?.let {
            BottomDialog.Builder(it)
                .setIcon(getIconMedalla(medalla, it))
                .setTitle(medalla.modalTitulo ?: "")
                .setTitleBold()
                .setBackground(R.drawable.skill_balls)
                .setBackgroundHeight(100F)
                .setContent(getDescriptionMedalla(medalla))
                .setNeutralText(getString(R.string.msj_entendido))
                .setNeutralBackgroundColor(R.color.magenta)
                .onNeutral(object : BottomDialog.ButtonCallback {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                    }
                })
                .show()
        }
    }

    override fun onScrollPeriodo(cantidadPeriodos: Int, posicion: Int, ivwBack: ImageView, ivwNext: ImageView) {
        if (cantidadPeriodos > 0) {
            if (posicion == 0) {
                ivwBack.visibility = View.GONE
            } else {
                ivwBack.visibility = View.VISIBLE
            }
            if (posicion == cantidadPeriodos - 1) {
                ivwNext.visibility = View.GONE
            } else {
                ivwNext.visibility = View.VISIBLE
            }
        }
    }

    private fun getDescriptionMedalla(medalla: LogroCaminoBrillante.Indicador.Medalla): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(medalla.modalDescripcion ?: "", Html.FROM_HTML_MODE_COMPACT)
    } else Html.fromHtml(medalla.modalDescripcion ?: "")


    private fun getIconMedalla(medalla: LogroCaminoBrillante.Indicador.Medalla, context: Context): View? {
        var view: View? = null
        when (medalla.tipo) {
            MedallaCaminoBrilanteType.CIRCULAR -> {
                view = getIndicador(medalla, context)
            }
            MedallaCaminoBrilanteType.PEDIDO -> {
                view = getPedido(context)
            }
        }
        return view
    }

    private fun getIndicador(medalla: LogroCaminoBrillante.Indicador.Medalla, context: Context): View {
        return Indicador.Builder(context)
            .setText(medalla.valor ?: "")
            .setTextSize(32F)
            .setTextColor(R.color.white)
            .setBackground(R.drawable.bg_medalla_active)
            .setSize(52F)
            .build()
    }

    private fun getPedido(context: Context): View {
        return Icon.Builder(context)
            .setSize(52F)
            .setBackground(R.drawable.bg_medalla_active)
            .setIcon(R.drawable.ic_logro_pedido)
            .build()
    }

    override fun setCanBack(canBack: Boolean) {
        listener.setCanBack(canBack)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    interface Listener {
        fun setCanBack(canBack: Boolean)
    }

}
