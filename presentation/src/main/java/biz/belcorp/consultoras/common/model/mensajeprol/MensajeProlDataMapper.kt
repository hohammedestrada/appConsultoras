package biz.belcorp.consultoras.common.model.mensajeprol

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.MensajeProl
import java.util.ArrayList
import javax.inject.Inject

@PerActivity
class MensajeProlDataMapper @Inject constructor() {

    fun transform(input: MensajeProl?): MensajeProlModel? {
        var mensajeProlModel: MensajeProlModel? = null

        if (null != input) {
            mensajeProlModel = MensajeProlModel()
            mensajeProlModel.code = input.code
            mensajeProlModel.message = input.message
            mensajeProlModel.image = input.imageUrl
        }
        return mensajeProlModel
    }

    fun transform(input: MensajeProlModel?): MensajeProl? {
        var mensajeProl: MensajeProl? = null

        if (null != input) {
            mensajeProl = MensajeProl()
            mensajeProl.code = input.code
            mensajeProl.message = input.message
        }
        return mensajeProl
    }

    fun transform(list: Collection<MensajeProlModel>?): List<MensajeProl> {
        val mensajes = ArrayList<MensajeProl>()

        if (null == list) {
            return emptyList()
        }

        for (model in list) {
            val mensaje = transform(model)
            if (null != mensaje) {
                mensajes.add(mensaje)
            }
        }

        return mensajes
    }

    fun transformToDomainModel(list: Collection<MensajeProl?>?): List<MensajeProlModel> {
        val mensajes = ArrayList<MensajeProlModel>()

        if (null == list) {
            return emptyList()
        }

        for (model in list) {
            val mensaje = transform(model)
            if (null != mensaje) {
                mensajes.add(mensaje)
            }
        }

        return mensajes
    }
}
