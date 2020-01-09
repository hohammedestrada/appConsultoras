package biz.belcorp.consultoras.common.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.adapter.CountryAdapter;
import biz.belcorp.consultoras.common.model.country.CountryModel;
import butterknife.ButterKnife;

/**
 * Clase Dialog de Lista General con titulo
 */
@SuppressLint("ValidFragment")
public class ListDialog extends DialogFragment {

    private String stringTitle = "";
    private int resTitle = 0;

    private BaseAdapter adapter;
    private ListDialogListener listener;

    private static boolean shown = false;
    private List<CountryModel> countries;

    public ListDialog() {
        // EMPTY
    }

    public ListDialog setTitle(@StringRes int resId) {
        resTitle = resId;
        return this;
    }

    public ListDialog setTitle(@NonNull String value) {
        stringTitle = value;
        return this;
    }

    public ListDialog setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public ListDialog setListViewListener(ListDialogListener listener) {
        this.listener = listener;
        return this;
    }

    public ListDialog setCountries(List<CountryModel> countries) {
        this.countries = countries;
        return this;
    }

    /**********************************************************/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_list_custom, container, false);

        ImageView ivwClose = ButterKnife.findById(view, R.id.ivw_close);
        TextView tvwTitle = ButterKnife.findById(view, R.id.tvw_title);
        ListView lvCollection = ButterKnife.findById(view, R.id.lv_collection);

        if (TextUtils.isEmpty(stringTitle) && resTitle == 0)
            stringTitle = "ERROR";

        if (null == adapter)
            adapter = new CountryAdapter(getContext(), countries);

        if (TextUtils.isEmpty(stringTitle) && resTitle != 0)
            tvwTitle.setText(resTitle);
        else
            tvwTitle.setText(stringTitle);

        ivwClose.setOnClickListener(v -> dismiss());

        lvCollection.setAdapter(adapter);
        lvCollection.setOnItemClickListener((parent, view1, position, id) -> {
            dismiss();
            if (null != listener)
                listener.selectedItem(position);
        });

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (shown) return;

        super.show(manager, tag);
        shown = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        shown = false;
        super.onDismiss(dialog);
    }

    /**********************************************************/

    public interface ListDialogListener {
        void selectedItem(int position);
    }
}
