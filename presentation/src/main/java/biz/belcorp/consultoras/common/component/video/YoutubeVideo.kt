package biz.belcorp.consultoras.common.component.video

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import biz.belcorp.consultoras.R
import biz.belcorp.mobile.components.core.helpers.StylesHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.youtube.player.*
import kotlinx.android.synthetic.main.view_video_youtube.view.*

class YoutubeVideo : LinearLayout {

    private var hashAPI: String? = null
    private var videoId: String? = null
    private var autoPlay: Boolean = true
    private var lightBoxMode: Boolean = false

    var youtubeVideoErrorListener: ErrorListener? = null
    var youtubeVideoSuccessListener: SuccessListener? = null

    private var placeHolder: Drawable? = ContextCompat.getDrawable(context, R.drawable.placeholder_video)
    var isLoadedSuccess = false

    private var widthYoutubeVideo: Int = context.resources.getDimensionPixelSize(R.dimen.youtube_video_default_width)

    private val typeFaceRegular: Typeface? = ResourcesCompat.getFont(context, R.font.lato_regular)
    private var stylesHelper: StylesHelper = StylesHelper(context)

    var textColor: Int = ContextCompat.getColor(context, R.color.black)
    var textSize: Float = context.resources.getDimension(R.dimen.youtube_video_default_title_size)
    var textTitle: String? = null

    constructor(context: Context) : super(context) {
        inflate(context, R.layout.view_video_youtube, this)
        setupUI()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        inflate(context, R.layout.view_video_youtube, this)
        setupAttrs(context, attrs)
        setupUI()
    }

    private fun setupAttrs(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.YoutubeVideo, 0, 0)
        try {
            placeHolder = ta.getDrawable(R.styleable.YoutubeVideo_youtube_video_placeholder)
            autoPlay = ta.getBoolean(R.styleable.YoutubeVideo_youtube_video_auto_play, autoPlay)
            lightBoxMode = ta.getBoolean(R.styleable.YoutubeVideo_youtube_video_light_box_mode, lightBoxMode)
        } finally {
            ta.recycle()
        }
    }

    private fun setupUI() {
        orientation = VERTICAL

        placeHolder?.let { ivwPlaceholder.setImageDrawable(placeHolder) }
        stylesHelper.updateTextViewStyle(tvwTitle, typeFaceRegular, textColor, textSize)

        ivwPlay.visibility = View.GONE
        pbrLoading.visibility = View.GONE

        // loadThumbnailYoutube()
        loadThumbnailVideo()
        setupListener()
    }

    private fun loadYouTubeThumbnail() {
        hashAPI?.let {
            videoId?.let {
                pbrLoading.visibility = View.VISIBLE
                videoPreview.initialize(hashAPI, object : YouTubeThumbnailView.OnInitializedListener {

                    override fun onInitializationSuccess(yThumbnailView: YouTubeThumbnailView?, yThumbnailLoader: YouTubeThumbnailLoader?) {
                        yThumbnailLoader?.setVideo(videoId)

                        yThumbnailLoader?.setOnThumbnailLoadedListener(object : YouTubeThumbnailLoader.OnThumbnailLoadedListener {
                            override fun onThumbnailLoaded(thumbnail: YouTubeThumbnailView?, videoId: String?) {
                                isLoadedSuccess = true
                                pbrLoading.visibility = View.GONE
                                ivwPlaceholder.visibility = View.GONE
                                ivwPlay.visibility = View.VISIBLE
                                yThumbnailLoader.release()
                            }

                            override fun onThumbnailError(thumbnail: YouTubeThumbnailView?, thumbnailLoader: YouTubeThumbnailLoader.ErrorReason?) {
                                isLoadedSuccess = false

                                yThumbnailLoader.release()
                            }
                        })
                    }

                    override fun onInitializationFailure(p0: YouTubeThumbnailView?, p1: YouTubeInitializationResult?) {
                        isLoadedSuccess = false
                        pbrLoading.visibility = View.GONE
                    }
                })
            }
        }

    }

    private fun loadThumbnailVideo() {
        pbrLoading.visibility = View.VISIBLE

        val url = "https://img.youtube.com/vi/${this.videoId}/0.jpg"
        Glide.with(context)
            .load(url)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    ivwPlay.visibility = View.GONE
                    pbrLoading.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    // hacer algo cuando la imagen ya está cargada
                    ivwPlay.visibility = View.VISIBLE
                    pbrLoading.visibility = View.GONE
                    return false
                }
            })
            .into(ivwPlaceholder)
    }

    private fun setupListener() {
        val activity = context as AppCompatActivity

        this.setOnClickListener {
            if (YouTubeIntents.canResolveChannelIntent(activity) &&
                YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(activity) == YouTubeInitializationResult.SUCCESS) {

                val intent = YouTubeStandalonePlayer.createVideoIntent(activity, hashAPI, videoId, 0, autoPlay, lightBoxMode)
                activity.startActivity(intent)
                youtubeVideoSuccessListener?.successPlayerYoutube()

            } else if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(activity) == YouTubeInitializationResult.SERVICE_VERSION_UPDATE_REQUIRED) {
                youtubeVideoErrorListener?.isRequiredUpdateYoutube()
            } else if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(activity) == YouTubeInitializationResult.SERVICE_DISABLED) {
                youtubeVideoErrorListener?.isRequiredActivateYoutube()
            } else if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(activity) == YouTubeInitializationResult.SERVICE_MISSING) {
                youtubeVideoErrorListener?.isRequiredInstallYoutube()
            } else {
                youtubeVideoErrorListener?.unknownErrorYoutube()
            }

            // https://www.programcreek.com/java-api-examples/?class=com.google.android.youtube.player.YouTubeStandalonePlayer&method=createVideoIntent
        }
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        super.setLayoutParams(params)
        val layoutParams = this.layoutParams
        layoutParams.width = this.widthYoutubeVideo
        layoutParams.height = LayoutParams.WRAP_CONTENT
        this.requestLayout()
    }

    fun setMatchParentYoutubeVideo() {
        this.widthYoutubeVideo = LayoutParams.MATCH_PARENT
        val layoutParams = this.layoutParams
        layoutParams.width = this.widthYoutubeVideo
        layoutParams.height = LayoutParams.WRAP_CONTENT
        this.requestLayout()
    }

    fun setVideoId(id: String) {
        this.videoId = id
        loadThumbnailVideo()
    }

    fun setHashAPI(hash: String) {
        this.hashAPI = hash
    }

    fun setAutoPlay(auto: Boolean) {
        this.autoPlay = auto
    }

    fun setLightBoxMode(lightMode: Boolean) {
        this.lightBoxMode = lightMode
    }

    fun setPlaceHolderImage(placeHolder: Drawable) {
        this.placeHolder = placeHolder
        ivwPlaceholder.setImageDrawable(this.placeHolder)
    }

    fun setTitle(title: String?) {
        this.textTitle = title
        tvwTitle.text = title
    }

    /**
     * Listener
     */

    interface ErrorListener {
        fun isRequiredUpdateYoutube()
        fun isRequiredActivateYoutube()
        fun isRequiredInstallYoutube()
        fun unknownErrorYoutube()
    }

    interface SuccessListener {
        fun successPlayerYoutube()
    }

}
