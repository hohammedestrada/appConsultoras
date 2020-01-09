package biz.belcorp.consultoras.common.model.config

import biz.belcorp.consultoras.common.model.country.CountryModel

/**
 * Created by david.ruiz on 27/04/2017.
 */

class ConfigModel {
    var textGreeting: String? = null
    var urlImageEsikaBackground: String? = null
    var urlImageEsikaLogo: String? = null
    var urlImageLBelBackground: String? = null
    var urlImageLBelLogo: String? = null
    var idVideo: String? = null
    var urlVideo: String? = null
    var countries: List<CountryModel>? = null

}
