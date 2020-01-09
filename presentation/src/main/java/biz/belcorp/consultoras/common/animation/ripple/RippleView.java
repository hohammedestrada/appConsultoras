package biz.belcorp.consultoras.common.animation.ripple;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.Deque;
import java.util.LinkedList;

import biz.belcorp.consultoras.R;
import biz.belcorp.library.log.BelcorpLogger;

/**
 *
 */
public class RippleView extends View {

    private int defaultRippleColor;
    private int defaultRippleToColor;
    private int defaultRippleStrokeWidth;

    private static final int DEFAULT_RIPPLE_DURATION = 2000;
    private static final float DEFAULT_RIPPLE_INTERVAL_FACTOR = 1F;

    /**
     * The list of {@link CircleEntry} which is rendered in {@link #render(Float)}
     */
    private Deque<CircleEntry> shapeRippleEntries;

    /**
     * The actual animator for the ripples, used in {@link #render(Float)}
     */
    private ValueAnimator rippleValueAnimator;

    /**
     * The {@link Interpolator} of the {@link #rippleValueAnimator}, by default it is {@link LinearInterpolator}
     */
    private Interpolator rippleInterpolator;

    /**
     * The renderer of shape ripples which is drawn in the {@link Circle#onDraw(Canvas, int, int, float, int, Paint)}
     */
    private Circle rippleShape;

    /**
     * The default paint for the ripple
     */
    private Paint shapePaint;

    /**
     * This flag will handle that it was stopped by the user
     */
    private boolean isStopped;
    private boolean isFromDebt = true;

    private int minViewWidth;
    private int minViewHeight;

    private int viewWidth;
    private int viewHeight;
    private int maxRippleRadius;
    private int rippleCount;
    private float rippleInterval;
    private float lastMultiplierValue = 0f;

    public RippleView(Context context) {
        super(context);
        init();
    }

