package org.axel.dataakansalle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MunicipalityPagerAdapter extends FragmentStateAdapter {
    private final String municipality;
    public MunicipalityPagerAdapter(@NonNull FragmentActivity fragmentActivity, String municipality) {
        super(fragmentActivity);
        this.municipality = municipality;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return ChartFragment.newInstance(municipality);
            case 1:
                return WeatherFragment.getInstance(municipality);
            case 2:
                return MunicipalityFragment.newInstance(municipality);
            default:
                return new ChartFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
