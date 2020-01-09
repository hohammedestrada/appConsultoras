package biz.belcorp.consultoras.feature.finaloffer

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.common.model.orders.OfertaFinalModel
import biz.belcorp.consultoras.common.model.orders.OfertaFinalModelDataMapper
import biz.belcorp.consultoras.common.model.orders.OrderModel
import biz.belcorp.consultoras.common.model.orders.OrderModelDataMapper
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.exception.VersionException
import biz.belcorp.consultoras.domain.interactor.*
import biz.belcorp.consultoras.util.GlobalConstant
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@PerActivity
class FinalOfferPresenter @Inject
internal constructor(
    private val userUseCase: UserUseCase,
    private val orderUseCase: OrderUseCase,
    private val accountUseCase: AccountUseCase,
    private val loginModelDataMapper: LoginModelDataMapper,
    private val ofertaFinalModelDataMapper: OfertaFinalModelDataMapper,
    private val orderModelDataMapper: OrderModelDataMapper,
    private val productUseCase: ProductUseCase,
    private val sessionUseCase: SessionUseCase,
    private val origenMarcacionUseCase: OrigenMarcacionUseCase
) : Presenter<FinalOfferView>, SafeLet {

    private var finalOfferView: FinalOfferView? = null
    private var usuaria: User? = null
    private var newEmail: String? = null
    var updateMailOF : Boolean = false
    override fun attachView(view: FinalOfferView) {
        finalOfferView = view
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
        this.origenMarcacionUseCase.dispose()
        this.finalOfferView = null
    }

    /** Métodos */

    fun initScreenTrack() {
        userUseCase[UserPropertyObserver()]
    }

    fun data() {
        finalOfferView?.showLoading()
        userUseCase[GetUser()]
    }

    fun addFinalOffer(item: OfertaFinalModel, position: Int, identifier: String) {
        finalOfferView?.showLoading()
        orderUseCase.addFinalOffer(ofertaFinalModelDataMapper.transform(item), identifier,
            AddFinalOfferObserver(item, position))
    }

    fun getOrder() {
        finalOfferView?.showLoading()
        orderUseCase.getOrdersFormatted(GetFormattedOrdersObserver())
    }

    fun reserve(order: OrderModel?) {
        finalOfferView?.showLoading()
        order?.let {
            orderUseCase.reserve(orderModelDataMapper.transform(it)!!, ReservaPedidoObserver())
        }
    }

    fun autoSaveGift(identifier: String?) {
        finalOfferView?.showLoading()
        productUseCase.saveAutoGift(AutoSaveGiftObserver(),
            GiftAutoSaverRequest().apply {
                campaniaID = usuaria?.campaing?.toInt()
                nroCampanias = usuaria?.numberOfCampaings?.toInt()
                codigoPrograma = usuaria?.codigoPrograma
                consecutivoNueva = usuaria?.consecutivoNueva
                this.identifier = identifier
            })
    }

    fun setStatusIsShowingGiftAnimation(status: Boolean) {
        finalOfferView?.showLoading()
        sessionUseCase.saveStatusGiftAnimation(status, GiftObserverSetStatus())
    }

    fun updateConsultEmail(newEmail:String){
        this.newEmail = newEmail
        accountUseCase.enviarCorreo(newEmail, UpdateEmailObserver())
    }

    fun initializeListOfferFinal(ubicacion: String, seccion: String) {
        GlobalScope.launch {
            try {
                val nameList = origenMarcacionUseCase.getValorLista(ubicacion, seccion)
                GlobalScope.launch(Dispatchers.Main) {
                    finalOfferView?.initializeListOfferFinal(nameList)
                }
            }
            catch (e: Exception) {}
        }
    }

    /** Observers */

    inner class GiftObserverSetStatus : BaseObserver<Boolean>() {
        override fun onNext(t: Boolean) {
            finalOfferView?.hideLoading()
            super.onNext(t)
        }

        override fun onError(exception: Throwable) {
            super.onError(exception)
            finalOfferView?.hideLoading()
        }

    }


    private inner class AutoSaveGiftObserver : DisposableObserver<Boolean?>() {
        override fun onComplete() {
            //nada
        }

        override fun onNext(t: Boolean) {
            finalOfferView!!.onAutoSavedGift()
            finalOfferView!!.hideLoading()
        }

        override fun onError(e: Throwable) {
            finalOfferView!!.hideLoading()

        }
    }


    private inner class UserPropertyObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            finalOfferView?.initScreenTrack(loginModelDataMapper.transform(t))
        }
    }

    private inner class GetUser : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            finalOfferView?.hideLoading()
            t?.let {
                finalOfferView?.setData(it)
                sessionUseCase.isUpdateMail(t.consultantCode,UpdateMailObserver())
                usuaria = it
            }
        }

        override fun onError(exception: Throwable) {
            finalOfferView?.hideLoading()
        }
    }

    private inner class UpdateMailObserver : BaseObserver<Boolean>() {

        override fun onNext(updateMail: Boolean) {
            updateMailOF = updateMail
        }
    }

    private inner class GetFormattedOrdersObserver : BaseObserver<FormattedOrder?>() {

        override fun onNext(t: FormattedOrder?) {
            finalOfferView?.hideLoading()
            t?.let {
                finalOfferView?.updateOrder(orderModelDataMapper.transform(t))
            }
        }

        override fun onError(exception: Throwable) {
            if (finalOfferView == null) return
            when (exception) {
                is VersionException -> {
                    finalOfferView?.hideLoading()
                    finalOfferView?.onVersionError(exception.isRequiredUpdate, exception.url)
                }
                else -> finalOfferView?.updateOrder(null)
            }
        }
    }

    private inner class ReservaPedidoObserver : BaseObserver<BasicDto<ReservaResponse>?>() {
        override fun onNext(t: BasicDto<ReservaResponse>?) {
            finalOfferView?.hideLoading()
            t?.let {
                if (it.code == GlobalConstant.CODE_OK) {
                    finalOfferView?.onReserveSuccess(t.data)
                } else {
                    it.message?.let { msg ->
                        finalOfferView?.onReserveError(msg, t.data)
                    }
                }
            }
        }
    }

    private inner class AddFinalOfferObserver(var item: OfertaFinalModel, var position: Int)
        : BaseObserver<BasicDto<Collection<MensajeProl?>?>?>() {

        override fun onNext(t: BasicDto<Collection<MensajeProl?>?>?) {
            t?.let {
                if (it.code == GlobalConstant.CODE_OK) {
                    finalOfferView?.onFinalOfferAddResponse(item, position)
                    getOrder()
                } else {
                    it.message?.let { msg ->
                        finalOfferView?.onError(msg)
                    }
                }
            }
        }
    }

    private inner class UpdateEmailObserver : BaseObserver<BasicDto<Boolean>?>() {

        override fun onNext(t: BasicDto<Boolean>?) {

            if(t?.code.equals(GlobalConstant.CODE_OK)){
                finalOfferView?.onEmailUpdated(newEmail)
            } else {
                t?.message?.let {
                    finalOfferView?.hideLoadingDialog()
                    finalOfferView?.onError(it)
                }
            }

        }

        override fun onError(exception: Throwable) {

            exception?.message?.let {
                finalOfferView?.hideLoadingDialog()
                finalOfferView?.onError(it)
            }
        }

    }

}
