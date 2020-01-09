package biz.belcorp.consultoras.common.model.incentivos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PremioModel(var id: Int? = null,
    var nivelLocalId: Int? = null,
    var codigoPremio: String? = null,
    var descripcionPremio: String? = null,
    var numeroPremio: Int? = null,
    var imagenPremio: String? = null): Parcelable

