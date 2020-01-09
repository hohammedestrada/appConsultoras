package biz.belcorp.consultoras.feature.payment.online.tipopago

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.pagoonline.PagoOnlineConfigMapper
import biz.belcorp.consultoras.common.model.pagoonline.PagoOnlineConfigModel
import biz.belcorp.consultoras.common.model.pagoonline.TipoPagoModel
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import java.util.ArrayList
import javax.inject.Inject

class TipoPagoPresenter  @Inject
internal constructor(private val pagoOnlineConfigMapper: PagoOnlineConfigMapper,
                     private  var userUseCase: UserUseCase): Presenter<TipoPagoView> {

    private var tipoPagoView: TipoPagoView? = null
    lateinit var userTrack : User

    override fun attachView(view: TipoPagoView) {
        tipoPagoView = view
    }

    override fun resume() {}

    override fun pause() {}

    override fun destroy() {}

    private var tipoPago: TipoPagoModel? = null

    fun createTipoPago(estadoCuenta: PagoOnlineConfigModel.EstadoCuenta?, tarjeta: PagoOnlineConfigModel.MetodoPago?, tipoPago: ArrayList<PagoOnlineConfigModel.TipoPago>?, url: String?) {
        tipoPagoView!!.showLoading()
        this.tipoPago=pagoOnlineConfigMapper.createTipoPago(url!!,estadoCuenta!!,tarjeta!!,tipoPago!!)
        this.userUseCase[GetUser()]

    }

     private inner class GetUser : BaseObserver<User?>() {

        override fun onNext(user: User?) {
            super.onNext(user)
            if (null == user) return

            userTrack = user
            tipoPago!!.vencimientoDeuda = user.detail!![5]!!.detailDescription!!
            tipoPago!!.simboloMoneda = user.countryMoneySymbol!!
            tipoPagoView!!.getTipo(tipoPago!!)
            tipoPagoView!!.hideLoading()
        }


    }

}
