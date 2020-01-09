package biz.belcorp.consultoras.common.dialog

import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import biz.belcorp.consultoras.R
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.dialog_intrigue.*
import kotlin.math.roundToInt

class IntrigueDialog(context: Context,
                     private val urlImage: String,
                     private val onIntrigueEnd : OnIntrigueEnd?) : Dialog(context, R.style.full_screen_dialog) {

    private var scrollAnim : ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_intrigue)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        scrollView.setOnTouchListener { _, _ -> true }
        Glide.with(context).asDrawable().load(urlImage)
            .apply(RequestOptions.noTransformation().error(R.drawable.ic_offline_intrigue)
                .placeholder(R.drawable.ic_offline_intrigue).priority(Priority.HIGH))
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?, model: Any?, target: Target<Drawable>?,
                    isFirstResource: Boolean): Boolean {
                    dismiss()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable, model: Any?, target: Target<Drawable>?, dataSource: DataSource?,
                    isFirstResource: Boolean): Boolean {
                    initOnClick()
                    startEnterAnimation((resource.intrinsicWidth * 1.5).roundToInt())
                    finishDialog()
                    return false
                }
            }).into(intrigueImage)
    }

    private fun initOnClick(){
        intrigueImageLinear.setOnClickListener {
            endDialog()
        }
    }

    private fun startEnterAnimation(drawableWidth: Int) {
        scrollAnim = ValueAnimator.ofInt(0, drawableWidth)
        scrollAnim?.duration = 2300
        scrollAnim?.startDelay = 100
        scrollAnim?.addUpdateListener { scrollView.scrollTo(scrollAnim?.animatedValue as Int, 0) }
        scrollAnim?.start()

        val revealTextAnim = ValueAnimator.ofInt(0,
            context.resources.getDimension(R.dimen.size_large_text_intrigue).toInt())
        revealTextAnim.duration = 520
        revealTextAnim.startDelay = 120
        revealTextAnim.addUpdateListener {
            val params = comingSoonLinear.layoutParams
            params.height = revealTextAnim.animatedValue as Int
            comingSoonLinear.layoutParams = params
        }
        revealTextAnim.start()

        val appearMessageLeftAnimation = AnimationUtils.loadAnimation(context, R.anim.intrigue_appear_left)
        messageLinear.visibility = View.VISIBLE
        appearMessageLeftAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                // Empty
            }

            override fun onAnimationEnd(animation: Animation?) {
                startFlickerAnimation()
                startFadeInLogoAnimation()
            }

            override fun onAnimationStart(animation: Animation?) {
                // Empty
            }

        })
        messageLinear.startAnimation(appearMessageLeftAnimation)
    }

    private fun startFlickerAnimation() {
        val anim = AlphaAnimation(1f, 0f)
        anim.duration = 70
        anim.repeatCount = Animation.INFINITE
        anim.repeatMode = Animation.REVERSE
        anim.startOffset = 200
        anim.fillAfter = true
        messageLinear.startAnimation(anim)
    }

    private fun startFadeInLogoAnimation() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.intrigue_fade_in)
        logoImage.visibility = View.VISIBLE
        logoImage.startAnimation(animation)
    }

    private fun finishDialog() {
        Handler().postDelayed({
            endDialog()
        }, 6000)
    }

    private fun endDialog(){
        scrollAnim?.cancel()
        messageLinear.animation?.cancel()
        onIntrigueEnd?.onAnimationEnd() ?: endAnimationDialog()
    }

    private fun endAnimationDialog(){
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.duration = 1000
        intrigueContent.animation = fadeOut
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                // Empty
            }

            override fun onAnimationEnd(animation: Animation?) {
                dismiss()
            }

            override fun onAnimationStart(animation: Animation?) {
                // Empty
            }
        })
        intrigueContent.startAnimation(fadeOut)
    }

    class Builder(private var context: Context) {

        private var urlImage = ""
        private var onIntrigueEnd : OnIntrigueEnd? = null

        fun withImage(urlImage: String): Builder {
            this.urlImage = urlImage
            return this
        }

        fun onEnd(onIntrigueEnd:OnIntrigueEnd) : Builder{
            this.onIntrigueEnd = onIntrigueEnd
            return this
        }

        fun show() = IntrigueDialog(context, urlImage, onIntrigueEnd).show()
    }

    interface OnIntrigueEnd {
        fun onAnimationEnd()
    }
}
