package biz.belcorp.consultoras.feature.home.survey

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.survey.OptionSurveyModel
import biz.belcorp.consultoras.util.anotation.SurveyQualificationType
import kotlinx.android.synthetic.main.item_option_survey.view.*
import javax.inject.Inject

class OptionSurveyAdapter @Inject constructor() : RecyclerView.Adapter<OptionSurveyAdapter.OptionSurveyViewHolder>() {

    var optionList = mutableListOf<OptionSurveyModel>()
    var onClickOptionSurvey: OnClickOptionSurvey? = null
    var isExpanded = false
    var optionSurveyModelSelected : OptionSurveyModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionSurveyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_option_survey, parent, false)
        return OptionSurveyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return optionList.size
    }

    override fun onBindViewHolder(holder: OptionSurveyViewHolder, position: Int) {
        holder.itemView.apply {
            val optionSurvey = optionList[holder.adapterPosition]
            optionImage.setImageResource(getResourceImage(optionSurvey.calificacionID ?: 0))
            nameOptionText.text = optionSurvey.calificacion
            nameOptionText.visibility = if (optionSurvey.isSelected) View.VISIBLE else View.INVISIBLE
            if (isExpanded) {
                optionImage.alpha = if (optionSurvey.isSelected) 1.0f else 0.4f
            }
            optionLinear.setOnClickListener {
                optionSurveyModelSelected = optionSurvey
                onClickOptionSurvey?.let {
                    it.onClickOptionSurvey(optionSurvey)
                }
            }
        }
    }

    private fun getResourceImage(id: Int): Int {
        when (id) {
            SurveyQualificationType.LOUSLY -> return R.drawable.ic_pesimo
            SurveyQualificationType.BAD -> return R.drawable.ic_mal
            SurveyQualificationType.REGULAR -> return R.drawable.ic_regular
            SurveyQualificationType.GOOD -> return R.drawable.ic_bueno
            SurveyQualificationType.EXCELLENT -> return R.drawable.ic_excelente
        }
        return 0
    }

    inner class OptionSurveyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    interface OnClickOptionSurvey {
        fun onClickOptionSurvey(optionSurveyModel: OptionSurveyModel)
    }
}
