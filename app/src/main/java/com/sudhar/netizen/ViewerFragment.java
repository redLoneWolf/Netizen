package com.sudhar.netizen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.sudhar.netizen.CallBacks.BookmarkListener;
import com.sudhar.netizen.CallBacks.CommentListener;
import com.sudhar.netizen.CallBacks.FollowListener;
import com.sudhar.netizen.CallBacks.GeneralCallBacks;
import com.sudhar.netizen.CallBacks.GetContentListener;
import com.sudhar.netizen.CallBacks.LikeListener;
import com.sudhar.netizen.CallBacks.ReplyListener;
import com.sudhar.netizen.CallBacks.SearchUserListener;
import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.NetUtils.RetrofitClient;
import com.sudhar.netizen.PagedAdapter.CommentPagedAdapter;
import com.sudhar.netizen.PagedAdapter.LoadStateAdapter;
import com.sudhar.netizen.PagedAdapter.TestAdapter;
import com.sudhar.netizen.RoomDB.Daos.MainDao;
import com.sudhar.netizen.RoomDB.Entities.CommentEntity;
import com.sudhar.netizen.RoomDB.Entities.CommentWithReplies;
import com.sudhar.netizen.RoomDB.Entities.Meme;
import com.sudhar.netizen.RoomDB.db.AppDatabase;
import com.sudhar.netizen.ViewModels.CommentViewModel;
import com.sudhar.netizen.ViewModels.CommentWithoutRoom;
import com.sudhar.netizen.ViewModels.MyViewModelFactory;
import com.sudhar.netizen.ViewModels.RoomFac;
import com.sudhar.netizen.models.Comment;
import com.sudhar.netizen.models.MemeDetailModel;
import com.sudhar.netizen.models.PagedResponse;
import com.sudhar.netizen.models.TemplateDetailModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static autodispose2.AutoDispose.autoDisposable;
import static com.sudhar.netizen.Utils.hideSoftKeyboard;

public class ViewerFragment extends Fragment {
    private final String TAG = "ViewerFragment";
    private RestInterface restInterface;

    //    private CommentAdapter mCommentAdapter;
    private TextView mUsername, mDescription;
    private RecyclerView mCommentRV;
    private RecyclerView mMentionRV;

    private TextInputEditText mCommentField;
    private MaterialButton mCommentPostBtn;
    ConstraintLayout mReplyingToCL;
    ImageButton mReplyingToCancelBtn;
    TextView mReplyingToUsername, mReplyingToBody;
    AppBarLayout appBarLayout;
    Meme mMemeData;


    private MaterialButton mLikeBtn, mFavBtn, mFollowBtn;
    private ContentType contentType;
    String contentTypeString;
    String contentId;
    SharedPreferenceHelper sharedPreferenceHelper;
    UserMentionAdapter userMentionAdapter;
    int position = 0;

    String found;

    ViewPager2 viewPager2;
    TabLayout imageTabLayout;
    AppDatabase appDatabase;
    MainDao mainDao;
    GeneralCallBacks generalCallBacks;
    CommentViewModel commentViewModel;
    CommentPagedAdapter commentPagedAdapter;

    //    TestAdapter testAdapter;
//    CommentWithoutRoom commentWithoutRoom;
    LinearLayoutManager llmTools = new LinearLayoutManager(getContext());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewer_back, container, false);


        view.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(getActivity());
                return false;
            }
        });

        mUsername = view.findViewById(R.id.Username);
        mDescription = view.findViewById(R.id.ViewerDescription);

        appBarLayout = view.findViewById(R.id.materialup_appbar);
        mCommentField = view.findViewById(R.id.CommentField);
        mCommentPostBtn = view.findViewById(R.id.CommentPostBtn);
        mLikeBtn = view.findViewById(R.id.LikeBtn);
        mFavBtn = view.findViewById(R.id.FavBtn);
        mFollowBtn = view.findViewById(R.id.FollowBtn);

        mReplyingToCL = view.findViewById(R.id.ReplyingToCL);
        mReplyingToCL.setVisibility(View.GONE);

        mReplyingToCancelBtn = view.findViewById(R.id.ReplyingToCancelBtn);
        mReplyingToUsername = view.findViewById(R.id.ReplyingToUsername);
        mReplyingToBody = view.findViewById(R.id.ReplyingToBody);


        viewPager2 = view.findViewById(R.id.MemeImageViewPager);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        imageTabLayout = view.findViewById(R.id.ImageTabLayout);


        mMentionRV = view.findViewById(R.id.MentiontRv);
        userMentionAdapter = new UserMentionAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mMentionRV.setLayoutManager(linearLayoutManager);
        userMentionAdapter.setUsernameSelectListener(new UserMentionAdapter.UsernameSelectListener() {
            @Override
            public void OnUserSelect(String Username) {
                String text = mCommentField.getText().toString();

                String ne = text.replaceAll(found + "\\b", "@" + Username);
                SpannableStringBuilder ss = makeSpannable(ne, "@(\\w)+\\b");
                mCommentField.setText(ss);
                position = mCommentField.getText().length() + 1;
                mCommentField.setSelection(mCommentField.getText().length());
                userMentionAdapter.clear();
                mMentionRV.setVisibility(View.GONE);


            }
        });
        mMentionRV.setAdapter(userMentionAdapter);

        mReplyingToCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReplyingToCL.setVisibility(View.GONE);
                mCommentPostBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        generalCallBacks.postComment(mCommentField.getText().toString().trim(), contentTypeString, contentId);


                    }
                });
            }
        });
        mCommentField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    appBarLayout.setExpanded(false, true);
                }
            }
        });
        mCommentPostBtn.setEnabled(false);
