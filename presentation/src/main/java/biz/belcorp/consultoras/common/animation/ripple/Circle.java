package biz.belcorp.consultoras.common.animation.ripple;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 *
 */
class Circle {

    protected int width;
    protected int height;

    Circle() {
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    void onDraw(Canvas canvas, int x, int y, float radiusSize, int color, Paint shapePaint) {
        shapePaint.setColor(color);
        canvas.drawCircle(x, y, radiusSize, shapePaint);
    }
}
