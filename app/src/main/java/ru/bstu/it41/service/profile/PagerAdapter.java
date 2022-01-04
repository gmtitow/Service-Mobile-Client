package ru.bstu.it41.service.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Герман on 23.04.2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    ProfileFragmentPresenter presenter;

    /*public PagerAdapter(FragmentManager fm) {
        super(fm);
    }*/

    public PagerAdapter(FragmentManager fm, ProfileFragmentPresenter presenter) {
        super(fm);
       this.presenter = presenter;
    }

    @Override
    public Fragment getItem(int position) {
        /*android.support.v4.app.Fragment fragment = null;
        Class fragmentClass = null;
        switch (presenter.getStateModel().getCurrentFragment()) {
            case 0:
                fragmentClass = ProfileFragment.class;
                break;
            case 1:
                fragmentClass = ProfilePlacesFragment.class;
                break;
            case 2:
                fragmentClass = ProfileCategoriesFragment.class;
                break;
            case 3:
                fragmentClass = SettingsFragment.class;
                break;
            case 11:
                fragmentClass = ProfileChangeFragment.class;
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", presenter.getStateModel());
        bundle.putSerializable("presenter", presenter);
        // replaceFragment(fragmentClass, bundle, null);*/
        return null;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
