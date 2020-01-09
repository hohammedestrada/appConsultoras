package biz.belcorp.consultoras.domain.repository

import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.dreammeter.DreamMeter

interface DreamMeterRepository {

    suspend fun getDreamMeter(): BasicDto<DreamMeter?>
    suspend fun saveDreamMeter(programDreamId: Int?,nameDream: String, amountDream: String): BasicDto<Boolean>
    suspend fun updateDreamMeter(programDreamId: Int?, nameDream: String, amountDream: String): BasicDto<Boolean>
    suspend fun updateStatus(programDreamId: Int?, dreamStatus: Boolean) : BasicDto<Boolean>

}
