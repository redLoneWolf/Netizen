package com.sudhar.netizen;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.models.ContentList;
import com.sudhar.netizen.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserContentFragment extends Fragment {

    private final String TAG = "UserMemeFragment";

    RecyclerView mUserMemeRecyclerView;
    UserContentAdapter mMemeAdapter;
    private RestInterface restInterface;
    private final int FIRST_PAGE = 1;
    private int nextPage = FIRST_PAGE;

    private boolean isLoading = false;
    private User user;
    private boolean isLastPage = false;
    private ContentType contentType;

    public UserContentFragment() {

    }

    public void setRestInterface(RestInterface restInterface) {
        this.restInterface = restInterface;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public void setContentType(ContentType type) {
        this.contentType = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        mUserMemeRecyclerView = view.findViewById(R.id.GridCardRv);
//        initRV();

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRV();
        getMore(nextPage);
    }

    private void initRV() {
        mMemeAdapter = new UserContentAdapter(getContext());
        mMemeAdapter.setContentType(contentType);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mUserMemeRecyclerView.setLayoutManager(gridLayoutManager);
        mUserMemeRecyclerView.setAdapter(mMemeAdapter);
        mUserMemeRecyclerView.addOnScrollListener(new ScrollListener(10) {
            @Override
            protected void loadMoreItems() {

                isLoading = true;
                mMemeAdapter.addLoading();
                getMore(nextPage);
            }

            @Override
            public int getTotalPageCount() {
                return 0;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

    }

    private void getMore(int page) {
        switch (contentType) {
            case MEME:
                getMoreMemes(page);
                break;
            case TEMPLATE:
                getMoreTemplates(page);
                break;
        }
    }

    private void getMoreMemes(int page) {
        Call<ContentList> call = restInterface.getUserMemes(user.getUsername(), page);

        call.enqueue(new Callback<ContentList>() {
            @Override
            public void onResponse(Call<ContentList> call, Response<ContentList> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Response Error");
                    return;
                }

                isLoading = false;
                ContentList contentList = response.body();
                if (!mMemeAdapter.isEmpty()) {
                    mMemeAdapter.removeLoading();
                }

                mMemeAdapter.addAll(contentList.getContents());
                nextPage += 1;
                if (contentList.getNextPage() == null) {
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<ContentList> call, Throwable t) {
                Log.d(TAG, "Failure");
                Toast.makeText(getContext(), "user mame frag Failure", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getMoreTemplates(int page) {
        Call<ContentList> call = restInterface.getUserTemplates(user.getUsername(), page);

        call.enqueue(new Callback<ContentList>() {
            @Override
            public void onResponse(Call<ContentList> call, Response<ContentList> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Response Error");
                    return;
                }

                isLoading = false;
                ContentList contentList = response.body();
                if (!mMemeAdapter.isEmpty()) {
                    mMemeAdapter.removeLoading();
                }

                mMemeAdapter.addAll(contentList.getContents());
                nextPage += 1;
                if (contentList.getNextPage() == null) {
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<ContentList> call, Throwable t) {
                Log.d(TAG, "Failure");
                Toast.makeText(getContext(), "user mame frag Failure", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
