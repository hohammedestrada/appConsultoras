package biz.belcorp.consultoras.util

import android.view.View
import android.view.ViewGroup

object AndroidUtils {

    fun <T : View> getChildViewsByClass(parent: ViewGroup, clazz: Class<T>): ArrayList<T> {
        val children = ArrayList<T>()

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            if (clazz.isInstance(child)) {
                @Suppress("UNCHECKED_CAST")
                children.add(child as T)
            } else if (child is ViewGroup) {
                children.addAll(getChildViewsByClass(child, clazz))
            }
        }

        return children
    }
}
