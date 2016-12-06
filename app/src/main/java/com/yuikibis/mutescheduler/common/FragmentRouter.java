package com.yuikibis.mutescheduler.common;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

public class FragmentRouter {
    public enum Tag {
        MAIN,
        SETTING,
        LICENCE
    }

    private static final Map<Tag, Class> fragments = new HashMap<>();

    private FragmentRouter() {
    }

    public static void register(Tag tag, Class c) {
        fragments.put(tag, c);
    }

    private static Class get(Tag tag) {
        return fragments.get(tag);
    }

    public static void replace(FragmentManager fragmentManager, int container, Tag tag, Bundle args, boolean addToBackStack) {
        Fragment fragment = fragmentManager.findFragmentByTag(String.valueOf(tag));
        if (fragment == null) {
            Class c = get(tag);
            try {
                Object object = c.newInstance();
                fragment = (Fragment) object;
                fragment.setArguments(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.replace(container, fragment, String.valueOf(tag));
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
}
