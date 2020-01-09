package biz.belcorp.consultoras.feature.ficha.enriquecida

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.component.video.YoutubeVideo
import biz.belcorp.consultoras.common.component.video.model.VideoModel
import biz.belcorp.consultoras.common.model.ficha.FichaSectionEnriquecidaModel
import biz.belcorp.consultoras.domain.entity.Componente
import biz.belcorp.consultoras.domain.entity.Detalle
import biz.belcorp.consultoras.domain.entity.Secciones
import biz.belcorp.consultoras.feature.ficha.adapter.FichaSectionEnriquecidaAdapter
import biz.belcorp.consultoras.feature.ficha.util.FichaCarouselsHelper.generateDetailRecycler
import biz.belcorp.consultoras.feature.ficha.util.FichaCarouselsHelper.generateDetailTitle
import biz.belcorp.consultoras.feature.ficha.util.FichaCarouselsHelper.generateSpaceDivider
import biz.belcorp.consultoras.util.CommunicationUtils
import biz.belcorp.consultoras.util.UXCamUtils
import biz.belcorp.consultoras.util.analytics.Ficha
import biz.belcorp.consultoras.util.anotation.SectionType
import biz.belcorp.mobile.components.core.extensions.betterSmoothScrollToPosition
import biz.belcorp.mobile.components.design.text.model.TextSectionModel
import biz.belcorp.mobile.components.dialogs.bottom.BottomDialog
import com.uxcam.UXCam
import kotlinx.android.synthetic.main.fragment_ficha_enriquecida.*
import java.util.*

class FichaEnriquecidaFragment : BaseFragment(),
    FichaSectionEnriquecidaAdapter.Listener, 
    YoutubeVideo.ErrorListener,
    SafeLet {

    private lateinit var adapter: FichaSectionEnriquecidaAdapter

    private var fichaRecycler: RecyclerView? = null
    private var data: Componente? = null

    private var fromGanaMas: Boolean = false

    override fun context(): Context? {

        val activity = activity
        return activity?.applicationContext
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ficha_enriquecida, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        setupExtras()
        showData()
        UXCam.tagScreenName(UXCamUtils.FichaEnriquecidaFragmentName)

    }

    private fun setupExtras() {
        fromGanaMas = arguments?.getBoolean(FROM_GANAMAS_KEY) ?: false
    }

    /*********** Llamar updateAdapter antes de añadir fragment ***********/

    fun initAdapter(context: Context) {

        adapter = FichaSectionEnriquecidaAdapter(context, this, this)

    }

    fun updateAdapter(component: Componente?) {

        data = component?.copy()

        component?.secciones?.let { sections ->
            if (sections.isNotEmpty()) {

                adapter.component = component.copy()
                adapter.setList(transformListSections(sections))

            } else {

                resetData()

            }

        } ?: run {

            resetData()

        }
    }

    private fun resetData() {

        data = null
        adapter.component = null
        adapter.items.clear()

    }

    fun hasData() : Boolean = adapter.items.isNotEmpty()

    private fun showData() {

        data?.secciones?.let { sections ->

            if (sections.isNotEmpty()) {

                val fichaDivider = generateSpaceDivider(requireContext())
                val fichaTitle = generateDetailTitle(requireContext())
                fichaRecycler = generateDetailRecycler(requireContext())

                fichaEnriquecidaContainer.removeAllViews()
                fichaEnriquecidaContainer.addView(fichaDivider)
                fichaEnriquecidaContainer.addView(fichaTitle)
                fichaEnriquecidaContainer.addView(fichaRecycler)

                fichaTitle.text = String.format(getString(R.string.ficha_detalle_de), data?.nombreComercial?.trim())

                fichaRecycler?.layoutManager = LinearLayoutManager(context)
                fichaRecycler?.isNestedScrollingEnabled = false
                fichaRecycler?.adapter = adapter

            }

        }

    }

    private fun transformListSections(sections: List<Secciones?>): ArrayList<FichaSectionEnriquecidaModel> {
        
        return arrayListOf<FichaSectionEnriquecidaModel>().apply {

            sections.forEach { section ->

                safeLet(section?.titulo, section?.tipo) { titulo, tipo ->

                    section?.detalles?.let { detalle ->
                        add(FichaSectionEnriquecidaModel(titulo, tipo, 
                            BuildConfig.API_YOUTUBE, transformListDetail(detalle, tipo)))
                    }

                }

            }

        }
        
    }

    private fun transformListDetail(details: List<Detalle?>, tipo: Int): ArrayList<Comparable<*>> {
        
        return arrayListOf<Comparable<*>>().apply {
            
            details.forEach { det ->
                
                when (tipo) {
                    
                    SectionType.VIDEO -> {
                        if (!det?.key.isNullOrEmpty()) {
                            add(VideoModel(det?.key, det?.titulo ?: ""))
                        }
                    }
                    
                    SectionType.TEXT -> {
                        safeLet(det?.titulo, det?.descripcion, det?.key) { titulo, descripcion, key ->
                            add(TextSectionModel(titulo, descripcion))
                        }
                    }
                    
                    else -> {}
                    
                }
                
            }
            
        }
        
    }

    /**
     *  FichaSectionEnriquecidaAdapter.Listener
     */

    override fun onPlayerSectionVideoYoutube(item: Componente?) {

        item?.let { componente ->
            if (fromGanaMas) {
                Ficha.clickVideo(componente)
            } else {
                Ficha.clickVideoFromProductDetail(componente)
            }
        }

    }

    override fun onPressedSectionEnriquecida(position: Int) {
        fichaRecycler?.betterSmoothScrollToPosition(position)
    }

    override fun onPressedSectionExpand(position: Int, data: FichaSectionEnriquecidaModel, item: Componente?) {

        item?.let { componente ->
            if (fromGanaMas) {
                Ficha.clickTab(data, componente)
            } else {
                Ficha.clickTabFromProductDetail(data, componente)
            }
        }

    }

    /**
     *  YoutubeVideo.ErrorListener
     */

    override fun isRequiredUpdateYoutube() {

        showMessageErrorYoutube(getString(R.string.youtube_not_updated),
            getString(R.string.go_play_store))

    }

    override fun isRequiredActivateYoutube() {

        showMessageErrorYoutube(getString(R.string.youtube_not_activated),
            getString(R.string.go_play_store))

    }

    override fun isRequiredInstallYoutube() {

        showMessageErrorYoutube(getString(R.string.youtube_not_installed),
            getString(R.string.go_play_store))

    }

    override fun unknownErrorYoutube() {
        showDefaultError(getString(R.string.ficha_message_error_video))
    }

    private fun showMessageErrorYoutube(message: String, textPositive: String = getString(R.string.button_aceptar)) {

        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(textPositive) { _, _ ->
                CommunicationUtils.openYoutubePlayStore(requireContext())
            }
            .setNegativeButton(R.string.button_cancelar) { dialog, _ ->
                dialog.dismiss()
            }
            .create().show()

    }

    private fun showDefaultError(message: String) {

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

    companion object {

        val TAG: String = FichaEnriquecidaFragment::class.java.simpleName
        const val FROM_GANAMAS_KEY = "FROM_GANAMAS_KEY"

        fun makeInstance(fromGanaMas: Boolean) : FichaEnriquecidaFragment {

            val fragment = FichaEnriquecidaFragment()
            val bundle = Bundle()
            bundle.putBoolean(FROM_GANAMAS_KEY, fromGanaMas)
            fragment.arguments = bundle
            return fragment

        }

    }

}
