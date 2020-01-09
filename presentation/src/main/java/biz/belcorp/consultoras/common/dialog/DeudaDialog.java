package biz.belcorp.consultoras.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.component.CurrencyEditText;

public class DeudaDialog extends Dialog{

    private TextView tvwMonto;
    private TextView tvwCurrency;
    private CurrencyEditText edtAmount;
    private Button btnAction1;
    private ImageView ivwClose;

    public DeudaDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_deuda);
        getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        tvwMonto = findViewById(R.id.tvw_monto);
        tvwCurrency = findViewById(R.id.tvw_currency);
        edtAmount =  findViewById(R.id.edt_amount);
        btnAction1 = findViewById(R.id.btn_action_1);
        ivwClose = findViewById(R.id.ivw_close);
        ivwClose.setOnClickListener(v -> dismiss());
    }


    public void setButtonAction1Text(String text){
        btnAction1.setText(text);
    }

    public void setButtonAction1Text(int text){
        btnAction1.setText(text);
    }


    public void setButtonAction1Listener(View.OnClickListener btnAcion1Listener){
        btnAction1.setOnClickListener(btnAcion1Listener);
    }

    public void setCloseListener(View.OnClickListener btnAcion1Listener){
        ivwClose.setOnClickListener(btnAcion1Listener);
    }

    @Override
    public void show() {
        super.show();

        if(btnAction1.getText().equals("")){
            btnAction1.setVisibility(View.GONE);
        }

    }

    public CurrencyEditText getEdtAmount() {
        return edtAmount;
    }

    public TextView getTvwMonto() {
        return tvwMonto;
    }

    public TextView getTvwCurrency() {
        return tvwCurrency;
    }
}
