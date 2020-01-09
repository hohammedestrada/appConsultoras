package biz.belcorp.consultoras.feature.orders

import javax.inject.Inject

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.common.model.client.ClienteModelDataMapper
import biz.belcorp.consultoras.common.model.orders.OrderModelDataMapper
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.FormattedOrder
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.Login
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.AccountUseCase
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.OrderUseCase
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.consultoras.feature.orderdetail.OrderDetailView
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.log.BelcorpLogger

@PerActivity
class OrderDetailPresenter @Inject
internal constructor(
    private val userUseCase: UserUseCase,
    private val orderUseCase: OrderUseCase,
    private val clienteModelDataMapper: ClienteModelDataMapper,
    private val orderModelDataMapper: OrderModelDataMapper,
    private val loginModelDataMapper: LoginModelDataMapper
) : Presenter<OrderDetailView>, SafeLet {

    private var ordersView: OrderDetailView? = null

    override fun attachView(view: OrderDetailView) {
        ordersView = view
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        this.userUseCase.dispose()
        this.orderUseCase.dispose()
        this.ordersView = null
    }

    fun data() {
        ordersView?.showLoading()
        userUseCase[GetUser()]
    }

    fun getOrdersList() {
        ordersView?.showLoading()
        orderUseCase.getOrdersFormatted( GetOrdersFormattedObserver())
    }

    fun deshacerReserva() {
        ordersView?.showLoading()
        orderUseCase.deshacerReserva(DeshacerReservaObserver())
    }

    fun initTrack() {
        userUseCase[UserPropertyTrackObserver()]
    }

    fun trackBackPressed() {
        userUseCase[BackPressedObserver()]
    }

    /** */

    private inner class UserPropertyTrackObserver : BaseObserver<User?>() {

        override fun onNext(t: User?) {
            t?.let {
                ordersView?.initScreenTrack(loginModelDataMapper.transform(it))
            }
        }
    }

    private inner class BackPressedObserver : BaseObserver<User?>() {

        override fun onNext(t: User?) {
            t?.let {
                ordersView?.trackBackPressed(loginModelDataMapper.transform(it))
            }
        }
    }

    private inner class GetUser : BaseObserver<User?>() {

        override fun onNext(t: User?) {
            t?.let {
                safeLet(it.countryISO, it.countryMoneySymbol, it.consultantName,
                    it.montoMinimoPedido, it.montoMaximoPedido) {
                    countryISO, countryMoneySymbol, consultantName, montoMinimoPedido, montoMaximoPedido ->
                    ordersView?.setData(countryISO, countryMoneySymbol, consultantName, montoMinimoPedido, montoMaximoPedido)
                }
            }
        }

        override fun onError(exception: Throwable) {
            BelcorpLogger.w(OrderDetailPresenter.TAG, "ERROR!", exception)
        }
    }

    private inner class GetOrdersFormattedObserver : BaseObserver<FormattedOrder?>() {
        override fun onNext(t: FormattedOrder?) {
            userUseCase.getLogin(GetLogin(t))
        }
    }

    private inner class GetLogin(var formattedOrder: FormattedOrder?) : BaseObserver<Login?>() {
        override fun onNext(t: Login?) {
            val orderModel = orderModelDataMapper.transform(formattedOrder)
            orderModel?.isPagoContado = formattedOrder?.pedidoValidado ?: false && t?.isPagoContado ?: false
            ordersView?.onFormattedOrder(orderModel,
                clienteModelDataMapper.transform(formattedOrder?.clientesDetalle))
            ordersView?.hideLoading()
        }
    }

    private inner class DeshacerReservaObserver : BaseObserver<BasicDto<Boolean>?>() {
        override fun onNext(t: BasicDto<Boolean>?) {
            ordersView?.hideLoading()
            t?.let {
                if (it.code == GlobalConstant.CODE_OK) {
                    ordersView?.onOrderReservedBack()
                } else {
                    it.message?.let {
                        ordersView?.onOrderError(it)
                    }
                }
            }

        }
    }

    companion object {
        private const val TAG = "OrderDetailPresenter"
    }

}
