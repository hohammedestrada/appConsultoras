package biz.belcorp.consultoras.util;

import android.app.Activity;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

/**
 *
 */

public class StatusBarUtil {

    public static void setColorBackground(Activity act, int color){

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(act.getBaseContext(), color));
        }

    }
}
