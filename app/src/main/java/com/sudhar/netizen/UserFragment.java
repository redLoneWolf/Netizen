package com.sudhar.netizen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.JsonObject;
import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.NetUtils.RetrofitClient;
import com.sudhar.netizen.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {

    private final String TAG = "UserFragment";

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 50;
    private boolean mIsAvatarShown = true;

    private ImageView mProfileImage;
    private int mMaxScrollSize;

    private ImageButton menuBtn;
    private User user;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private RestInterface restInterface;
    private TextView mUsername, mAbout, mPostsCountTV;
    private UserContentFragment userMemeFragment;
    private UserContentFragment userTemplateFragment;
    private TextView mFollowersCountTV, mFollowingCountTV;
    private MaterialButton mFollowBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        mUsername = view.findViewById(R.id.Username);
        mAbout = view.findViewById(R.id.AboutText);
        tabLayout = (TabLayout) view.findViewById(R.id.materialup_tabs);
        viewPager = (ViewPager2) view.findViewById(R.id.materialup_viewpager);

        mPostsCountTV = view.findViewById(R.id.PostsCount);
        mFollowersCountTV = view.findViewById(R.id.FollowersCount);
        mFollowingCountTV = view.findViewById(R.id.FollowingCount);

        mProfileImage = (ImageView) view.findViewById(R.id.materialup_profile_image);
        menuBtn = view.findViewById(R.id.menuBtn);
        mFollowBtn = view.findViewById(R.id.FollowBtn);


        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), menuBtn);
                popup.getMenuInflater()
                        .inflate(R.menu.user_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.Report:
                                ReportDialogFragment reportDialogFragment = ReportDialogFragment.newInstance();
                                reportDialogFragment.setAnotherUserId(user.getId());
                                reportDialogFragment.show(getChildFragmentManager(), reportDialogFragment.getTag());
                                return true;
                            case R.id.Block:


                                return true;

                        }
                        return true;
                    }
                });

                popup.show();

            }
        });

        UserFragmentArgs args = UserFragmentArgs.fromBundle(getArguments());
        restInterface = RetrofitClient.getClient(getContext()).create(RestInterface.class);
        getUser(args.getUsername());
        return view;


    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

    private void getUser(String Username) {
        Call<User> call = restInterface.getUser(Username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Response Error");
                    if (response.code() == 404) {
                        getView().setVisibility(View.GONE);
                        new MaterialAlertDialogBuilder(getContext())
                                .setTitle("User Not Found")
                                .setMessage("The user not found")
                                .setNegativeButton("back", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Navigation.findNavController(getView()).popBackStack();
                                    }
                                })
                                .show();
                    }


                    return;
                }

                user = response.body();
                setUser();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void setUser() {

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(getContext()).load(user.getProfilePicURL()).apply(options).into(mProfileImage);


        if (user.is_following()) {
            mFollowBtn.setText("UnFollow");
        } else {
            mFollowBtn.setText("Follow");
        }

        mUsername.setText(user.getUsername());
        if (user.getAbout().isEmpty()) {
            mAbout.setVisibility(View.GONE);
        } else {
            mAbout.setText(user.getAbout());
        }

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

        mFollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followUser(user.getUsername(), mFollowBtn);
            }
        });

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

    private void followUser(String username, MaterialButton button) {
        Call<JsonObject> call = restInterface.followUser(username);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Response Error");
                    return;
                }
                JsonObject jsonObject = response.body();
                boolean is_followed = jsonObject.get("is_following").getAsBoolean();
                if (is_followed) {
                    button.setText("UnFollow");
                } else {
                    button.setText("Follow");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "Failure follow");
                Toast.makeText(getContext(), "Failure follow", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
