package com.sudhar.netizen.PagedAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.sudhar.netizen.CallBacks.BlockListener;
import com.sudhar.netizen.CallBacks.BookmarkListener;
import com.sudhar.netizen.CallBacks.CommentListener;
import com.sudhar.netizen.CallBacks.DeleteContentListener;
import com.sudhar.netizen.CallBacks.FollowListener;
import com.sudhar.netizen.CallBacks.GeneralCallBacks;
import com.sudhar.netizen.CallBacks.LikeListener;
import com.sudhar.netizen.CallBacks.SearchUserListener;
import com.sudhar.netizen.CommentAdapter;
import com.sudhar.netizen.ContentType;
import com.sudhar.netizen.ImageSliderAdapter;
import com.sudhar.netizen.MemeFragmentDirections;
import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.NetUtils.RetrofitClient;
import com.sudhar.netizen.R;
import com.sudhar.netizen.RoomDB.Daos.MainDao;
import com.sudhar.netizen.RoomDB.Entities.CommentEntity;
import com.sudhar.netizen.RoomDB.Entities.Meme;
import com.sudhar.netizen.RoomDB.Entities.MemeEntity;
import com.sudhar.netizen.RoomDB.db.AppDatabase;
import com.sudhar.netizen.SharedPreferenceHelper;
import com.sudhar.netizen.UserMentionAdapter;
import com.sudhar.netizen.models.Comment;
import com.sudhar.netizen.models.MemeDetailModel;
import com.sudhar.netizen.models.TemplateDetailModel;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sudhar.netizen.Utils.hideSoftKeyboard;


public class PagedMemeAdapter extends PagingDataAdapter<Meme, PagedMemeAdapter.MemeVH> {

    private Context context;

    private RestInterface restInterface;
    private final String TAG = "MemeAdapter";

    SharedPreferenceHelper sharedPreferenceHelper;

    MainDao mainDao;
    AppDatabase appDatabase;

    MemeShareAndReportListener memeShareAndReportListener;

    public PagedMemeAdapter(Context context) {
        super(Meme.CALLBACK);

        this.context = context;
        this.restInterface = RetrofitClient.getClient(context).create(RestInterface.class);

        sharedPreferenceHelper = new SharedPreferenceHelper(context);
        appDatabase = AppDatabase.getInstance(context);
        mainDao = appDatabase.mainDao();


    }


    public interface MemeShareAndReportListener {
        void OnReportButtonClick(Meme meme);

        void OnShareButtonClick(Meme meme);
    }

    public void setMemeShareAndReportListener(MemeShareAndReportListener memeShareAndReportListener) {
        this.memeShareAndReportListener = memeShareAndReportListener;
    }

