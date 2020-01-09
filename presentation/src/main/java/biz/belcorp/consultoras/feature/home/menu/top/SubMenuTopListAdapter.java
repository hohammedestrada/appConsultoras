package biz.belcorp.consultoras.feature.home.menu.top;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.menu.MenuModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by andres.escobar on 25/04/2017.
 */

public class SubMenuTopListAdapter extends RecyclerView.Adapter<SubMenuTopListAdapter.Holder> {


    private List<MenuModel> mList;
    private RelativeLayout.LayoutParams layoutParams;
    private Context context;
    private MenuModel menuModel;
    private int menuPosition;

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvw_item_nombre)
        TextView tvvItemNombre;
        @BindView(R.id.rlt_item_top)
        RelativeLayout rltItemTop;

        @OnClick(R.id.rlt_item_top)
        public void onClick() {
            if(onMenuItemSelectedListener != null) {
                onMenuItemSelectedListener.onMenuItemSelected(menuModel, menuPosition,
                    mList.get(getAdapterPosition()),getAdapterPosition());
            }
        }

        public Holder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    private OnItemSelectedListener onMenuItemSelectedListener;

    public SubMenuTopListAdapter(MenuModel menuModel, int menuPosition, Context context) {
        this.menuModel = menuModel;
        this.menuPosition = menuPosition;
        this.context = context;
        mList = menuModel.getSubMenus();
    }

    public void init() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int divisor;
        if (mList.size() <= 5) {
            divisor = metrics.widthPixels / mList.size();
        } else {
            divisor = metrics.widthPixels / 5;
        }
        layoutParams = new RelativeLayout.LayoutParams(divisor, metrics.heightPixels / 10);
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sub_menu_top, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        final MenuModel item = mList.get(position);

        String name = item.getDescripcion().trim();

        if (name.length() > 10) name = name.substring(0, 9) + "..";
        holder.tvvItemNombre.setText(name);
        holder.rltItemTop.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public List<MenuModel> getList() {
        return mList;
    }

    public void setList(List<MenuModel> list) {
        this.mList = list;
        init();
    }

    public void setOnSubMenuItemSelectedListener(OnItemSelectedListener onMenuItemSelectedListener) {
        this.onMenuItemSelectedListener = onMenuItemSelectedListener;
    }

    public interface OnItemSelectedListener {
        void onMenuItemSelected(MenuModel menuModel, int menuPosition, MenuModel subMenuModel,
                                int subMenuPosition);
    }

}
