package biz.belcorp.consultoras.feature.galery

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.entity.gallery.FiltroGaleria
import biz.belcorp.consultoras.domain.interactor.GaleryUseCase
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@PerActivity
class GalleryContainerPresenter
@Inject
internal constructor(
    private var userUseCase: UserUseCase,
    private val useCase: GaleryUseCase) : Presenter<GalleryView> {

    private var view: GalleryView? = null
    var user : User? = null
    private var mapper : GalleryModelMapper? = null

    companion object{
        const val IDPADRE_DEFAULT = 0
    }

    override fun attachView(view: GalleryView) {
        this.view = view

        mapper = GalleryModelMapper()
        GlobalScope.launch {
            user = userUseCase.getUser()
            Tracker.Gallery.trackScreen(user)
            start()
        }
    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        useCase.dispose()
        view = null
    }

    fun start(){
        user?.campaing?.let {camp ->
            GlobalScope.launch {
                try {
                    view?.showLoading()
                    val response = useCase.getGalery(camp)

                    GlobalScope.launch(Dispatchers.Main) {
                        view?.hideLoading()

                        response?.let { r ->
                            r.filtroGaleria?.let { filtros ->
                                if (filtros.isNullOrEmpty()) {
                                    view?.onGetGaleryNoItems()
                                } else {
                                    val parentFilters = ArrayList<FiltroGaleria?>()
                                    val sectionsFilter = ArrayList<SectionGalleryFilterItemModel>()

                                    parentFilters.addAll(filtros.filter {
                                        it?.CodigoPadre.isNullOrBlank()
                                    })

                                    if(parentFilters.isEmpty()){
                                        view?.onGetGaleryNoItems()
                                        return@launch
                                    }

                                    mapper?.let {maper ->
                                        parentFilters.filterNotNull().forEach { parent ->
                                            sectionsFilter.add(SectionGalleryFilterItemModel().apply {
                                                tab = maper.transform(parent)

                                                children.addAll(maper.transformFiltros(ArrayList(filtros.filter {
                                                    it?.CodigoPadre.equals(parent.Codigo, true)
                                                })))
                                            })
                                        }
                                    }

                                    view?.onGetFiltrosSuccess(sectionsFilter)

                                    val pageItems = ArrayList<SectionGalleryItemModel>()
                                    filtros.filterNotNull().forEach {tab ->

                                        if(tab.IdPadre != IDPADRE_DEFAULT) {
                                            val page = SectionGalleryItemModel()

                                            page.tab = mapper?.transform(tab)

                                            if(tab.EsSeccion){
                                                val listado = r.listadoImagen?.filterNotNull()?.filter {
                                                    it.seccion == tab.Codigo
                                                }?.toMutableList() ?: mutableListOf()
                                                page.files = mapper?.transformFiles(ArrayList(listado))
                                            }else{
                                                val listado = r.listadoImagen?.filterNotNull()?.filter {
                                                    it.categoria == tab.Codigo
                                                }?.toMutableList() ?: mutableListOf()
                                                page.files = mapper?.transformFiles(ArrayList(listado))
                                            }

                                            pageItems.add(page)
                                        }
                                    }

                                    view?.onGetGalerySuccess(pageItems)
                                }
                            }?: run {
                                view?.onGetGaleryNoItems()
                            }
                        }?: run {
                            view?.onGetGaleryNoItems()
                        }
                    }
                } catch (e: Exception) {
                    GlobalScope.launch(Dispatchers.Main) {
                        view?.hideLoading()
                        view?.onGetGaleryFails()
                    }
                }
            }
        }
    }
}
