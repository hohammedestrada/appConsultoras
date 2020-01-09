package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.FestivalConfiguracion

import biz.belcorp.consultoras.domain.entity.FestivalSello
import kotlinx.coroutines.Deferred

interface FestivalRepository{
     suspend fun getConfiguracion(grantype: Int): Deferred<FestivalConfiguracion?>

     suspend fun getLocalConfiguracion(): FestivalConfiguracion?

    suspend fun getSelloConfiguracion(): FestivalSello?
}
