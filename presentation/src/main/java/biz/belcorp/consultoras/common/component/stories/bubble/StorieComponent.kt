package biz.belcorp.consultoras.common.component.stories.bubble

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.stories.StorieModel
import kotlinx.android.synthetic.main.component_storie_bubble.view.*

class StorieComponent : LinearLayout {

    var listener: OnClickBubbleStorie? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    fun init(context: Context, attributeSet: AttributeSet?) {
        View.inflate(getContext(), R.layout.component_storie_bubble, this)
        storieAvatarView.setOnClickListener {
            listener?.onClickBubble()
        }
    }

    fun animateAndEnable(state: Boolean){
        storieAvatarView.isClickable = state
        storieAvatarView.isEnabled = state
        storieAvatarView.isAnimating = !state
    }

    fun getImageviewBubble(): StorieAvatarView = storieAvatarView

    private fun setAllSeen(){
        storieAvatarView.borderColor = ContextCompat.getColor(context, R.color.ripple_gray)
        storieAvatarView.borderColorEnd = ContextCompat.getColor(context, R.color.ripple_gray)
        storieAvatarView.highlightBorderColor = ContextCompat.getColor(context, R.color.ripple_gray)
        storieAvatarView.highlightBorderColorEnd = ContextCompat.getColor(context, R.color.ripple_gray)
    }

    fun changeAllSeen(contenidoDetalle: MutableList<StorieModel.ContenidoDetalleModel>) {
        val count = contenidoDetalle.count {
            it.visto
        }
        if( count >= contenidoDetalle.size )
            setAllSeen()

    }

    interface OnClickBubbleStorie {
        fun onClickBubble()
    }
}
