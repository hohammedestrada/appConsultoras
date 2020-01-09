package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.*
import biz.belcorp.consultoras.data.entity.caminobrillante.NivelConsultoraCaminoBrillanteEntity
import biz.belcorp.consultoras.data.entity.contenidoresumen.ContenidoDetalleEntity
import biz.belcorp.consultoras.data.entity.contenidoresumen.ContenidoResumenEntity
import biz.belcorp.consultoras.domain.entity.*
import javax.inject.Inject
import javax.inject.Singleton
import biz.belcorp.consultoras.domain.util.Constant
import biz.belcorp.library.net.dto.ServiceDto


@Singleton
class AccountDataMapper @Inject
internal constructor() {

    fun transform(terms: Boolean?, privacity: Boolean?): List<TermsRequestEntity> {
        val list = mutableListOf<TermsRequestEntity>()
        list.add(TermsRequestEntity().apply { tipo = Constant.TERMS_ONLY; aceptado = terms })
        list.add(TermsRequestEntity().apply { tipo = Constant.PRIVACY_ONLY; aceptado = privacity })
        return list
    }

    fun transform(smsRequest: SMSResquest) : SMSRequestEntity{
        return SMSRequestEntity().apply {
            campaniaID = smsRequest.campaniaID
            celularActual = smsRequest.celularActual
            celularNuevo = smsRequest.celularNuevo
            codigoSMS = smsRequest.codigoSMS
            soloValidar = smsRequest.soloValidar
            origenDescripcion = smsRequest.origenDescripcion
            origenID = smsRequest.origenID
            estadoActividadID = smsRequest.estadoActividadID
        }
    }

    fun transform(request: CreditAgreement) : CreditAgreementRequestEntity{
        return CreditAgreementRequestEntity().apply {
            aceptado = request.aceptado
            ip = request.ip
            so = request.so
            imei = request.imei
            deviceID = request.deviceID
        }
    }

    fun transform(request: ResumenRequest): ResumenRequestEntity{
        return ResumenRequestEntity().apply {
            this.campaing = request.campaing
            this.codeRegion = request.codeRegion
            this.codeSection = request.codeSection
            this.codeZone = request.codeZone
            this.idContenidoDetalle = request.idContenidoDetalle
            this.indConsulDig = request.indConsulDig
            this.numeroDocumento = request.numeroDocumento
            this.primerNombre = request.primerNombre
            this.primerApellido = request.primerApellido
            this.fechaNacimiento = request.fechaNacimiento
            this.correo = request.correo
            this.esLider = request.esLider
            this.codigoContenido = request.codigoContenido
        }
    }

    /** UserAccountConfig */

    fun transform(input: UserConfigDataEntity?): UserConfigData? {
        return input?.let {
            UserConfigData().apply {
                code = it.code
                value1 = it.value1
                value2 = it.value2
                value3 = it.value3
            }
        }
    }

    fun transform(input: List<UserConfigDataEntity?>?): Collection<UserConfigData?>? {
        return input?.let {
            it
                    .asSequence()
                    .map { it1 -> transform(it1) }
                    .filter { it1 -> null != it1 }
                    .toList()
        } ?: run {
            emptyList<UserConfigData>()
        }
    }

    fun transform(resumen: ServiceDto<List<ContenidoResumenEntity?>>): BasicDto<Collection<ContenidoResumen>> {

        return resumen.let {
            BasicDto<Collection<ContenidoResumen>>().apply {
                code = it.code
                data = transformContenido(it.data as List<ContenidoResumenEntity>)
            }
        }
    }

    private fun transformContenido(input: List<ContenidoResumenEntity>): Collection<ContenidoResumen>? {
        return input.asSequence()?.map { it1 -> transform(it1) }?.filter { it1 -> null != it1 }?.toList()
    }

    private fun transform(input: ContenidoResumenEntity): ContenidoResumen {
        return input?.let {
            ContenidoResumen().apply {
                codigoResumen = it.codigoResumen

                urlMiniatura = it.urlMiniatura

                totalContenido = it.totalContenido

                contenidoVisto = it.contenidoVisto

                contenidoDetalle = transformContenidoResumen(it.contenidoDetalleEntity)
            }
        }
    }

    private fun transformContenidoResumen(contenidoDetalleEntity: List<ContenidoDetalleEntity>?): List<ContenidoResumen.ContenidoDetalle>? {
        return contenidoDetalleEntity?.asSequence()?.map { it1 ->
            ContenidoResumen.ContenidoDetalle().apply {

                grupo = it1.grupo

                typeContenido = it1.typeContenido

                idContenido = it1.idContenido ?: ""

                codigoDetalleResumen = it1.codigoDetalleResumen

                urlDetalleResumen = it1.urlDetalleResumen

                accion = it1.accion

                ordenDetalleResumen = it1.ordenDetalleResumen

                visto = it1.isVisto

                descripcion = it1.Descripcion
            }
        }?.filter { it1 -> null != it1 }?.toList()
    }

    fun transform(input: List<NivelConsultoraCaminoBrillanteEntity>): List<UserCaminoBrillante>? {
        val list = mutableListOf<UserCaminoBrillante>()
        for (camino in input) {
            transform(camino)?.let { list.add(it) }
        }
        return list

    }

    fun transform(input: NivelConsultoraCaminoBrillanteEntity): UserCaminoBrillante? {
        return input.let {
            UserCaminoBrillante().apply {
                periodoCae = it.periodoCae
                campania = it.campania
                nivelActual = it.nivel
                montoPedido = it.montoPedido
                fechaIngreso = it.fechaIngreso
                gananciaPeriodo = it.gananciaPeriodo
                gananciaAnual = it.gananciaAnual
                kitSolicitado = it.kitSolicitado
                gananciaCampania = it.gananciaCampania
            }
        }
    }


}
