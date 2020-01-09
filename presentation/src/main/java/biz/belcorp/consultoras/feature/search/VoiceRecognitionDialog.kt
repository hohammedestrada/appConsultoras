package biz.belcorp.consultoras.feature.search

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import biz.belcorp.consultoras.R
import kotlinx.android.synthetic.main.dialog_voice_recognition.*

class VoiceRecognitionDialog (context: Context,
                              private var title: String?,
                              private var subtitle: String?,
                              private var listener: VoiceRecognitionDialog.VoiceRecognitionListener?)
    : Dialog(context, R.style.voice_recognition_dialog), RecognitionListener {

    private lateinit var speech: SpeechRecognizer
    private lateinit var intent: Intent
    private var canActivate: Boolean = false
    private var retryActive: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_voice_recognition)

        this.setCanceledOnTouchOutside(true)
        this.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        showNormalState(true, title, subtitle)
        this.setOnCancelListener {
            listener?.onRecognitionCancel(retryActive)
        }

        rvw_animation.setOnClickListener {
            if(canActivate) {
                retryActive = true
                listener?.onRecognitionRetry()
                initRecognition()
            }
        }
        lnl_close.setOnClickListener {
            listener?.onRecognitionCancel(retryActive)
            dismiss()
        }

        initRecognition()

    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(listener)
        speech.destroy()
    }

    override fun onStop() {
        super.onStop()
        speech.destroy()
    }


    // Private Methods
    private fun initRecognition(){

        intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context?.packageName)

        speech = SpeechRecognizer.createSpeechRecognizer(context)
        speech.setRecognitionListener(this)
        speech.startListening(intent)

    }

    private fun showNormalState(state: Boolean, title: String?, subtitle: String?){
        if(state){
            title?.let { txt_action.text = it; txt_action.visibility = View.VISIBLE }
            subtitle?.let { txt_try.text = it; txt_try.visibility = View.VISIBLE }
            rvw_animation.isClickable = false
        }else{
            txt_try.visibility = View.GONE
            txt_action.text = context.resources.getString(R.string.voice_recognition_error)
            rvw_animation.isClickable = true
        }
    }

    private fun endVoiceEffect(){
        ripple_view.scaleX = 0f
        ripple_view.scaleY = 0f
    }

    // Recognition Listener
    override fun onBeginningOfSpeech() {}
    override fun onBufferReceived(arg0: ByteArray) {}
    override fun onEvent(arg0: Int, arg1: Bundle) {}
    override fun onPartialResults(partialResults: Bundle) {}
    override fun onRmsChanged(rmsdB: Float) {
        ripple_view.scaleX = (rmsdB * 0.025f) + 0.9f
        ripple_view.scaleY = (rmsdB * 0.025f) + 0.9f
    }
    override fun onReadyForSpeech(params: Bundle) {
        canActivate = false
        showNormalState(true, title, subtitle)
    }
    override fun onEndOfSpeech() {
        canActivate = true
        rvw_animation.isClickable = true
        endVoiceEffect()
    }

    override fun onError(arg0: Int) {
        canActivate = true
        showNormalState(false, title, subtitle)
        endVoiceEffect()
        speech.stopListening()
        speech.destroy()
    }

    override fun onResults(results: Bundle) {
        speech.stopListening()
        val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        matches?.let { listener?.onRecognitionSuccess( it[0] ) }
        dismiss()
    }


    // Listener
    interface VoiceRecognitionListener{
        fun onRecognitionSuccess(found: String)
        fun onRecognitionCancel(retryActive: Boolean)
        fun onRecognitionRetry()
        fun onRecognitionDismiss()
    }

}
