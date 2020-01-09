package biz.belcorp.consultoras.feature.product

import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.OrderListItem
import biz.belcorp.consultoras.domain.entity.MensajeProl
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.OrderUseCase
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.consultoras.util.anotation.UpdateProductCode
import javax.inject.Inject

@PerActivity
class ProductPresenter @Inject
internal constructor(
    private val userUseCase: UserUseCase
    , private val loginModelDataMapper: LoginModelDataMapper
    , private val orderUseCase: OrderUseCase)
    : Presenter<ProductView> {

    // Variables
    private var productView: ProductView? = null

    // Overrides Presenter
    override fun attachView(view: ProductView) {
        productView = view
    }

    override fun destroy() {
        this.userUseCase.dispose()
        this.orderUseCase.dispose()
        this.productView = null
    }

    override fun resume() {
        // EMPTY
    }
    override fun pause() {
        // EMPTY
    }

    /** functions */

    fun initScreenTrack() {
        userUseCase[UserPropertyObserver()]
    }

    fun updateProduct(pedidoID: Int, identifier: String, updateProduct:OrderListItem?){
        productView?.hideTooltipError()
        productView?.showLoading()
        updateProduct?.let {
            orderUseCase.updateProduct(pedidoID, identifier, updateProduct, OrderUpdatedObserver())
        }
    }

    /** Observers */

    private inner class UserPropertyObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            productView?.initScreenTrack(loginModelDataMapper.transform(t))
        }
    }

    private inner class OrderUpdatedObserver : BaseObserver<BasicDto<Collection<MensajeProl?>?>?>() {
        override fun onNext(t: BasicDto<Collection<MensajeProl?>?>?) {
            t?.let {
                when(t.code){
                    UpdateProductCode.OK ->{

                        if(t.data!=null ){
                            t.data?.let { it1 ->
                                productView?.onMensajeProl(it1)
                            }

                        }

                        productView?.onProductSaved(it.message)
                    }
                    else ->{
                        when(t.code){
                            UpdateProductCode.ERROR_MONTO_MINIMO_RESERVADO -> productView?.showBootomMessage(t.message)
                            UpdateProductCode.ERROR_CANTIDAD_EXCEDIDA -> productView?.showErrorExcedido(t.message)
                            else -> productView?.showTooltipError(t.message)
                        }

                    }
                }
                productView?.hideLoading()
            }
        }
    }

}
