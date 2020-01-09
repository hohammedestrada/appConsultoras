package biz.belcorp.consultoras.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import biz.belcorp.consultoras.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.dialog_incentive_gift.*

class IncentiveGiftDialog(context: Context,
                          private val urlImage: String,
                          private val name: String) : Dialog(context, R.style.full_screen_dialog) {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_incentive_gift)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        Glide.with(context).load(urlImage).into(giftImage)
        nameText.text = name
        closeImage.setOnClickListener {
            cancel()
        }
    }

    class Builder(private var context: Context) {

        private var urlImage = ""
        private var name = ""

        fun withImage(urlImage: String) = apply { this.urlImage = urlImage }

        fun withName(name: String) = apply { this.name = name }

        fun show() = IncentiveGiftDialog(context, urlImage, name).show()
    }
}
