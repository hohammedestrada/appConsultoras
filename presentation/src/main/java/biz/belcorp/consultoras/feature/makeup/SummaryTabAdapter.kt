package biz.belcorp.consultoras.feature.makeup

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import biz.belcorp.consultoras.data.entity.ProductoMasivo
import java.text.DecimalFormat

class SummaryTabAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    var dataAccepted : List<ProductoMasivo> = listOf()
    var dataRejected : List<ProductoMasivo> = listOf()

    private var moneySymbol : String? = null
    private var decimalFormat: DecimalFormat? = null
    private val fragments = arrayOf("AGREGADOS", "NO AGREGADOS")

    override fun getCount(): Int {
        return fragments.size
    }

    private val addedFragment: AddedFragment
        get() {
            val fragment = AddedFragment()
            return fragment
        }

    private val rejectedFragment: RejectedFragment
        get() {
            val fragment = RejectedFragment()
            return fragment
        }

    override fun getItem(position: Int): Fragment? {

        val bundle = Bundle()

        return when (position) {
            0 -> {
                val fragment = addedFragment
                fragment.setData(dataAccepted, moneySymbol, decimalFormat)
                fragment.arguments = bundle
                fragment
            }
            1 -> {
                val fragment = rejectedFragment
                fragment.setData(dataRejected, moneySymbol, decimalFormat)
                fragment.arguments = bundle
                fragment
            }
            else -> null
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {

        return when (position) {
            0 -> { return "${fragments[position]} (${dataAccepted.size})" }
            1 -> { return "${fragments[position]} (${dataRejected.size})" }
            else -> null
        }
    }

    fun setData(moneySymbol: String?, decimalFormat: DecimalFormat?) {
        this.moneySymbol = moneySymbol
        this.decimalFormat = decimalFormat
    }

    fun refresh() {
        addedFragment.loadData(dataAccepted)
        rejectedFragment.loadData(dataRejected)
    }
}
