package biz.belcorp.consultoras.feature.home.marquee

import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView

class MarqueeSnapHelper (private val onSelectItemChange : OnSelectItemChange) : PagerSnapHelper() {

    var lastPosition : Int = 0

    override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager?, velocityX: Int, velocityY: Int): Int {
        var position = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
        if(position != RecyclerView.NO_POSITION && lastPosition != position && position < layoutManager?.itemCount ?: 0){
            onSelectItemChange.onSelectedItemChange(position)
            lastPosition = position
        }
        return position
    }

    interface OnSelectItemChange{
        fun onSelectedItemChange(position: Int)
    }


}

