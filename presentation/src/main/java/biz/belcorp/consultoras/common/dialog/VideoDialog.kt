package biz.belcorp.consultoras.common.dialog

import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.databinding.DataBindingUtil.setContentView
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import biz.belcorp.consultoras.R
import kotlinx.android.synthetic.main.dialog_video.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class VideoDialog(context: Context,
                  private val urlVideo: String,
                  private val listener: VideoListener?) : Dialog(context, R.style.full_screen_dialog) {

    private lateinit var dialogo: VideoDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.dialog_video)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialogo = this
        initVideo()
    }

    private fun initVideo() {

        GlobalScope.launch(Dispatchers.Default) {
            mPreview.setVideoURI(Uri.parse(urlVideo))

            mPreview.setOnPreparedListener {
                viewLoading.visibility = View.GONE
                mPreview.start()
            }

            mPreview.setOnCompletionListener {
                val endAnim = buildAnimation(context, R.anim.renew_fade_out)
                containerroot.startAnimation(endAnim)
            }
        }

        mPreview.setOnClickListener {

            listener?.onVideoEnded()
            mPreview.stopPlayback()
            dismiss()
        }
    }


    private fun buildAnimation(context: Context, anim: Int): Animation {
        val animation = AnimationUtils.loadAnimation(context, anim)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                // Empty
            }

            override fun onAnimationEnd(animation: Animation?) {
                listener?.onVideoEnded()
                dismiss()
            }

            override fun onAnimationStart(animation: Animation?) {
                //nada
            }
        })
        return animation
    }


    class Builder(private var context: Context) {

        private var urlVideo = ""
        private var listen: VideoListener? = null

        fun withurlVideo(urlVideo: String) = apply { this.urlVideo = urlVideo }

        fun withListener(listener: VideoListener?) = apply { this.listen = listener }

        fun show() = VideoDialog(context, urlVideo, listen).show()
    }

    interface VideoListener {
        fun onStaring()
        fun onVideoEnded()
    }
}



