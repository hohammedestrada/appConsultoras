package biz.belcorp.consultoras.feature.caminobrillante.feature.logrounificado

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.view.LoadingView
import biz.belcorp.consultoras.domain.entity.caminobrillante.LogroCaminoBrillante

interface LogroUnificadoView: View, LoadingView {

    fun setPhotoUser(urlImagen: String)
    fun setResumenLogro(tiempo : String, mensaje: String, lblIndicador1: String, indicador1: String,lblIndicador2: String, indicador2: String,lblIndicador3: String, indicador3: String)
    fun setDataIndicadores(logros : List<LogroCaminoBrillante>)
    fun setCanBack(canBack: Boolean)

}
