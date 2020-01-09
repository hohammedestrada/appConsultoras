package biz.belcorp.consultoras.feature.home.addorders.choosegift

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.EstrategiaCarrusel
import biz.belcorp.consultoras.domain.entity.User

interface GiftView: View, LoadingView {
    fun onListGiftRecived(gift: Collection<EstrategiaCarrusel?>){
        //empty
    }
    fun onAutoSavedGift(){
        //empty
    }
    fun onGiftAdded(cuv: String){//empty
         }

    fun assignUser(usuario : User?){
        //empty
    }

}
