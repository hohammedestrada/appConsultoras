package biz.belcorp.consultoras.data.mapper

import javax.inject.Inject
import javax.inject.Singleton

import biz.belcorp.consultoras.data.entity.FacebookProfileEntity
import biz.belcorp.consultoras.domain.entity.FacebookProfile

@Singleton
class FacebookEntityProfileDataMapper @Inject
internal constructor() {

    fun transform(model: FacebookProfileEntity?): FacebookProfile? {
        return model?.let {
            FacebookProfile().apply {
                id = it.id
                name = it.name
                email = it.email
                image = it.image
                firstName = it.firstName
                lastName = it.lastName
                linkProfile = it.linkProfile
                birthday = it.birthday
                gender = it.gender
                location = it.location
            }
        }
    }

    fun transform(entity: FacebookProfile?): FacebookProfileEntity? {
        return entity?.let {
            FacebookProfileEntity().apply {
                id = it.id
                name = it.name
                email = it.email
                image = it.image
                firstName = it.firstName
                lastName = it.lastName
                linkProfile = it.linkProfile
                birthday = it.birthday
                gender = it.gender
                location = it.location
            }
        }
    }

}
