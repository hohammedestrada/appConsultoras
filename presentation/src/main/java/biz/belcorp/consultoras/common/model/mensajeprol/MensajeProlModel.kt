package biz.belcorp.consultoras.common.model.mensajeprol

import android.os.Parcel
import android.os.Parcelable

class MensajeProlModel() :Parcelable{

    var code: String? = null
    var message: String? = null
    var image: String? = null

    constructor(parcel: Parcel) : this() {
        code = parcel.readString()
        message = parcel.readString()
        image = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(code)
        parcel.writeString(message)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MensajeProlModel> {
        override fun createFromParcel(parcel: Parcel): MensajeProlModel {
            return MensajeProlModel(parcel)
        }

        override fun newArray(size: Int): Array<MensajeProlModel?> {
            return arrayOfNulls(size)
        }
    }

}
