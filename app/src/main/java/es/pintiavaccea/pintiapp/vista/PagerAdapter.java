package es.pintiavaccea.pintiapp.vista;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Miguel on 09/06/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

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
