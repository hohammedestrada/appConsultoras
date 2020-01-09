package biz.belcorp.consultoras.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

/**
 * Created by andres.escobar on 2/06/2017.
 */

public class ViewUtils {

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to getIncentives resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to getIncentives resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    /**
     * Redimensiona un Drawable
     * @param drawable El drawable a redimensionar
     * @param newWidthInPx el nuevo ancho
     * @param newHeightInPx ek nuevo alto
     * @param context El contexto del view
     * @ el Drawable redimensionado
     */
    public static Drawable changeDrawableSize(Drawable drawable, int newWidthInPx, int newHeightInPx, Context context) {
        // Read your drawable from somewhere
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        // Scale it
        return new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, newWidthInPx, newHeightInPx, true));
    }


    public static String findWordForRightHanded(String str, int offset) { // when you touch ' ', this method returns left word.
        if (str.length() == offset) {
            offset--; // without this code, you will getIncentives exception when touching end of the text
        }

        if (str.charAt(offset) == ' ') {
            offset--;
        }
        int startIndex = offset;
        int endIndex = offset;

        try {
            while (str.charAt(startIndex) != ' ' && str.charAt(startIndex) != '\n') {
                startIndex--;
            }
        } catch (StringIndexOutOfBoundsException e) {
            startIndex = 0;
        }

        try {
            while (str.charAt(endIndex) != ' ' && str.charAt(endIndex) != '\n') {
                endIndex++;
            }
        } catch (StringIndexOutOfBoundsException e) {
            endIndex = str.length();
        }

        // without this code, you will getIncentives 'here!' instead of 'here'
        // if you use only english, just check whether this is alphabet,
        // but 'I' use korean, so i use below algorithm to getIncentives clean word.
        char last = str.charAt(endIndex - 1);
        if (last == ',' || last == '.' ||
            last == '!' || last == '?' ||
            last == ':' || last == ';') {
            endIndex--;
        }

        return str.substring(startIndex, endIndex);
    }


}
