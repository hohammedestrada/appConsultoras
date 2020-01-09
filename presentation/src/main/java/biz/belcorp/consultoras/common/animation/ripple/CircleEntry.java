package biz.belcorp.consultoras.common.animation.ripple;

import android.graphics.Color;

/**
 *
 */
class CircleEntry {

    /**
     * The shape renderer of the ripple
     */
    private Circle baseShape;

    /**
     * Flag for when the ripple is ready to be rendered
     * to the view
     */
    private boolean isRender;

    /**
     * The current radius size of the ripple
     */
    private float radiusSize;

    /**
     * The current multiplier value of the ripple
     */
    private float multiplierValue;

    /**
     * The X position of the ripple, defaulted to the middle of the view
     */
    private int x;

    /**
     * The Y position of the ripple, defaulted to the middle of the view
     */
    private int y;

    /**
     * The original color value which is only changed when view is created or
     * the ripple list is re configured
     */
    private int originalColorValue;

    /**
     * The changeable color value which is used when color transition,
     * on measure to the view, when render process happens
     */
    private int changingColorValue;

    @SuppressWarnings("unused")
    public CircleEntry() {
        // EMPTY
    }

    CircleEntry(Circle baseShape) {
        this.baseShape = baseShape;
    }

    Circle getBaseShape() {
        return baseShape;
    }

    float getRadiusSize() {
        return radiusSize;
    }

    void setRadiusSize(float radiusSize) {
        this.radiusSize = radiusSize;
    }

    int getOriginalColorValue() {
        return originalColorValue;
    }

    void setOriginalColorValue(int originalColorValue) {
        this.originalColorValue = originalColorValue;
        setChangingColorValue(originalColorValue);
    }

    float getMultiplierValue() {
        return multiplierValue;
    }

    void setMultiplierValue(float multiplierValue) {
        this.multiplierValue = multiplierValue;
    }

    boolean isRender() {
        return isRender;
    }

    void setRender(boolean render) {
        isRender = render;
    }

    int getX() {
        return x;
    }

    void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    int getChangingColorValue() {
        return changingColorValue;
    }

    void setChangingColorValue(int changingColorValue) {
        this.changingColorValue = changingColorValue;
    }

    /**
     * Reset all data of this shape ripple entries
     */
    void reset() {
        isRender = false;
        multiplierValue = -1;
        radiusSize = 0;
        originalColorValue = Color.TRANSPARENT;
        changingColorValue = Color.TRANSPARENT;
    }
}
