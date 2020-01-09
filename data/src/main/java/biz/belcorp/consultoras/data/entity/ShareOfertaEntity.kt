package biz.belcorp.consultoras.data.entity

import com.google.gson.annotations.SerializedName

data class ShareOfertaEntity(
    @SerializedName("Id") var id: Int?,
    @SerializedName("UrlCompartir") var urlCompartir: String?
) {

    companion object {
        fun getUrl(input: ShareOfertaEntity?) : String? {
            input?.run {
                return urlCompartir
            }
            return null
        }
    }

}