    @NonNull
    @Override
    public MemeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemeVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_meme, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull MemeVH holder, int position) {
        Meme item = getItem(position);


        if (item != null) {
            MemeEntity memeEntity = item.getMeme();
            holder.mUsername.setText(memeEntity.getUsername());
            holder.mDescription.setText(memeEntity.getDescription());
            Log.d(TAG, "onBindViewHolder: NuLL " + position);


            if (item.getImages() != null) {
                ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(item.getImages(), context);
                imageSliderAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MemeFragmentDirections.ActionMemeFragmentToViewerFragment action = MemeFragmentDirections.actionMemeFragmentToViewerFragment(memeEntity.getId(), ContentType.MEME);
                        Navigation.findNavController(v).navigate(action);
                    }
                });
                holder.viewPager2.setAdapter(imageSliderAdapter);

                if (item.getImages().size() > 1) {
                    new TabLayoutMediator(holder.imageTabLayout, holder.viewPager2,
                            (tab, pos) -> {

                            }).attach();
                }

            }

            CommentAdapter commentAdapter = new CommentAdapter(context, 3);
            commentAdapter.setCommentList(item.getComments());
            holder.mCommentRV.setAdapter(commentAdapter);

            if (memeEntity.is_liked()) {
                holder.mLikeBtn.setIcon(context.getDrawable(R.drawable.ic_heart_solid));
            } else {
                holder.mLikeBtn.setIcon(context.getDrawable(R.drawable.ic_heart));
            }

            if (memeEntity.is_bookmarked()) {
                holder.mFavBtn.setIcon(context.getDrawable(R.drawable.ic_bookmark_solid));
            } else {
                holder.mFavBtn.setIcon(context.getDrawable(R.drawable.ic_bookmark));
            }

            if (memeEntity.getUserId() == sharedPreferenceHelper.getUserId() && memeEntity.getUsername().equals(sharedPreferenceHelper.getUsername())) {
                holder.mUsername.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Navigation.findNavController(v).navigate(R.id.action_memeFragment_to_profileFragment);
                    }
                });
            } else {
                holder.mUsername.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MemeFragmentDirections.ActionMemeFragmentToUserFragment action = MemeFragmentDirections.actionMemeFragmentToUserFragment(memeEntity.getUsername());
                        Navigation.findNavController(v).navigate(action);
                    }
                });
            }
        }



    }


    public class MemeVH extends RecyclerView.ViewHolder {

        TextView mUsername, mDescription;
        RecyclerView mCommentRV, mMentionRV;
        MaterialButton optionBtn;
        MaterialButton mLikeBtn, mFavBtn;

        Button mCommentPostBtn;
        TextInputEditText mCommentField;
        View mDivider;
        int pos = 0;
        UserMentionAdapter userMentionAdapter;
        String found;
        ViewPager2 viewPager2;
        TabLayout imageTabLayout;
        GeneralCallBacks generalCallBacks;
        public MemeVH(@NonNull final View itemView) {
            super(itemView);


            mUsername = itemView.findViewById(R.id.Username);
            mDescription = itemView.findViewById(R.id.Description);
            optionBtn = itemView.findViewById(R.id.PostOptionBtn);
            mCommentField = itemView.findViewById(R.id.CommentField);
            mDivider = itemView.findViewById(R.id.divider);
            mCommentPostBtn = itemView.findViewById(R.id.CommentPostBtn);

            mLikeBtn = itemView.findViewById(R.id.LikeBtn);
            mFavBtn = itemView.findViewById(R.id.FavBtn);


            mMentionRV = itemView.findViewById(R.id.MentiontRv);
            viewPager2 = itemView.findViewById(R.id.MemeImageViewPager);
            viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            imageTabLayout = itemView.findViewById(R.id.ImageTabLayout);


            userMentionAdapter = new UserMentionAdapter();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            mMentionRV.setLayoutManager(linearLayoutManager);
            userMentionAdapter.setUsernameSelectListener(new UserMentionAdapter.UsernameSelectListener() {
                @Override
                public void OnUserSelect(String Username) {
                    String text = mCommentField.getText().toString();

                    String ne = text.replaceAll(found + "\\b", "@" + Username);
                    mCommentField.setText(ne);
                    pos = mCommentField.getText().length() + 1;
                    mCommentField.setSelection(mCommentField.getText().length());
                    userMentionAdapter.clear();
                    mMentionRV.setVisibility(View.GONE);


                }
            });
            mMentionRV.setAdapter(userMentionAdapter);

            mLikeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    generalCallBacks.postLike(getItem(getLayoutPosition()).getMeme().getContentType(), getItem(getLayoutPosition()).getMeme().getId());
                }
            });

            mFavBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    generalCallBacks.postBookmark(getItem(getLayoutPosition()).getMeme().getContentType(), getItem(getLayoutPosition()).getMeme().getId());
                }
            });


            mCommentPostBtn.setEnabled(false);
            mCommentField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String commentInput = mCommentField.getText().toString().trim();
                    mCommentPostBtn.setEnabled(!commentInput.isEmpty());
                    try {


                        Pattern regex = Pattern.compile("@(\\w)+\\b");
                        Matcher regexMatcher = regex.matcher(commentInput.substring(pos));
                        Log.d(TAG, "afterTextChanged: " + pos);
                        if (regexMatcher.find()) {
                            found = regexMatcher.group(0);
                            mMentionRV.setVisibility(View.VISIBLE);
                            generalCallBacks.searchUsers(found.substring(1));


                        }
                    } catch (StringIndexOutOfBoundsException e) {

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard((Activity) context);
                    return false;
                }
            });


            optionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = getPopMenu(context, optionBtn, getItem(getLayoutPosition()), generalCallBacks);
                    popup.show();
                }
            });


            mCommentRV = itemView.findViewById(R.id.CommentRv);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            mCommentRV.setLayoutManager(layoutManager);


            mCommentPostBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    generalCallBacks.postComment(mCommentField.getText().toString().trim(), getItem(getLayoutPosition()).getMeme().getContentType(), getItem(getLayoutPosition()).getMeme().getId());

                }
            });


            generalCallBacks = new GeneralCallBacks(restInterface);
            init();
        }

        private void init() {
            generalCallBacks.setCommentListener(new CommentListener() {
                @Override
                public void OnComment(Comment comment) {

                    Meme memeEntity = getItem(getLayoutPosition());

                    CommentEntity commentEntity = new CommentEntity(comment.getId(), comment.getUsername(), null, "meme", memeEntity.getMeme().getId(), comment.getUserId(), comment.getBody(), memeEntity.getMeme().getCreated(), "");

                    appDatabase.runInTransaction(new Runnable() {
                        @Override
                        public void run() {
                            mainDao.insertComment(commentEntity);
                            memeEntity.getComments().add(0, commentEntity);
                            CommentAdapter commentAdapter = (CommentAdapter) mCommentRV.getAdapter();
                            commentAdapter.setCommentList(getItem(getLayoutPosition()).getComments());
                            mCommentField.getText().clear();

                            pos = 0;
                        }
                    });


                }

                @Override
                public void OnCommentError(int ResponseCode, ResponseBody Error) {

                }

                @Override
                public void OnCommentFailure(Throwable throwable) {
                    Log.d(TAG, "Failure comment");
                    Toast.makeText(context, "Failure comment", Toast.LENGTH_SHORT).show();
                }
            });

            generalCallBacks.setLikeListener(new LikeListener() {
                @Override
                public void OnLike(boolean isLiked, int LikeCount) {
                    if (isLiked) {
                        mLikeBtn.setIcon(context.getDrawable(R.drawable.ic_heart_solid));
                    } else {
                        mLikeBtn.setIcon(context.getDrawable(R.drawable.ic_heart));
                    }
                }

                @Override
                public void OnLikeError(int ResponseCode, ResponseBody Error) {

                }

                @Override
                public void OnLikeFailure(Throwable throwable) {
                    Log.d(TAG, "Failure like");
                    Toast.makeText(context, "Failure like", Toast.LENGTH_SHORT).show();
                }
            });

            generalCallBacks.setBookmarkListener(new BookmarkListener() {
                @Override
                public void OnBookmark(boolean isBookmarked) {
                    if (isBookmarked) {
                        mFavBtn.setIcon(context.getDrawable(R.drawable.ic_bookmark_solid));
                    } else {
                        mFavBtn.setIcon(context.getDrawable(R.drawable.ic_bookmark));
                    }
                }

                @Override
                public void OnBookmarkError(int ResponseCode, ResponseBody Error) {

                }

                @Override
                public void OnBookmarkFailure(Throwable throwable) {
                    Log.d(TAG, "Failure bookmark");
                    Toast.makeText(context, "Failure bookmark", Toast.LENGTH_SHORT).show();
                }
            });

            generalCallBacks.setSearchUserListener(new SearchUserListener() {
                @Override
                public void OnFound(List<UserMentionAdapter.DataModel> users) {
                    userMentionAdapter.clear();
                    userMentionAdapter.addAll(users);
                }

                @Override
                public void OnSearchError(int ResponseCode, ResponseBody Error) {

                }

                @Override
                public void OnSearchFailure(Throwable throwable) {
                    Log.d(TAG, "Failure follow");
                    Toast.makeText(context, "user search failure", Toast.LENGTH_SHORT).show();
                }
            });

            generalCallBacks.setFollowListener(new FollowListener() {
                @Override
                public void OnFollow(boolean isFollowing, String username) {
                    if (isFollowing) {

                        appDatabase.runInTransaction(new Runnable() {
                            @Override
                            public void run() {
                                mainDao.updateFollow(true, username);
                            }
                        });
                    } else {

                        appDatabase.runInTransaction(new Runnable() {
                            @Override
                            public void run() {
                                mainDao.updateFollow(false, username);
                            }
                        });
                    }
                }


                @Override
                public void OnFollowError(int ResponseCode, ResponseBody Error) {

                }

                @Override
                public void OnFollowFailure(Throwable throwable) {

                }
            });

            generalCallBacks.setBlockListener(new BlockListener() {
                @Override
                public void OnBlock(boolean isBlocked) {
                    Toast.makeText(context, "Blocked : " + isBlocked, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnBlockError(int ResponseCode, ResponseBody Error) {

                }

                @Override
                public void OnBlockFailure(Throwable throwable) {

                }
            });

            generalCallBacks.setDeleteContentListener(new DeleteContentListener() {

                @Override
                public void OnDelete(String id, ContentType contentType) {
                    Toast.makeText(context, "Deleted " + contentType.name(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnDeleteError(ContentType contentType, int ResponseCode, ResponseBody Error) {

                }

                @Override
                public void OnDeleteFailure(ContentType contentType, Throwable throwable) {

                }
            });

        }

    }


    private PopupMenu getPopMenu(Context context, Button optionBtn, Meme meme, GeneralCallBacks generalCallBacks) {
        PopupMenu popup = new PopupMenu(context, optionBtn);
        MemeEntity memeDetailModel = meme.getMeme();

        if (memeDetailModel.getUsername().equals(sharedPreferenceHelper.getUsername())
                &&
                memeDetailModel.getUserId() == sharedPreferenceHelper.getUserId()) {


            popup.getMenuInflater()
                    .inflate(R.menu.meme_option_for_me, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.EditPost:
                            MemeFragmentDirections.ActionMemeFragmentToEditPostFragment action = MemeFragmentDirections.actionMemeFragmentToEditPostFragment(memeDetailModel.getId(), ContentType.MEME);
                            Navigation.findNavController(optionBtn).navigate(action);
                            return true;
                        case R.id.DeletePost:
                            generalCallBacks.deleteMeme(memeDetailModel.getId(), memeDetailModel.getContentType());
                            return true;
                        case R.id.Share:
                            memeShareAndReportListener.OnShareButtonClick(meme);
                            return true;

                    }
                    return true;
                }
            });
        } else {
            popup.getMenuInflater()
                    .inflate(R.menu.meme_option, popup.getMenu());
            Log.d(TAG, "getPopMenu: " + memeDetailModel.is_following());
            if (memeDetailModel.is_following()) {

                popup.getMenu().getItem(0).setTitle("Unfollow");

            } else {
                popup.getMenu().getItem(0).setTitle("Follow");
            }

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.Follow:
                            generalCallBacks.followUser(memeDetailModel.getUsername(), memeDetailModel.getUserId());
                            return true;
                        case R.id.Report:
                            memeShareAndReportListener.OnReportButtonClick(meme);
                            return true;
                        case R.id.Block:
                            generalCallBacks.blockUser(memeDetailModel.getUsername());
                            return true;

                        case R.id.Share:
                            memeShareAndReportListener.OnShareButtonClick(meme);
                            return true;

                    }
                    return false;
                }
            });
        }
        return popup;

    }








}

