package biz.belcorp.consultoras.feature.campaigninformation

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.OrderUseCase
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.consultoras.util.GlobalConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@PerActivity
class CampaignInformationPresenter
@Inject constructor(
    private var userUseCase: UserUseCase,
    private var orderUseCase: OrderUseCase) : Presenter<CampaignInformationView> {

    private var view: CampaignInformationView? = null

    public var user : User? = null

    override fun attachView(view: CampaignInformationView) {
        this.view = view

        GlobalScope.launch {
            user = userUseCase.getUser()
            val info = orderUseCase.getInfoCampanias(user?.campaing, GlobalConstant.CANTIDAD_INFO_CAMPANIAS_ANTERIORES, GlobalConstant.CANTIDAD_INFO_CAMPANIAS_FUTURAS)
            GlobalScope.launch(Dispatchers.Main) {
                view.onInfoCampania(info)
            }
        }
    }

    override fun resume() {
        //Empty
    }

    override fun pause() {
        //Empty
    }

    override fun destroy() {
        this.userUseCase.dispose()
        this.orderUseCase.dispose()
    }


}