    public RippleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {

        defaultRippleColor = ContextCompat.getColor(getContext(), R.color.ripple_color);
        defaultRippleToColor = ContextCompat.getColor(getContext(), R.color.ripple_to_color);
        defaultRippleStrokeWidth = getResources().getDimensionPixelSize(R.dimen.ripple_stroke_width);

        minViewWidth = getResources().getDimensionPixelSize(R.dimen.ripple_min_width);
        minViewHeight = getResources().getDimensionPixelSize(R.dimen.ripple_min_height);

        shapePaint = new Paint();
        shapePaint.setAntiAlias(true);
        shapePaint.setDither(true);
        shapePaint.setStyle(Paint.Style.FILL);

        shapeRippleEntries = new LinkedList<>();

        rippleShape = new Circle();
        rippleInterpolator = new LinearInterpolator();

        start(DEFAULT_RIPPLE_DURATION);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (CircleEntry shapeRippleEntry : shapeRippleEntries) {

            if (shapeRippleEntry.isRender()) {
                shapeRippleEntry.getBaseShape().onDraw(canvas, shapeRippleEntry.getX(),
                    shapeRippleEntry.getY(),
                    shapeRippleEntry.getRadiusSize(),
                    shapeRippleEntry.getChangingColorValue(),
                    shapePaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // Get the measure base of the measure spec
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        viewHeight = isFromDebt && viewHeight < minViewHeight ? minViewHeight : viewHeight;
        viewWidth = isFromDebt && viewWidth < minViewWidth ? minViewWidth : viewWidth;

        initializeEntries(rippleShape);

        rippleShape.setWidth(viewWidth);
        rippleShape.setHeight(viewHeight);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        stop();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        stop();
    }

    /**
     * This method will initialize the list of {@link CircleEntry} with
     * initial position, color, index, and multiplier value.)
     *
     * @param shapeRipple the renderer of shape ripples
     */
    private void initializeEntries(Circle shapeRipple) {
        // Sets the stroke width of the ripple
        shapePaint.setStrokeWidth(defaultRippleStrokeWidth);

        // we remove all the shape ripples entries
        shapeRippleEntries.clear();

        // the ripple radius based on the controlAnimation or y
        maxRippleRadius = (Math.min(viewWidth, viewHeight) / 2 - (defaultRippleStrokeWidth / 2));

        // Calculate the max number of ripples
        rippleCount = rippleCount > 0 ? rippleCount : maxRippleRadius / defaultRippleStrokeWidth;

        rippleInterval = DEFAULT_RIPPLE_INTERVAL_FACTOR / rippleCount;

        for (int i = 0; i < rippleCount; i++) {
            CircleEntry shapeRippleEntry = new CircleEntry(shapeRipple);
            shapeRippleEntry.setX(viewWidth / 2);
            shapeRippleEntry.setY(viewHeight / 2);
            shapeRippleEntry.setMultiplierValue(-(rippleInterval * (float) i));
            shapeRippleEntry.setOriginalColorValue(defaultRippleColor);

            shapeRippleEntries.add(shapeRippleEntry);
        }
    }

    /**
     * Start the {@link #rippleValueAnimator} with specified duration for each ripple.
     *
     * @param millis the duration in milliseconds
     */
    void start(int millis) {
        // Do not render when entries are empty
        if (shapeRippleEntries.isEmpty()) {
            BelcorpLogger.a("There are no ripple entries that was created!!");
            return;
        }
        isStopped = false;

        // Do a ripple value renderer
        rippleValueAnimator = ValueAnimator.ofFloat(0f, 1f);
        rippleValueAnimator.setDuration(millis);
        rippleValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        rippleValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rippleValueAnimator.setInterpolator(rippleInterpolator);
        rippleValueAnimator.addUpdateListener(animation -> render((Float) animation.getAnimatedValue()));

        rippleValueAnimator.start();
    }

    /**
     * This is the main renderer for the list of ripple, we always check that the first ripple is already
     * finished.
     * <p>
     * When the ripple is finished it is {@link CircleEntry#reset()} and move to the end of the list to be reused all over again
     * to prevent creating a new instance of it.
     * <p>
     * Each ripple will be configured to be either rendered or not rendered to the view to prevent extra rendering process.
     *
     * @param multiplierValue the current multiplier value of the {@link #rippleValueAnimator}
     */
    private void render(Float multiplierValue) {

        // Do not render when entries are empty
        if (shapeRippleEntries.isEmpty()) {
            BelcorpLogger.a("There are no ripple entries that was created!!");
            return;
        }

        CircleEntry firstEntry = shapeRippleEntries.peekFirst();

        // Calculate the multiplier value of the first entry
        float firstEntryMultiplierValue = firstEntry.getMultiplierValue() + Math.max(multiplierValue - lastMultiplierValue, 0);

        // Check if the first entry is done the ripple (happens when the ripple reaches to end)
        if (firstEntryMultiplierValue >= 1.0f) {

            // Remove and relocate the first entry to the last entry
            CircleEntry removedEntry = shapeRippleEntries.pop();
            removedEntry.reset();
            removedEntry.setOriginalColorValue(defaultRippleColor);
            shapeRippleEntries.addLast(removedEntry);

            // Get the new first entry of the list
            firstEntry = shapeRippleEntries.peekFirst();

            // Calculate the new multiplier value of the first entry of the list
            firstEntryMultiplierValue = firstEntry.getMultiplierValue() + Math.max(multiplierValue - lastMultiplierValue, 0);

            firstEntry.setX(viewWidth / 2);
            firstEntry.setY(viewHeight / 2);
        }

        int index = 0;
        for (CircleEntry shapeRippleEntry : shapeRippleEntries) {

            // calculate the shape multiplier by index
            float currentEntryMultiplier = firstEntryMultiplierValue - rippleInterval * index;

            // Check if we render the current ripple in the list
            // We render when the multiplier value is >= 0
            if (currentEntryMultiplier >= 0) {
                shapeRippleEntry.setRender(true);
            } else {
                // We continue to the next items
                // since we know that we do not
                // need the calculations below
                shapeRippleEntry.setRender(false);
                continue;
            }

            // We already calculated the multiplier value of the first entry of the list
            if (index == 0) {
                shapeRippleEntry.setMultiplierValue(firstEntryMultiplierValue);
            } else {
                shapeRippleEntry.setMultiplierValue(currentEntryMultiplier);
            }

            // calculate the color if we enabled the color transition
            shapeRippleEntry.setChangingColorValue(evaluateTransitionColor(currentEntryMultiplier, shapeRippleEntry.getOriginalColorValue(), defaultRippleToColor));

            // calculate the current ripple size
            shapeRippleEntry.setRadiusSize(maxRippleRadius * currentEntryMultiplier);

            index += 1;
        }

        // save the last multiplier value
        lastMultiplierValue = multiplierValue;

        // we draw the shapes
        invalidate();
    }

    /**
     * Stop the {@link #rippleValueAnimator} and clears the {@link #shapeRippleEntries}
     */
    void stop() {

        if (rippleValueAnimator != null) {
            rippleValueAnimator.cancel();
            rippleValueAnimator.end();
            rippleValueAnimator.removeAllUpdateListeners();
            rippleValueAnimator.removeAllListeners();
            rippleValueAnimator = null;
        }

        if (shapeRippleEntries != null) {
            shapeRippleEntries.clear();
            invalidate();
        }
    }

    /**
     * Starts the ripple by stopping the current {@link #rippleValueAnimator} using the {@link #stop()}
     * then initializing ticket entries using the {@link #initializeEntries(Circle)}
     * and lastly starting the {@link #rippleValueAnimator} using {@link #start(int)}
     */
    public void startRipple() {
        //stop the animation from previous before starting it again
        stop();
        initializeEntries(rippleShape);
        start(DEFAULT_RIPPLE_DURATION);

        this.isStopped = false;
    }

    /**
     * Stops the ripple see {@link #stop()} for more details
     */
    public void stopRipple() {
        stop();

        this.isStopped = true;
    }

    /**
     * Check the playing state of the ripple
     */
    public boolean isPlaying(){
        return !isStopped;
    }

    /**
     * This restarts the ripple or continue where it was left off, this is mostly used
     * for {@link RippleLifeCycle}.
     */
    protected void restartRipple() {
        if (this.isStopped) {
            BelcorpLogger.a("Restarted from stopped ripple!!");
            return;
        }

        startRipple();
    }

    private int evaluateTransitionColor(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) |
            ((startR + (int) (fraction * (endR - startR))) << 16) |
            ((startG + (int) (fraction * (endG - startG))) << 8) |
            (startB + (int) (fraction * (endB - startB)));
    }

    public void setFromDebt(boolean fromDebt) {
        isFromDebt = fromDebt;
    }

    public void initLifeCycleCallback(Fragment fragment) {
        RippleLifeCycle lifeCycleManager = new RippleLifeCycle(this);
        lifeCycleManager.attachListener(fragment);
    }

    public void setDefaultRippleColor(int defaultRippleColor) {
        this.defaultRippleColor = defaultRippleColor;
    }

    public void setDefaultRippleToColor(int defaultRippleToColor) {
        this.defaultRippleToColor = defaultRippleToColor;
    }
}
