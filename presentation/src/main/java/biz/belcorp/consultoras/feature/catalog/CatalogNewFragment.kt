package biz.belcorp.consultoras.feature.catalog

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.catalog.CatalogModel
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.BrandCode

import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.fragment_catalog_new.*
import java.lang.NullPointerException

/**
 *
 */
class CatalogNewFragment : BaseFragment(), CatalogTopAdapter.CatalogTopAdapterListener{

    private var listener: CatalogAdapter.CatalogEventListener? = null
    private var newListener: CatalogNewListener? = null

    private var campaingBrand: String? = ""

    private var remoteConfig : FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_catalog_new, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState == null) {
            init()
        }
    }

    private fun init() {

        val arguments = arguments ?: return

        val catalogs = arguments.getParcelableArrayList<CatalogModel>(GlobalConstant.BOOK_BY_CAMPAIGN_KEY) ?: ArrayList()
        val marcaId = arguments.getInt(GlobalConstant.MARCA_ID)
        val esBrillante = arguments.getBoolean(GlobalConstant.REVISTA_USER_ES_TOP_BRILLA, false)

        catalogs?.let {catalogsValidate ->
            loadCatalogsConsultora(marcaId,catalogsValidate, esBrillante)
        }

        val campaingName = arguments.getString(GlobalConstant.CAMPAIGN_KEY)
        tvwCampaignName.text = campaingName

        catalogs?.let {
            campaingBrand = it[0].campaniaId
            val adapter = CatalogTopAdapter()
            adapter.setEventListener { descripcion, title -> newListener?.onDownloadPDFRequest(descripcion, title) }
            adapter.setCatalogs(it)
            rvwCatalogTop.layoutManager = LinearLayoutManager(activity)
            rvwCatalogTop.adapter = adapter

        }

        if(esBrillante){
            vwConsultoraStandar.visibility = View.GONE
            vwConsultoraTopBrilla.visibility = View.VISIBLE

            arguments.getString(GlobalConstant.URL_CATALOGO)?.let { url->
                btnTopCompartir.setOnClickListener {
                    shareUrl(url)
                }

                ivwCatalogsTop.setOnClickListener {
                    openCatalog(url)
                }

                layoutCatalogoTop.setOnClickListener {
                    openCatalog(url)
                }
            }
        }else{
            vwConsultoraStandar.visibility = View.VISIBLE
            vwConsultoraTopBrilla.visibility = View.GONE

            val adapter = CatalogAdapter()
            adapter.setCatalogs(catalogs)

            if (listener != null) adapter.setEventListener(listener)

            lltCatalogDescription.visibility = View.INVISIBLE

            remoteConfig.getString(BuildConfig.REMOTE_CONFIG_TEXT_DESCRIPTION_CATALOG).let { descriptionText ->
                wvwCatalogDescription.setBackgroundColor(0)
                wvwCatalogDescription.settings.apply {
                    cacheMode = WebSettings.LOAD_NO_CACHE
                    setAppCacheEnabled(false)
                    setGeolocationEnabled(false)
                    loadsImagesAutomatically = true
                    blockNetworkImage = true
                    setNeedInitialFocus(false)
                    defaultFontSize = 14
                }
                wvwCatalogDescription.loadDataWithBaseURL(GlobalConstant.ASSETS_WEBVIEW_URL,descriptionText,GlobalConstant.WEB_MIME_TYPE,GlobalConstant.WEB_ENCODING,null)
                wvwCatalogDescription.webViewClient =  object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        lltCatalogDescription.visibility = View.VISIBLE
                    }
                }
            }


            arguments.getString(GlobalConstant.URL_CATALOGO)?.let { url->
                btnCompartir.setOnClickListener {
                    shareUrl(url)
                }

                ivwCatalogs.setOnClickListener {
                    openCatalog(url)
                }

                layoutCatalogo.setOnClickListener {
                    openCatalog(url)
                }
            }
        }
    }


    fun loadCatalogsConsultora(marcaId: Int, catalogs: ArrayList<CatalogModel> , esBrillante : Boolean){

        if(esBrillante){
            when(marcaId){
                BrandCode.LBEL -> {
                    Glide.with(context()).load(catalogs[2].urlImagen).into(ivwCatalogoTop1)
                    Glide.with(context()).load(catalogs[1].urlImagen).into(ivwCatalogoTop2)
                    Glide.with(context()).load(catalogs[0].urlImagen).into(ivwCatalogoTop3)
                }
                else -> {
                    Glide.with(context()).load(catalogs[2].urlImagen).into(ivwCatalogoTop1)
                    Glide.with(context()).load(catalogs[0].urlImagen).into(ivwCatalogoTop2)
                    Glide.with(context()).load(catalogs[1].urlImagen).into(ivwCatalogoTop3)
                }
            }
        }else{
            when(marcaId){
                BrandCode.LBEL -> {
                    Glide.with(context()).load(catalogs[2].urlImagen).into(ivwCatalogo1)
                    Glide.with(context()).load(catalogs[1].urlImagen).into(ivwCatalogo2)
                    Glide.with(context()).load(catalogs[0].urlImagen).into(ivwCatalogo3)
                }
                else -> {
                    Glide.with(context()).load(catalogs[2].urlImagen).into(ivwCatalogo1)
                    Glide.with(context()).load(catalogs[0].urlImagen).into(ivwCatalogo2)
                    Glide.with(context()).load(catalogs[1].urlImagen).into(ivwCatalogo3)
                }
            }
        }

        scroll.post(object : Runnable{
            override fun run() {
                scroll.smoothScrollTo(0, 0)
            }
        })

        load4Catalog(esBrillante)
    }

    fun openCatalog(url: String) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, R.string.catalog_activity_not_found, Toast.LENGTH_LONG).show()
        }

    }

    fun shareUrl(url: String){

        listener?.trackEvent(GlobalConstant.EVENT_CAT_CATALOG,
            GlobalConstant.EVENT_ACTION_CATALOG_DIGITAL, campaingBrand,
            GlobalConstant.EVENT_SHARE_CATALOG)

        val arguments = arguments

        arguments?.let {

            val numCampaign = arguments.getString(GlobalConstant.NUM_CAMPAIGN_KEY, null)

            numCampaign?.let{
                val share = Intent(android.content.Intent.ACTION_SEND)
                share.type = "text/plain"
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)

                share.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.compartir_catalogo))
                remoteConfig.getString(BuildConfig.REMOTE_CONFIG_TEXT_SHARE_CATALOG).let { sharingText ->
                    val msg = String.format(sharingText, "${resources.getString(R.string.campania).toLowerCase()} $numCampaign")
                    share.putExtra(Intent.EXTRA_TEXT,  msg + "\n" + url)
                }

                startActivity(Intent.createChooser(share, resources.getString(R.string.compartir_catalogo)))
            }
        }
    }

    fun setTopBrillaListener(topListener : CatalogNewListener){
        this@CatalogNewFragment.newListener = topListener
    }

    override fun context(): Context {
        return  context?.let { it } ?: throw NullPointerException("No hay Contexto")
    }

    companion object {

        fun newInstance(listener: CatalogAdapter.CatalogEventListener): CatalogNewFragment {
            val fragment = CatalogNewFragment()
            fragment.listener = listener

            return fragment
        }
    }

    override fun onDownloadPDFRequest(descripcion: String, title : String) {
        newListener?.onDownloadPDFRequest(descripcion, title)
    }

    private fun load4Catalog(esBrillante : Boolean = false){

        arguments?.let {args ->

            if(!args.containsKey(CatalogContainerActivity.PAIS)) return
            if(!args.containsKey(GlobalConstant.CAMPAIGN_FULL_KEY)) return

            remoteConfig.getString(BuildConfig.REMOTE_CONFIG_BANNER_CATALOG).let { response ->

                val finalUrl = String.format(response, args.getString(CatalogContainerActivity.PAIS),
                    args.getString(GlobalConstant.CAMPAIGN_FULL_KEY),
                    args.getString(GlobalConstant.CAMPAIGN_FULL_KEY))

                activity?.let {context ->
                    Glide.with(context).asBitmap().load(finalUrl)
                        .apply(RequestOptions.noTransformation().priority(Priority.HIGH))
                        .listener(object : RequestListener<Bitmap> {
                            override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                resource?.let {bitmap ->
                                    if(esBrillante){
                                        ivwCatalogoTop.setImageBitmap(bitmap)
                                        layoutCatalogoTop.visibility = View.VISIBLE
                                        ivwCatalogoTop.visibility = View.VISIBLE
                                    }else{
                                        ivwCatalogo.setImageBitmap(bitmap)
                                        layoutCatalogo.visibility = View.VISIBLE
                                        ivwCatalogo.visibility = View.VISIBLE
                                    }

                                    scroll.post(object : Runnable{
                                        override fun run() {
                                            scroll.smoothScrollTo(0, 0)
                                        }
                                    })
                                }
                                return false
                            }

                            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                                layoutCatalogo.visibility = View.GONE
                                ivwCatalogo.visibility = View.GONE
                                layoutCatalogoTop.visibility = View.GONE
                                ivwCatalogoTop.visibility = View.GONE
                                return false
                            }
                        })
                        .submit()
                }
            }
        }
    }

    interface CatalogNewListener{
        fun onDownloadPDFRequest(descripcion : String, title : String)
    }
}
