package biz.belcorp.consultoras.feature.home.incentives

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.base.SafeLet
import biz.belcorp.consultoras.common.model.incentivos.ConcursoModel
import biz.belcorp.consultoras.feature.home.BaseHomeFragment
import biz.belcorp.consultoras.util.GlobalConstant
import biz.belcorp.library.annotation.Country
import kotlinx.android.synthetic.main.fragment_incentivos_regalo_history.*


class GiftHistoryFragment : BaseHomeFragment(), SafeLet {

    private lateinit var currentListAdapter: GiftHistoryAdapter

    private var currentContest: List<ConcursoModel>? = null
    lateinit var country: String
    private var trackListener: IncentivesContainerAdapter.TrackEventListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_incentivos_regalo_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) init()
    }

    private fun init() {
        val arguments = arguments ?: return

        currentContest = arguments.getParcelableArrayList(GlobalConstant.CURRENT_CONTEST_KEY)
        country = arguments.getString(GlobalConstant.TRACK_VAR_COUNTRY, "")

        val subTitle = if (Country.CO == country) getString(R.string.incentives_title) else getString(R.string.bonificaciones_title)
        tvwCurrentTitle.text = String.format(getString(R.string.incentives_title_history), subTitle)
        show()
    }

    private fun show() {
        //Current
        if (currentContest != null && currentContest!!.isNotEmpty()) {
            var count = 0

            for (concursoModel in currentContest!!) {
                if (concursoModel.niveles != null && concursoModel.niveles.isNotEmpty()) {
                    count++
                }
            }

            if (count != 0) {
                rvwContest?.visibility = View.VISIBLE
                showCurrentContest(currentContest as ArrayList)
                tvwImageDisclaimer.visibility = View.VISIBLE
            }
        } else {
            //Ninguna Campaña
            tvwCurrentSubtitle.visibility = View.VISIBLE
            tvwCurrentTitle.visibility = View.GONE
            tvwImageDisclaimer.visibility = View.GONE
        }
    }

    private fun showCurrentContest(currentContest: List<ConcursoModel>) {

        currentListAdapter = GiftHistoryAdapter(currentContest, country)
        currentListAdapter.setTrackListener(trackListener!!)
        rvwContest?.adapter = currentListAdapter
        rvwContest?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvwContest?.setHasFixedSize(true)
        rvwContest?.isNestedScrollingEnabled = false
    }

    fun setTrackListener(trackListener: IncentivesContainerAdapter.TrackEventListener) {
        this.trackListener = trackListener
    }

    companion object {

        fun newInstance(): GiftHistoryFragment {
            return GiftHistoryFragment()
        }
    }
}
