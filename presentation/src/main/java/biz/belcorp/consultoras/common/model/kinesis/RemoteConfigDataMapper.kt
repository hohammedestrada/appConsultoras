package biz.belcorp.consultoras.common.model.kinesis

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.util.KinesisCode
import com.google.gson.JsonParser
import javax.inject.Inject



@PerActivity
class RemoteConfigDataMapper @Inject constructor() {

    fun transform(data: String?): KinesisModel? {

        data?.let {

            try {

                val kinesisModel = JsonParser().parse(it).asJsonObject
                val streamsJson = kinesisModel.get(KinesisCode.STREAMS).asJsonObject
                val streamsMap = mutableMapOf<String,String>()

                streamsJson.keySet().forEach {
                    streamsMap[it] = streamsJson.get(it).asString
                }

                return KinesisModel().apply { id = kinesisModel.get(KinesisCode.ID).asString
                    streams = streamsMap
                    type = kinesisModel.get(KinesisCode.TYPE).asInt
                    region = kinesisModel.get(KinesisCode.REGION).asInt }

            }catch (e: Exception){
                e.printStackTrace()
            }

        }.run {
            return null
        }

    }

    fun List<KinesisModel>.getType(type: Int): KinesisModel?{
        this.forEach {
            if(it.type == type) return it
        }
        return null
    }
}
