package biz.belcorp.consultoras.feature.notifications.list

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.domain.entity.Notificacion
import biz.belcorp.library.util.StringUtil
import kotlinx.android.synthetic.main.view_notification_list.view.*
import java.util.*

class NotificationListAdapter constructor(private val listener: NotificationListener) : RecyclerView.Adapter<NotificationListAdapter.ViewHolder>() {

    private val notificationList = ArrayList<Notificacion>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val default_emoji = 128582

        @SuppressLint("SetTextI18n")
        fun bind(item: Notificacion, listener: NotificationListener) = with(itemView) {

            item.emoji?.let {
                if(it == 0){
                    txtEmoji.text = StringUtil.getEmojiByUnicode(default_emoji)
                }else{
                    txtEmoji.text = StringUtil.getEmojiByUnicode(it)
                }
            }

            if(item.estado == 1){
                lnlBackground.setBackgroundColor(resources.getColor(R.color.white))
            }else{
                lnlBackground.setBackgroundColor(resources.getColor(R.color.notification))
            }

            txtBody.text = item.descripcion
            item.fecha?.let { txtTime.text = getTimePased(it) }

            itemView.setOnClickListener {
                listener.onOpenNotification(item)
            }

        }

        // Private Function
        private fun getTimePased(date: Date): String{

            val now = Calendar.getInstance()
            val start = Calendar.getInstance()
            start.time = date

            val milliseconds1 = start.timeInMillis
            val milliseconds2 = now.timeInMillis
            val diff = milliseconds2 - milliseconds1
            val diffSeconds = diff / 1000
            val diffMinutes = diff / (60 * 1000)
            val diffHours = diff / (60 * 60 * 1000)
            val diffDays = diff / (24 * 60 * 60 * 1000)

            return when {
                diffDays > 0 -> if(diffDays == 1L) "Hace $diffDays día" else "Hace $diffDays días"
                diffHours > 0 -> if(diffHours == 1L) "Hace $diffHours hora" else "Hace $diffHours horas"
                diffMinutes > 0 -> if(diffMinutes == 1L) "Hace $diffMinutes minuto" else "Hace $diffMinutes minutos"
                else -> if(diffSeconds == 0L) "Justo ahora" else if(diffSeconds == 1L) "Hace $diffSeconds segundo" else "Hace $diffSeconds segundos"
            }

        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): NotificationListAdapter.ViewHolder {
        return NotificationListAdapter.ViewHolder(LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.view_notification_list, viewGroup, false))
    }

    override fun onBindViewHolder(holder: NotificationListAdapter.ViewHolder, position: Int)
        = holder.bind(notificationList[position], listener)

    override fun getItemCount(): Int = notificationList.size

    // Interface
    interface NotificationListener {
        fun onOpenNotification(item: Notificacion)
    }

    // Public Functions
    fun setList(list: List<Notificacion?>?) {
        notificationList.clear()
        list?.filterNotNull()?.let { notificationList.addAll(it) }
        notifyDataSetChanged()
    }
    fun clearData(){
        notificationList.clear()
        notifyDataSetChanged()
    }

    fun changeNotification(notificacion: Notificacion){
        notificationList.forEach {
            if(it.id == notificacion.id && it.consultoraId == notificacion.consultoraId){
                it.estado = notificacion.estado
            }
            notifyDataSetChanged()
        }
    }

}
