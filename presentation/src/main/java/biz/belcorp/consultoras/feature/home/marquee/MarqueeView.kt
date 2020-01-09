package biz.belcorp.consultoras.feature.home.marquee

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SnapHelper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.marquee_view.view.*
import java.util.*
import android.support.v7.widget.RecyclerView
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.common.tracking.Tracker


class MarqueeView : FrameLayout, MarqueeSnapHelper.OnSelectItemChange{


    @JvmOverloads constructor(
            context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr)

    var currentPosition : Int =  0
    var slideTimer: Timer? = null
    var marqueeAdapter: MarqueeAdapter? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.marquee_view, this, true)

        rvMarquee.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper : SnapHelper = MarqueeSnapHelper(this)

        snapHelper.attachToRecyclerView(rvMarquee)

        btnLeft.setOnClickListener{
            startTimer()
            marqueeAdapter?.let{
                if(currentPosition - 1 < 0){
                    currentPosition = it.getRealItemsCount() - 1
                    (rvMarquee?.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(currentPosition, 0)
                }else{
                    currentPosition--
                    (rvMarquee?.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(currentPosition, 0)

                }
            }



        }
        btnRight.setOnClickListener{
                startTimer()
                marqueeAdapter?.let{
                if(currentPosition + 1 >= it.getRealItemsCount()){
                    currentPosition = 0
                    (rvMarquee?.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(currentPosition, 0)
                }else{
                    currentPosition++
                    (rvMarquee?.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(currentPosition, 0)

                }


            }
        }


    }



    override fun onSelectedItemChange(position: Int) {
        currentPosition = position
    }

    fun setAdapter(adapter: MarqueeAdapter?){
        marqueeAdapter = adapter
        rvMarquee.adapter = marqueeAdapter
        marqueeAdapter?.let {
            if(it.getRealItemsCount() <= 1){
                btnLeft.visibility = View.GONE
                btnRight.visibility = View.GONE
                slideTimer?.cancel()
                rvMarquee.layoutManager =  object : LinearLayoutManager(context){
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }

            }else{
                rvMarquee.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val linearLayoutManager = (rvMarquee.layoutManager as LinearLayoutManager)
                        val firstItemVisible = linearLayoutManager.findFirstVisibleItemPosition()
                        val items = adapter?.getRealItemsCount()?:0

                        if (firstItemVisible != 1 && firstItemVisible % items == 1) {
                            currentPosition = 1
                            linearLayoutManager.scrollToPosition(currentPosition)

                        }

                        if (firstItemVisible != RecyclerView.NO_POSITION && firstItemVisible == marqueeAdapter?.itemCount ?:0 % items - 1) {
                            currentPosition = items + 1
                            linearLayoutManager.scrollToPositionWithOffset(currentPosition, 0)

                        }
                    }
                })
            }
        }


    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startTimer()
    }

    fun setOnMarqueeItemClickListener(onClickListener: MarqueeAdapter.OnClickListener ){
        rvMarquee.adapter?.let {
            (it as MarqueeAdapter).onClickListener = onClickListener
        }
    }

    fun startTimer(){
        stopTimer()
        slideTimer = Timer()
        slideTimer?.schedule(SlideTimerTask(),6000,6000)
    }

    fun stopTimer(){
        if(slideTimer != null){
            slideTimer?.cancel()
            slideTimer?.purge()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopTimer()

    }

    inner class SlideTimerTask : TimerTask(){
        override fun run() {

            rvMarquee.post {
                if(currentPosition + 1 > marqueeAdapter?.getRealItemsCount()?:0){
                    currentPosition = 0
                    rvMarquee?.scrollToPosition(currentPosition)
                }else{
                    currentPosition++
                    rvMarquee?.smoothScrollToPosition(currentPosition)

                    if(currentPosition == marqueeAdapter?.getRealItemsCount()?:0){
                        currentPosition = 0
                        rvMarquee?.scrollToPosition(currentPosition)
                    }

                }

            }


        }

    }


}

