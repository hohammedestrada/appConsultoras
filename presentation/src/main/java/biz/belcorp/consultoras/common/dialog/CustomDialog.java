package biz.belcorp.consultoras.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import biz.belcorp.consultoras.R;

public class CustomDialog extends Dialog{

    private ImageView ivwIcon;
    private TextView tvwCounter;
    private TextView tvwTitle;
    private TextView tvwMessage;
    private Button btnAction1;
    private Button btnAction2;
    private ImageView ivwClose;

    public CustomDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_custom);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        ivwIcon = findViewById(R.id.ivw_icon);
        tvwCounter = findViewById(R.id.tvw_counter);
        tvwTitle = findViewById(R.id.tvw_title);
        tvwMessage = findViewById(R.id.tvw_message);
        btnAction1 = findViewById(R.id.btn_action_1);
        btnAction2 = findViewById(R.id.btn_action_2);
        ivwClose = findViewById(R.id.ivw_close);
        ivwClose.setOnClickListener(v -> dismiss());
    }

    public void setTitle(String title){
        tvwTitle.setText(title);
    }

    public void setMessage(String message){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            tvwMessage.setText(Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvwMessage.setText(Html.fromHtml(message));
        }
    }


    public void setMessage(int message){
        tvwMessage.setText(message);
    }

    public void setCounterText(String text){
        tvwCounter.setText(text);
    }

    public void setButtonAction1Text(String text){
        btnAction1.setText(text);
    }

    public void setButtonAction1Text(int text){
        btnAction1.setText(text);
    }

    public void setButtonAction2Text(String text){
        btnAction2.setText(text);
    }

    public void setButtonAction2Text(int text){
        btnAction2.setText(text);
    }

    public void setIconDrawable(int iconDrawable){
        Glide.with(getContext()).load(iconDrawable).into(ivwIcon);
    }

    public void setIconDrawable(Drawable iconDrawable){
        Glide.with(getContext()).load(iconDrawable).into(ivwIcon);
    }

    public void setButtonAction1Listener(View.OnClickListener btnAcion1Listener){
        btnAction1.setOnClickListener(btnAcion1Listener);
    }

    public void setButtonAction2Listener(View.OnClickListener btnAcion1Listener){
        btnAction2.setOnClickListener(btnAcion1Listener);
    }

    public void setCloseListener(View.OnClickListener btnAcion1Listener){
        ivwClose.setOnClickListener(btnAcion1Listener);
    }

    @Override
    public void show() {
        super.show();


        if(tvwTitle.getText().equals("")){
            tvwTitle.setVisibility(View.GONE);
        }

        if(tvwMessage.getText().equals("")){
            tvwMessage.setVisibility(View.GONE);
        }

        if(btnAction1.getText().equals("")){
            btnAction1.setVisibility(View.GONE);
        }
        if(btnAction2.getText().equals("")){
            btnAction2.setVisibility(View.GONE);
        }
        if(ivwIcon.getDrawable() == null){
            ivwIcon.setVisibility(View.GONE);
        }

        if(!tvwCounter.getText().toString().equals("")){
            tvwCounter.setVisibility(View.VISIBLE);
        }
    }
}
