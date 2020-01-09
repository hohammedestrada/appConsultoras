package biz.belcorp.consultoras.feature.home.storie

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.stories.StorieModel
import biz.belcorp.mobile.components.core.helpers.ImagesHelper
import biz.belcorp.consultoras.feature.home.storie.di.StorieCompound
import biz.belcorp.consultoras.util.GlobalConstant
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.fragment_storie_contenido.*

class StorieFragmentContent : BaseFragment() {
    var listener: IStorieFragment? = null
    private var detailModel: StorieModel.ContenidoDetalleModel? = null

    override fun context(): Context? = activity?.applicationContext
    @Throws(IllegalStateException::class)

    override fun onInjectView(): Boolean {
        getComponent(StorieCompound::class.java).inject(this)
        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_storie_contenido, container, false)

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is IStorieFragment) {
            this.listener = context
        }
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        init()
    }

    fun init() {
        arguments?.let {
            detailModel = it.getParcelable(GlobalConstant.STORIE_UNIQUE)
        }
        if (detailModel != null) {
            val url = detailModel?.urlDetalleResumen?.let {
                it
            } ?: kotlin.run { "" }

            var imageHelper: ImagesHelper? =null
            context?.let {
                imageHelper = ImagesHelper(it)
            }

            Glide.with(this)
                .asBitmap()
                .load(imageHelper?.getResolutionURL(url))
                .apply(
                    RequestOptions().onlyRetrieveFromCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                )
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onLoadStarted(placeholder: Drawable?) {
                        viewLoadStorie.visibility = View.VISIBLE
                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>) {
                        errorTvwStorie.visibility = View.GONE
                        errorTvwStorieDetail.visibility = View.GONE

                        imageStorie.visibility = View.VISIBLE
                        imageStorie.setImageBitmap(resource)
                        viewLoadStorie.visibility = View.GONE
                        listener?.let { _ ->
                            detailModel.let { detail ->
                                detail?.accion?.let { action ->
                                    btnAccionStorie.visibility = gestionVisibilidad(action)
                                }
                            }
                        }
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        errorTvwStorie.visibility = View.VISIBLE
                        errorTvwStorieDetail.visibility = View.VISIBLE
                        imageStorie.visibility = View.GONE
                        viewLoadStorie.visibility = View.GONE
                        btnAccionStorie.visibility = View.GONE
                    }
                })

            btnAccionStorie.setOnClickListener {
                detailModel?.let {
                    it.codigoDetalleResumen?.let { codigo ->
                        listener?.let { list ->
                            list.redirect(codigo)
                        }
                    }

                }
            }
        }
    }

    private fun gestionVisibilidad(accion: String?): Int {
        return accion?.let {
            if (accion.isNotBlank()) {
                textBtnStorie.text = accion.replace("_"," ")
                View.VISIBLE
            } else
                View.GONE
        } ?: kotlin.run { View.GONE }
    }


    interface IStorieFragment {
        fun redirect(donde: String)
    }
}
