package biz.belcorp.consultoras.feature.search.single

import android.os.Parcel
import android.os.Parcelable
import biz.belcorp.consultoras.common.model.client.ClienteModel
import biz.belcorp.consultoras.common.model.orders.OrderModel
import biz.belcorp.consultoras.domain.entity.ProductCUV
import java.text.DecimalFormat

class ProductParams : Parcelable {

    var prdCUV: ProductCUV? = null
    var clntMdl: ClienteModel? = null
    var mnySymb: String? = null
    var prdPrsntr: SearchProductPresenter? = null
    var dcmlFrmt: DecimalFormat? = null
    var order: OrderModel? = null
    var listTagOrder: MutableMap<Int, String?> = mutableMapOf()

    constructor()

    constructor(source: Parcel) : this()
    override fun describeContents() = 0
    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {}

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ProductParams> = object : Parcelable.Creator<ProductParams> {
            override fun createFromParcel(source: Parcel): ProductParams = ProductParams(source)
            override fun newArray(size: Int): Array<ProductParams?> = arrayOfNulls(size)
        }
    }
}
