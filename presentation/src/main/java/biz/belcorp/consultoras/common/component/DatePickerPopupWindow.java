package biz.belcorp.consultoras.common.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.util.GlobalConstant;
import biz.belcorp.library.log.BelcorpLogger;

/**
 * PopupWindow for Date Pick
 */
public class DatePickerPopupWindow extends PopupWindow implements OnClickListener {

    private static final int DEFAULT_MIN_YEAR = 1900;
    private static final int DEFAULT_YEAR = 2016;
    private Button cancelBtn;
    private Button confirmBtn;
    private LoopView yearLoopView;
    private LoopView monthLoopView;
    private LoopView dayLoopView;
    private View pickerContainerV;
    private View contentView;//root view

    private int minYear; // min year
    private int maxYear; // max year
    private int yearPos = 0;
    private int monthPos = 0;
    private int dayPos = 0;
    private Context mContext;
    private String textCancel;
    private String textConfirm;
    private int colorCancel;
    private int colorConfirm;
    private int btnTextsize;//text btnTextsize of cancel and confirm button
    private int viewTextSize;
    private int typeDatePicker; // 0 = yyyy/mm/dd, 1 = dd/mm/yyyy, 2 = mm/dd/yyyy
    private boolean showDay;
    private boolean showMonth;
    private boolean showMonthOrdinal;
    private boolean showYear;

    private List<String> yearList = new ArrayList();
    private List<String> monthList = new ArrayList();

    public static class Builder {

        //Required
        private Context context;
        private OnDatePickedListener listener;

        public Builder(Context context, OnDatePickedListener listener) {
            this.context = context;
            this.listener = listener;
        }

        //Option
        private int typeDatePicker = 0;
        private boolean showDay = true;
        private boolean showMonth = true;
        private boolean showMonthOrdinal = true;
        private boolean showYear = true;

        private int minYear = DEFAULT_MIN_YEAR;
        private int maxYear = Calendar.getInstance().get(Calendar.YEAR) + 1;
        private String textCancel = "Cancelar";
        private String textConfirm = "Confirmar";
        private String dateChose = getStrDate();
        private int colorCancel = Color.parseColor("#999999");
        private int colorConfirm = Color.parseColor("#303F9F");
        private int btnTextSize = 13;
        private int viewTextSize = 22;

        public Builder colorCancel(int colorCancel) {
            this.colorCancel = colorCancel;
            return this;
        }

        public Builder colorConfirm(int colorConfirm) {
            this.colorConfirm = colorConfirm;
            return this;
        }

        public Builder viewTextSize(int textSize) {
            this.viewTextSize = textSize;
            return this;
        }

        public DatePickerPopupWindow build() {
            if (minYear > maxYear) {
                throw new IllegalArgumentException();
            }
            return new DatePickerPopupWindow(this);
        }

        public Builder setTypeDatePicker(int typeDatePicker) {
            this.typeDatePicker = typeDatePicker;
            return this;
        }

        public Builder showMonthOrdinal(boolean showMonthOrdinal) {
            this.showMonthOrdinal = showMonthOrdinal;
            return this;
        }

        public Builder showYear(boolean showYear) {
            this.showYear = showYear;
            return this;
        }

        private String getStrDate() {
            SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            return dd.format(new Date());
        }
    }

    private DatePickerPopupWindow(Builder builder) {
        this.minYear = builder.minYear;
        this.maxYear = builder.maxYear;
        this.textCancel = builder.textCancel;
        this.textConfirm = builder.textConfirm;
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.colorCancel = builder.colorCancel;
        this.colorConfirm = builder.colorConfirm;
        this.btnTextsize = builder.btnTextSize;
        this.viewTextSize = builder.viewTextSize;
        this.typeDatePicker = builder.typeDatePicker;
        this.showDay = builder.showDay;
        this.showMonth = builder.showMonth;
        this.showMonthOrdinal = builder.showMonthOrdinal;
        this.showYear = builder.showYear;
        setSelectedDate(builder.dateChose);
        initView();
    }

