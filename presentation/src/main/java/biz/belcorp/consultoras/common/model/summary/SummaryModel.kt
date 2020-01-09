package biz.belcorp.consultoras.common.model.summary

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal


@Parcelize
data class SummaryModel(var cuv: String?,
                        var description: String?,
                        var precioCatalogo: BigDecimal?,
                        var tipoEstrategiaID: Int?,
                        var flagNueva: Boolean?,
                        var tipoOfertaSisID: Int?,
                        var configuracionOfertaID: Int?,
                        var indicadorMontoMinimo: Int?,
                        var marcaID: Int?,
                        var estrategiaID: Int?,
                        var codigoEstrategia: String?,
                        var clienteID: Int?,
                        var urlImagen: String?,
                        var flagAgregado: Boolean?,
                        var codigo: String?,
                        var mesasage: String?) : Parcelable
