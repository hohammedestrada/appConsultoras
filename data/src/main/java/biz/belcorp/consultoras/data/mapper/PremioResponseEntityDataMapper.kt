package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.MensajeProlEntity
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.MensajeProl
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.net.dto.ServiceDto
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton

class PremioResponseEntityDataMapper @Inject

internal constructor(private val observacionPedidoEntityDataMapper: ObservacionPedidoEntityDataMapper
                     , private val orderEntityDataMapper: OrderEntityDataMapper) {



    fun transformUpResponse(input: ServiceDto<List<MensajeEntity?>?>): BasicDto<Collection<MensajeProl?>?>? {
        return input?.let {
            BasicDto<Collection<MensajeProl?>?>().apply {
                code = it.code
                message = it.message
                val json = Gson().toJson(it.data)
                val  type = object : TypeToken<List<MensajeEntity?>?>() {}.type
                try {
                    val predata: List<MensajeEntity?>? = Gson().fromJson(json, type)
                    data = transformBy(predata)
                } catch (e: Exception) {
                    BelcorpLogger.d(Constant.ERROR_TRANSFORM)
                }

            }
        }
    }

    fun transformBy(input: List<MensajeEntity?>?): Collection<MensajeProl?>? {
        return input?.asSequence()?.map { it1 -> transform(it1) }?.filter { it1 -> null != it1 }?.toList()
            ?: run {
                emptyList<MensajeProl>()
            }
    }

    fun transform(data: MensajeEntity?): MensajeProl? {
        return MensajeProl().apply {
            this.code = data?.code
            this.message = data?.message
        }
    }

}

class MensajeEntity {
    @SerializedName("Success")
    var code: String ? = null
    @SerializedName("Mensaje")
    var message: String ? = null
}

