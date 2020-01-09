package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.ParamsEntity
import biz.belcorp.consultoras.data.entity.UserCatalogoRequestEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParamsEntityDataMapper @Inject
internal constructor() {

    fun transform(input: UserCatalogoRequestEntity?): ParamsEntity? {

        return input?.let {
            ParamsEntity().apply {
                campaniaActual = it.campania
                codigoZona = it.codigoZona
                topAnterior = it.topLast
                topSiguiente = it.topNext
                nroCampanias = it.campaignNumber
                isMostrarCampaniaActual = it.isShowCurrent
                isBrillante = it.isBrillante
            }
        }

    }

    fun transform(input: ParamsEntity?): UserCatalogoRequestEntity? {

        return input?.let {
            UserCatalogoRequestEntity().apply {
                campania = it.campaniaActual
                codigoZona = it.codigoZona
                topLast = it.topAnterior ?: 0
                topNext = it.topSiguiente ?: 0
                campaignNumber = it.nroCampanias
                isShowCurrent = it.isMostrarCampaniaActual ?: false
                isBrillante = it.isBrillante ?: false
            }
        }

    }

}
