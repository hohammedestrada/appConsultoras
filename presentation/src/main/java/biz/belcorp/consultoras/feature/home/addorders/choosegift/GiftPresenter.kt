package biz.belcorp.consultoras.feature.home.addorders.choosegift

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.domain.entity.*
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.OrderUseCase
import biz.belcorp.consultoras.domain.interactor.ProductUseCase
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

class GiftPresenter
@Inject
internal constructor(private val userUseCase: UserUseCase
                     , private val orderUseCase: OrderUseCase,
                     private val productUseCase: ProductUseCase) : Presenter<GiftView> {
    private var viewGift: GiftView? = null
    private var usuario: User? = null
    var controlAnimation = 0
    override fun attachView(view: GiftView) {
        this.viewGift = view
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        productUseCase.dispose()
        userUseCase.dispose()
        orderUseCase.dispose()
        this.viewGift = null
    }

    fun getListGift() {
        viewGift?.showLoading()
        userUseCase[GetUser()]
    }

    fun autoSaveGift(iden: String?) {
        viewGift?.showLoading()
        userUseCase[GetUserToSaveGift(iden)]
    }

    fun saveGift(regalo: EstrategiaCarrusel, identificador: String) {
        viewGift?.showLoading()
        orderUseCase.saveGiftSelected(regalo, identificador, GiftSaveObserver(regalo.cuv))

    }

    private inner class GetUserToSaveGift(val identifier2: String?) : BaseObserver<User?>() {
        override fun onNext(user: User?) {
            if (null == user) return
            usuario = user
            viewGift?.assignUser(usuario)
            productUseCase.saveAutoGift(GiftAutoSaveObserver(),
                GiftAutoSaverRequest().apply {

                        campaniaID = usuario?.campaing?.toInt()
                        nroCampanias = usuario?.numberOfCampaings?.toInt()
                        codigoPrograma = usuario?.codigoPrograma
                        consecutivoNueva = usuario?.consecutivoNueva
                        identifier = identifier2
                })

        }

        override fun onError(exception: Throwable) {
            viewGift?.hideLoading()

        }
    }

    private inner class GetUser : BaseObserver<User?>() {

        override fun onNext(t: User?) {

            if (null == t) return

            usuario = t
            viewGift?.assignUser(usuario)
            t.let {
                productUseCase.getListProductGift(
                    GiftObserver(),
                    it.campaing?.toInt(),
                    it.numberOfCampaings?.toInt(),
                    it.codigoPrograma,
                    it.consecutivoNueva
                )
            }
        }

        override fun onError(exception: Throwable) {
            viewGift?.hideLoading()
        }
    }


    private inner class GiftAutoSaveObserver : DisposableObserver<Boolean?>() {
        override fun onComplete() {
            //nada
        }

        override fun onNext(t: Boolean) {
            viewGift?.onAutoSavedGift()
        }

        override fun onError(e: Throwable) {
            viewGift?.hideLoading()

        }
    }


    private inner class GiftObserver : DisposableObserver<Collection<EstrategiaCarrusel?>?>() {
        override fun onComplete() {
            //nada
        }

        override fun onNext(t: Collection<EstrategiaCarrusel?>) {

            if (!t.isEmpty()) {
                viewGift?.hideLoading()
                viewGift?.onListGiftRecived(t)
            } else {
                viewGift?.hideLoading()
                return
            }
        }

        override fun onError(e: Throwable) {
            viewGift?.hideLoading()
        }

    }

    private inner class GiftSaveObserver(private var cuv: String?) : BaseObserver<BasicDto<Collection<MensajeProl?>?>?>() {
        override fun onNext(t: BasicDto<Collection<MensajeProl?>?>?) {
            super.onNext(t)
            viewGift?.onGiftAdded(cuv!!)
            viewGift?.hideLoading()
        }

        override fun onComplete() {
            super.onComplete()
        }

        override fun onError(exception: Throwable) {
            super.onError(exception)
            viewGift?.hideLoading()
        }
    }

}
