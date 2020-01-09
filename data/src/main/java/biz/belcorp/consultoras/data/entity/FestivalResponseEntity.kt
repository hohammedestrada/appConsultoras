package biz.belcorp.consultoras.data.entity

import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.library.net.dto.ServiceDto
import com.google.gson.annotations.SerializedName

data class FestivalResponseEntity(
    @SerializedName("ListaPremios") var listAwards: List<FestivalAwardEntity?>?,
    @SerializedName("ListaCondiciones") var listConditions: List<FestivalProductEntity?>?,
    @SerializedName("Filtros") var filters: List<FestivalFilterEntity?>?
) {
    companion object {
        fun transform(input: FestivalResponseEntity?): FestivalResponse? {
            return input?.let {
                FestivalResponse(
                    FestivalAwardEntity.transformList(it.listAwards),
                    FestivalProductEntity.transformList(it.listConditions),
                    FestivalFilterEntity.transformList(it.filters)
                )
            }
        }

        fun transformFestival(service: ServiceDto<FestivalResponseEntity?>?): BasicDto<FestivalResponse?> {
            return BasicDto<FestivalResponse?>().apply {
                code = service?.code
                data = transform(service?.data)
                message = service?.message
            }
        }
    }
}

data class FestivalAwardEntity(
    @SerializedName("MontoTotalNivel") var totalAmountLevel: Double?,
    @SerializedName("MontoRestante") var remainingAmount: Double?,
    @SerializedName("ProgresoNivel") var progressLevel: Int?,
    @SerializedName("FlagPremioAgregado") var flagPremioAgregado: Boolean?,
    @SerializedName("Premio") var product: FestivalProductEntity?
) {
    companion object {
        fun transformList(list: List<FestivalAwardEntity?>?): List<FestivalAward>? {
            return mutableListOf<FestivalAward>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: FestivalAwardEntity?): FestivalAward? {
            return input?.let {
                FestivalAward(
                    it.totalAmountLevel,
                    it.remainingAmount,
                    it.progressLevel,
                    it.flagPremioAgregado,
                    FestivalProductEntity.transform(it.product)
                )
            }
        }
    }
}

data class FestivalProductEntity(
    @SerializedName("CUV") var cuv: String?,
    @SerializedName("Descripcion") var description: String?,
    @SerializedName("MarcaID") var brandId: Int?,
    @SerializedName("NombreMarca") var brandName: String?,
    @SerializedName("PrecioCatalogo") var catalogPrice: Double?,
    @SerializedName("PrecioValorizado") var valuedPrice: Double?,
    @SerializedName("Ganancia") var profit: Double?,
    @SerializedName("ImagenURL") var imageUrl: String?,
    @SerializedName("TipoEstrategiaID") var strategyTypeId: String?,
    @SerializedName("CodigoEstrategia") var strategyCode: String?,
    @SerializedName("EstrategiaID") var strategyId: Int?,
    @SerializedName("IndicadorMontoMinimo") var minAmountIndicator: Int?,
    @SerializedName("LimiteVenta") var limitSale: Int?,
    @SerializedName("TipoEstrategiaImagenMostrar") var strategyTypeImageShow: Int?,
    @SerializedName("FlagEligeOpcion") var flagChoiceOption: Boolean?,
    @SerializedName("FlagNueva") var flagNew: Int?,
    @SerializedName("EsSubCampania") var isSubCampaign: Boolean?,
    @SerializedName("FlagIndividual") var flagIndividual: Boolean?,
    @SerializedName("CodigoProducto") var productCode: String?,
    @SerializedName("Agotado") var isSoldOut: Boolean?,
    @SerializedName("TipoOferta") var offerType: String?
) {
    companion object {
        fun transformList(list: List<FestivalProductEntity?>?): List<FestivalProduct>? {
            return mutableListOf<FestivalProduct>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }.toList()
        }

        fun transform(input: FestivalProductEntity?): FestivalProduct? {
            return input?.let {
                FestivalProduct(it.cuv,
                    it.description,
                    it.brandId,
                    it.brandName,
                    it.catalogPrice,
                    it.valuedPrice,
                    it.profit,
                    it.imageUrl,
                    it.strategyTypeId,
                    it.strategyCode,
                    it.strategyId,
                    it.minAmountIndicator,
                    it.limitSale,
                    it.strategyTypeImageShow,
                    it.flagChoiceOption,
                    (it.flagNew == 1),
                    it.isSubCampaign,
                    it.flagIndividual,
                    it.productCode,
                    it.isSoldOut,
                    it.offerType)
            }
        }
    }
}

data class FestivalFilterEntity(
    @SerializedName("NombreGrupo") var groupName: String? = null,
    @SerializedName("Opciones") var options: List<FestivalFilterChildEntity?>? = null) {

    companion object {
        fun transform(input: FestivalFilterEntity?): FestivalFilter? {
            return input?.let {
                FestivalFilter(it.groupName,
                    FestivalFilterChildEntity.transformList(it.options))
            }
        }

        fun transformList(list: List<FestivalFilterEntity?>?): List<FestivalFilter> {
            return mutableListOf<FestivalFilter>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }
        }
    }
}

data class FestivalFilterChildEntity(
    @SerializedName("IdFiltro") var filterId: String? = null,
    @SerializedName("NombreFiltro") var filterName: String? = null,
    @SerializedName("Cantidad") var quantity: Int? = null,
    @SerializedName("Marcado") var checked: Boolean? = false) {

    companion object {

        fun transformList(list: List<FestivalFilterChildEntity?>?): List<FestivalFilterChild> {
            return mutableListOf<FestivalFilterChild>().apply {
                list?.forEach {
                    it?.let { it1 -> transform(it1) }?.let { it2 -> add(it2) }
                }
            }
        }

        fun transform(input: FestivalFilterChildEntity?): FestivalFilterChild? {
            return input?.let {
                FestivalFilterChild(it.filterId,
                    it.filterName,
                    it.quantity,
                    it.checked)
            }
        }
    }
}
