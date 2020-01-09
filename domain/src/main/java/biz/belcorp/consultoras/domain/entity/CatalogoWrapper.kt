package biz.belcorp.consultoras.domain.entity

import java.util.Collections

/**
 *
 */
class CatalogoWrapper {

    var campaignName: String? = null
    var catalogoEntities: List<Catalogo?>? = null
    var magazineEntity: Catalogo? = null

    companion object {

        fun empty(): CatalogoWrapper {
            val catalogoWrapper = CatalogoWrapper()
            catalogoWrapper.campaignName = ""
            catalogoWrapper.catalogoEntities = emptyList()
            catalogoWrapper.magazineEntity = null
            return catalogoWrapper
        }
    }
}
