package biz.belcorp.consultoras.common.component;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class EmptyCurrencyTextWatcher implements TextWatcher {

    private DecimalFormat decimalFormat;
    private EditText editText;
    private String current = "";
    private boolean showDecimals;

    public EmptyCurrencyTextWatcher(EditText editText, DecimalFormat decimalFormat, int decimals) {
        this.editText = editText;
        this.decimalFormat = decimalFormat;
        this.showDecimals = decimals != 0;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // EMPTY
    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {

        String value = s.toString();

        if (value.length() == 0 || value.equals(current)) {
            current = "";
            return;
        }

        editText.removeTextChangedListener(this);

        if (value.equals(".") || value.equals(",") || value.equals("-")) {
            restoreEditText();
            return;
        }

        boolean decimalValue = value.contains(showDecimals ? "." : ",");
        boolean zeroDecimalValue = decimalValue && value.endsWith("0");

        String cleanString = value.replaceAll("[,]", "");

        if (showDecimals && cleanString.matches("^\\d+(\\.\\d{3,})")) {
            restoreEditText();
            return;
        }

        if (!showDecimals && !cleanString.isEmpty())
            cleanString = cleanString.replaceAll("[.]", "");

        BigDecimal parsed = new BigDecimal(cleanString);
        String formatted = decimalFormat.format(parsed);

        if (decimalValue && showDecimals && !formatted.contains(".")) formatted += ".";
        if (zeroDecimalValue && showDecimals) formatted += "0";

        current = formatted;
        editText.setText(formatted);

        int l = editText.getText().length();

        editText.setSelection(l <= formatted.length() ? l : formatted.length());
        editText.addTextChangedListener(this);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // EMPTY
    }

    private void restoreEditText() {
        editText.setText(current);
        editText.addTextChangedListener(this);
    }
}
