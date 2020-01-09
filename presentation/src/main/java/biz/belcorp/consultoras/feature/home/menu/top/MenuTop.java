package biz.belcorp.consultoras.feature.home.menu.top;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.List;

import javax.inject.Inject;

import biz.belcorp.consultoras.ConsultorasApp;
import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.common.model.menu.MenuModel;
import biz.belcorp.consultoras.di.module.ActivityModule;
import biz.belcorp.consultoras.feature.home.di.DaggerHomeComponent;
import biz.belcorp.consultoras.feature.home.di.HomeComponent;
import biz.belcorp.consultoras.feature.home.di.HomeModule;
import biz.belcorp.consultoras.feature.home.menu.MenuPresenter;
import biz.belcorp.consultoras.feature.home.menu.MenuView;
import biz.belcorp.consultoras.util.anotation.MenuCodeTop;
import biz.belcorp.consultoras.util.MenuPosition;

/**
 * @author andres.escobar on 4/05/2017.
 */
public class MenuTop extends LinearLayout implements MenuView, MenuTopListAdapter.OnMenuItemSelectedListener {

    private MenuTopListAdapter menuTopListAdapter;
    private String menuCode = MenuCodeTop.HOME;

    @Inject
    MenuPresenter menuPresenter;

    public MenuTop(Context context) {
        super(context);
        init();
    }

    public MenuTop(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MenuTop(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.view_menu_top, this);

        RecyclerView rvwMenu = findViewById(R.id.rvw_menu);
//        ImageView selector = findViewById(R.id.selector);

        HomeComponent homeComponent = DaggerHomeComponent.builder()
            .appComponent(((ConsultorasApp) (getContext().getApplicationContext())).getAppComponent())
            .activityModule(new ActivityModule((Activity) getContext()))
            .homeModule(new HomeModule())
            .build();

        homeComponent.inject(this);
        menuPresenter.attachView(this);

        rvwMenu.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        menuTopListAdapter = new MenuTopListAdapter(
                getContext()
//              , selector
            );
        menuTopListAdapter.setInnerListener(this);
        rvwMenu.setAdapter(menuTopListAdapter);
        menuPresenter.get(MenuPosition.ARRIBA);
    }

    public void reload() {
        menuCode = MenuCodeTop.HOME;
        menuPresenter.get(MenuPosition.ARRIBA);
    }

    @Override
    public void showMenuList(List<MenuModel> menuList) {
        menuTopListAdapter.setList(menuList);
        selectCurrentItem(menuCode);
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void onVersionError(boolean required, String url) {
        // EMPTY
    }

    /**
     * Agrega un listener al hacer click sobre el elemento de un Menu
     *
     * @param onMenuItemSelectedListener listener menutop
     */
    public void setOnMenuItemSelectedListener(MenuTopListAdapter.OnMenuItemSelectedListener onMenuItemSelectedListener) {
        menuTopListAdapter.setOnMenuItemSelectedListener(onMenuItemSelectedListener);
    }

    @Override
    public void onMenuItemSelected(MenuModel menuModel, int position) {
        // EMPTY
    }

    @Override
    public void onMenuClick(MenuModel menuModel, int position) {

    }

    @Override
    public boolean shouldMove() {
        return false;
    }

    public void selectCurrentItem(String menuCode) {
        menuTopListAdapter.getPosByMenuCode(menuCode);
    }

    public void setCountryISO(String countryISO) {
        menuTopListAdapter.setCountryISO(countryISO);
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

}
