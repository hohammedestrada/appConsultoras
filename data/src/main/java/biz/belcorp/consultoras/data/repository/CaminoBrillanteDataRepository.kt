package biz.belcorp.consultoras.data.repository

import biz.belcorp.consultoras.data.entity.caminobrillante.AnimRequestUpdate
import biz.belcorp.consultoras.data.mapper.CaminoBrillanteDataMapper
import biz.belcorp.consultoras.data.repository.datasource.caminobrillante.CaminoBrillanteDataStoreFactory
import biz.belcorp.consultoras.data.repository.datasource.user.UserDataStoreFactory
import biz.belcorp.consultoras.domain.entity.BasicDto
import biz.belcorp.consultoras.domain.entity.Oferta
import biz.belcorp.consultoras.domain.entity.caminobrillante.*
import biz.belcorp.consultoras.domain.repository.CaminoBrillanteRepository
import io.reactivex.Observable
import javax.inject.Inject

class CaminoBrillanteDataRepository @Inject
internal constructor(private val caminoBrillanteDataStoreFactory: CaminoBrillanteDataStoreFactory,
                     private val caminoBrillanteDataMapper: CaminoBrillanteDataMapper,
                     private val userDataStoreFactory: UserDataStoreFactory)
    : CaminoBrillanteRepository {


    override suspend fun getResumenConsultora(): NivelConsultoraCaminoBrillante? {
        return this.caminoBrillanteDataStoreFactory.createDB().getResumenConsultora().await()?.let {
            this.caminoBrillanteDataMapper.transformNivelConsultora(it)
        }
    }

    override fun getResumenConsultoraAsObservable(): Observable<NivelConsultoraCaminoBrillante?> {
        return this.caminoBrillanteDataStoreFactory.createDB().getResumenConsultoraAsObservable().map {
            this.caminoBrillanteDataMapper.transformNivelConsultora(it)
        }
    }

    override fun getNivelConsultoraCaminoBrillanteAsObservable(): Observable<Int> {
        return this.caminoBrillanteDataStoreFactory.createDB().getNivelConsultoraCaminoBrillanteAsObservable()
    }

    override suspend fun getNivelesCaminoBrillante(): List<NivelCaminoBrillante>? {
        return this.caminoBrillanteDataStoreFactory.createDB().getNivelesCaminoBrillante().await()?.let {
            this.caminoBrillanteDataMapper.transformNivelesCaminoBrillante(it)
        }
    }

    override suspend fun getResumenLogros(): LogroCaminoBrillante? {
        return this.caminoBrillanteDataStoreFactory.createDB().getResumenLogros().await()?.let {
            this.caminoBrillanteDataMapper.transformLogro(it)
        }
    }

    override suspend fun getLogros(): List<LogroCaminoBrillante>? {
        return this.caminoBrillanteDataStoreFactory.createDB().getLogros().await()?.let {
            this.caminoBrillanteDataMapper.transformLogros(it)
        }
    }

    override suspend fun getPedidosPeriodoActual(): List<LogroCaminoBrillante.Indicador.Medalla>? {
        val medallas = this.caminoBrillanteDataStoreFactory.createDB().getPedidosPeriodoActual().await()
        return this.caminoBrillanteDataMapper.transformMedallas(medallas)
    }

    override suspend fun getNivelCaminoBrillanteById(idNivel: String): NivelCaminoBrillante? {
        return this.caminoBrillanteDataStoreFactory.createDB().getNivelCaminoBrillanteById(idNivel).await()?.let {
            this.caminoBrillanteDataMapper.transformNivelCaminoBrillante(it)
        }
    }

    override suspend fun getKitsOfertas(): BasicDto<List<KitCaminoBrillante>>? {
        val caminoBrillanteDataStoreDB = caminoBrillanteDataStoreFactory.createDB()
        val caminoBrillanteDataStoreCloud = caminoBrillanteDataStoreFactory.create()

        return caminoBrillanteDataStoreDB.getResumenConsultora().await()?.let { resumen ->
            userDataStoreFactory.createDB().get()?.let { user ->
                user.campaing?.let {
                    this.caminoBrillanteDataMapper.transformKitsCaminoBrillante(
                        resumen.nivel?.toInt()?.let { it1 -> caminoBrillanteDataStoreCloud.getKitsOfertas(it.toInt(), it1).await() })
                }
            }
        }
    }

    override suspend fun getDemostradoresOfertas(orden: String?, filtro: String?, inicio: Int?, cantidad: Int?): BasicDto<List<DemostradorCaminoBrillante>>? {
        val caminoBrillanteDataStoreDB = caminoBrillanteDataStoreFactory.createDB()
        val caminoBrillanteDataStoreCloud = caminoBrillanteDataStoreFactory.create()

        return caminoBrillanteDataStoreDB.getResumenConsultora().await()?.let { resumen ->
            userDataStoreFactory.createDB().get()?.let { user ->
                user.campaing?.let {
                    this.caminoBrillanteDataMapper.transformDemostradoresCaminoBrillante(
                        resumen.nivel?.toInt()?.let { it1 -> caminoBrillanteDataStoreCloud.getDemostradoresOfertas(it.toInt(), it1, orden, filtro, inicio, cantidad).await() })
                }
            }
        }
    }

    override suspend fun getConfiguracionDemostrador(): BasicDto<ConfiguracionDemostrador> {
        val caminoBrillanteDataStore = caminoBrillanteDataStoreFactory.create()
        caminoBrillanteDataStore.getConfiguracionDemostrador().await().let {
            return caminoBrillanteDataMapper.transformConfiguracionDemostrador(it)
        }
    }

    override suspend fun getOfertasCarousel(): BasicDto<Carousel>? {
        val caminoBrillanteDataStoreCloud = caminoBrillanteDataStoreFactory.create()
        val caminoBrillanteDataStoreDB = caminoBrillanteDataStoreFactory.createDB()

        return caminoBrillanteDataStoreDB.getResumenConsultora().await()?.let { resumen ->
            userDataStoreFactory.createDB().get()?.let { user ->
                user.campaing?.let {
                    this.caminoBrillanteDataMapper.transformCarousel(
                        resumen.nivel?.toInt()?.let { it1 -> caminoBrillanteDataStoreCloud.getOfertasCarousel(it.toInt(), it1).await() })
                }
            }
        }
    }

    override suspend fun getNivelesHistoricoCaminoBrillante(): List<NivelConsultoraCaminoBrillante>? {
        val caminoBrillanteDataStoreDB = caminoBrillanteDataStoreFactory.createDB()
        return caminoBrillanteDataStoreDB.getNivelesHistoricoCaminoBrillante().await()?.let {
            this.caminoBrillanteDataMapper.transformNivelConsultora(it)
        }
    }

    override suspend fun getFichaProducto(tipo: String, cuv: String): BasicDto<Oferta?>? {
        val caminoBrillanteDataStoreCloud = caminoBrillanteDataStoreFactory.create()
        val caminoBrillanteDataStoreDB = caminoBrillanteDataStoreFactory.createDB()

        return caminoBrillanteDataStoreDB.getResumenConsultora().await()?.let { resumen ->
            userDataStoreFactory.createDB().get()?.let { user ->
                user.campaing?.let {
                    this.caminoBrillanteDataMapper.transformOferta(
                        resumen.nivel?.toInt()?.let { it1 -> caminoBrillanteDataStoreCloud.getFichaProducto("0", it.toInt(), cuv, it1).await() }
                    )
                }
            }
        }
    }

    override suspend fun getNivelActual(): NivelCaminoBrillante? {
        val caminoBrillanteDataStoreDB = caminoBrillanteDataStoreFactory.createDB()
        return caminoBrillanteDataStoreDB.getNivelConsultoraCaminoBrillante().await()?.let {
            caminoBrillanteDataStoreDB.getNivelCaminoBrillanteById(it.toString()).await()?.let {
                this.caminoBrillanteDataMapper.transformNivelCaminoBrillante(it)
            }
        }
    }

    override suspend fun getNivelSiguiente(): NivelCaminoBrillante? {
        val caminoBrillanteDataStoreDB = caminoBrillanteDataStoreFactory.createDB()
        return caminoBrillanteDataStoreDB.getNivelConsultoraCaminoBrillante().await()?.let {
            caminoBrillanteDataStoreDB.getSiguienteNivelCaminoBrillante(it).await()?.let {
                this.caminoBrillanteDataMapper.transformNivelCaminoBrillante(it)
            }
        }
    }

    override suspend fun updateAnimFlag(animRequest: AnimRequest): BasicDto<Boolean> {
        val caminoBrillanteDataStore = caminoBrillanteDataStoreFactory.create()
        return caminoBrillanteDataStore.updatelagAnim(AnimRequestUpdate(animRequest.key, animRequest.value, animRequest.repeat)).await().let {
            this.caminoBrillanteDataMapper.transformBoolean(it)
        }
    }

    override suspend fun getNivelCampanaAnterior(): Int? {
        val caminoBrillanteDataRepository = caminoBrillanteDataStoreFactory.createDB()
        return caminoBrillanteDataRepository.getNivelCampanaAnterior().await()
    }

}
