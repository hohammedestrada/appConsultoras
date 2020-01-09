package biz.belcorp.consultoras.common.component.video.model

data class VideoModel(
    val key: String?,
    val title: String?,
    var finishLoaded: Boolean = false
): Comparable<VideoModel> {

    override fun compareTo(other: VideoModel): Int {
        return 0
    }

}
