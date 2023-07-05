package com.vishcorp.vchat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class allpage extends FragmentPagerAdapter {
 int tabs;
    public allpage(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new chatfrag();
            case 1:
                return new callfrag();

            default:
            return null;
        }
    }

    @Override
    public int getCount() {
        return tabs;
    }
}
