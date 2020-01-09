package biz.belcorp.consultoras.common.model.orders

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal


@Parcelize
data class OfertaFinalModel(var cuv: String?,
                            var nombreComercial: String?,
                            var nombreComercialCorto: String?,
                            var precioCatalogo: BigDecimal?,
                            var precioValorizado: BigDecimal?,
                            var marcaID: Int?,
                            var nombreMarca: String?,
                            var fotoProducto: String?,
                            var fotoProductoSmall: String?,
                            var fotoProductoMedium: String?,
                            var tipoMeta: String?,
                            var indicadorMontoMinimo: Int?,
                            var tipoEstrategiaID: Int?,
                            var tipoOfertaSisID: Int?,
                            var configuracionOfertaID: Int?,
                            var cantidad: Int? = 0,
                            var added: Boolean = false,
                            var index : Int = 0) : Parcelable


