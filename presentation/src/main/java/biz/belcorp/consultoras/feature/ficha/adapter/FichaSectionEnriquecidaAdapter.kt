package biz.belcorp.consultoras.feature.ficha.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.common.component.video.YoutubeVideoList
import biz.belcorp.consultoras.common.component.video.model.VideoModel
import biz.belcorp.consultoras.common.model.ficha.FichaSectionEnriquecidaModel
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.component.video.YoutubeVideo
import biz.belcorp.consultoras.util.anotation.SectionType
import biz.belcorp.consultoras.domain.entity.Componente
import biz.belcorp.mobile.components.design.accordion.Accordion
import biz.belcorp.mobile.components.design.text.TextSection
import biz.belcorp.mobile.components.design.text.model.TextSectionModel

class FichaSectionEnriquecidaAdapter(
    val context: Context,
    val listener: Listener,
    private val youtubeVideoErrorListener: YoutubeVideo.ErrorListener
) : RecyclerView.Adapter<FichaSectionEnriquecidaAdapter.ViewHolder>() {

    val items: ArrayList<FichaSectionEnriquecidaModel> = arrayListOf()
    var component: Componente? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_ficha_section_enriquecida, viewGroup, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, p1: Int) {
        viewHolder.accordion.setTitle(items[p1].title)
        viewHolder.accordion.setBoldTitle(false)

        if (items[p1].data != null) {
            when (items[p1].type) {
                SectionType.TEXT -> {
                    val titleDetailView = TextSection(context)
                    titleDetailView.setListTextSection(items[p1].data as ArrayList<TextSectionModel>)
                    viewHolder.accordion.setView(titleDetailView)

                }
                SectionType.VIDEO -> {
                    val videoCarouselView = YoutubeVideoList(context)
                    videoCarouselView.youtubeVideoListListener = object : YoutubeVideoList.PlayerListener {
                        override fun onPlayerVideo(position: Int, data: VideoModel) {
                            listener.onPlayerSectionVideoYoutube(component)
                        }
                    }
                    items[p1].hashAPI?.let { videoCarouselView.setHashAPI(it) }
                    videoCarouselView.setYoutubeVideoErrorListener(youtubeVideoErrorListener)
                    videoCarouselView.setList(items[p1].data as ArrayList<VideoModel>)
                    viewHolder.accordion.setView(videoCarouselView)
                    viewHolder.accordion.setColorTitle(ContextCompat.getColor(context, R.color.magenta))
                    viewHolder.accordion.expand()

                }
            }
        } else {
            viewHolder.accordion.hideView()
        }

        viewHolder.adapterPosition

        viewHolder.accordion.accordionClickListener = object : Accordion.AccordionClickListener {
            override fun onClick(view: View) {
                if (viewHolder.accordion.isExpanded()) {
                    viewHolder.accordion.setColorTitle(ContextCompat.getColor(context, R.color.black))
                    viewHolder.accordion.contract()
                } else {
                    viewHolder.accordion.setColorTitle(ContextCompat.getColor(context, R.color.magenta))
                    viewHolder.accordion.expand()
                    listener.onPressedSectionEnriquecida(viewHolder.adapterPosition)
                    listener.onPressedSectionExpand(viewHolder.adapterPosition, items[p1], component)
                }
            }
        }

        if (viewHolder.adapterPosition == (items.size - 1)) {
            viewHolder.accordion.setDividerVisible(false)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val accordion = itemView.findViewById<Accordion>(R.id.accordionSectionEnriquecida)
    }

    fun setList(list: ArrayList<FichaSectionEnriquecidaModel>) {
        items.clear()
        items.addAll(list.toList())

        notifyDataSetChanged()
    }

    interface Listener {
        fun onPlayerSectionVideoYoutube(item: Componente?)
        fun onPressedSectionEnriquecida(position: Int)
        fun onPressedSectionExpand(position: Int, data: FichaSectionEnriquecidaModel, item: Componente?)
    }
}
