package biz.belcorp.consultoras.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.res.ResourcesCompat
import android.text.Html
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import biz.belcorp.consultoras.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.dialog_renew.*
import org.jetbrains.anko.runOnUiThread

class RenewDialog(context: Context,
                  private val urlImageLogo: String,
                  private val urlImage: String,
                  private val message: String,
                  private val onRenewEnd: OnRenewEnd?) : Dialog(context, R.style.full_screen_dialog) {

    companion object{
        private const val DELAY_TIME = 2500L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_renew)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        var firstImgLoaded = false
        var secondImgLoaded = false

        Glide.with(context).load(urlImage).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?,
                                      isFirstResource: Boolean): Boolean {
                dismiss()
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                         dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                if(secondImgLoaded){
                    initDialog()
                }
                firstImgLoaded = true
                return false
            }

        }).into(backgroundImage)

        Glide.with(context).load(urlImageLogo).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?,
                                      isFirstResource: Boolean): Boolean {
                dismiss()
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                         dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                logoImage.visibility = View.GONE
                if(firstImgLoaded){
                    initDialog()
                }
                secondImgLoaded = true
                return false
            }

        }).into(logoImage)

    }

    private fun initDialog() {
        messageText.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT) else @Suppress("DEPRECATION") Html.fromHtml(message)
        messageText.typeface = ResourcesCompat.getFont(context, R.font.freight_big_book)
        val zoomStartAnimation = AnimationUtils.loadAnimation(context, R.anim.renew_zoom_start)
        backgroundImage.startAnimation(zoomStartAnimation)
        val translateToTopAnimation = buildAnimation(context, R.anim.renew_translate_to_top, ::startAnimationLogo)
        revealView.startAnimation(translateToTopAnimation)
    }

    private fun startAnimationLogo() {
        logoImage.visibility = View.VISIBLE
        val appearCutAnimation = buildAnimation(context, R.anim.renew_appear_cut, ::alphaEnterAnimation)
        logoImage.startAnimation(appearCutAnimation)
    }

    private fun alphaEnterAnimation() {
        messageText.visibility = View.VISIBLE
        val alpha = buildAnimation(context, R.anim.renew_fade_in, ::startEndAnimation)
        messageText.startAnimation(alpha)

        logoImage.startAnimation(AnimationUtils.loadAnimation(context, R.anim.renew_rotate))
    }

    private fun startEndAnimation() {

        onRenewEnd?.let {
            Handler().postDelayed({
                context.runOnUiThread {
                    it.onAnimationEnd()
                }
            }, DELAY_TIME)
        } ?: endDialog()
    }

    private fun endDialog() {
        val endAnim = buildAnimation(context, R.anim.renew_fade_out, ::dismiss)
        backgroundImage.startAnimation(endAnim)

        val endTextAnim = AnimationUtils.loadAnimation(context, R.anim.renew_content_end)
        messageText.startAnimation(endTextAnim)

        val endLogoAnim = AnimationUtils.loadAnimation(context, R.anim.renew_logo_end)
        logoImage.startAnimation(endLogoAnim)
    }

    private fun buildAnimation(context: Context, anim: Int, onEndAnimation: () -> Unit?): Animation {
        val animation = AnimationUtils.loadAnimation(context, anim)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                // Empty
            }

            override fun onAnimationEnd(animation: Animation?) {
                onEndAnimation()
            }

            override fun onAnimationStart(animation: Animation?) {
                // Empty
            }
        })
        return animation
    }

    class Builder(private var context: Context) {

        private var urlImage = ""
        private var urlImageLogo = ""
        private var message = ""
        private var onRenewEnd: OnRenewEnd? = null

        fun withImage(urlImage: String) = apply { this.urlImage = urlImage }

        fun withImageLogo(urlImageLogo: String) = apply { this.urlImageLogo = urlImageLogo }

        fun message(message: String) = apply { this.message = message }

        fun onEnd(onRenewEnd: OnRenewEnd) = apply { this.onRenewEnd = onRenewEnd }

        fun show() = RenewDialog(context, urlImageLogo,urlImage, message, onRenewEnd).show()
    }

    interface OnRenewEnd {
        fun onAnimationEnd()
    }
}
