package biz.belcorp.consultoras.common.component;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import biz.belcorp.library.log.BelcorpLogger;


public class LeftTextInputLayout extends TextInputLayout {
    private Object collapsingTextHelper;
    private Rect bounds;
    private Method recalculateMethod;

    public static final String FIELDS_1 = "mCollapsingTextHelper";
    public static final String FIELDS_2 = "mCollapsedBounds";
    public static final String FIELDS_3 = "recalculate";

    public LeftTextInputLayout(Context context) {
        this(context, null);
    }

    public LeftTextInputLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        adjustBounds();
    }

    private void init() {
        try {
            Field cthField = TextInputLayout.class.getDeclaredField(FIELDS_1);
            cthField.setAccessible(true);
            collapsingTextHelper = cthField.get(this);

            Field boundsField = collapsingTextHelper.getClass().getDeclaredField(FIELDS_2);
            boundsField.setAccessible(true);
            bounds = (Rect) boundsField.get(collapsingTextHelper);

            recalculateMethod = collapsingTextHelper.getClass().getDeclaredMethod(FIELDS_3);
        }
        catch (NoSuchFieldException e) {
            collapsingTextHelper = null;
            BelcorpLogger.w("NoSuchFieldException", e.getMessage());
        } catch (IllegalAccessException e){
            bounds = null;
            BelcorpLogger.w("IllegalAccessException", e.getMessage());
        } catch (NoSuchMethodException e){
            recalculateMethod = null;
            BelcorpLogger.w("NoSuchMethodException", e.getMessage());
        }
    }

    private void adjustBounds() {
        if (collapsingTextHelper == null) {
            return;
        }

        try {
            bounds.left = getEditText().getLeft() + getEditText().getPaddingLeft();
            recalculateMethod.invoke(collapsingTextHelper);
        }
        catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException | NullPointerException e) {
            BelcorpLogger.w("adjustBounds", e.getMessage());
        }
    }
}
