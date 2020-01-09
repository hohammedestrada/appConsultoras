package biz.belcorp.consultoras.feature.home.menu.lateral;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.menu.MenuModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by andres.escobar on 25/04/2017.
 */

public class SubMenuLateralListAdapter extends
    RecyclerView.Adapter<SubMenuLateralListAdapter.Holder> {


    private List<MenuModel> list;
    private MenuModel menuModelParent;
    private int menuPosition;
    private Context context;
    private OnItemSelectedListener onMenuItemSelectedListener;

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivw_item_imagen)
        ImageView ivwItemImagen;
        @BindView(R.id.tvw_item_nombre)
        TextView tvvItemNombre;
        @BindView(R.id.llt_item_lateral)
        LinearLayout lltItemLateral;

        @OnClick(R.id.llt_item_lateral)
        public void onClick() {
            if (onMenuItemSelectedListener != null) {
                onMenuItemSelectedListener.onSubMenuItemSelected(menuModelParent, menuPosition,
                    list.get(getAdapterPosition()), getAdapterPosition());
            }
        }

        public Holder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    public SubMenuLateralListAdapter(Context context, List<MenuModel> list) {
        this.context = context;
        this.list = list;
    }

    SubMenuLateralListAdapter(MenuModel menuModelParent, int menuPosition, Context context) {
        this.menuModelParent = menuModelParent;
        this.menuPosition = menuPosition;
        this.context = context;
        this.list = menuModelParent.getSubMenus();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.item_sub_menu_lateral, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        final MenuModel item = list.get(position);
        holder.tvvItemNombre.setText(StringUtils.capitalize(item.getDescripcion().toLowerCase()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<MenuModel> list) {
        this.list = list;
    }

    void setOnMenuItemSelectedListener(OnItemSelectedListener onMenuItemSelectedListener) {
        if (onMenuItemSelectedListener != null) {
            this.onMenuItemSelectedListener = onMenuItemSelectedListener;
        }
    }

    public interface OnItemSelectedListener {
        void onSubMenuItemSelected(MenuModel menuModel, int menuPosition, MenuModel subMenuModel,
                                   int subMenuPosition);
    }
}
