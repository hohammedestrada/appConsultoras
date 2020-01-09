package biz.belcorp.consultoras.feature.galery

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import kotlinx.coroutines.GlobalScope
import javax.inject.Inject
import kotlinx.coroutines.launch

@PerActivity
class GalleryDetailPresenter
@Inject constructor(private var userUseCase: UserUseCase ) : Presenter<GalleryDetailView>{

    private var view : GalleryDetailView? = null
    var user : User? = null

    override fun attachView(view: GalleryDetailView) {
        this.view = view

        GlobalScope.launch {
            user = userUseCase.getUser()
        }
    }

    override fun resume() {
        //Empty
    }

    override fun pause() {
        //Empty
    }

    override fun destroy() {
        //Empty
    }
}
