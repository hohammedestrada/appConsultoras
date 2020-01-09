package biz.belcorp.consultoras.feature.home.menu.lateral;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import biz.belcorp.consultoras.common.model.menu.MenuModel;

public class MenuGrid extends RecyclerView {

    private MenuLateralGridAdapter menuLateralGridAdapter;
    private GridLayoutManager gridLayoutManager;

    public MenuGrid(Context context) {
        super(context);
        init();
    }

    public MenuGrid(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MenuGrid(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        menuLateralGridAdapter = new MenuLateralGridAdapter();
        setLayoutManager(gridLayoutManager = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false));
        setAdapter(menuLateralGridAdapter);
    }

    public void setOnMenuItemSelectedListener(MenuLateralListAdapter.OnItemSelectedListener onMenuItemSelectedListener) {
        menuLateralGridAdapter.setOnMenuItemSelectedListener(onMenuItemSelectedListener);
    }

    public void setList(MenuModel group) {
        if(group.getSubMenus() != null){
            if(group.getSubMenus().size() != gridLayoutManager.getSpanCount()){
                gridLayoutManager.setSpanCount(Math.max(3, group.getSubMenus().size()));
            }
        }
        menuLateralGridAdapter.setList(group.getSubMenus());
        menuLateralGridAdapter.notifyDataSetChanged();
    }
}
