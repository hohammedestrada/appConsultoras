package biz.belcorp.consultoras.feature.home.menu;

import java.util.List;

import biz.belcorp.consultoras.base.View;
import biz.belcorp.consultoras.common.model.menu.MenuModel;

/**
 * @author andres.escobar on 4/05/2017.
 */

public interface MenuView extends View {

    void showMenuList(List<MenuModel> menuModels);
}
