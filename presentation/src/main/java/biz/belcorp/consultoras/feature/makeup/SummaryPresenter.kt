package biz.belcorp.consultoras.feature.makeup

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.data.entity.ProductoMasivo
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.MenuUseCase
import biz.belcorp.consultoras.domain.interactor.OrderUseCase
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.consultoras.util.anotation.ResponseResultCode
import biz.belcorp.library.log.BelcorpLogger
import javax.inject.Inject

@PerActivity
class SummaryPresenter @Inject
constructor(private val userUseCase: UserUseCase,
            private val menuUseCase: MenuUseCase,
            private val orderUseCase: OrderUseCase,
            private val loginModelDataMapper: LoginModelDataMapper)
    : Presenter<SummaryView> {

    private var view: SummaryView? = null

    override fun resume() {
        // EMPTY
    }
    override fun pause() {
        // EMPTY
    }

    override fun attachView(view: SummaryView) {
        this.view = view
    }

    override fun destroy() {
        this.userUseCase.dispose()
        this.menuUseCase.dispose()
        this.orderUseCase.dispose()
        this.view = null
    }

    /** functions */

    fun getUser(){ userUseCase[GetUser()] }

    fun trackBackPressed() { userUseCase[UserBackPressedObserver()] }

    fun getMenuActive(code1: String, code2: String) {
        menuUseCase.getActive(code1, code2, GetMenuObserver())
    }

    fun insercionMasivaPedido(productos: List<ProductoMasivo>, identifier: String){
        orderUseCase.insercionMasivaPedido(productos, identifier, PedidoMasivoObserver())
    }

    /** Observers */

    private inner class UserBackPressedObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            view?.trackBackPressed(loginModelDataMapper.transform(t))
        }
    }

    private inner class GetUser : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            view?.setUser(t)
        }
    }

    private inner class GetMenuObserver : BaseObserver<Menu?>() {
        override fun onNext(t: Menu?) {
            t?.let { view?.onGetMenu(it) }
        }
    }

    private inner class PedidoMasivoObserver : BaseObserver<BasicDto<List<ProductoMasivo>>?>() {
        override fun onNext(t: BasicDto<List<ProductoMasivo>>?) {
            val productosOK = mutableListOf<ProductoMasivo>()
            val productosError = mutableListOf<ProductoMasivo>()
            t?.data?.forEach {
                if(it.codigoRespuesta  == ResponseResultCode.OK){
                    productosOK.add(it)
                } else {
                    productosError.add(it)
                }
            }
            view?.postInit(productosOK.toList(), productosError.toList())
        }

        override fun onError(exception: Throwable) {
            BelcorpLogger.d(exception)
            view?.onErrorMessage(exception)
        }

    }

}
