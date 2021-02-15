package com.afrosin.notes;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Navigation {

    private FragmentManager fragmentManager;

    public Navigation(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void addFragment(Fragment fragment, boolean useBackStack, String tag, int containerId) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerId, fragment, tag);
        if (useBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public void deleteFragmentByTag(String fragmentTag) {
        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragmentToRemove = getFragmentByTag(fragmentTag);
            if (fragmentToRemove != null) {
                fragmentTransaction.remove(fragmentToRemove);
            }
            fragmentTransaction.commit();
        }
    }

    public Fragment getFragmentByTag(String fragmentTag) {
        return fragmentManager.findFragmentByTag(fragmentTag);
    }
}
