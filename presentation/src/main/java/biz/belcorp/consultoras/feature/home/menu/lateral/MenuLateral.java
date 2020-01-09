package biz.belcorp.consultoras.feature.home.menu.lateral;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import biz.belcorp.consultoras.util.anotation.MenuCode;
import biz.belcorp.consultoras.util.MenuPosition;

/**
 * @author andres.escobar on 4/05/2017.
 */
public class MenuLateral extends LinearLayout implements MenuView {

    @Inject
    MenuPresenter menuPresenter;
    private TextView tvwInfoApp;
    private MenuList menuList;
    private MenuGrid menuGrid;

    public MenuLateral(Context context) {
        super(context);
        init();
    }

    public MenuLateral(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MenuLateral(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        HomeComponent homeComponent = DaggerHomeComponent.builder()
            .appComponent(((ConsultorasApp) (getContext().getApplicationContext())).getAppComponent())
            .activityModule(new ActivityModule((Activity) getContext()))
            .homeModule(new HomeModule())
            .build();
        homeComponent.inject(this);
        menuPresenter.attachView(this);

        addView(LayoutInflater.from(getContext()).inflate(R.layout.view_menu_lateral, this, false));

        tvwInfoApp = findViewById(R.id.tvw_info_app);
        menuList = findViewById(R.id.mlt_menu);
        menuGrid = findViewById(R.id.mgd_menu);

        menuList.setNestedScrollingEnabled(false);

        menuGrid.addItemDecoration(new GridDecoration(getContext(), RecyclerView.HORIZONTAL));
        menuPresenter.get(MenuPosition.LATERAL);
    }

    public void reload() {
        menuPresenter.get(MenuPosition.LATERAL);
    }

    public void showMenuOption(String code, Boolean status, String title){
        menuList.showMenuOption(code, status, title);
    }

    @Override
    public void showMenuList(List<MenuModel> menuList) {
        for (MenuModel model : menuList)
            for (MenuModel menuModel : model.getSubMenus()) {
                switch (menuModel.getCodigo()) {
                    case MenuCode.HOME:
                        menuModel.setDrawable(R.drawable.ic_home_black);
                        break;
                    case MenuCode.CLIENTS:
                        menuModel.setDrawable(R.drawable.ic_clientes_black);
                        break;
                    case MenuCode.DEBTS:
                        menuModel.setDrawable(R.drawable.ic_menu_collection);
                        break;
                    case MenuCode.ORDERS:
                        menuModel.setDrawable(R.drawable.ic_pedidos_black);
                        break;
                    case MenuCode.INCENTIVES:
                        menuModel.setDrawable(R.drawable.ic_bonificacion_black);
                        break;
                    case MenuCode.TUTORIAL:
                        menuModel.setDrawable(R.drawable.ic_tutorial_gray);
                        break;
                    case MenuCode.BUZON:
                        menuModel.setDrawable(R.drawable.ic_menu_feedback);
                        break;
                    case MenuCode.TERMINOS:
                        menuModel.setDrawable(R.drawable.ic_tyc_gray);
                        break;
                    case MenuCode.CLOSE:
                        menuModel.setDrawable(R.drawable.ic_padlock_black);
                        break;
                    case MenuCode.PRODUCTOS_AGOTADOS:
                        menuModel.setDrawable(R.drawable.ic_product_spend_black);
                        break;
                    case MenuCode.ESTADO_CUENTA:
                        menuModel.setDrawable(R.drawable.ic_estado_cuenta_black);
                        break;
                    case MenuCode.CATALOG:
                        menuModel.setDrawable(R.drawable.ic_catalogos_black);
                        break;
                    case MenuCode.TRACK_ORDERS:
                        menuModel.setDrawable(R.drawable.track_orders_black);
                        break;
                    case MenuCode.OFFERS:
                        menuModel.setDrawable(R.drawable.ic_ofertas_black);
                        break;
                    case MenuCode.CLOSEOUT:
                        menuModel.setDrawable(R.drawable.ic_closeout_black);
                        break;
                    case MenuCode.CAMBIOS_DEVOLUCIONES:
                        menuModel.setDrawable(R.drawable.ic_closeout_black);
                        break;
                    case MenuCode.MI_NEGOCIO:
                        menuModel.setDrawable(R.drawable.ic_minegocio_black);
                        break;
                    case MenuCode.ACADEMIA:
                        menuModel.setDrawable(R.drawable.ic_miacademia_black);
                        break;
                    case MenuCode.TUVOZ:
                        menuModel.setDrawable(R.drawable.ic_tuvozonline_black);
                        break;
                    case MenuCode.CAMINO:
                        menuModel.setDrawable(R.drawable.ic_path_success);
                        break;
                    case MenuCode.CHATBOT:
                        menuModel.setDrawable(R.drawable.ico_chatbot);
                        break;
                    case MenuCode.MQVIRTUAL:
                        menuModel.setDrawable(R.drawable.ic_maquillador);
                        break;
                    case MenuCode.ASESOR_REGALO:
                        menuModel.setDrawable(R.drawable.ic_asesor_regalo);
                        break;
                    case MenuCode.ESCANER_QR:
                        menuModel.setDrawable(R.drawable.ic_qr);
                        break;
                    case MenuCode.INFO_CAMPANIA:
                        menuModel.setDrawable(R.drawable.ic_info_campanias);
                        break;
                    case MenuCode.MEN_LAT_SEC_DESCARGA:
                        menuModel.setDrawable(R.drawable.ic_galery);
                    break;
                        case MenuCode.NAVI_FEST:
                        menuModel.setDrawable(R.drawable.ic_navifest);
                    break;
                    case MenuCode.MEN_ESIKA_AHORA:
                        menuModel.setDrawable(R.drawable.ic_esikaahora);
                            break;
                    default:
                        break;
                }
            }

        if (!menuList.isEmpty())
            this.menuList.setList(menuList.get(0));
        if (menuList.size() > 1) {
            this.menuGrid.setList(menuList.get(1));
            tvwInfoApp.setText(menuList.get(1).getDescripcion());
        }
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
     * @param onMenuItemSelectedListener items listener
     */
    public void setOnMenuItemSelectedListener(MenuLateralListAdapter.OnItemSelectedListener onMenuItemSelectedListener) {
        menuList.setOnMenuItemSelectedListener(onMenuItemSelectedListener);
        menuGrid.setOnMenuItemSelectedListener(onMenuItemSelectedListener);
    }

    /**
     * Agrega un listener al hacer click sobre el elemento de un SubMenu
     *
     * @param onMenuItemSelectedListener subMennuListener
     */
    public void setOnSubMenuItemSelectedListener(SubMenuLateralListAdapter.OnItemSelectedListener onMenuItemSelectedListener) {
        menuList.setOnSubMenuItemSelectedListener(onMenuItemSelectedListener);
    }
}
