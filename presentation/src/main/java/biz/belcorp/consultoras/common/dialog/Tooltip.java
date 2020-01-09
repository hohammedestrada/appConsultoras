package biz.belcorp.consultoras.common.dialog;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.library.log.BelcorpLogger;

public class Tooltip {
    private Context context;

    public static final int GRAVITY_TOP = 0;

    static final int GRAVITY_BOTTOM = 1;

    public static final int GRAVITY_LEFT = 2;

    public static final int GRAVITY_RIGHT = 3;

    private Dialog dialog;

    private int[] location;

    private int gravity;

    private View contentView;

    private ImageView ivTriangle;

    private LinearLayout llContent;

    private boolean touchOutsideDismiss;

    private RelativeLayout rlOutsideBackground;

    private int defaultLeftMargin;
    private int defaultRightMargin;

    public Tooltip(Context context) {
        initDialog(context);
    }

    private void initDialog(final Context context) {
        this.context = context;
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View dialogView = layoutInflater.inflate(R.layout.easy_dialog, null);

        ViewTreeObserver viewTreeObserver = dialogView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(() -> relocation(location));

        rlOutsideBackground = dialogView.findViewById(R.id.rlOutsideBackground);
        setTouchOutsideDismiss(true);
        ivTriangle = dialogView.findViewById(R.id.ivTriangle);
        llContent = dialogView.findViewById(R.id.llContent);
        dialog = new Dialog(context, isFullScreen() ? android.R.style.Theme_Translucent_NoTitleBar_Fullscreen : R.style.AppTheme_Traslucent);
        dialog.setContentView(dialogView);
        dialog.setOnDismissListener(dialog -> {
            if (onEasyDialogDismissed != null) {
                onEasyDialogDismissed.onDismissed();
            }
        });
        dialog.setOnShowListener(dialog -> {
            if (onEasyDialogShow != null) {
                onEasyDialogShow.onShow();
            }
        });
        animatorSetForDialogShow = new AnimatorSet();
        animatorSetForDialogDismiss = new AnimatorSet();
        objectAnimatorsForDialogShow = new ArrayList<>();
        objectAnimatorsForDialogDismiss = new ArrayList<>();
        defaultLeftMargin = context.getResources().getDimensionPixelOffset(R.dimen.tooltip_default_left_margin);
        defaultRightMargin = context.getResources().getDimensionPixelOffset(R.dimen.tooltip_default_right_margin);
        ini();
    }

