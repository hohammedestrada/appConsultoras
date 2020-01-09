package biz.belcorp.consultoras.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.domain.entity.ProductCUV
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.dialog_agregar_producto_reserva.*

class AgregarProductoReservaDialog(context : Context) : Dialog(context) {

    var  onTombolaDialogListener : OnTombolaDialogListener? = null

    private var productoCUV : ProductCUV? = null
    private var mensaje: String? = null
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_agregar_producto_reserva)

        window?.let{
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popUpMsgClose.setOnClickListener {
                onTombolaDialogListener?.onCerrarDialog()
                dismiss()
            }

            popUpMsgBody.text = mensaje

            Glide.with(context).asDrawable().load(imageUrl).apply(RequestOptions.noTransformation().priority(Priority.HIGH)).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    popUpImage.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    popUpImage.visibility = View.VISIBLE
                    return false
                }

            }).into(popUpImage)

            popUpMsgBtnAgregar.setOnClickListener {
                productoCUV?.run {
                    onTombolaDialogListener?.agregarProducto(this)
                }
                
                dismiss()
            }
        }
    }

    fun setMensaje(mensaje: String?){
        this.mensaje = mensaje
    }

    fun setImageUrl(imageUrl: String?){

        this.imageUrl = imageUrl
    }

    fun setProducto(productCUV: ProductCUV){
        this.productoCUV = productCUV
    }

    interface OnTombolaDialogListener{

        fun agregarProducto(productCUV: ProductCUV)

        fun onCerrarDialog()

    }



}




