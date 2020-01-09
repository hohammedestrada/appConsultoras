package biz.belcorp.consultoras.feature.home.menu.lateral;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import biz.belcorp.consultoras.common.model.menu.MenuModel;

/**
 *
 */
public class MenuList extends RecyclerView {

    private MenuLateralListAdapter menuLateralGroupAdapter;

    public MenuList(Context context) {
        super(context);
        init();
    }

    public MenuList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MenuList(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        menuLateralGroupAdapter = new MenuLateralListAdapter(getContext());

        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
            false));
        setAdapter(menuLateralGroupAdapter);
    }

    public void setOnMenuItemSelectedListener(
        MenuLateralListAdapter.OnItemSelectedListener onMenuItemSelectedListener) {
        menuLateralGroupAdapter.setOnMenuItemSelectedListener(onMenuItemSelectedListener);
    }

    public void setOnSubMenuItemSelectedListener(
        SubMenuLateralListAdapter.OnItemSelectedListener onSubMenuItemSelectedListener) {
        menuLateralGroupAdapter.setOnSubMenuItemSelectedListener(onSubMenuItemSelectedListener);
    }

    public void setList(MenuModel group) {
        menuLateralGroupAdapter.setList(group.getSubMenus());
        menuLateralGroupAdapter.notifyDataSetChanged();
    }

    public void showMenuOption(String code, boolean status, String title){
        menuLateralGroupAdapter.showMenuOption(code, status, title);
    }
}
