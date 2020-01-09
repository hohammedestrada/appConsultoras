package biz.belcorp.consultoras.common.model.orders

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PedidoConfigModel(var escalaDescuento: List<EscalaDescuentoModel?>?,
                             var mensajeMeta: List<MensajeMetaModel?>?) : Parcelable
