package biz.belcorp.consultoras.feature.home.menu.lateral;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.menu.MenuModel;

/**
 *
 */
class MenuLateralGridAdapter extends RecyclerView.Adapter<MenuLateralGridAdapter.GridHolder> {

    private List<MenuModel> items = new ArrayList<>();
    private MenuLateralListAdapter.OnItemSelectedListener onMenuItemSelectedListener;

    final class GridHolder extends RecyclerView.ViewHolder {

        private ImageView ivwItem;
        private TextView tvwItem;

        private GridHolder(View itemView) {
            super(itemView);

            ivwItem = itemView.findViewById(R.id.ivw_item);
            tvwItem = itemView.findViewById(R.id.tvw_item);

            itemView.setOnClickListener(v -> {
                if (onMenuItemSelectedListener != null
                    && onMenuItemSelectedListener.shouldMove()) {
                    onMenuItemSelectedListener.onMenuItemSelected(items.get(getAdapterPosition()),
                        getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public GridHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_grid,
            parent, false);
        return new GridHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridHolder holder, int position) {
        MenuModel model = items.get(position);

        if (model.getDrawable() != null)
            holder.ivwItem.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(),
                model.getDrawable()));

        holder.tvwItem.setText(model.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void setList(List<MenuModel> list) {
        items.clear();
        items.addAll(list);
    }

    void setOnMenuItemSelectedListener(MenuLateralListAdapter.OnItemSelectedListener onMenuItemSelectedListener) {
        this.onMenuItemSelectedListener = onMenuItemSelectedListener;
    }
}
