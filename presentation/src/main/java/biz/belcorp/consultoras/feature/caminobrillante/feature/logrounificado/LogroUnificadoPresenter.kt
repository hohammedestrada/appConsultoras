package biz.belcorp.consultoras.feature.caminobrillante.feature.logrounificado

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.domain.interactor.CaminoBrillanteUseCase
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.util.StringUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LogroUnificadoPresenter @Inject
internal constructor(private val caminobrillanteUseCase: CaminoBrillanteUseCase, private val userUseCase: UserUseCase) : Presenter<LogroUnificadoView> {

    private var view: LogroUnificadoView? = null

    override fun attachView(view: LogroUnificadoView) {
        this.view = view
    }

    fun init() {
        view?.let {
            it.setCanBack(false)
            GlobalScope.launch {
                getUserPhoto(it)
                getResumenLogros(it)
                getLogros(it)
                it.setCanBack(true)
            }
        }
    }

    private suspend fun getUserPhoto(view: LogroUnificadoView) {
        try {
            withContext(Dispatchers.IO) {
                userUseCase.getUser()?.let {
                    withContext(Dispatchers.Main) {
                        view.setPhotoUser(it.photoProfile ?: StringUtil.Empty)
                    }
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                e.message?.let {
                    BelcorpLogger.d(it)
                }
            }
        }
    }

    private suspend fun getResumenLogros(view: LogroUnificadoView) {
        try {
            caminobrillanteUseCase.getResumenLogros()?.let { logro ->
                withContext(Dispatchers.Main) {
                    val indicador = logro.indicadores?.get(0)
                    indicador?.let {
                        view.setResumenLogro(
                            it.titulo ?: StringUtil.Empty, it.descripcion ?: StringUtil.Empty,
                            it.medallas?.get(0)?.titulo ?: StringUtil.Empty, it.medallas?.get(0)?.valor
                            ?: "",
                            it.medallas?.get(1)?.titulo ?: StringUtil.Empty, it.medallas?.get(1)?.valor
                            ?: "",
                            it.medallas?.get(2)?.titulo ?: StringUtil.Empty, it.medallas?.get(2)?.valor
                            ?: ""
                        )
                    }
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                e.message?.let {
                    BelcorpLogger.d(it)
                }
            }
        }
    }

    private suspend fun getLogros(view: LogroUnificadoView) {
        try {
            caminobrillanteUseCase.getLogros()?.let {
                withContext(Dispatchers.Main) {
                    view.setDataIndicadores(it)
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                e.message?.let {
                    BelcorpLogger.d(it)
                }
            }
        }
    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun destroy() {
        this.view = null
        this.caminobrillanteUseCase.dispose()
        this.userUseCase.dispose()
    }

}