    private OnDatePickedListener mListener;

    private void initView() {

        Typeface tfRegular = Typeface.createFromAsset(this.mContext.getAssets(), GlobalConstant.LATO_REGULAR_SOURCE);

        int res = R.layout.layout_date_picker_ymd;
        switch (typeDatePicker) {
            case 1:
                res = R.layout.layout_date_picker_dmy;
                break;
            case 2:
                res = R.layout.layout_date_picker_mdy;
                break;
            default:
                break;
        }

        contentView = LayoutInflater.from(mContext).inflate(res, null);
        cancelBtn = contentView.findViewById(R.id.btn_cancel);
        cancelBtn.setTextColor(colorCancel);
        cancelBtn.setTextSize(btnTextsize);
        cancelBtn.setTypeface(tfRegular);
        confirmBtn = contentView.findViewById(R.id.btn_confirm);
        confirmBtn.setTextColor(colorConfirm);
        confirmBtn.setTextSize(btnTextsize);
        confirmBtn.setTypeface(tfRegular);
        yearLoopView = contentView.findViewById(R.id.picker_year);
        monthLoopView = contentView.findViewById(R.id.picker_month);
        dayLoopView = contentView.findViewById(R.id.picker_day);
        pickerContainerV = contentView.findViewById(R.id.container_picker);

        yearLoopView.setTextSize(viewTextSize);
        monthLoopView.setTextSize(viewTextSize);
        dayLoopView.setTextSize(viewTextSize);

        yearLoopView.setLoopListener(item -> {
            yearPos = item;
            initDayPickerView();
        });
        monthLoopView.setLoopListener(item -> {
            monthPos = item;
            initDayPickerView();
        });
        dayLoopView.setLoopListener(item -> dayPos = item);

        if (!showDay) dayLoopView.setVisibility(View.GONE);
        if (!showMonth) monthLoopView.setVisibility(View.GONE);
        if (!showYear) yearLoopView.setVisibility(View.GONE);

        initPickerViews(); // show year and month loop view
        initDayPickerView(); //show day loop view

        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        contentView.setOnClickListener(this);

        if (!TextUtils.isEmpty(textConfirm)) {
            confirmBtn.setText(textConfirm);
        }

        if (!TextUtils.isEmpty(textCancel)) {
            cancelBtn.setText(textCancel);
        }

        setTouchable(true);
        setFocusable(true);
        setAnimationStyle(R.style.FadeInPopWin);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * Init year and month loop view,
     * Let the day loop view be handled separately
     */
    private void initPickerViews() {

        if (showYear) {
            int yearCount = maxYear - minYear;

            for (int i = 0; i < yearCount; i++) {
                yearList.add(format2LenStr(minYear + i));
            }

            yearLoopView.setDataList(yearList);
            yearLoopView.setInitPosition(yearPos);
        }

        if (showMonth) {

            if (showMonthOrdinal) {

                for (int j = 0; j < 12; j++)
                    monthList.add(format2LenStr(j + 1));

            } else {

                String[] months = new DateFormatSymbols().getMonths();
                for (String month : months)
                    monthList.add(month.substring(0, 3) + ".");

            }

            monthLoopView.setDataList(monthList);
            monthLoopView.setInitPosition(monthPos);

        }

    }

    /**
     * Init day items
     */
    private void initDayPickerView() {

        int dayMaxInMonth;
        Calendar calendar = Calendar.getInstance();
        List<String> dayList = new ArrayList<>();

        if (showYear) calendar.set(Calendar.YEAR, minYear + yearPos);
        else calendar.set(Calendar.YEAR, DEFAULT_YEAR);
        calendar.set(Calendar.MONTH, monthPos);

        //getIncentives max day in month
        dayMaxInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < dayMaxInMonth; i++) {
            dayList.add(format2LenStr(i + 1));
        }

        dayLoopView.setDataList(dayList);
        dayLoopView.setInitPosition(dayPos);
    }

