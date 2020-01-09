package biz.belcorp.consultoras.feature.home.winonclick

import biz.belcorp.consultoras.base.Presenter
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.model.auth.LoginModelDataMapper
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.domain.entity.User
import biz.belcorp.consultoras.domain.interactor.BaseObserver
import biz.belcorp.consultoras.domain.interactor.UserUseCase
import biz.belcorp.consultoras.util.anotation.VideoType
import java.util.regex.Pattern
import javax.inject.Inject

class WinOnClickPresenter @Inject
internal constructor(
    private val userUseCase: UserUseCase,
    private val loginModelDataMapper: LoginModelDataMapper) : Presenter<WinOnClickWebView> {

    private var view: WinOnClickWebView? = null
    private var user: LoginModel? = null

    override fun attachView(view: WinOnClickWebView) {
        this.view = view

    }

    override fun resume() {
        // EMPTY
    }

    override fun pause() {
        // EMPTY
    }

    override fun destroy() {
        this.view = null
    }

    fun initScreenTrack() {
        userUseCase[UserPropertyObserver()]
    }

    fun trackBack() {
        userUseCase[UserBackPressedObserver()]
    }

    fun setUrl(url: String, videoType: String?) {
        if (url.isEmpty()) {
            view?.showError()
            return
        }
        view?.showLoading()
        when (videoType) {
            VideoType.YOUTUBE -> view?.showUrlYoutube(extractYoutubeId(url))
            VideoType.S3 -> view?.showUrlVideoView(url)
            else -> view?.showUrlVideoView(url)
        }
    }

    private fun extractYoutubeId(ytUrl: String): String {
        var vId = ""
        val pattern = Pattern.compile(
            "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)",
            Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(ytUrl)
        if (matcher.matches()) {
            vId = matcher.group(1)
        }
        return vId
    }

    public fun trackScreenView(){
        Tracker.ConferenciaDigital.PantallaVista(user)
    }

    public fun trackStartVideo(){
        Tracker.ConferenciaDigital.InicioFinVideo(user,true)
    }

    public fun trackEndVideo(){
        Tracker.ConferenciaDigital.InicioFinVideo(user,false)
    }

    private inner class UserPropertyObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            user = loginModelDataMapper.transform(t)
            view?.initScreenTrack(loginModelDataMapper.transform(t))
        }
    }

    private inner class UserBackPressedObserver : BaseObserver<User?>() {
        override fun onNext(t: User?) {
            view?.trackBack(loginModelDataMapper.transform(t))
        }
    }
}
