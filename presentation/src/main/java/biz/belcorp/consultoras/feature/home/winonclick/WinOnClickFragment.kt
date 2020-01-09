package biz.belcorp.consultoras.feature.home.winonclick

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.BaseFragment
import biz.belcorp.consultoras.common.model.auth.LoginModel
import biz.belcorp.consultoras.common.tracking.Tracker
import biz.belcorp.consultoras.feature.home.winonclick.di.WinOnClickComponent
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.consultoras.util.anotation.VideoType
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import kotlinx.android.synthetic.main.fragment_winonclick.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 */
class WinOnClickFragment : BaseFragment(), WinOnClickWebView {

    @Inject
    lateinit var presenter: WinOnClickPresenter

    private var position = 0
    internal var listener: Listener? = null

    private var mediaController: MediaController? = null
    private var videoType: String? = null
    private var youTubePlayer: YouTubePlayer? = null

    override fun onInjectView(): Boolean {
        getComponent(WinOnClickComponent::class.java).inject(this)
        return true
    }

    override fun onViewInjected(savedInstanceState: Bundle?) {
        super.onViewInjected(savedInstanceState)
        this.presenter.attachView(this)
        this.presenter.trackScreenView()
        initVideoView()
        arguments?.let {
            val url = it.getString(GlobalConstant.WINONCLICK_VIDEO_URL, "")
            videoType = it.getString(GlobalConstant.WINONCLICK_VIDEO_TYPE, "")
            presenter.setUrl(url, videoType)
        } ?: run {
            this.showError()
        }
    }

    private fun initVideoView() {
        mediaController = MediaController(activity)
        mediaController?.setMediaPlayer(videoView)
        videoView.setMediaController(mediaController)
        videoView.setOnPreparedListener {
            hideLoading()
            videoView.seekTo(position)
            if (position == 0) {
                videoView.start()
            } else {
                videoView.pause()
            }
        }
    }

    private fun initYoutubePlayer(videoId: String) {
        val youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance()

        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.youtubePlayerFragment, youTubePlayerFragment)
        transaction?.commit()

        youTubePlayerFragment.initialize(BuildConfig.API_YOUTUBE, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, p1: YouTubePlayer?, p2: Boolean) {
                youTubePlayer = p1
                youTubePlayer?.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL)
                youTubePlayer?.cueVideo(videoId)

                youTubePlayer?.setPlayerStateChangeListener(object : YouTubePlayer.PlayerStateChangeListener{
                    override fun onLoading() {
                        //EMPTY
                    }

                    override fun onLoaded(p0: String?) {
                        //EMPTY
                    }

                    override fun onError(p0: YouTubePlayer.ErrorReason?) {
                        //EMPTY
                    }

                    override fun onAdStarted() {
                        //EMPTY
                    }

                    override fun onVideoStarted() {
                        presenter.trackStartVideo()
                    }

                    override fun onVideoEnded() {
                        presenter.trackEndVideo()
                    }
                })
            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
                hideLoading()
                showError()
            }
        })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Listener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_winonclick, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)

        savedInstanceState?.let {
            if (videoType != VideoType.YOUTUBE) {
                val position = it.getInt(POSITION_TAG)
                videoView.seekTo(position)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (videoType != VideoType.YOUTUBE) {
            outState.putInt(POSITION_TAG, videoView.currentPosition)
            videoView.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.destroy()
    }

    override fun showLoading() {
        if (isVisible && videoType != VideoType.YOUTUBE) {
            videoView.visibility = View.GONE
        }
        super.showLoading()
    }

    override fun hideLoading() {
        if (isVisible && videoType != VideoType.YOUTUBE) {
            videoView.visibility = View.VISIBLE
        }
        super.hideLoading()
    }

    override fun initScreenTrack(model: LoginModel) {
        // Empty
    }

    override fun trackBack(model: LoginModel) {
        listener?.onBackFromFragment()
    }

    override fun showUrlVideoView(url: String) {
        videoView.visibility = View.VISIBLE
        videoView.setVideoPath(url)
        videoView.start()
        presenter.trackStartVideo()

        videoView.setOnCompletionListener(object : MediaPlayer.OnCompletionListener{
            override fun onCompletion(p0: MediaPlayer?) {
                presenter.trackEndVideo()
            }
        })
    }

    override fun showUrlYoutube(url: String) {
        hideLoading()
        youtubePlayerContain.visibility = View.VISIBLE
        initYoutubePlayer(url)
    }

    override fun showError() {
        if (isVisible) {
            videoView.visibility = View.GONE
            rltContentError.visibility = View.VISIBLE
        }
    }

    fun trackBackPressed() {
        presenter?.trackBack()
    }

    override fun context(): Context? {
        val activity = activity
        return activity?.applicationContext
    }

    internal interface Listener {
        fun onBackFromFragment()
    }

    companion object {
        private const val POSITION_TAG = "Posicion"

        fun newInstance(): WinOnClickFragment {
            return WinOnClickFragment()
        }
    }
}
