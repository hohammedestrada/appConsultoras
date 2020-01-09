package biz.belcorp.consultoras.common.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import biz.belcorp.consultoras.R
import android.view.ViewGroup
import android.view.Gravity
import android.view.View
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.domain.entity.CatalogoWrapper
import biz.belcorp.consultoras.util.anotation.BrandType
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.dialog_siguiente_campania.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class ShareNextCampaing(private var contexto: Context,
                        private var url: String?,
                        private var campaing: String?,
                        private var catalogos: List<CatalogoWrapper>,
                        private var listener: ShareNextCampaingListener?) : Dialog(contexto, R.style.full_screen_dialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_siguiente_campania)
        val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setGravity(Gravity.CENTER)
        init(remoteConfig)
        closeDialog.setOnClickListener {
            listener?.onCloseShareDialog()
            dismiss()
        }

        btnCompartir.setOnClickListener {
            shareUrl(remoteConfig)
        }

    }

    fun init(remoteConfig: FirebaseRemoteConfig) {
        tvwCabecera.text = String.format(context.resources.getString(R.string.comenzo_campania_catalogo), campaing)
        remoteConfig.getString(BuildConfig.REMOTE_CONFIG_DIALOG_SHARE_CATALOG).let { bodyText ->
            tvwMensaje.text = bodyText
        }
        gestionarTapaCatalogo(catalogLeft, catalogos[1], BrandType.CYZONE)
        gestionarTapaCatalogo(catalogCenter, catalogos[1], BrandType.ESIKA)
        gestionarTapaCatalogo(catalogRight, catalogos[1], BrandType.LBEL_ORIGEN)
    }

    fun gestionarTapaCatalogo(imgView: AppCompatImageView?, catalogoWrapper: CatalogoWrapper, marca: String) {

        GlobalScope.launch(Dispatchers.Main) {
            catalogoWrapper.catalogoEntities?.find { it?.marcaDescripcion?.toLowerCase() == marca.toLowerCase() }?.let { catalogo ->
                contexto.let {
                    Glide.with(it).load(catalogo.urlImagen).listener(
                        object : RequestListener<Drawable> {
                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                imgView?.setImageDrawable(resource)
                                gestionarLoading(marca)
                                return true
                            }

                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                val resource = when (marca) {
                                    BrandType.ESIKA -> R.drawable.catalogo_no_disponible_esika
                                    BrandType.LBEL_ORIGEN -> R.drawable.catalogo_no_disponible_lbel
                                    else -> R.drawable.catalogo_no_disponible_cyzone
                                }

                                imgView?.setImageDrawable(ContextCompat.getDrawable(it, resource))
                                gestionarLoading(marca)
                                return true
                            }
                        }
                    ).into(imgView)

                }

            }

        }

    }

    private fun gestionarLoading(marca: String) {
        when (marca) {
            BrandType.ESIKA -> loadingCenter.visibility = View.GONE
            BrandType.LBEL_ORIGEN -> loadingRight.visibility = View.GONE
            else -> loadingLeft.visibility = View.GONE
        }
    }

    fun shareUrl(remoteConfig: FirebaseRemoteConfig) {

        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        share.putExtra(Intent.EXTRA_SUBJECT, context.resources.getString(R.string.compartir_catalogo))
        remoteConfig.getString(BuildConfig.REMOTE_CONFIG_TEXT_SHARE_CATALOG).let { sharingText ->
            val msg = String.format(sharingText, "${context.resources.getString(R.string.campania).toLowerCase()} $campaing")
            share.putExtra(Intent.EXTRA_TEXT, msg + "\n" + url)
        }
        context.startActivity(Intent.createChooser(share, context.resources.getString(R.string.compartir_catalogo)))

    }

    class Builder(val context: Context) {
        private var campaing: String? = null
        private var url: String? = null
        private var catalogos = listOf<CatalogoWrapper>()
        private var listener: ShareNextCampaingListener? = null

        fun withUrlToShare(url: String) = apply { this.url = url }

        fun withCampaing(campaing: String?) = apply { this.campaing = campaing }


        fun withCatalogos(lista: List<CatalogoWrapper>) = apply { this.catalogos = lista }


        fun withListener(listener: ShareNextCampaingListener) = apply { this.listener = listener }

        fun show() = ShareNextCampaing(context, url, campaing, catalogos, listener).show()

    }

    interface ShareNextCampaingListener {
        fun onCloseShareDialog()
    }
}