    /**
     * set selected date position value when initView.
     *
     * @param dateStr Date String
     */
    private void setSelectedDate(String dateStr) {

        if (!TextUtils.isEmpty(dateStr)) {

            long milliseconds = getLongFromyyyyMMdd(dateStr);
            Calendar calendar = Calendar.getInstance(new Locale("es", "US"));

            if (milliseconds != -1) {

                calendar.setTimeInMillis(milliseconds);
                yearPos = calendar.get(Calendar.YEAR) - minYear;
                monthPos = calendar.get(Calendar.MONTH);
                dayPos = calendar.get(Calendar.DAY_OF_MONTH) - 1;
            }
        }
    }

    /**
     * set selected date position value when initView.
     *
     * @param dateStr Date String
     */
    public void setSelectedDateddMMyyyy(String dateStr) {

        if (!TextUtils.isEmpty(dateStr)) {

            long milliseconds = getLongFromddMMyyyy(dateStr);
            Calendar calendar = Calendar.getInstance(new Locale("es", "US"));

            if (milliseconds != -1) {

                calendar.setTimeInMillis(milliseconds);
                yearPos = calendar.get(Calendar.YEAR) - minYear;
                monthPos = calendar.get(Calendar.MONTH);
                dayPos = calendar.get(Calendar.DAY_OF_MONTH) - 1;

                initDayPickerView();
            }
        }
    }

    /**
     * Show date picker popWindow
     *
     * @param activity Activity Context
     */
    public void showPopWin(Activity activity) {

        if (null != activity) {

            TranslateAnimation trans = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
                0, Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0);

            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM,
                0, 0);
            trans.setDuration(400);
            trans.setInterpolator(new AccelerateDecelerateInterpolator());

            pickerContainerV.startAnimation(trans);
        }
    }

    /**
     * Dismiss date picker popWindow
     */
    private void dismissPopWin() {

        TranslateAnimation trans = new TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
            Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);

        trans.setDuration(400);
        trans.setInterpolator(new AccelerateInterpolator());
        trans.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // EMPTY
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // EMPTY
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }
        });

        pickerContainerV.startAnimation(trans);
    }

    @Override
    public void onClick(View v) {

        if (v == contentView || v == cancelBtn) {

            dismissPopWin();
        } else if (v == confirmBtn) {

            if (null != mListener) {

                int year = minYear + yearPos;
                int month = monthPos + 1;
                int day = dayLoopView.getSelectedItem() + 1;

                String sb = String.format("%1$s/%2$s/%3$s", format2LenStr(day), format2LenStr(month), String.valueOf(2016));

                mListener.onDatePickCompleted(year, month, day, sb);
            }

            dismissPopWin();
        }
    }

    /**
     * getIncentives long from yyyy-MM-dd
     *
     * @param date Date String
     * @return Long Date
     */
    private static long getLongFromyyyyMMdd(String date) {
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date parse = null;
        try {
            parse = mFormat.parse(date);
        } catch (ParseException e) {
            BelcorpLogger.w("getLongFromyyyyMMdd", e);
        }
        if (parse != null) {
            return parse.getTime();
        } else {
            return -1;
        }
    }

    private static long getLongFromddMMyyyy(String date) {
        SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date parse = null;
        try {
            parse = mFormat.parse(date);
        } catch (ParseException e) {
            BelcorpLogger.w("getLongFromddMMyyyy", e);
        }
        if (parse != null) {
            return parse.getTime();
        } else {
            return -1;
        }
    }

    /**
     * Transform int to String with prefix "0" if less than 10
     *
     * @param num Integer
     * @return String value
     */
    private static String format2LenStr(int num) {

        return (num < 10) ? "0" + num : String.valueOf(num);
    }

    public interface OnDatePickedListener {

        /**
         * VinculacionListener when date has been checked
         *
         * @param year     Year integer
         * @param month    Month integer
         * @param day      Day integer
         * @param dateDesc yyyy-MM-dd
         */
        void onDatePickCompleted(int year, int month, int day,
                                 String dateDesc);
    }
}
