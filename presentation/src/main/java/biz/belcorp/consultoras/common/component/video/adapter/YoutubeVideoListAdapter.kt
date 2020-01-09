package biz.belcorp.consultoras.common.component.video.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.component.video.YoutubeVideo
import biz.belcorp.consultoras.common.component.video.model.VideoModel
import kotlinx.android.synthetic.main.view_video_item_youtube.view.*

class YoutubeVideoListAdapter(
    private val listener: PlayerListener
) : RecyclerView.Adapter<YoutubeVideoListAdapter.VideoViewHolder>() {

    private val data: ArrayList<VideoModel> = arrayListOf()
    private var hashAPI: String? = null

    var videoYoutubeErrorListener: YoutubeVideo.ErrorListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_video_item_youtube, parent, false)

        return VideoViewHolder(view, data)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val element = data[position]

        element.key?.let { key ->
            if (!element.finishLoaded) {
                hashAPI?.let { hash ->

                    if (data.size == 1) {
                        holder.youtubeVideo.setMatchParentYoutubeVideo()
                    }

                    holder.youtubeVideo.setHashAPI(hash)
                    holder.youtubeVideo.setVideoId(key)
                    holder.youtubeVideo.setTitle(element.title)

                    element.finishLoaded = true
                }
            }
        }

        holder.youtubeVideo.youtubeVideoSuccessListener = object : YoutubeVideo.SuccessListener {
            override fun successPlayerYoutube() {
                listener.onPlayerVideo(holder.adapterPosition, element)
            }
        }

        holder.youtubeVideo.youtubeVideoErrorListener = videoYoutubeErrorListener
    }

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    fun updateData(newData: List<VideoModel>, hash: String) {
        data.clear()
        data.addAll(newData)
        this.hashAPI = hash
        notifyDataSetChanged()
    }

    class VideoViewHolder(val view: View, val data: List<VideoModel>) : RecyclerView.ViewHolder(view) {
        val youtubeVideo: YoutubeVideo = view.video
    }

    interface PlayerListener {
        fun onPlayerVideo(position: Int, data: VideoModel)
    }
}
