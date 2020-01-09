package biz.belcorp.consultoras.common.model.pagoonline

class BancosConfig {

    var listaOpcionPago: List<OpcionPago>? = emptyList()

    var listaBancoOnline: List<PagoOnlineConfigModel.Banco>? = emptyList()


    class OpcionPago{

        var medioPagoId: Int? = null //el id padre

        var medioPagoDetalleId: Int? = null

        var description: String? = null

        var codigo: String? = null //medio

        var rutaIcono: String? = null
    }
}