//        int pos = mCommentField.getSelectionStart();
        mCommentField.addTextChangedListener(new TextWatcher() {


            int initCursorPosition = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                initCursorPosition = mCommentField.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int finalCursorPosition = mCommentField.getSelectionStart();

                if (!(finalCursorPosition - initCursorPosition > 0)) {

                    position = initCursorPosition - 2;
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                String commentInput = mCommentField.getText().toString();
                mCommentPostBtn.setEnabled(!commentInput.isEmpty());


                try {


                    Pattern regex = Pattern.compile("@(\\w)+\\b");
                    Matcher regexMatcher = regex.matcher(commentInput.substring(position));
                    Log.d(TAG, "afterTextChanged: " + position);
                    if (regexMatcher.find()) {
                        found = regexMatcher.group(0);
                        mMentionRV.setVisibility(View.VISIBLE);
                        generalCallBacks.searchUsers(found.substring(1));


                    }
                } catch (StringIndexOutOfBoundsException e) {

                }


            }
        });

        mCommentPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                generalCallBacks.postComment(mCommentField.getText().toString().trim(), contentTypeString, contentId);


            }
        });


        mCommentRV = view.findViewById(R.id.CommentRv);

        commentPagedAdapter.setReplyButtonListener(new CommentPagedAdapter.ReplyButtonListener() {
            @Override
            public void OnReplyButtonClick(CommentAdapter RepliesAdapter, String ParentId, String Username, String Body) {
                Log.d(TAG, "Reply to " + ParentId + "   " + Username);


                mReplyingToUsername.setText(Username);
                mReplyingToBody.setText(Body);
                mReplyingToCL.setVisibility(View.VISIBLE);
                mCommentField.setText("@" + Username);
                mCommentField.requestFocus();
                position = mCommentField.getText().length() - 1;
                mCommentField.setSelection(mCommentField.getText().length());
                mCommentPostBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        postComment(mCommentField, contentTypeString, contentId, mCommentAdapter);
//                        generalCallBacks.postReply(mCommentField, contentTypeString, contentId, ParentId, RepliesAdapter);
                        mReplyingToCL.setVisibility(View.GONE);

                    }
                });
            }
        });


        mCommentRV.setLayoutManager(llmTools);
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mCommentRV);
        mCommentRV.setOnFlingListener(snapHelper);

        commentViewModel.getFlowablePagedData().subscribeOn(Schedulers.io())
                .onErrorReturn(new Function<Throwable, PagingData<CommentWithReplies>>() {
                    @Override
                    public PagingData<CommentWithReplies> apply(Throwable throwable) throws Throwable {
                        Log.d(TAG, "apply: " + throwable.getStackTrace().toString());
                        return null;
                    }
                })
                .to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(commentEntityPagingData -> commentPagedAdapter.submitData(getViewLifecycleOwner().getLifecycle(), commentEntityPagingData), Throwable::printStackTrace);


        mCommentRV.setAdapter(commentPagedAdapter);
