package biz.belcorp.consultoras.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.model.product.ProductCUVModel
import biz.belcorp.consultoras.domain.entity.ProductCUV
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

object ImageUtils {

    //var url: String? = null

    fun getPhotoNameUrl(url: String?): String {

        var photoName = ""

        url?.let {
            if (!TextUtils.isEmpty(url)) {
                if (url.contains("amazon")) {
                    val parts = url.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val length = parts.size
                    photoName = parts[length - 1]
                }
            }
        }

        return photoName
    }

    fun verifiedImageUrl(productCUV: ProductCUV): String {
        var url = if (productCUV.fotoProducto != null) productCUV.fotoProducto
        else {
            if (productCUV.fotoProductoMedium != null) productCUV.fotoProductoMedium else productCUV.fotoProductoSmall
        }
        return url.toString()
    }

    fun verifiedImageUrl(vararg data: String?): String? {
        return data.firstOrNull { it != null }
    }

    fun verifiedImageUrl(productCUV: ProductCUVModel): String {
        var url = if (productCUV.fotoProducto != null) productCUV.fotoProducto
        else {
            if (productCUV.fotoProductoMedium != null) productCUV.fotoProductoMedium else productCUV.fotoProductoSmall
        }
        return url.toString()
    }

    fun setProductTypePhoto(context: Context, rl: RelativeLayout, url: String, myCall: (url: String) -> Unit) {
        var imageView = ImageView(context)
        val params = LinearLayout.LayoutParams(20, 20)

        rl?.addView(imageView, params)
        imageView.visibility = android.view.View.GONE

        Glide.with(context)
            .load(url)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {

                    resource?.let {
                        var bitmap = (it as BitmapDrawable).bitmap
                        var lista = ArrayList<Int>()

                        var px = bitmap.getPixel(1, 1)
                        lista.add(Color.rgb(Color.red(px), Color.green(px), Color.blue(px)))

                        val w1 = bitmap.width / 2
                        val h1 = bitmap.height / 2

                        px = bitmap.getPixel(w1, h1)
                        lista.add(Color.rgb(Color.red(px), Color.green(px), Color.blue(px)))

                        px = bitmap.getPixel(w1 + 2, h1 + 2)
                        lista.add(Color.rgb(Color.red(px), Color.green(px), Color.blue(px)))

                        px = bitmap.getPixel(w1 - 2, h1 + 2)
                        lista.add(Color.rgb(Color.red(px), Color.green(px), Color.blue(px)))

                        px = bitmap.getPixel(w1 - 2, h1 - 4)
                        lista.add(Color.rgb(Color.red(px), Color.green(px), Color.blue(px)))

                        px = bitmap.getPixel(w1 + 2, h1 - 4)
                        lista.add(Color.rgb(Color.red(px), Color.green(px), Color.blue(px)))

                        px = bitmap.getPixel(w1 - 2, h1)
                        lista.add(Color.rgb(Color.red(px), Color.green(px), Color.blue(px)))

                        px = bitmap.getPixel(w1 + 2, h1)
                        lista.add(Color.rgb(Color.red(px), Color.green(px), Color.blue(px)))

                        px = bitmap.getPixel(w1, h1 + 2)
                        lista.add(Color.rgb(Color.red(px), Color.green(px), Color.blue(px)))

                        px = bitmap.getPixel(w1, h1 - 2)
                        lista.add(Color.rgb(Color.red(px), Color.green(px), Color.blue(px)))

                        val cant = lista.distinct().toList().size
                        if (cant > 2)
                            myCall.invoke(url)
                    }
                    return false
                }
            })
            .into(imageView)

    }

}
