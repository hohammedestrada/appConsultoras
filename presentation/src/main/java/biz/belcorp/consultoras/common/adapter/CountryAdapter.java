package biz.belcorp.consultoras.common.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.country.CountryModel;
import biz.belcorp.library.util.CountryUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CountryAdapter extends BaseAdapter {

    protected Context context;
    private List<CountryModel> items = new ArrayList<>();

    @SuppressWarnings("unused")
    public CountryAdapter() {
        // EMPTY
    }

    public CountryAdapter(Context context, List<CountryModel> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int pos) {
        return items.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    public static class ViewHolder {
        @BindView(R.id.tvw_country)
        TextView tvwCountryName;
        @BindView(R.id.iv_country)
        ImageView imgFlag;

        private ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View converView, ViewGroup parent) {

        final ViewHolder holder;
        View vi = converView;

        if (converView == null) {
            vi = LayoutInflater.from(context).inflate(R.layout.item_country, null, false);
            holder = new ViewHolder(vi);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        final CountryModel item = items.get(position);

        if (item != null) {
            holder.imgFlag.setImageDrawable(ContextCompat.getDrawable(context, CountryUtil.getFlag(item.getIso())));
            holder.tvwCountryName.setText(item.getName());
        }
        return vi;
    }
}
