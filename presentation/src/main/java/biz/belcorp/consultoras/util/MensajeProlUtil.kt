package biz.belcorp.consultoras.util

import android.content.Context
import biz.belcorp.consultoras.common.dialog.AgregarProductoReservaDialog
import biz.belcorp.consultoras.common.dialog.DialogRegaloProducto
import biz.belcorp.consultoras.common.model.mensajeprol.MensajeProlModel
import biz.belcorp.consultoras.domain.entity.ProductCUV
import biz.belcorp.library.util.StringUtil

class MensajeProlUtil {
    companion object {
        fun showMensajeProl(context: Context?, mensajes: Collection<MensajeProlModel?>?) {
            var msg: String = StringUtil.Empty
            mensajes?.forEach {
                msg += it?.message
            }
            context?.let {
                DialogRegaloProducto(it, msg).show()
            }
        }


        fun showAgregarProductoProl(context: Context?, imagen:String ,mensajes: String, producto : ProductCUV, listener: AgregarProductoReservaDialog.OnTombolaDialogListener){

            context?.run {
                val dialog = AgregarProductoReservaDialog(this)
                dialog.setMensaje(mensajes)
                dialog.setImageUrl(imagen)
                dialog.setProducto(producto)
                dialog.onTombolaDialogListener = listener
                dialog.show()
            }
        }
    }
}