//        testAdapter.setReplyButtonListener(new TestAdapter.ReplyButtonListener() {
//            @Override
//            public void OnReplyButtonClick(ViewerCommentAdapter RepliesAdapter, String ParentId, String Username, String Body) {
//
//                mReplyingToUsername.setText(Username);
//                mReplyingToBody.setText(Body);
//                mReplyingToCL.setVisibility(View.VISIBLE);
//                mCommentField.setText("@" + Username);
//                mCommentField.requestFocus();
//                position = mCommentField.getText().length() - 1;
//                mCommentField.setSelection(mCommentField.getText().length());
//                mCommentPostBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        postComment(mCommentField, contentTypeString, contentId, mCommentAdapter);
//                        generalCallBacks.postReply(mCommentField, contentTypeString, contentId, ParentId, RepliesAdapter);
//                        mReplyingToCL.setVisibility(View.GONE);
//
//                    }
//                });
//            }
//        });
//        mCommentRV.setAdapter(testAdapter.withLoadStateHeaderAndFooter(new LoadStateAdapter(listener), new LoadStateAdapter(listener)));
//        commentWithoutRoom.getFlowablePagedData().subscribeOn(Schedulers.io()).to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
//                .subscribe(commentPagingData -> testAdapter.submitData(getViewLifecycleOwner().getLifecycle(), commentPagingData), Throwable::printStackTrace);
//        ;


        sharedPreferenceHelper = new SharedPreferenceHelper(getContext());

        return view;

    }

    private void retry() {
        commentPagedAdapter.retry();
    }

    View.OnClickListener listener = v -> retry();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewerFragmentArgs args = ViewerFragmentArgs.fromBundle(getArguments());
        String id = args.getId();

        appDatabase = AppDatabase.getInstance(getContext());
        mainDao = appDatabase.mainDao();

        commentViewModel = new ViewModelProvider(this, new RoomFac(this.getActivity().getApplication(), id)).get(CommentViewModel.class);
        commentPagedAdapter = new CommentPagedAdapter(getContext());

//        commentWithoutRoom = new ViewModelProvider(this, new MyViewModelFactory(this.getActivity().getApplication(), "meme", id)).get(CommentWithoutRoom.class);
//
//        testAdapter = new TestAdapter(getContext());

    }

    @Override
    public void onStart() {
        super.onStart();
        assert getArguments() != null;
        ViewerFragmentArgs args = ViewerFragmentArgs.fromBundle(getArguments());
        String id = args.getId();
        contentType = args.getContentType();
        restInterface = RetrofitClient.getClient(getContext()).create(RestInterface.class);
        generalCallBacks = new GeneralCallBacks(restInterface);


        init();

        switch (contentType) {
            case MEME:

//                generalCallBacks.getMeme(id);
                appDatabase.runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        mMemeData = mainDao.getMemeAllById(id);
                        setMeme();
                    }
                });
                break;
            case TEMPLATE:

