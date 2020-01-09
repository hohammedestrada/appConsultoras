package biz.belcorp.consultoras.feature.caminobrillante.feature.home.viewmodels

import android.view.View

class BarraMontoAcumuladoViewModel {
    var isVisibleIndicador: Boolean = true
    var isVisibleFlag: Boolean = true
    var isVisibleNextLevel: Int = View.VISIBLE
    var isVisibleAmountNextLevel: Boolean = true
    var visibilityIcon1: Int = View.VISIBLE
    var visibilityIcon2: Int = View.VISIBLE
    var isVisibleText1: Boolean = true
    var isVisibleText2: Boolean = true

    var isVisibleAccumulatedAmount: Boolean = true

    var idResColorIndicador: Int? = null

    var montoMinimoPorcentaje: Double = 0.0
    var montoAcumuladoPorcentaje: Double = 0.0

    var textNameNextLevel: String = ""
    var textAmountNextLevel: String = ""
    var textAccumulatedAmount: String = ""

    var text1: String = ""
    var text1SizeDp: Float = 0f
    var text1IsBold: Boolean = false
    var text1Alignment: Int? = null

    var text2: String = ""
    var text2SizeDp: Float = 0f
    var text2IsBold: Boolean = false
    var text2Alignment: Int? = null

    var text3: String = ""

}
