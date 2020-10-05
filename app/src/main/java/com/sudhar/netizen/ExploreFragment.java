package com.sudhar.netizen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class ExploreFragment extends Fragment {


    public ExploreFragment() {
        // Required empty public constructor
    }


    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.materialup_tabs);
        ViewPager2 viewPager = (ViewPager2) view.findViewById(R.id.materialup_viewpager);

//        viewPager.setAdapter(new ScreenSlidePagerAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setText("Memes");
                                break;
                            case 1:
                                tab.setText("Templates");
                                break;
                        }

                    }
                }).attach();

        return view;
    }

//    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
//
//
//        public ScreenSlidePagerAdapter(@NonNull Fragment fragment) {
//            super(fragment);
//        }
//
//        @NonNull
//        @Override
//        public Fragment createFragment(int position) {
//            switch (position){
//                case 0:
//                    return new UserContentFragment();
//                case 1:
//                    return new UserTemplateFragment();
//
//            }
//            return  null;
//        }
//
//
//
//        @Override
//        public int getItemCount() {
//            return 2;
//        }
//    }
}