//                generalCallBacks.getTemplate(id);
                break;
        }


    }


    public SpannableStringBuilder makeSpannable(String text, String regex) {

        StringBuffer sb = new StringBuffer();
        SpannableStringBuilder spannable = new SpannableStringBuilder();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            sb.setLength(0); // clear
            String group = matcher.group();
            // caution, this code assumes your regex has single char delimiters
            String spanText = group.substring(0, group.length());
            matcher.appendReplacement(sb, spanText);

            spannable.append(sb.toString());
            int start = spannable.length() - spanText.length();

            spannable.setSpan(new ForegroundColorSpan(Color.BLUE), start, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        sb.setLength(0);
        matcher.appendTail(sb);
        spannable.append(sb.toString());
        return spannable;
    }

    private void init() {


        generalCallBacks.setCommentListener(new CommentListener() {
            @Override
            public void OnComment(Comment comment) {
                CommentEntity commentEntity = new CommentEntity(comment.getId(), comment.getUsername(), null, "meme", mMemeData.getMeme().getId(), comment.getUserId(), comment.getBody(), mMemeData.getMeme().getCreated(), "");


                appDatabase.runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        mainDao.insertComment(commentEntity);

                        commentPagedAdapter.notifyItemInserted(commentPagedAdapter.getItemCount() + 1);
                        commentPagedAdapter.peek(commentPagedAdapter.getItemCount() - 1);

//                                mCommentRV.scrollToPosition(commentPagedAdapter.getItemCount()-1);

                        llmTools.scrollToPosition(commentPagedAdapter.getItemCount() - 1);

                        mCommentField.getText().clear();
                    }
                });


                position = 0;


            }

            @Override
            public void OnCommentError(int ResponseCode, ResponseBody Error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnCommentFailure(Throwable throwable) {
                Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        });


        generalCallBacks.setReplyListener(new ReplyListener() {
            @Override
            public void OnReply(Comment comment, ViewerCommentAdapter RepliesAdapter) {
                CommentEntity commentEntity = new CommentEntity(comment.getId(), comment.getUsername(), null, "meme", mMemeData.getMeme().getId(), comment.getUserId(), comment.getBody(), mMemeData.getMeme().getCreated(), "");

                appDatabase.runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        mainDao.insertComment(commentEntity);
                        mCommentRV.scrollToPosition(0);
                        RepliesAdapter.notifyItemInserted(0);
                        position = 0;
                    }
                });

            }

            @Override
            public void OnReplyError(int ResponseCode, ResponseBody Error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnReplyFailure(Throwable throwable) {
                Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        });


        generalCallBacks.setLikeListener(new LikeListener() {
            @Override
            public void OnLike(boolean isLiked, int LikeCount) {
                if (isLiked) {
                    mLikeBtn.setBackgroundColor(Color.GREEN);
                } else {
                    mLikeBtn.setBackgroundColor(Color.TRANSPARENT);
                }
            }

            @Override
            public void OnLikeError(int ResponseCode, ResponseBody Error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnLikeFailure(Throwable throwable) {
                Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        });


        generalCallBacks.setBookmarkListener(new BookmarkListener() {
            @Override
            public void OnBookmark(boolean isBookmarked) {
                if (isBookmarked) {
                    mFavBtn.setBackgroundColor(Color.GREEN);
                } else {
                    mFavBtn.setBackgroundColor(Color.TRANSPARENT);
                }
            }

            @Override
            public void OnBookmarkError(int ResponseCode, ResponseBody Error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnBookmarkFailure(Throwable throwable) {
                Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        });

        generalCallBacks.setFollowListener(new FollowListener() {
            @Override
            public void OnFollow(boolean isFollowing, String username) {
                if (isFollowing) {
                    mFollowBtn.setText("Unfollow");
                } else {
                    mFollowBtn.setText("Follow");
                }
            }

            @Override
            public void OnFollowError(int ResponseCode, ResponseBody Error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnFollowFailure(Throwable throwable) {
                Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnSearchFailure(Throwable throwable) {
                Toast.makeText(getContext(), "Failure", Toast.LENGTH_SHORT).show();
            }
        });




    }



    private void setMeme() {
        contentTypeString = mMemeData.getMeme().getContentType();
        contentId = mMemeData.getMeme().getId();
        mUsername.setText(mMemeData.getMeme().getUsername());

        mDescription.setText(mMemeData.getMeme().getDescription());

        if (mMemeData.getMeme().is_liked()) {
            mLikeBtn.setBackgroundColor(Color.GREEN);
        } else {
            mLikeBtn.setBackgroundColor(Color.TRANSPARENT);
        }

        if (mMemeData.getMeme().is_bookmarked()) {
            mFavBtn.setBackgroundColor(Color.GREEN);
        } else {
            mFavBtn.setBackgroundColor(Color.TRANSPARENT);
        }


        viewPager2.setAdapter(new ImageSliderAdapter(mMemeData.getImages(), getContext()));

        new TabLayoutMediator(imageTabLayout, viewPager2,
                (tab, pos) -> {

                }).attach();

        mLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generalCallBacks.postLike(mMemeData.getMeme().getContentType(), mMemeData.getMeme().getId());
            }
        });

        mFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generalCallBacks.postBookmark(mMemeData.getMeme().getContentType(), mMemeData.getMeme().getId());
            }
        });

        if (!mMemeData.getMeme().is_following() && mMemeData.getMeme().getUserId() != sharedPreferenceHelper.getUserId()) {
            mFollowBtn.setVisibility(View.VISIBLE);

            mFollowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    generalCallBacks.followUser(mMemeData.getMeme().getUsername(), mMemeData.getMeme().getUserId());
                }
            });
        } else {
            mFollowBtn.setVisibility(View.GONE);
        }

    }

