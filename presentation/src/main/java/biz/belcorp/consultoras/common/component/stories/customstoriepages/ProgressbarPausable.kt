package biz.belcorp.consultoras.common.component.stories.customstoriepages

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import biz.belcorp.consultoras.R
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.progress_pausable_storie.view.*


class ProgressbarPausable : LinearLayout {

    private var duration = DEFAULT_PROGRESS_DURATION.toLong()
    private var mTimerRunning: Boolean = false
    private var mCountDownTimer: biz.belcorp.consultoras.common.component.stories.customstoriepages.CountDownTimer? = null//CountDownTimer? = null
    var storieCallback: CallbackProgress? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        addView(LayoutInflater.from(context).inflate(R.layout.progress_pausable_storie, this, false))

        val paramsLayout = frameProgressStorie.layoutParams as LinearLayout.LayoutParams
        paramsLayout.marginEnd = 1
        paramsLayout.marginStart = 1
        paramsLayout.weight = 1.0f
        this.layoutParams = paramsLayout
    }

    fun start() {

        storieBar.progress = 0
        starTimer()
    }

    private fun starTimer() {
        mCountDownTimer = object : biz.belcorp.consultoras.common.component.stories.customstoriepages.CountDownTimer(duration, 30) {
            override fun onTick(millisUntilFinished: Long) {
                    updateBar()
            }

            override fun onFinish() {
                mTimerRunning = false
                storieBar.progress = storieBar.max
                if (storieCallback != null) storieCallback?.onFinishProgreBar()

            }
        }.start()
    }

    fun pauseCountDownBar() {
        mCountDownTimer?.pause()
        mTimerRunning = false
    }

    fun setDuration(duration: Long) {
        this.duration = duration
        storieBar.max = duration.toInt()

    }

    fun updateBar() {
        storieBar.incrementProgressBy(30)
    }

    fun continuarCountDown() {
        mCountDownTimer?.resume()
        mTimerRunning = true

    }

    fun setFullProgress() {
        mCountDownTimer?.cancel()
        storieBar.progress = storieBar.max
        mTimerRunning = false
    }

    fun setEmptyProgress() {
        mCountDownTimer?.cancel()
        storieBar.progress = 0
        mTimerRunning = false
    }

    fun clearBar() {
        mCountDownTimer?.cancel()
        storieBar.progress = 0
        mTimerRunning = false
    }


    interface CallbackProgress {
        fun onFinishProgreBar()
    }

    companion object {
        private const val DEFAULT_PROGRESS_DURATION = 2000
    }
}
