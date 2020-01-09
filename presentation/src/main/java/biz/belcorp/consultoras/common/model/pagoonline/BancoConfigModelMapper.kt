package biz.belcorp.consultoras.common.model.pagoonline

import biz.belcorp.consultoras.data.util.Constant
import biz.belcorp.consultoras.domain.entity.PagoOnlineConfig
import java.util.*
import javax.inject.Inject

class BancoConfigModelMapper @Inject internal constructor() {

    fun transform(metodoPago: PagoOnlineConfigModel.MetodoPago?, medioPago: PagoOnlineConfigModel.MedioPago?): BancosConfig.OpcionPago{
        return BancosConfig.OpcionPago().apply {
                metodoPago.let {
                    this.description = it?.descripcion
                    this.medioPagoDetalleId = it?.medioPagoDetalleId
                }
                medioPago.let {it1->
                    this.medioPagoId=it1?.medioId
                   // this.medioPagoDetalleId = it1?.m
                    this.rutaIcono = it1?.rutaIcono
                    this.codigo=it1?.codigo
                }
        }
    }

    fun transform(description: String, medioPago: PagoOnlineConfigModel.MedioPago?): BancosConfig.OpcionPago{
        return BancosConfig.OpcionPago().apply {

                this.description = description

            medioPago.let {it1->
                this.medioPagoId=it1?.medioId
                this.rutaIcono = it1?.rutaIcono
                this.codigo=it1?.codigo
            }
        }
    }

    fun transform(metodoPago: List<PagoOnlineConfigModel.MetodoPago>?, medioPago: List<PagoOnlineConfigModel.MedioPago>?): Collection<BancosConfig.OpcionPago>{
        Collections.sort<PagoOnlineConfigModel.MedioPago>(medioPago) { o1, o2 -> (o1.orden)!!.compareTo(o2.orden!!) }
        Collections.sort<PagoOnlineConfigModel.MetodoPago>(metodoPago) { o1, o2 ->(o1.orden)!!.compareTo(o2.orden!!) }

            var retorno: MutableList<BancosConfig.OpcionPago> = mutableListOf<BancosConfig.OpcionPago>()

            medioPago?.forEach { it ->
                if(!it.codigo.equals(Constant.CODIGO_MEDIO_PAGO_INTERNET)){ //si no es la lista de bancos
                   if(it.estado!!){ //si esta habilitado el medio de pago entonces busco sus hijos
                       metodoPago?.forEach { it1 ->
                           if(it.medioId==it1.medioPagoId) {
                               if (it1.estado!!) {
                                   retorno.add(transform(it1, it))
                               }
                           }
                       }
                   }
                }
                else{
                    if(it.estado!!){
                        retorno.add(transform(it.descripcion!!,it))
                    }
                }
            }

        return retorno
    }

    fun transform (metodoPago: List<PagoOnlineConfigModel.MetodoPago>?, medioPago: List<PagoOnlineConfigModel.MedioPago>?, listaBancoOnline:List<PagoOnlineConfigModel.Banco>?): BancosConfig {
        var bancoOnline: MutableList<PagoOnlineConfigModel.Banco> = mutableListOf<PagoOnlineConfigModel.Banco>()
        var medioEdited: MutableList<PagoOnlineConfigModel.MedioPago> =   mutableListOf<PagoOnlineConfigModel.MedioPago>()


        if(medioPago!!.isNotEmpty())
            medioEdited.addAll(medioPago)
        if(listaBancoOnline!!.isNotEmpty()){
            listaBancoOnline.forEach {
                if(it.estado!!){
                    bancoOnline.add(it)
                }
            }
        }
        else{
            medioPago.forEach {
                if(it.codigo.equals(Constant.CODIGO_MEDIO_PAGO_INTERNET)){
                   medioEdited.remove(it)
                }
            }
        }
        return BancosConfig().apply {
            this.listaOpcionPago = transform(metodoPago, medioEdited) as List<BancosConfig.OpcionPago>//transform(metodoPago, medioPago) as List<BancosConfig.OpcionPago>
            this.listaBancoOnline = bancoOnline
        }
    }
}
