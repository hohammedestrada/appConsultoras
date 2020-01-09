package biz.belcorp.consultoras.domain.executor

import io.reactivex.Scheduler

/**
 * Interfaceque implementa la ejecucion de un proceso en un hilo diferente al principal
 *
 * @version 1.0
 * @since 2017-04-14
 */
interface PostExecutionThread {
    val scheduler: Scheduler
}
