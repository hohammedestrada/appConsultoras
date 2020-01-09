package biz.belcorp.consultoras.feature.home.menu.top;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.menu.MenuModel;
import biz.belcorp.consultoras.util.SafeClickListener;
import biz.belcorp.consultoras.util.anotation.MenuCodeTop;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author andres.escobar on 25/04/2017.
 */
public class MenuTopListAdapter extends RecyclerView.Adapter<MenuTopListAdapter.Holder> {

    private List<MenuModel> mList = new ArrayList<>();

    private OnMenuItemSelectedListener innerListener;
    private OnMenuItemSelectedListener onMenuItemSelectedListener;

    private RelativeLayout.LayoutParams layoutParams;
    private Context context;
    private String countryISO = "";
    private int lastPosition = 0;

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivw_item_imagen)
        ImageView ivwItemImagen;
        @BindView(R.id.tvw_item_nombre)
        TextView tvvItemNombre;
        @BindView(R.id.rlt_item_top)
        RelativeLayout rltItemTop;

        public Holder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    private SafeClickListener safeListener = new SafeClickListener() {
        @Override
        public void performClick(View view) {

            int pos = Integer.parseInt(view.getTag().toString());

            onMenuItemSelectedListener.onMenuClick(mList.get(pos), pos);


            if (lastPosition == pos) return;
            if (onMenuItemSelectedListener != null && onMenuItemSelectedListener.shouldMove()) {

                final MenuModel item = mList.get(pos);

                boolean isOrderClicked = !item.getCodigo().equals(MenuCodeTop.ORDERS)
                    && !item.getCodigo().equals(MenuCodeTop.ORDERS_NATIVE)
                    && !item.getCodigo().equals(MenuCodeTop.OFFERS)
                    && !item.getCodigo().equals(MenuCodeTop.CATALOG)
                    && !item.getCodigo().equals(MenuCodeTop.ACCOUNT_STATE)
                    && !item.getCodigo().equals(MenuCodeTop.STOCKOUT)
                    && !item.getCodigo().equals(MenuCodeTop.TRACK_ORDERS);

                //mover selector
                if (isOrderClicked)

                    if (innerListener != null) {
                        innerListener.onMenuItemSelected(mList.get(pos), pos);
                    }

                if (onMenuItemSelectedListener != null && !mList.isEmpty()) {
                    if (isOrderClicked) {

                        if (lastPosition != -1) {
                            mList.get(lastPosition).setState(false);
                            notifyItemChanged(lastPosition);
                        }

                        mList.get(pos).setState(true);
                        notifyItemChanged(pos);

                        lastPosition = pos;
                    }

                    onMenuItemSelectedListener.onMenuItemSelected(mList.get(pos), pos);
                }
            }

        }
    };





    MenuTopListAdapter(Context context) {
        this.context = context;
    }

    private void init() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int itemWidth;
        if (!mList.isEmpty() && mList.size() <= 5) {
            itemWidth = metrics.widthPixels / mList.size();
        } else {
            itemWidth = metrics.widthPixels / 5;
        }

        layoutParams = new RelativeLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_menu_top, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        final MenuModel item = mList.get(position);

        if (null != item) {

            String name = item.getDescripcion() != null ? item.getDescripcion().trim():"";
            holder.rltItemTop.setTag(position);

            if(name.contains(" ")){
                if (name.length() > 13) name = name.replace(" ", "\n");
            } else{
                if (name.length() > 13) name = name.substring(0, 12) + "..";
            }

            holder.tvvItemNombre.setText(name);
            holder.rltItemTop.setLayoutParams(layoutParams);
            holder.rltItemTop.setOnClickListener(safeListener);

            holder.ivwItemImagen.setImageDrawable(ContextCompat.getDrawable(context, getDrawable(item.getCodigo())));
            holder.ivwItemImagen.setSelected(item.isState());
            holder.tvvItemNombre.setSelected(item.isState());
        }
    }



    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setList(List<MenuModel> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
        init();
    }

    void getPosByMenuCode(String menuCode) {

        int pos = 0;
        boolean inMenu = false;

        if (null != mList && !mList.isEmpty())
            for (MenuModel model : mList) {
                if (model.getCodigo().equals(menuCode)) {
                    pos = model.getOrden()>0?(model.getOrden()-1):0;
                    inMenu = true;
                }
            }

        if (inMenu) {
            selectItemByPosition(pos);
        }
        else {
            if (null != mList && !mList.isEmpty() && mList.size() > lastPosition && lastPosition != -1) {
                mList.get(lastPosition).setState(false);
                notifyItemChanged(lastPosition);
                lastPosition = -1;
            }
        }
    }

    @DrawableRes
    private int getDrawable(String codigo) {
        switch (codigo) {
            case MenuCodeTop.HOME:
                return R.drawable.selector_ivw_menu_home_top;
            case MenuCodeTop.CLIENTS:
                return R.drawable.selector_ivw_menu_clients_top;
            case MenuCodeTop.CATALOG:
                return R.drawable.selector_ivw_menu_catalog_top;
            case MenuCodeTop.ORDERS:
                return R.drawable.selector_ivw_menu_orders_top;
            case MenuCodeTop.ORDERS_NATIVE:
                return R.drawable.selector_ivw_menu_orders_top;
            case MenuCodeTop.INCENTIVES:
                return R.drawable.selector_ivw_menu_incentives_top;
            case MenuCodeTop.OFFERS:
                return R.drawable.selector_ivw_menu_offers_top;
            case MenuCodeTop.ACCOUNT_STATE:
                return R.drawable.selector_ivw_menu_account_state;
            case MenuCodeTop.STOCKOUT:
                return R.drawable.selector_ivw_menu_stockout;
            case MenuCodeTop.TRACK_ORDERS:
                return R.drawable.selector_ivw_menu_track_order;
            case MenuCodeTop.TUVOZ:
                return R.drawable.selector_ivw_menu_tuvoz;
            default:
                return R.drawable.selector_ivw_menu_home_top;
        }
    }

    void setInnerListener(OnMenuItemSelectedListener innerListener) {
        this.innerListener = innerListener;
    }

    void setOnMenuItemSelectedListener(OnMenuItemSelectedListener onMenuItemSelectedListener) {
        this.onMenuItemSelectedListener = onMenuItemSelectedListener;
    }

    private void selectItemByPosition(int pos) {
        if (null != mList && !mList.isEmpty()) {
            if (lastPosition != -1) {
                mList.get(lastPosition).setState(false);
                notifyItemChanged(lastPosition);
            }

            mList.get(pos).setState(true);
            notifyItemChanged(pos);

            lastPosition = pos;
        }
    }

    void setCountryISO(String countryISO) {
        this.countryISO = countryISO;
        notifyDataSetChanged();
    }

    public interface OnMenuItemSelectedListener {
        void onMenuItemSelected(MenuModel menuModel, int position);
        void onMenuClick(MenuModel menuModel, int position);
        boolean shouldMove();
    }


}