//    private void setTemplate() {
//        contentTypeString = mTemplateData.getContentType();
//        contentId = mTemplateData.getId();
//        mUsername.setText(mTemplateData.getUsername());
//
//        mDescription.setText(mTemplateData.getDescription());
//
//        if (mTemplateData.is_liked()) {
//            mLikeBtn.setBackgroundColor(Color.GREEN);
//        } else {
//            mLikeBtn.setBackgroundColor(Color.TRANSPARENT);
//        }
//
//        if (mTemplateData.is_bookmarked()) {
//            mFavBtn.setBackgroundColor(Color.GREEN);
//        } else {
//            mFavBtn.setBackgroundColor(Color.TRANSPARENT);
//        }
//
//
////        mCommentAdapter.addAll(mTemplateData.getComments());
////        viewPager2.setAdapter(new ImageSliderAdapter(mTemplateData.getImages(), getContext()));
//        new TabLayoutMediator(imageTabLayout, viewPager2,
//                (tab, pos) -> {
//
//                }).attach();
//        mLikeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                generalCallBacks.postLike(mTemplateData.getContentType(), mTemplateData.getId());
//            }
//        });
//
//        mFavBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                generalCallBacks.postBookmark(mTemplateData.getContentType(), mTemplateData.getId());
//            }
//        });
//
//        if (!mTemplateData.is_following() && mTemplateData.getUserId() != sharedPreferenceHelper.getUserId()) {
//            mFollowBtn.setVisibility(View.VISIBLE);
//
//            mFollowBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    generalCallBacks.followUser(mTemplateData.getUsername(), mTemplateData.getUserId());
//                }
//            });
//        } else {
//            mFollowBtn.setVisibility(View.GONE);
//        }
//
//    }

    private void blockUser(MemeDetailModel memeDetailModel) {
        Call<JsonObject> call = restInterface.blockUser(memeDetailModel.getUsername());

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Response Error");
                    return;
                }

                JsonObject jsonObject = response.body();
                boolean is_blocked = jsonObject.get("is_blocked").getAsBoolean();
                Toast.makeText(getContext(), "Blocked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "Failure block");
                Toast.makeText(getContext(), "Failure block", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private PopupMenu getPopMenu(Context context, Button optionBtn, MemeDetailModel memeDetailModel) {
        PopupMenu popup = new PopupMenu(context, optionBtn);
        if (memeDetailModel.getUsername().equals(sharedPreferenceHelper.getUsername())
                &&
                memeDetailModel.getUserId() == sharedPreferenceHelper.getUserId()) {


            popup.getMenuInflater()
                    .inflate(R.menu.meme_option_for_me, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.EditPost:
                            ViewerFragmentDirections.ActionViewerFragmentToEditPostFragment action = ViewerFragmentDirections.actionViewerFragmentToEditPostFragment(memeDetailModel.getId(), ContentType.MEME);
                            Navigation.findNavController(optionBtn).navigate(action);
                            return true;
                        case R.id.DeletePost:
                            deleteMeme(memeDetailModel);
                            return true;
                        case R.id.Share:
                            share(memeDetailModel);
                            return true;

                    }
                    return true;
                }
            });
        } else {
            popup.getMenuInflater()
                    .inflate(R.menu.meme_option, popup.getMenu());

            if (memeDetailModel.is_following()) {
                popup.getMenu().getItem(0).setTitle("Unfollow");

            } else {
                popup.getMenu().getItem(0).setVisible(false);
            }

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.Follow:
                            followUser(memeDetailModel, item);
                            return true;
                        case R.id.Report:
                            FragmentManager fm = getChildFragmentManager();
                            ReportDialogFragment mReportDialogFragment = ReportDialogFragment.newInstance();
                            switch (contentType) {
                                case TEMPLATE:
//                                    mReportDialogFragment.setTemplateId(mTemplateData.getId());
                                    break;
                                case MEME:
                                    mReportDialogFragment.setMemeId(memeDetailModel.getId());
                                    break;

                            }

                            mReportDialogFragment.show(fm, mReportDialogFragment.getTag());
                            return true;
                        case R.id.Block:
                            blockUser(memeDetailModel);
                            return true;

                        case R.id.Share:
                            share(memeDetailModel);
                            return true;

                    }
                    return false;
                }
            });
        }
        return popup;

    }

    private void deleteMeme(MemeDetailModel memeDetailModel) {
        Call<JsonObject> call = restInterface.deleteMeme(memeDetailModel.getId());

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), "Failure block", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void followUser(MemeDetailModel memeDetailModel, MenuItem item) {
        Call<JsonObject> call = restInterface.followUser(memeDetailModel.getUsername());

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
                    item.setTitle("Unfollow");
                } else {
                    item.setVisible(false);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "Failure follow");
                Toast.makeText(getContext(), "Failure follow", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void share(MemeDetailModel memeDetailModel) {


        Glide.with(this)
                .asBitmap()
                .load(memeDetailModel.getImages().get(0).getImageUrl())
                .into((new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("image/jpeg");
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                File f = new File(Environment.getExternalStorageDirectory()
                                        + File.separator + "temporary_file.jpg");
                                try {
                                    f.createNewFile();
                                    FileOutputStream fo = new FileOutputStream(f);
                                    fo.write(bytes.toByteArray());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                share.putExtra(Intent.EXTRA_STREAM,
                                        Uri.parse(f.getPath()));
                                startActivity(Intent.createChooser(share, "Share Image"));
                                share.putExtra(Intent.EXTRA_TEXT, "image from netizen");
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        })

                );


    }
}
