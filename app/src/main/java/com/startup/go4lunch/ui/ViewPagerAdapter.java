package com.startup.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0 : return new MapFragment();
            case 1 : return new RestaurantListFragment();
            case 2 : return new WorkmateListFragment();
        }
        return new MapFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}