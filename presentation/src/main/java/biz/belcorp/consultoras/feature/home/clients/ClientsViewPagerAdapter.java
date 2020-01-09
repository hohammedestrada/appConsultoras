package biz.belcorp.consultoras.feature.home.clients;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import biz.belcorp.consultoras.R;
import biz.belcorp.consultoras.feature.home.clients.all.AllClientsFragment;
import biz.belcorp.consultoras.feature.home.clients.pedido.PedidoClientsFragment;
import biz.belcorp.consultoras.feature.home.clients.porcobrar.PorCobrarFragment;

class ClientsViewPagerAdapter extends FragmentPagerAdapter {

    private List<String> tabTitles;
    private Context context;
    private AllClientsFragment.ClientListListener listListener;
    private PorCobrarFragment.ClientListListener pcListListener;
    private Bundle bundle;
    private Fragment mCurrentFragment;

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }
    //...
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    ClientsViewPagerAdapter(FragmentManager fm, Context context, AllClientsFragment.ClientListListener listListener
    , PorCobrarFragment.ClientListListener pcListListener,Bundle bundle) {
        super(fm);
        this.context = context;
        this.listListener = listListener;
        this.pcListListener = pcListListener;
        this.bundle = bundle;
        this.tabTitles = loadTabsTitles();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int pos) {
        Fragment fragment = null;
        switch (pos) {
            case 0:
                AllClientsFragment allClientsFragment = AllClientsFragment.getInstance();
                allClientsFragment.setListListener(listListener);
                fragment = allClientsFragment;
                break;
            case 1:
                PorCobrarFragment porCobrarFragment = PorCobrarFragment.getInstance();
                porCobrarFragment.setListListener(pcListListener);
                porCobrarFragment.setArguments(bundle);
                fragment = porCobrarFragment;
                break;
            case 2:
                fragment = new PedidoClientsFragment();
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return tabTitles.get(position);
    }

    private List<String> loadTabsTitles() {
        tabTitles = new ArrayList<>();
        tabTitles.add(context.getString(R.string.client_todos));
        tabTitles.add(context.getString(R.string.client_deuda));
        tabTitles.add(context.getString(R.string.client_pedido));

        return tabTitles;
    }
}
