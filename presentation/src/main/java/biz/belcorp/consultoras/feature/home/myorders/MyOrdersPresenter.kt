package biz.belcorp.consultoras.feature.home.myorders

import org.jetbrains.annotations.NotNull

import javax.inject.Inject

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.common.model.menu.MenuModelDataMapper
import biz.belcorp.consultoras.common.model.order.MyOrderModelDataMapper
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.Menu
import biz.belcorp.consultoras.domain.entity.MyOrder
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.exception.VersionException
import biz.belcorp.consultoras.domain.interactor.AccountUseCase
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.MenuUseCase
import biz.belcorp.consultoras.domain.interactor.OrderUseCase
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.consultoras.util.anotation.ResponseResultCode
import biz.belcorp.consultoras.util.anotation.UserConfigAccountCode
import biz.belcorp.library.log.BelcorpLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception


@PerActivity
class MyOrdersPresenter @Inject
internal constructor(
    private val orderUseCase: OrderUseCase,
    private val userUseCase: UserUseCase,
    private val menuUseCase: MenuUseCase,
    private val accountUseCase: AccountUseCase,
    private val menuModelDataMapper: MenuModelDataMapper,
    private val loginModelDataMapper: LoginModelDataMapper,
    private val myOrderModelDataMapper: MyOrderModelDataMapper
) : Presenter<MyOrdersView> {

    private var myOrderView: MyOrdersView? = null

    companion object {
        private const val TAG = "MyOrdersPresenter"
    }

    override fun attachView(view: MyOrdersView) {
        myOrderView = view
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        this.orderUseCase.dispose()
        this.userUseCase.dispose()
        this.menuUseCase.dispose()
        this.accountUseCase.dispose()
        this.myOrderView = null
    }

    /**********************************************************/

    fun initScreenTrack() {
        userUseCase[UserPropertyObserver()]
    }

    fun trackBackPressed() {
        userUseCase[UserBackPressedObserver()]
    }

    fun data() {
        myOrderView?.showLoading()
        accountUseCase.getConfigActive(UserConfigAccountCode.PDF_PAQDOC, GetConfigPDF())
    }

    fun getMenuActive(code1: String, code2: String) {
        menuUseCase.getActive(code1, code2, GetMenuObserver())
    }

    fun paqueteDocumentario(numeroPedido: String?) {
        myOrderView?.showLoading()
        orderUseCase.paqueteDocumentario(numeroPedido, PaqueteDocumentarioObserver())
    }

    fun evaluatePendingOrders(){
        myOrderView?.showLoading()
        myOrderView?.showPedidosPendientesButton(0, false)
        userUseCase[GetUserpendingOrders()]
    }

    /**********************************************************/

    private inner class GetConfigPDF : BaseObserver<Boolean?>() {

        override fun onNext(result: Boolean?) {
            result?.let {
                if (it) {
                    myOrderView?.activePDF()
                }
                userUseCase[GetUser(it)]
            }

        }
    }

    private inner class GetUser(var pdfActive: Boolean) : BaseObserver<User?>() {
        override fun onNext(user: User?) {
            user?.let {
                orderUseCase.get(it.campaing, !pdfActive, GetOrders())
            }
        }

        override fun onError(exception: Throwable) {
            BelcorpLogger.w(TAG, "ERROR!", exception)
        }
    }

    private inner class GetUserpendingOrders : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            super.onNext(t)
            t?.let {
                GlobalScope.launch {
                    try {
                        val pedidosPendientes = orderUseCase.getPedidosPendientes(it.campaing)
                        val isEnabled = pedidosPendientes?.pedidoPendiente ?: 0 > 0
                        GlobalScope.launch(Dispatchers.Main) {
                            myOrderView?.let { it2 ->
                                it2.showPedidosPendientesButton(pedidosPendientes?.pedidoPendiente ?: 0, isEnabled)
                            }
                        }

                    } catch (ex: Exception) {
                        BelcorpLogger.w(TAG, "ERROR!", ex)
                    }
                }
            }
        }
    }

    private inner class GetOrders : BaseObserver<Collection<MyOrder?>?>() {

        override fun onNext(myOrders: Collection<MyOrder?>?) {
            myOrderView?.let {
                it.showOrders(myOrderModelDataMapper.transform(myOrders))
                it.hideLoading()
            }
        }

        override fun onError(exception: Throwable) {
            myOrderView?.let {
                it.hideLoading()
                if (exception is VersionException) {
                    val vE = exception
                    it.onVersionError(vE.isRequiredUpdate, vE.url)
                } else {
                    it.onError(exception)
                }
            }
        }
    }

    inner class UserPropertyObserver : BaseObserver<User?>() {

        override fun onNext(user: User?) {
            myOrderView?.initScreenTrack(loginModelDataMapper.transform(user))
        }
    }

    private inner class PaqueteDocumentarioObserver : BaseObserver<BasicDto<String>?>() {
        override fun onNext(body: BasicDto<String>?) {
            body?.let {
                if (it.code.equals(ResponseResultCode.OK)) {
                    it.data?.let { it1 -> myOrderView?.openPDF(it1) }
                } else {
                    myOrderView?.showPDFError()
                }
            }
        }

        override fun onError(@NotNull exception: Throwable) {
            myOrderView?.let {
                it.hideLoading()
                it.showPDFError()
                super.onError(exception)
            }
        }
    }

    private inner class UserBackPressedObserver : BaseObserver<User?>() {
        override fun onNext(user: User?) {
            myOrderView?.trackBackPressed(loginModelDataMapper.transform(user))
        }
    }

    private inner class GetMenuObserver : BaseObserver<Menu?>() {

        override fun onNext(menu: Menu?) {
            myOrderView?.let {
                it.showMenuOrder(menuModelDataMapper.transform(menu))
            }
        }
    }
}
