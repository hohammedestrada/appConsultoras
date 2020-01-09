package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.MensajeProlEntity
import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.MensajeProl
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.net.dto.ServiceDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BasicDtoDataMapper @Inject
internal constructor()// EMPTY
{

    fun transformBoolean(input: ServiceDto<Boolean>?): BasicDto<Boolean>? {
        return input?.let {
            BasicDto<Boolean>().apply {
                code = it.code
                data = it.data
                message = it.message
            }
        }
    }

    fun transformString(input: ServiceDto<String>?): BasicDto<String>? {
        return input?.let {
            BasicDto<String>().apply {
                code = it.code
                data = it.data
                message = it.message
            }
        }
    }

    fun transformAny(input: ServiceDto<Any?>?): BasicDto<Any?>? {
        return input?.let {
            BasicDto<Any?>().apply {
                code = it.code
                data = it.data
                message = it.message
            }
        }
    }

    fun transformStringResponse(input: ServiceDto<List<MensajeProlEntity?>?>): BasicDto<Collection<MensajeProl?>?>? {
        return input?.let {
            BasicDto<Collection<MensajeProl?>?>().apply {
                code = it.code
                message = it.message
                val json = Gson().toJson(it.data)
                val  type = object : TypeToken<List<MensajeProlEntity?>?>() {}.type
                try {
                    val predata: List<MensajeProlEntity> = Gson().fromJson(json, type)
                    data = transformBy(predata)
                } catch (e: Exception) {
                    BelcorpLogger.d(Constant.ERROR_TRANSFORM)
                }

            }
        }

    }
    private fun transformBy(input: List<MensajeProlEntity?>?): Collection<MensajeProl?>? {
        return input?.asSequence()?.map { it1 -> transform(it1) }?.filter { it1 -> null != it1 }?.toList()
            ?: run {
                emptyList<MensajeProl>()
            }
    }

    fun transform(data: MensajeProlEntity?): MensajeProl? {
        return MensajeProl().apply {
            this.code = data?.code
            this.message = data?.message
        }
    }
}
