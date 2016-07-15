package es.pintiavaccea.pintiapp.vista;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Miguel on 09/06/2016.
 *
 * Adaptador para el TabLayout de la pantalla principal. Muestra dos vistas, la primera para la
 * geolocalización y la segunda para la lista de hitos.
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new GeoFragment();
            case 1:
                return new ListaHitosFragment();
            default:
                return new GeoFragment();
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
