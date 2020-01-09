package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.DataPagoConfigEntity
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.PagoOnlineConfig
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.net.dto.ServiceDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class PagoConfigEntityDataMapper
@Inject internal constructor(private val estadoCuentaEntityDataMapper: EstadoCuentaEntityDataMapper,
                             private val tipoPagoEntityDataMapper: TipoPagoEntityDataMapper,
                             private val medioPagoEntityDataMapper: MedioPagoEntityDataMapper,
                             private val metodoPagoDataMapper: MetodoPagoDataMapper,
                             private val bancoEntityDataMapper: BancoEntityDataMapper) {

    fun transform(input: DataPagoConfigEntity?): PagoOnlineConfig? {
        return input?.let {
            PagoOnlineConfig().apply {
                estadoCuenta = estadoCuentaEntityDataMapper.transform(input.estadoCuenta)
                listaTipoPago = tipoPagoEntityDataMapper.transform(input.listaTipoPago) as List<PagoOnlineConfig.TipoPago>
                listaMedioPago = medioPagoEntityDataMapper.transform(input.listaMedioPago) as List<PagoOnlineConfig.MedioPago>
                listaMetodoPago = metodoPagoDataMapper.transform(input.listaMetodoPago) as List<PagoOnlineConfig.MetodoPago>
                listaBanco = bancoEntityDataMapper.transform(input.listaBanco) as List<PagoOnlineConfig.Banco>
            }
        }
    }

    fun transform(input: ServiceDto<DataPagoConfigEntity>?): BasicDto<PagoOnlineConfig>? {
        return input?.let {
            BasicDto<PagoOnlineConfig>().apply {
                code = it.code
                message = it.message
                val json = Gson().toJson(it.data)
                val type = object : TypeToken<DataPagoConfigEntity>() {}.type
                try {
                    val epe: DataPagoConfigEntity = Gson().fromJson(json, type)
                    data = transform(epe)
                } catch (e: Exception) {
                    BelcorpLogger.d("TarjetaPago Online: obtencion de configuracion inicial nulo")
                }
            }
        }
    }
}
