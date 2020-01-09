package biz.belcorp.consultoras.data.mapper

import biz.belcorp.consultoras.data.entity.AcademyUrlEntity
import biz.belcorp.consultoras.domain.entity.AcademyUrl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AcademyUrlEntityDataMapper @Inject
internal constructor()// EMPTY
{

    fun transform(input: AcademyUrl?): AcademyUrlEntity? {
        return input?.let {
            AcademyUrlEntity().apply{
                urlMiAcademia = it.urlMiAcademia
                token = it.token
            }
        }
    }

    fun transform(input: AcademyUrlEntity?): AcademyUrl? {
        return input?.let {
            AcademyUrl().apply{
                urlMiAcademia = it.urlMiAcademia
                token = it.token
            }
        }
    }

}
