package biz.belcorp.consultoras.feature.campaigninformation

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.model.campaignInformation.InfoCampaignDetailModel
import kotlinx.android.synthetic.main.fragment_informacion_campania_pasadas.*

class CampaignInformationFragmentFuture : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_informacion_campania_futuras, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
    }

    private fun init(){
        arguments?.let {

            if(it.containsKey(CampaignInformationFragment.PARAM_NAME_CAMPANIAS_SIGUIENTE)){
                val list = it.getParcelableArrayList<InfoCampaignDetailModel>(CampaignInformationFragment.PARAM_NAME_CAMPANIAS_SIGUIENTE)
                list?.let{listValidate->
                    if(!listValidate.isEmpty()){
                        rcvw.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                        rcvw.adapter = CampaignInformationPastAdapter(listValidate)
                        showData()
                    }else{
                        showNoData()
                    }
                }

            }else{
                showNoData()
            }
        }
    }

    private fun showData(){
        rlvwSinData.visibility = View.GONE
        rcvw.visibility = View.VISIBLE
    }

    private fun showNoData(){
        rlvwSinData.visibility = View.VISIBLE
        rcvw.visibility = View.GONE
    }
}
