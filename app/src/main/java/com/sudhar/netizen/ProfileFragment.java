package com.sudhar.netizen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.NetUtils.RetrofitClient;
import com.sudhar.netizen.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    private final String TAG = "ProfileFragment";

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 50;
    private boolean mIsAvatarShown = true;

    private ImageView mProfileImage;
    private int mMaxScrollSize;
    private View rootView;
    private ImageButton menuBtn;
    private User user;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private RestInterface restInterface;
    private TextView mUsername, mAbout, mPostsCountTV;
    private UserContentFragment userMemeFragment;
    private UserContentFragment userTemplateFragment;
    private TextView mFollowersCountTV, mFollowingCountTV;
    private MaterialButton mEditProfileBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        mUsername = rootView.findViewById(R.id.Username);
        mAbout = rootView.findViewById(R.id.AboutText);
        tabLayout = (TabLayout) rootView.findViewById(R.id.materialup_tabs);
        viewPager = (ViewPager2) rootView.findViewById(R.id.materialup_viewpager);

        mPostsCountTV = rootView.findViewById(R.id.PostsCount);
        mFollowersCountTV = rootView.findViewById(R.id.FollowersCount);
        mFollowingCountTV = rootView.findViewById(R.id.FollowingCount);

        mProfileImage = (ImageView) rootView.findViewById(R.id.materialup_profile_image);
        menuBtn = rootView.findViewById(R.id.menuBtn);
        mEditProfileBtn = rootView.findViewById(R.id.EditProfileBtn);
        mEditProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(rootView).navigate(R.id.action_profileFragment_to_userMenuFragment);
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), menuBtn);
                popup.getMenuInflater()
                        .inflate(R.menu.profile_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.logout:
                                SharedPreferences preferences = getContext().getSharedPreferences("Netizen", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.apply();
                                Navigation.findNavController(rootView).navigate(R.id.action_profileFragment_to_authActivity);

                                return true;

                        }
                        return true;
                    }
                });

                popup.show();

            }
        });


        return rootView;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        restInterface = RetrofitClient.getClient(getContext()).create(RestInterface.class);
        getMe();


    }


    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return userMemeFragment;
                case 1:
                    return userTemplateFragment;

            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

    private void getMe() {
        Call<User> call = restInterface.getMe();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Response Error");

                    return;
                }
                user = response.body();
                setData();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void setData() {

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(getContext()).load(user.getProfilePicURL()).apply(options).into(mProfileImage);

        mUsername.setText(user.getUsername());
        mAbout.setText(user.getAbout());
        String postsCount = user.getMemeCount() + user.getTemplateCount() + " Posts";
        mPostsCountTV.setText(postsCount);

        String temp = user.getFollowersCount() + " Followers";
        mFollowersCountTV.setText(temp);
        temp = user.getFollowingCount() + " Following";
        mFollowingCountTV.setText(temp);

        userTemplateFragment = new UserContentFragment();
        userTemplateFragment.setContentType(ContentType.TEMPLATE);
        userTemplateFragment.setUser(user);
        userTemplateFragment.setRestInterface(restInterface);


        userMemeFragment = new UserContentFragment();
        userMemeFragment.setContentType(ContentType.MEME);
        userMemeFragment.setUser(user);
        userMemeFragment.setRestInterface(restInterface);


        viewPager.setAdapter(new ScreenSlidePagerAdapter(this));

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


    }


}