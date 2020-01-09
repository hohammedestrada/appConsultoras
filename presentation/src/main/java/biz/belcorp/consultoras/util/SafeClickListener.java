package biz.belcorp.consultoras.util;

import android.os.SystemClock;
import android.view.View;

public abstract class SafeClickListener implements View.OnClickListener {

    private int defaultInterval;
    private long lastTimeClicked = 0;

    protected SafeClickListener() {
        this(1000);
    }

    private SafeClickListener(int minInterval) {
        this.defaultInterval = minInterval;
    }

    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return;
        }
        lastTimeClicked = SystemClock.elapsedRealtime();
        performClick(v);
    }

    public abstract void performClick(View v);

}
