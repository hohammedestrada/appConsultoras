package biz.belcorp.consultoras.domain.entity

/**
 * Entidad de dominio Country
 * que recibe o envia los datos a la capa de datos o a la capa de presentacion
 *
 * @version 1.0
 * @since 2017-04-14
 */

class ConfigReponse {

    var textGreeting: String? = null
    var urlImageEsikaBackground: String? = null
    var urlImageEsikaLogo: String? = null
    var urlImageLBelBackground: String? = null
    var urlImageLBelLogo: String? = null
    var idVideo: String? = null
    var urlVideo: String? = null
    var countries: List<Country?>? = null
    var apps: List<App?>? = null
    var origenPedidoWeb: List<OrigenPedidoWeb?>? = null
}
