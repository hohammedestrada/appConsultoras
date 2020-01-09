package biz.belcorp.consultoras.domain.util

object OfferTypes {

    const val PALANCA_DEFAULT = "home"
    const val DEFAULT = "" // Ninguna palanca
    const val ODD = "ODD" // Oferta del Dia
    const val SR = "SR"   // ShowRoom
    const val MG = "MG"   // Más Ganadoras
    const val OPT = "OPT" // Ofertas para ti
    const val RD = "RD" // Ofertas para ti
    const val HV = "HV" // Herramientas de venta
    const val DP = "DP" // Duo Perfecto
    const val PN = "PN" // Pack de Nuevas
    const val ATP = "ATP" // Arma tu Pack
    const val LAN = "LAN" // Nuevos Lanzamientos
    const val OPM = "OPM" // Ofertas Para Mi
    const val LMG = "LMG" // Las Más Ganadoras
    
    const val OF = "OF" // Oferta Final
    const val CAT = "CAT" // Catalogo
    const val DIG = "DIG" // Digitado
    const val GND = "GND"   // GND
    const val LIQ = "LIQ" // Liquidación

    const val ALG = "ALG" // Ofertas del algoritmo Oferta Final

    //OTHERS
    const val OfertaParaTi = "001"

    fun getOfferTypeForAnalytics(type: String?): String{
        
        var finalType = type ?: DEFAULT
        
        when (type){

            OPT, RD, OPM -> {
                finalType = OPT
            }

        }

        return finalType
        
    }

}
