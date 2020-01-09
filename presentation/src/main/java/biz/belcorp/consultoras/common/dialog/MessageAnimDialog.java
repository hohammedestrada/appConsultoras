package biz.belcorp.consultoras.common.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.animation.ripple.RippleView;
import biz.belcorp.consultoras.util.FestivityAnimationUtil;
import biz.belcorp.library.log.BelcorpLogger;
import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class MessageAnimDialog extends DialogFragment {

    @BindView(R.id.rlt_dialog)
    RelativeLayout rltDialog;
    @BindView(R.id.ivw_close)
    ImageView ivwClose;
    @BindView(R.id.ivw_image)
    ImageView ivwIcon;
    @BindView(R.id.tvw_title)
    TextView tvwTitle;
    @BindView(R.id.tvw_message)
    TextView tvwMessage;
    @BindView(R.id.llt_buttons)
    LinearLayout lltButtons;
    @BindView(R.id.btn_aceptar)
    Button btnAceptar;
    @BindView(R.id.btn_cancelar)
    Button btnCancelar;
    @BindView(R.id.rlt_holiday)
    RelativeLayout rltHoliday;
    @BindView(R.id.btnExtra1)
    TextView btnExtra1;
    @BindView(R.id.separator)
    View separator;
    @BindView(R.id.btnExtra2)
    TextView btnExtra2;
    @BindView(R.id.ripple_view)
    RippleView rippleView;


    private String stringTitle = "";
    private int resTitle = 0;

    private String stringMessage = "";
    private int resMessage = 0;

    private Spanned messageHtml;

    private String stringAceptar = "";
    private int resAceptar = 0;

    private String stringCancelar = "";
    private int resCancelar = 0;

    private int iconResource = -1;
    private int iconIsVector = 0;
    private Drawable iconDrawable;

    private Boolean isClose = true;
    private Boolean isIcon = false;
    private Boolean isButtons = true;
    private Boolean isCancel = false;
    private Boolean isAnimated = false;
    private Boolean isRippleViewed = false;

    private MessageDialogListener listener;

    /**********************************************************/

    public MessageAnimDialog() {
        // EMPTY
    }

    public MessageAnimDialog setStringTitle(@StringRes int resId) {
        this.resTitle = resId;
        return this;
    }

    public MessageAnimDialog setResTitle(@NonNull String value) {
        this.stringTitle = value;
        return this;
    }

    public MessageAnimDialog setStringMessage(@StringRes int resId) {
        this.resMessage = resId;
        return this;
    }

    public MessageAnimDialog setResMessage(@NonNull String value) {
        this.stringMessage = value;
        return this;
    }

    public MessageAnimDialog setMessageHtml(@NonNull String value) {

        Spanned textHtml;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textHtml = Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY);
        } else {
            textHtml = Html.fromHtml(value);
        }

        this.messageHtml = textHtml;
        return this;
    }

    public MessageAnimDialog setStringAceptar(@StringRes int resId) {
        this.resAceptar = resId;
        return this;
    }

    public MessageAnimDialog setResAceptar(@NonNull String value) {
        this.stringAceptar = value;
        return this;
    }

    public MessageAnimDialog setStringCancelar(@StringRes int resId) {
        this.resCancelar = resId;
        return this;
    }

    public MessageAnimDialog setResCancelar(@NonNull String value) {
        this.stringCancelar = value;
        return this;
    }

    public MessageAnimDialog showRipple(@NonNull Boolean show) {
        this.isRippleViewed = show;
        return this;
    }

    String buttonExtra1Text = null;
    String buttonExtra2Text = null;
    View.OnClickListener buttonExtra1Listener = null;
    View.OnClickListener buttonExtra2Listener = null;


    public MessageAnimDialog enableExtraButton1(String text, View.OnClickListener listener) {
        if (text == null) {
            throw new NullPointerException("El texto no puede ser nulo");
        }

        buttonExtra1Text = text;
        buttonExtra1Listener = listener;

        return this;
    }

    public MessageAnimDialog enableExtraButton2(String text, View.OnClickListener listener) {
        if (text == null) {
            throw new NullPointerException("El texto no puede ser nulo");
        }

        buttonExtra2Text = text;
        buttonExtra2Listener = listener;

        return this;
    }

    public MessageAnimDialog setIcon(Drawable value, int iconIsVector) {
        this.iconDrawable = value;
        this.iconIsVector = iconIsVector;
        return this;
    }

    public MessageAnimDialog setIcon(@DrawableRes int resId, int iconIsVector) {
        this.iconResource = resId;
        this.iconIsVector = iconIsVector;
        return this;
    }

    public MessageAnimDialog showClose(boolean value) {
        this.isClose = value;
        return this;
    }

    public MessageAnimDialog showIcon(boolean value) {
        this.isIcon = value;
        return this;
    }

    public MessageAnimDialog showButtons(boolean value) {
        this.isButtons = value;
        return this;
    }

    public MessageAnimDialog showCancel(boolean value) {
        this.isCancel = value;
        return this;
    }

    public MessageAnimDialog setAnimated(boolean value) {
        this.isAnimated = value;
        return this;
    }


    public MessageAnimDialog setListener(MessageDialogListener listener) {
        this.listener = listener;
        return this;
    }

    /**********************************************************/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        //setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme);
    }

    @Override
    public int getTheme() {
        return R.style.FullScreenDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_message_anim, container, false);

        ButterKnife.bind(this, view);

        if (!isClose) {
            ivwClose.setVisibility(View.GONE);
        }

        if (!isIcon) {
            ivwIcon.setVisibility(View.GONE);
        } else {
            if (null != iconDrawable) {
                ivwIcon.setImageDrawable(iconDrawable);
            } else if (-1 != iconResource) {
                if (1 != iconIsVector)
                    ivwIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), iconResource));
                else
                    ivwIcon.setImageDrawable(VectorDrawableCompat.create(getContext().getResources()
                        , iconResource, null));
            }
        }

        if (TextUtils.isEmpty(stringTitle) && resTitle == 0)
            stringTitle = "Error";

        if (TextUtils.isEmpty(stringMessage) && resMessage == 0 && TextUtils.isEmpty(messageHtml))
            stringMessage = "No hay conexión a internet";

        if (TextUtils.isEmpty(stringTitle) && resTitle != 0)
            tvwTitle.setText(resTitle);
        else
            tvwTitle.setText(stringTitle);

        if (TextUtils.isEmpty(stringMessage) && resMessage != 0)
            tvwMessage.setText(resMessage);
        else if (!TextUtils.isEmpty(stringMessage))
            tvwMessage.setText(stringMessage);
        else tvwMessage.setText(messageHtml);

        if (!isButtons) {
            lltButtons.setVisibility(View.GONE);
        } else {
            if (TextUtils.isEmpty(stringAceptar) && resAceptar != 0) {
                btnAceptar.setText(resAceptar);
            } else if (!TextUtils.isEmpty(stringAceptar))
                btnAceptar.setText(stringAceptar);

            if (!isCancel) {
                btnCancelar.setVisibility(View.GONE);
            } else if (TextUtils.isEmpty(stringCancelar) && resCancelar != 0) {
                btnCancelar.setText(resCancelar);
            } else if (!TextUtils.isEmpty(stringCancelar))
                btnCancelar.setText(stringCancelar);
        }

        ivwClose.setOnClickListener(v -> getDialog().dismiss());

        btnAceptar.setOnClickListener(v -> {
            getDialog().dismiss();
            if (listener != null)
                listener.aceptar();
        });

        btnCancelar.setOnClickListener(v -> {
            getDialog().dismiss();
            if (listener != null)
                listener.cancelar();
        });

        if (isAnimated) {
            initAnimation();
        }

        btnExtra1.setVisibility(View.GONE);
        separator.setVisibility(View.GONE);
        btnExtra2.setVisibility(View.GONE);

        if (buttonExtra1Text != null) {
            btnExtra1.setVisibility(View.VISIBLE);
            btnExtra1.setText(buttonExtra1Text);
            btnExtra1.setPaintFlags(btnExtra1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            btnExtra1.setOnClickListener(buttonExtra1Listener);
        }

        if (buttonExtra2Text != null) {
            btnExtra2.setVisibility(View.VISIBLE);
            btnExtra2.setText(buttonExtra2Text);
            btnExtra2.setPaintFlags(btnExtra1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            btnExtra2.setOnClickListener(buttonExtra2Listener);
            if (btnExtra1.getVisibility() == View.VISIBLE) {
                separator.setVisibility(View.VISIBLE);
            }
        }

        if (isRippleViewed) {
            rippleView.setVisibility(View.VISIBLE);
        } else {
            rippleView.setVisibility(View.GONE);
        }

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commit();
        } catch (Exception e) {
            BelcorpLogger.w("BottomDialog", e.getMessage());
        }
    }

    private void initAnimation() {
        rltHoliday.postDelayed(() -> {
            FestivityAnimationUtil.getCommonConfetti(
                ContextCompat.getColor(getContext(), R.color.dorado),
                ContextCompat.getColor(getContext(), R.color.primary),
                getResources(), rltDialog);

            new Handler().postDelayed(() -> {
                rltHoliday.animate().alpha(0.0f).setDuration(1000)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            rltHoliday.setVisibility(View.GONE);

                            rltHoliday.clearAnimation();
                            rltHoliday.clearDisappearingChildren();

                        }
                    }).start();

            }, 7000);

        }, 300);
    }

    /**********************************************************/

    public interface MessageDialogListener {
        void aceptar();

        void cancelar();
    }
}
