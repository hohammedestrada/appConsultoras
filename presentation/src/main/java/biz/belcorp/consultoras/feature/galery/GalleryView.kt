package biz.belcorp.consultoras.feature.galery

import biz.belcorp.consultoras.base.View
import biz.belcorp.consultoras.common.view.LoadingView

interface GalleryView : View, LoadingView {

    fun onGetGalerySuccess(list: ArrayList<SectionGalleryItemModel>)

    fun onGetFiltrosSuccess(list: ArrayList<SectionGalleryFilterItemModel>)

    fun onGetGaleryNoItems()

    fun onGetGaleryFails()
}
