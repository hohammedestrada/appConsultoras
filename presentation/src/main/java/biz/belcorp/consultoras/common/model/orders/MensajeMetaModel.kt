package biz.belcorp.consultoras.common.model.orders

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class MensajeMetaModel(var tipoMensaje: String?,
                            var titulo: String?,
                            var mensaje: String?) : Parcelable
