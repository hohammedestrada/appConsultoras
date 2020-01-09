package biz.belcorp.consultoras.common.model.brand

import android.os.Parcel
import android.os.Parcelable

class BrandConfigModel(): Parcelable{

    var colorHome : String? = null
    var imageHomeUrl : String? = null
    var colorOrder: String? = null
    var imageOrderUrl : String? = null
    var colorOrderBar : String? = null
    var colorTextBar : String? = null
    var imageHomeHeader : String? = null

    constructor(parcel: Parcel) : this() {
        colorHome = parcel.readString()
        imageHomeUrl = parcel.readString()
        colorOrder = parcel.readString()
        imageOrderUrl = parcel.readString()
        colorOrderBar = parcel.readString()
        colorTextBar = parcel.readString()
        imageHomeHeader = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(colorHome)
        parcel.writeString(imageHomeUrl)
        parcel.writeString(colorOrder)
        parcel.writeString(imageOrderUrl)
        parcel.writeString(colorOrderBar)
        parcel.writeString(colorTextBar)
        parcel.writeString(imageHomeHeader)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BrandConfigModel> {
        override fun createFromParcel(parcel: Parcel): BrandConfigModel {
            return BrandConfigModel(parcel)
        }

        override fun newArray(size: Int): Array<BrandConfigModel?> {
            return arrayOfNulls(size)
        }
    }


}
