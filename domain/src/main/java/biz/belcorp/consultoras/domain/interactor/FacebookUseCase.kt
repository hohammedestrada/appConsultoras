package biz.belcorp.consultoras.domain.interactor

import javax.inject.Inject

import biz.belcorp.consultoras.domain.entity.FacebookProfile
import biz.belcorp.consultoras.domain.executor.PostExecutionThread
import biz.belcorp.consultoras.domain.executor.ThreadExecutor
import biz.belcorp.consultoras.domain.repository.FacebookRepository

class FacebookUseCase @Inject
constructor(private val repository: FacebookRepository, threadExecutor: ThreadExecutor,
            postExecutionThread: PostExecutionThread) : UseCase(threadExecutor, postExecutionThread) {

    operator fun get(observer: BaseObserver<FacebookProfile?>) {
        execute(this.repository.get(), observer)
    }

    fun save(facebookProfile: FacebookProfile, observer: BaseObserver<Boolean?>) {
        execute(this.repository.save(facebookProfile), observer)
    }

}
