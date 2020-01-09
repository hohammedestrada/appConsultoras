package biz.belcorp.consultoras.feature.home.storie

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class StoriePagesAdapter(
    private val context: Context?, fragmentManager: FragmentManager?,
    private val myFragments: MutableList<Fragment>,
    private val listenerLoadStorie: StorieFragmentContent.IStorieFragment) : FragmentPagerAdapter(fragmentManager) {


    override fun getItem(position: Int): Fragment {
        (myFragments[position] as StorieFragmentContent).listener = listenerLoadStorie
        return myFragments[position]
    }

    override fun getCount(): Int {

        return myFragments.size
    }

    fun add(c: Class<Fragment>, listener: StorieFragmentContent.IStorieFragment,b: Bundle) {

        var frag = Fragment.instantiate(context, c.name, b)

        if(frag is StorieFragmentContent){
            frag.listener = listener

            myFragments.add(frag)
        }
    }

    companion object {
        var pos = 0
    }
}
