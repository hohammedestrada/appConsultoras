package biz.belcorp.consultoras.common.component;

import android.content.Context;
import android.util.AttributeSet;

public class CurrencyEditText extends android.support.v7.widget.AppCompatEditText {

    public CurrencyEditText(Context context) {
        super(context);
    }

    public CurrencyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CurrencyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onSelectionChanged(int start, int end) {
        CharSequence text = getText();
        if (text != null && (start != text.length() || end != text.length())) {
            setSelection(text.length(), text.length());
            return;
        }

        super.onSelectionChanged(start, end);
    }
}
