package com.example.myapplication;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentTransition {
    public static void to(Fragment newFragment, FragmentActivity activity, boolean addToBackstack, int layoutViewID, String backStackTag)
    {
        FragmentTransaction transaction = activity
                .getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(layoutViewID, newFragment, backStackTag);
        if(addToBackstack)
            transaction.addToBackStack(backStackTag);

        transaction.commit();
    }

    public static void goBack(FragmentActivity activity, String backStackTag){
        activity.getSupportFragmentManager().popBackStack(backStackTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
