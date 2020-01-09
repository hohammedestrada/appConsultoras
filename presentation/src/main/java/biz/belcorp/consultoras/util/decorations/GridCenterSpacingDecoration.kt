package biz.belcorp.consultoras.util.decorations

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class GridCenterSpacingDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val headerNum: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        val position = parent.getChildAdapterPosition(view) - headerNum

        if (position >= 0) {

            val widthParent = parent.width
            val widthChild = (view.layoutParams.width * spanCount)
            val spaceTotal = (widthParent - widthChild) / spanCount
            val spaceMiddle = spacing / spanCount

            when (position % spanCount) {
                0 -> outRect.left = spaceTotal - spaceMiddle
                1 -> outRect.left = 0 + spaceMiddle
            }
            if (position < spanCount) {
                outRect.top = spacing
            }
            outRect.bottom = spacing

        } else {
            outRect.left = 0
            outRect.right = 0
            outRect.top = 0
            outRect.bottom = 0
        }
    }


    companion object {
        val TAG: String = GridCenterSpacingDecoration::class.java.simpleName
    }
}
