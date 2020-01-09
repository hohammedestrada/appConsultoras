package biz.belcorp.consultoras.common.component.video

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import biz.belcorp.consultoras.common.component.video.adapter.YoutubeVideoListAdapter
import biz.belcorp.consultoras.common.component.video.model.VideoModel
import biz.belcorp.consultoras.common.component.video.validator.YoutubeValidator
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.util.MenuPosition
import biz.belcorp.mobile.components.core.decorations.MarginHorizontalItemDecoration

class YoutubeVideoList : LinearLayout, YoutubeVideoListAdapter.PlayerListener {

    private var listVideos: List<VideoModel> = arrayListOf()
    private var hashAPI: String? = null
    private lateinit var videoListAdapter: YoutubeVideoListAdapter
    private lateinit var rvwVideoList: RecyclerView

    var youtubeVideoListListener: PlayerListener? = null

    constructor(context: Context) : super(context) {
        setupUI()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setupAttrs(context, attrs)
        setupUI()
    }

    private fun setupAttrs(context: Context, attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.YoutubeVideoList, 0, 0)
        try {

        } finally {
            ta.recycle()
        }
    }

    private fun setupUI() {
        this.orientation = VERTICAL

        setupRecycler()
    }

    private fun setupRecycler() {

        rvwVideoList = RecyclerView(context)
        videoListAdapter = YoutubeVideoListAdapter(this)

        rvwVideoList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvwVideoList.addItemDecoration(MarginHorizontalItemDecoration(16, 16))
        rvwVideoList.setHasFixedSize(true)
        rvwVideoList.adapter = videoListAdapter

        removeAllViews()
        addView(rvwVideoList, 0)

        val lp: LayoutParams = rvwVideoList.layoutParams as LayoutParams
        lp.width = LayoutParams.MATCH_PARENT
        lp.height = LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER_HORIZONTAL
        rvwVideoList.layoutParams = lp

    }

    fun setYoutubeVideoErrorListener(listener: YoutubeVideo.ErrorListener) {
        videoListAdapter.videoYoutubeErrorListener = listener
    }

    fun setList(list: List<VideoModel>) {
        if (list.isNotEmpty()) {
            this.listVideos = validateList(list)
            updateData()
        }
    }

    fun setHashAPI(hash: String) {
        this.hashAPI = hash
        updateData()
    }

    private fun updateData() {
        hashAPI?.let {
            if (this.listVideos.isNotEmpty())
                videoListAdapter.updateData(listVideos, it)
        }
    }

    private fun validateList(list: List<VideoModel>): List<VideoModel> {
        return list.filter { item -> item.key != null && item.key.let { YoutubeValidator.isIdVideo(it) } }
    }

    override fun onPlayerVideo(position: Int, data: VideoModel) {
        youtubeVideoListListener?.onPlayerVideo(position, data)
    }

    interface PlayerListener {
        fun onPlayerVideo(position: Int, data: VideoModel)
    }
}
