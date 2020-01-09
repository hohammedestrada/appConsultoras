package biz.belcorp.consultoras.util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import biz.belcorp.consultoras.common.component.CustomTypefaceSpan;
import biz.belcorp.consultoras.common.model.incentivos.CuponModel;
import biz.belcorp.consultoras.common.model.incentivos.PremioNuevaModel;

public class IncentivesUtil {

    IncentivesUtil() {
        // EMPTY
    }

    public static Spanned getTextDescriptionNew(Context context, String giftCodes, String giftDescription, String giftNumbers) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();

        Typeface tfLight = Typeface.createFromAsset(context.getAssets(), GlobalConstant.LATO_LIGHT_SOURCE);
        Typeface tfBold = Typeface.createFromAsset(context.getAssets(), GlobalConstant.LATO_BOLD_SOURCE);

        String[] codes = getStrings(giftCodes);
        String[] gifts = getStrings(giftDescription);
        Integer[] numbers = getIntegers(giftNumbers);

        if (giftDescription != null) {
            if (null != gifts && gifts.length > 1 && null != numbers && !isAllEqual(numbers)) {
                if (null != codes && codes.length > 1 && codes.length == numbers.length) {

                    int init = 0;
                    int end = 0;
                    int option = 0;

                    Map<Integer, Integer> mp = countOcurrences(numbers);
                    SortedSet<Integer> keys = new TreeSet<>(mp.keySet());

                    for (int key : keys) {
                        option += 1;
                        String value = "OPCIÓN " + option + " (" + codes[init] + "):\n";
                        end += Integer.parseInt(mp.get(key).toString());

                        Spannable spannableBold = Spannable.Factory.getInstance().newSpannable(value);
                        spannableBold.setSpan(new CustomTypefaceSpan(tfBold), 0, value.length(), 0);
                        spannableString.append(spannableBold);

                        for (int i = init; i < end; i++) {
                            String value2 = "_" + gifts[i] + "\n";
                            Spannable spannableLight = Spannable.Factory.getInstance().newSpannable(value2);
                            spannableLight.setSpan(new CustomTypefaceSpan(tfLight), 0, value2.length(), 0);
                            spannableString.append(spannableLight);
                        }

                        if (key != keys.last()) {
                            spannableString.append("\n");
                        }

                        init = end;
                    }
                }
            } else {
                Spannable spannableBold = Spannable.Factory.getInstance().newSpannable(giftDescription);
                spannableBold.setSpan(new CustomTypefaceSpan(tfBold), 0, giftDescription.length(), 0);
                spannableString.append(spannableBold);
            }
        }

        return spannableString;
    }

    private static String[] getStrings(String text) {
        String[] parts;
        if (null != text && !text.isEmpty())
            parts = text.split(Pattern.quote("\n"));
        else parts = null;
        return parts;
    }

    private static Integer[] getIntegers(String text) {
        Integer[] ints;
        if (null != text && !text.isEmpty()) {
            String[] strings = text.split(Pattern.quote("\n"));
            ints = new Integer[strings.length];
            for (int i = 0; i < strings.length; i++) {
                ints[i] = Integer.parseInt(strings[i]);
            }
        } else ints = null;
        return ints;
    }

    private static boolean isAllEqual(Integer[] array) {
        boolean flag = true;
        int first = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] != first) flag = false;
        }
        return flag;
    }

    public Integer[] getOcurrences(Integer[] array) {
        int temp;
        Integer[] ocurrences = new Integer[array.length];
        for (Integer anArray : array) {
            temp = anArray;
            ocurrences[temp]++;
        }

        return ocurrences;
    }

    private static Map<Integer, Integer> countOcurrences(Integer[] array) {

        Map<Integer, Integer> intCounter = new HashMap<>();
        for (Integer num : array) {
            if (intCounter.containsKey(num)) {
                intCounter.put(num, intCounter.get(num) + 1);
            } else {
                intCounter.put(num, 1);
            }
        }

        return intCounter;
    }

    public static Spanned getTextPremioNuevaDescription(List<PremioNuevaModel> list) {
        StringBuilder bld = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            bld.append(list.get(i).getDescripcionProducto());
            if (i < (list.size() - 1)) bld.append("<br/>");
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            return Html.fromHtml(bld.toString(), Html.FROM_HTML_MODE_LEGACY);
        else
            return Html.fromHtml(bld.toString());
    }

    public static Spanned getTextCuponesDescription(List<CuponModel> list) {
        StringBuilder bld = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            bld.append(list.get(i).getDescripcionProducto());
            if (i < (list.size() - 1)) bld.append("<br/>");
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            return Html.fromHtml(bld.toString(), Html.FROM_HTML_MODE_LEGACY);
        else
            return Html.fromHtml(bld.toString());
    }

}