    private final View.OnTouchListener outsideBackgroundListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (touchOutsideDismiss && dialog != null) {
                onDialogDismiss();
            }
            return false;
        }
    };


    public Dialog getDialog() {
        return dialog;
    }


    private void ini() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.tooltipColor, typedValue, true);
        @ColorInt int color = typedValue.data;

        this.setLocation(new int[]{0, 0})
            .setGravity(GRAVITY_BOTTOM)
            .setTouchOutsideDismiss(true)
            .setOutsideColor(Color.TRANSPARENT)
            .setBackgroundColor(color)
            .setMatchParent(true)
            .setMarginLeftAndRight(defaultLeftMargin, defaultRightMargin);
    }


    public Tooltip setLayout(View layout) {
        if (layout != null) {
            this.contentView = layout;
        }
        return this;
    }


    public Tooltip setLayoutResourceId(int layoutResourceId) {
        View view = ((Activity) context).getLayoutInflater().inflate(layoutResourceId, null);
        setLayout(view);
        return this;
    }


    public Tooltip setLocation(int[] location) {
        this.location = location;
        return this;
    }


    private Tooltip setLocationByAttachedView(View attachedView) {
        if (attachedView != null) {
            this.attachedView = attachedView;
            int[] attachedViewLocation = new int[2];
            attachedView.getLocationOnScreen(attachedViewLocation);
            switch (gravity) {
                case GRAVITY_BOTTOM:
                    attachedViewLocation[0] += attachedView.getWidth() / 2;
                    attachedViewLocation[1] += attachedView.getHeight();
                    break;
                case GRAVITY_TOP:
                    attachedViewLocation[0] += attachedView.getWidth() / 2;
                    break;
                case GRAVITY_LEFT:
                    attachedViewLocation[1] += attachedView.getHeight() / 2;
                    break;
                case GRAVITY_RIGHT:
                    attachedViewLocation[0] += attachedView.getWidth();
                    attachedViewLocation[1] += attachedView.getHeight() / 2;
                    break;
                default:
                    break;
            }
            setLocation(attachedViewLocation);
        }
        return this;
    }


    private View attachedView = null;

    public View getAttachedView() {
        return this.attachedView;
    }


    public Tooltip setGravity(int gravity) {
        if (gravity != GRAVITY_BOTTOM && gravity != GRAVITY_TOP && gravity != GRAVITY_LEFT && gravity != GRAVITY_RIGHT) {
            gravity = GRAVITY_BOTTOM;
        }
        this.gravity = gravity;
        switch (this.gravity) {
            case GRAVITY_BOTTOM:
                ivTriangle.setBackgroundResource(R.drawable.triangle_light_bottom);
                break;
            case GRAVITY_TOP:
                ivTriangle.setBackgroundResource(R.drawable.triangle_light_top);
                break;
            case GRAVITY_LEFT:
                ivTriangle.setBackgroundResource(R.drawable.triangle_light_left);
                break;
            case GRAVITY_RIGHT:
                ivTriangle.setBackgroundResource(R.drawable.triangle_light_right);
                break;
            default:
                break;
        }
        llContent.setBackgroundResource(R.drawable.round_corner_bg);
        if (attachedView != null) {
            this.setLocationByAttachedView(attachedView);
        }
        this.setBackgroundColor(backgroundColor);
        return this;
    }


    public Tooltip setMatchParent(boolean matchParent) {
        ViewGroup.LayoutParams layoutParams = llContent.getLayoutParams();
        layoutParams.width = matchParent ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT;
        llContent.setLayoutParams(layoutParams);
        return this;
    }


    private Tooltip setMarginLeftAndRight(int left, int right) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llContent.getLayoutParams();
        layoutParams.setMargins(left, 0, right, 0);
        llContent.setLayoutParams(layoutParams);
        return this;
    }

    public Tooltip setMarginTopAndBottom(int top, int bottom) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llContent.getLayoutParams();
        layoutParams.setMargins(0, top, 0, bottom);
        llContent.setLayoutParams(layoutParams);
        return this;
    }


    public Tooltip setTouchOutsideDismiss(boolean touchOutsideDismiss) {
        this.touchOutsideDismiss = touchOutsideDismiss;
        if (touchOutsideDismiss) {
            rlOutsideBackground.setOnTouchListener(outsideBackgroundListener);
        } else {
            rlOutsideBackground.setOnTouchListener(null);
        }
        return this;
    }


    public Tooltip setOutsideColor(int color) {
        rlOutsideBackground.setBackgroundColor(color);
        return this;
    }

    private int backgroundColor;


    public Tooltip setBackgroundColor(int color) {
        backgroundColor = color;
        LayerDrawable drawableTriangle = (LayerDrawable) ivTriangle.getBackground();
        GradientDrawable shapeTriangle = (GradientDrawable) (((RotateDrawable) drawableTriangle.findDrawableByLayerId(R.id.shape_id)).getDrawable());
        if (shapeTriangle != null) shapeTriangle.setColor(color);

        GradientDrawable drawableRound = (GradientDrawable) llContent.getBackground();
        if (drawableRound != null) {
            drawableRound.setColor(color);
        }
        return this;
    }


    public Tooltip show() {
        if (dialog != null) {
            if (contentView == null) {
                throw new RuntimeException("do you set the layout view via setLayout() or setLayoutResourceId()？");
            }
            if (llContent.getChildCount() > 0) {
                llContent.removeAllViews();
            }
            llContent.addView(contentView);
            dialog.show();
            onDialogShowing();
        }
        return this;
    }


    public View getTipViewInstance() {
        return rlOutsideBackground.findViewById(R.id.rlParentForAnimate);
    }


    private static final int DIRECTION_X = 0;

    private static final int DIRECTION_Y = 1;


    public Tooltip setAnimationTranslationShow(int direction, int duration, float... values) {
        return setAnimationTranslation(true, direction, duration, values);
    }


    public Tooltip setAnimationTranslationDismiss(int direction, int duration, float... values) {
        return setAnimationTranslation(false, direction, duration, values);
    }

    private Tooltip setAnimationTranslation(boolean isShow, int direction, int duration, float... values) {
        if (direction != DIRECTION_X && direction != DIRECTION_Y) {
            direction = DIRECTION_X;
        }
        String propertyName = "";
        switch (direction) {
            case DIRECTION_X:
                propertyName = "translationX";
                break;
            case DIRECTION_Y:
                propertyName = "translationY";
                break;
            default:
                break;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(rlOutsideBackground.findViewById(R.id.rlParentForAnimate), propertyName, values)
            .setDuration(duration);
        if (isShow) {
            objectAnimatorsForDialogShow.add(animator);
        } else {
            objectAnimatorsForDialogDismiss.add(animator);
        }
        return this;
    }


    public Tooltip setAnimationAlphaShow(int duration, float... values) {
        return setAnimationAlpha(true, duration, values);
    }


    public Tooltip setAnimationAlphaDismiss(int duration, float... values) {
        return setAnimationAlpha(false, duration, values);
    }

    private Tooltip setAnimationAlpha(boolean isShow, int duration, float... values) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(rlOutsideBackground.findViewById(R.id.rlParentForAnimate), "alpha", values).setDuration(duration);
        if (isShow) {
            objectAnimatorsForDialogShow.add(animator);
        } else {
            objectAnimatorsForDialogDismiss.add(animator);
        }
        return this;
    }

    private AnimatorSet animatorSetForDialogShow;
    private AnimatorSet animatorSetForDialogDismiss;
    private List<Animator> objectAnimatorsForDialogShow;
    private List<Animator> objectAnimatorsForDialogDismiss;


    private void onDialogShowing() {
        if (animatorSetForDialogShow != null && objectAnimatorsForDialogShow != null && objectAnimatorsForDialogShow.size() > 0) {
            animatorSetForDialogShow.playTogether(objectAnimatorsForDialogShow);
            animatorSetForDialogShow.start();
        }
    }

    @SuppressLint("NewApi")
    private void onDialogDismiss() {
        if (animatorSetForDialogDismiss.isRunning()) {
            return;
        }
        if (animatorSetForDialogDismiss != null && objectAnimatorsForDialogDismiss != null && objectAnimatorsForDialogDismiss.size() > 0) {
            animatorSetForDialogDismiss.playTogether(objectAnimatorsForDialogDismiss);
            animatorSetForDialogDismiss.start();
            animatorSetForDialogDismiss.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (context instanceof Activity) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            if (!((Activity) context).isDestroyed()) {
                                dialog.dismiss();
                            }
                        } else {
                            try {
                                dialog.dismiss();
                            } catch (final IllegalArgumentException e) {
                                BelcorpLogger.d(e);
                            } catch (final Exception e) {
                                BelcorpLogger.d(e);
                            } finally {
                                dialog = null;
                            }
                        }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else {
            dialog.dismiss();
        }
    }


    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            onDialogDismiss();
        }
    }


    private void relocation(int[] location) {
        float statusBarHeight = isFullScreen() ? 0.0f : getStatusBarHeight();

        ivTriangle.setX(location[0] - ivTriangle.getWidth() / 2f);
        ivTriangle.setY(location[1] - ivTriangle.getHeight() / 2f - statusBarHeight);
        switch (gravity) {
            case GRAVITY_BOTTOM:
                llContent.setY(location[1] - ivTriangle.getHeight() / 2f - statusBarHeight + ivTriangle.getHeight());
                break;
            case GRAVITY_TOP:
                llContent.setY(location[1] - llContent.getHeight() - statusBarHeight - ivTriangle.getHeight() / 2f);
                break;
            case GRAVITY_LEFT:
                llContent.setX(location[0] - llContent.getWidth() - ivTriangle.getWidth() / 2f);
                break;
            case GRAVITY_RIGHT:
                llContent.setX(location[0] + ivTriangle.getWidth() / 2f);
                break;
            default:
                break;
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llContent.getLayoutParams();
        switch (gravity) {
            case GRAVITY_BOTTOM:
            case GRAVITY_TOP:
                int triangleCenterX = (int) (ivTriangle.getX() + ivTriangle.getWidth() / 2f);
                int contentWidth = llContent.getWidth();
                int rightMargin = getScreenWidth() - triangleCenterX;
                int leftMargin = getScreenWidth() - rightMargin;
                int availableLeftMargin = leftMargin - layoutParams.leftMargin;
                int availableRightMargin = rightMargin - layoutParams.rightMargin;
                int x;
                if (contentWidth / 2 <= availableLeftMargin && contentWidth / 2 <= availableRightMargin) {
                    x = triangleCenterX - contentWidth / 2;
                } else {
                    if (availableLeftMargin <= availableRightMargin) {
                        x = layoutParams.leftMargin;
                    } else {
                        x = getScreenWidth() - (contentWidth + layoutParams.rightMargin);
                    }
                }
                llContent.setX(x);
                break;
            case GRAVITY_LEFT:
            case GRAVITY_RIGHT:
                int triangleCenterY = (int) (ivTriangle.getY() + ivTriangle.getHeight() / 2f);
                int contentHeight = llContent.getHeight();
                int bottomMargin = getScreenHeight() - triangleCenterY;
                int availableTopMargin = triangleCenterY - layoutParams.topMargin;
                int availableBottomMargin = bottomMargin - layoutParams.bottomMargin;
                int y;
                if (contentHeight / 2 <= availableTopMargin && contentHeight / 2 <= availableBottomMargin) {
                    y = triangleCenterY - contentHeight / 2;
                } else {
                    if (availableTopMargin <= availableBottomMargin) {
                        y = layoutParams.topMargin;
                    } else {
                        y = getScreenHeight() - (contentHeight + layoutParams.topMargin);
                    }
                }
                llContent.setY(y);
                break;
            default:
                break;
        }
    }


    private int getScreenWidth() {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    private int getScreenHeight() {
        int statusBarHeight = isFullScreen() ? 0 : getStatusBarHeight();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels - statusBarHeight;
    }


    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private boolean isFullScreen() {
        int flg = ((Activity) context).getWindow().getAttributes().flags;
        boolean flag = false;
        if ((flg & 1024) == 1024) {
            flag = true;
        }
        return flag;
    }

    public Tooltip setCancelable(boolean cancelable) {
        dialog.setCancelable(cancelable);
        return this;
    }

    private OnEasyDialogDismissed onEasyDialogDismissed;

    public Tooltip setOnEasyDialogDismissed(OnEasyDialogDismissed onEasyDialogDismissed) {
        this.onEasyDialogDismissed = onEasyDialogDismissed;
        return this;
    }


    public interface OnEasyDialogDismissed {
        void onDismissed();
    }

    private OnEasyDialogShow onEasyDialogShow;

    public Tooltip setOnEasyDialogShow(OnEasyDialogShow onEasyDialogShow) {
        this.onEasyDialogShow = onEasyDialogShow;
        return this;
    }


    public interface OnEasyDialogShow {
        void onShow();
    }
}
