package biz.belcorp.consultoras.domain.interactor

import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.dreammeter.DreamMeter
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.DreamMeterRepository
import javax.inject.Inject

class DreamMeterUseCase @Inject
constructor(private val dreamMeterRepository: DreamMeterRepository,
            threadExecutor: ThreadExecutor,
            postExecutionThread: PostExecutionThread) : UseCase(threadExecutor, postExecutionThread) {

    suspend fun getDreamMeter(): BasicDto<DreamMeter?> {
        return this.dreamMeterRepository.getDreamMeter()
    }

    suspend fun saveDreamMeter(programDreamId: Int?, nameDream: String, amountDream: String): BasicDto<Boolean> {
        return this.dreamMeterRepository.saveDreamMeter(programDreamId, nameDream, amountDream)
    }

    suspend fun updateDreamMeter(programDreamId: Int?, nameDream: String, amountDream: String): BasicDto<Boolean> {
        return this.dreamMeterRepository.updateDreamMeter(programDreamId, nameDream, amountDream)
    }

    suspend fun updateStatus(programId: Int?, dreamStatus: Boolean) : BasicDto<Boolean> {
        return this.dreamMeterRepository.updateStatus(programId, dreamStatus)
    }

}
