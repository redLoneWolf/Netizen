//package com.sudhar.netizen;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Color;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.widget.PopupMenu;
//import androidx.navigation.Navigation;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.viewpager2.widget.ViewPager2;
//
//import com.bumptech.glide.request.RequestOptions;
//import com.google.android.material.button.MaterialButton;
//import com.google.android.material.tabs.TabLayout;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.sudhar.netizen.NetUtils.RestInterface;
//import com.sudhar.netizen.models.Comment;
//import com.sudhar.netizen.models.MemeDetailModel;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//import static com.sudhar.netizen.Utils.hideSoftKeyboard;
//
//public class MemeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    private List<MemeDetailModel> mMemeList = new ArrayList<>();
//    private Context context;
//    private static final int VIEW_TYPE_LOADING = 0;
//    private static final int VIEW_TYPE_NORMAL = 1;
//    boolean isLoaderVisible = false;
//    private boolean isLoadingAdded = false;
//    private RestInterface restInterface;
//    private final String TAG = "MemeAdapter";
//    RequestOptions options;
//    SharedPreferenceHelper sharedPreferenceHelper;
//    MemeAdapterListener memeAdapterListener;
//
//    public MemeAdapter(Context context, RestInterface restInterface) {
//        this.context = context;
//        this.restInterface = restInterface;
//
//        setHasStableIds(true);
//        options = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.mipmap.ic_launcher_round)
//                .error(R.mipmap.ic_launcher_round);
//        sharedPreferenceHelper = new SharedPreferenceHelper(context);
//    }
//
//    public void setMemeAdapterListener(MemeAdapterListener memeAdapterListener) {
//        this.memeAdapterListener = memeAdapterListener;
//    }
//
//    public interface MemeAdapterListener {
//        void OnReportButtonClick(MemeDetailModel memeDetailModel);
//
//        void OnShareButtonClick(MemeDetailModel memeDetailModel);
//    }
//
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////        View view = LayoutInflater.from(parent.getContext())
////                .inflate(R.layout.layout_card_meme, parent, false);
////        return new MemeVH(view);
//
//        switch (viewType) {
//            case VIEW_TYPE_NORMAL:
//                return new MemeVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_meme, parent, false));
//            case VIEW_TYPE_LOADING:
//                return new LoadingVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.loading, parent, false));
//            default:
//                return null;
//        }
//    }
//
//    //    int i=0;
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        MemeDetailModel item = mMemeList.get(position);
//        switch (getItemViewType(position)) {
//            case VIEW_TYPE_NORMAL:
//
//                MemeVH memeVH = (MemeVH) holder;
//
//
//                memeVH.mUsername.setText(item.getUsername());
//                memeVH.mDescription.setText(item.getDescription());
//
//                memeVH.mCommentAdapter.clear();
//                if (item.getImages() != null) {
////                    ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(item.getImages(), context);
////                    imageSliderAdapter.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////                            MemeFragmentDirections.ActionMemeFragmentToViewerFragment action = MemeFragmentDirections.actionMemeFragmentToViewerFragment(item.getId(), ContentType.MEME);
////                            Navigation.findNavController(v).navigate(action);
////                        }
////                    });
////                    memeVH.viewPager2.setAdapter(imageSliderAdapter);
////
////                    new TabLayoutMediator(memeVH.imageTabLayout, memeVH.viewPager2,
////                            (tab, pos) -> {
////
////                            }).attach();
//                }
//
//
//                if (item.getComments() != null) {
//                    if (memeVH.mCommentAdapter.isEmpty() && !item.getComments().isEmpty()) {
//                        memeVH.mCommentAdapter.addAll(item.getComments());
//
//                    }
//                }
//
//
//                if (item.is_liked()) {
//                    memeVH.mLikeBtn.setBackgroundColor(Color.GREEN);
//                } else {
//                    memeVH.mLikeBtn.setBackgroundColor(Color.TRANSPARENT);
//                }
//
//                if (item.is_bookmarked()) {
//                    memeVH.mFavBtn.setBackgroundColor(Color.GREEN);
//                } else {
//                    memeVH.mFavBtn.setBackgroundColor(Color.TRANSPARENT);
//                }
//
//                if (item.getUserId() == sharedPreferenceHelper.getUserId() && item.getUsername().equals(sharedPreferenceHelper.getUsername())) {
//                    memeVH.mUsername.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            Navigation.findNavController(v).navigate(R.id.action_memeFragment_to_profileFragment);
//                        }
//                    });
//                } else {
//                    memeVH.mUsername.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            MemeFragmentDirections.ActionMemeFragmentToUserFragment action = MemeFragmentDirections.actionMemeFragmentToUserFragment(item.getUsername());
//                            Navigation.findNavController(v).navigate(action);
//                        }
//                    });
//                }
//
//
//
//                break;
//            case VIEW_TYPE_LOADING:
//                Log.d(TAG, "onBindViewHolder: " + "loading");
//                break;
//        }
//
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (isLoaderVisible) {
//            Log.d(TAG, "onBindViewHolder: " + isLoaderVisible);
//            return mMemeList.get(position).getId() == null ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
//        } else {
//            return VIEW_TYPE_NORMAL;
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return mMemeList.size();
//    }
//
//    public void addLoading() {
//        isLoaderVisible = true;
//        mMemeList.add(new MemeDetailModel());
//        notifyItemInserted(mMemeList.size() - 1);
//    }
//
//    public void removeLoading() {
//        isLoaderVisible = false;
//        int position = mMemeList.size() - 1;
//        MemeDetailModel item = getItem(position);
//        if (item != null) {
//            mMemeList.remove(position);
//            notifyItemRemoved(position);
//        }
//    }
//
//    public void append(MemeDetailModel comment) {
//        mMemeList.add(0, comment);
//        notifyItemInserted(getItemCount());
//
//    }
//
//    private void add(MemeDetailModel meme) {
//        mMemeList.add(meme);
//        notifyDataSetChanged();
//    }
//
//    public void addAll(List<MemeDetailModel> memes) {
//        for (MemeDetailModel meme : memes) {
//            add(meme);
//        }
//    }
//
//    public void remove(MemeDetailModel meme) {
//        int position = mMemeList.indexOf(meme);
//        if (position > -1) {
//            mMemeList.remove(position);
//            notifyItemRemoved(position);
//        }
//    }
//
//
//    public MemeDetailModel getItem(int position) {
//        return mMemeList.get(position);
//    }
//
//    public void clear() {
//        isLoadingAdded = false;
//        while (getItemCount() > 0) {
//            remove(getItem(0));
//        }
//    }
//
//    public boolean isEmpty() {
//        return getItemCount() == 0;
//    }
//
//    public class LoadingVH extends RecyclerView.ViewHolder {
//
//        public LoadingVH(@NonNull View itemView) {
//            super(itemView);
//        }
//    }
//
//
//    public class MemeVH extends RecyclerView.ViewHolder {
//        //        ImageView mMemeImageView;
//        TextView mUsername, mDescription;
//        RecyclerView mCommentRV, mMentionRV;
//        MaterialButton optionBtn;
//        MaterialButton mLikeBtn, mFavBtn;
//        CommentAdapter mCommentAdapter;
//        Button mCommentPostBtn;
//        TextInputEditText mCommentField;
//        View mDivider;
//        int pos = 0;
//        UserMentionAdapter userMentionAdapter;
//        String found;
//        ViewPager2 viewPager2;
//        TabLayout imageTabLayout;
//
//        public MemeVH(@NonNull final View itemView) {
//            super(itemView);
//
////            mMemeImageView = itemView.findViewById(R.id.MemeImageView);
//            mUsername = itemView.findViewById(R.id.Username);
//            mDescription = itemView.findViewById(R.id.Description);
//            optionBtn = itemView.findViewById(R.id.PostOptionBtn);
//            mCommentField = itemView.findViewById(R.id.CommentField);
//            mDivider = itemView.findViewById(R.id.divider);
//            mCommentPostBtn = itemView.findViewById(R.id.CommentPostBtn);
//
//            mLikeBtn = itemView.findViewById(R.id.LikeBtn);
//            mFavBtn = itemView.findViewById(R.id.FavBtn);
//
//
//            mMentionRV = itemView.findViewById(R.id.MentiontRv);
//            viewPager2 = itemView.findViewById(R.id.MemeImageViewPager);
//            viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
//            imageTabLayout = itemView.findViewById(R.id.ImageTabLayout);
//
//
//            userMentionAdapter = new UserMentionAdapter();
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
//            mMentionRV.setLayoutManager(linearLayoutManager);
//            userMentionAdapter.setUsernameSelectListener(new UserMentionAdapter.UsernameSelectListener() {
//                @Override
//                public void OnUserSelect(String Username) {
//                    String text = mCommentField.getText().toString();
//
//                    String ne = text.replaceAll(found + "\\b", "@" + Username);
//                    mCommentField.setText(ne);
//                    pos = mCommentField.getText().length() + 1;
//                    mCommentField.setSelection(mCommentField.getText().length());
//                    userMentionAdapter.clear();
//                    mMentionRV.setVisibility(View.GONE);
//
//
//                }
//            });
//            mMentionRV.setAdapter(userMentionAdapter);
//
//            mLikeBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    postLike(mMemeList.get(getLayoutPosition()).getContentType(), mMemeList.get(getLayoutPosition()).getId());
//                }
//            });
//
//            mFavBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    postBookmark(mMemeList.get(getLayoutPosition()).getContentType(), mMemeList.get(getLayoutPosition()).getId());
//                }
//            });
//
//
//            mCommentPostBtn.setEnabled(false);
//            mCommentField.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    String commentInput = mCommentField.getText().toString().trim();
//                    mCommentPostBtn.setEnabled(!commentInput.isEmpty());
//                    try {
//
//
//                        Pattern regex = Pattern.compile("@(\\w)+\\b");
//                        Matcher regexMatcher = regex.matcher(commentInput.substring(pos));
//                        Log.d(TAG, "afterTextChanged: " + pos);
//                        if (regexMatcher.find()) {
//                            found = regexMatcher.group(0);
//                            mMentionRV.setVisibility(View.VISIBLE);
//                            searchUsers(found.substring(1));
//
//
//                        }
//                    } catch (StringIndexOutOfBoundsException e) {
//
//                    }
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });
//            itemView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    hideSoftKeyboard((Activity) context);
//                    return false;
//                }
//            });
//
//
//            optionBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    PopupMenu popup = getPopMenu(context, optionBtn, mMemeList.get(getLayoutPosition()));
//
//
//                    popup.show();
//
////                    FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
////                    PostOptionDialogFragment mPostOptionDialogFragment = PostOptionDialogFragment.newInstance();
////                    mPostOptionDialogFragment.setMemeData(mMemeList.get(getLayoutPosition()));
////                    mPostOptionDialogFragment.show(fm, mPostOptionDialogFragment.getTag());
//
//                }
//            });
//
//
//            mCommentAdapter = new CommentAdapter(context);
//            mCommentRV = itemView.findViewById(R.id.CommentRv);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
//            mCommentRV.setLayoutManager(layoutManager);
//            mCommentRV.setAdapter(mCommentAdapter);
//
////            mCommentAdapter.addAll(mMemeList.get(getLayoutPosition()).getComments());
//            mCommentPostBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    postComment(mCommentField, mMemeList.get(getLayoutPosition()).getId(), mCommentAdapter);
//
//                }
//            });
//
//
//        }
//
//
//        private void postComment(TextInputEditText commentInputField, String id, CommentAdapter commentAdapter) {
//            String commentInput = commentInputField.getText().toString().trim();
//            Call<Comment> call = restInterface.postComment("meme", id, commentInput);
//
//            call.enqueue(new Callback<Comment>() {
//                @Override
//                public void onResponse(@NotNull Call<Comment> call, @NotNull Response<Comment> response) {
//                    if (!response.isSuccessful()) {
//                        Log.d(TAG, "Response Error");
//                        return;
//                    }
//
//                    Comment newComment = response.body();
//                    commentAdapter.append(newComment);
//                    commentInputField.getText().clear();
////                    mCommentRV.scrollToPosition(0);
//                    pos = 0;
//                }
//
//                @Override
//                public void onFailure(@NotNull Call<Comment> call, @NotNull Throwable t) {
//                    Log.d(TAG, "Failure comment");
//                    Toast.makeText(context, "Failure comment", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
//
//        private void postLike(String contentType, String id) {
//
//
//            Call<JsonObject> call = restInterface.postLike(contentType, id);
//
//            call.enqueue(new Callback<JsonObject>() {
//                @Override
//                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                    if (!response.isSuccessful()) {
//                        Log.d(TAG, "Response Error");
//                        return;
//                    }
//                    JsonObject jsonObject = response.body();
//                    boolean is_liked = jsonObject.get("is_liked").getAsBoolean();
//                    int likes = jsonObject.get("likes").getAsInt();
//                    String object_id = jsonObject.get("post").getAsString();
//
//                    if (object_id.equals(id)) {
//                        if (is_liked) {
//                            mLikeBtn.setBackgroundColor(Color.GREEN);
//                        } else {
//                            mLikeBtn.setBackgroundColor(Color.TRANSPARENT);
//                        }
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<JsonObject> call, Throwable t) {
//                    Log.d(TAG, "Failure like");
//                    Toast.makeText(context, "Failure like", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
//
//        private void postBookmark(String contentType, String id) {
//            Call<JsonObject> call = restInterface.postBookmark(contentType, id);
//
//            call.enqueue(new Callback<JsonObject>() {
//                @Override
//                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//
//                    if (!response.isSuccessful()) {
//                        Log.d(TAG, "Response Error");
//                        return;
//                    }
//                    JsonObject jsonObject = response.body();
//                    boolean is_bookmarked = jsonObject.get("is_bookmarked").getAsBoolean();
//
//                    String object_id = jsonObject.get("post").getAsString();
//
//                    if (object_id.equals(id)) {
//                        if (is_bookmarked) {
//                            mFavBtn.setBackgroundColor(Color.GREEN);
//                        } else {
//                            mFavBtn.setBackgroundColor(Color.TRANSPARENT);
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<JsonObject> call, Throwable t) {
//                    Log.d(TAG, "Failure bookmark");
//                    Toast.makeText(context, "Failure bookmark", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//        private void searchUsers(String Username) {
//            Call<JsonObject> call = restInterface.getUsers(Username);
//
//            call.enqueue(new Callback<JsonObject>() {
//                @Override
//                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//
//                    JsonObject jsonObject = response.body();
//
//                    JsonArray jsonArray = jsonObject.getAsJsonArray("results");
////                String usernmae=jsonArray.get(0).getAsJsonObject().get("username").getAsString();
//
//                    List<UserMentionAdapter.DataModel> dataModels = new ArrayList<>();
//                    for (JsonElement jsonElement : jsonArray) {
//                        JsonObject jsonObject1 = jsonElement.getAsJsonObject();
//                        String username = jsonObject1.get("username").getAsString();
//                        dataModels.add(new UserMentionAdapter.DataModel(username, ""));
//
//                    }
//                    userMentionAdapter.clear();
//                    userMentionAdapter.addAll(dataModels);
//
//                }
//
//                @Override
//                public void onFailure(Call<JsonObject> call, Throwable t) {
//                    Log.d(TAG, "Failure follow");
//                    Toast.makeText(context, "user search failure", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//
//    }
//
//
//    private PopupMenu getPopMenu(Context context, Button optionBtn, MemeDetailModel memeDetailModel) {
//        PopupMenu popup = new PopupMenu(context, optionBtn);
//        if (memeDetailModel.getUsername().equals(sharedPreferenceHelper.getUsername())
//                &&
//                memeDetailModel.getUserId() == sharedPreferenceHelper.getUserId()) {
//
//
//            popup.getMenuInflater()
//                    .inflate(R.menu.meme_option_for_me, popup.getMenu());
//
//            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                public boolean onMenuItemClick(MenuItem item) {
//                    switch (item.getItemId()) {
//                        case R.id.EditPost:
//                            MemeFragmentDirections.ActionMemeFragmentToEditPostFragment action = MemeFragmentDirections.actionMemeFragmentToEditPostFragment(memeDetailModel.getId(), ContentType.MEME);
//                            Navigation.findNavController(optionBtn).navigate(action);
//                            return true;
//                        case R.id.DeletePost:
//                            deleteMeme(memeDetailModel);
//                            return true;
//                        case R.id.Share:
//                            memeAdapterListener.OnShareButtonClick(memeDetailModel);
//                            return true;
//
//                    }
//                    return true;
//                }
//            });
//        } else {
//            popup.getMenuInflater()
//                    .inflate(R.menu.meme_option, popup.getMenu());
//
//            if (memeDetailModel.is_following()) {
//                popup.getMenu().getItem(0).setTitle("Unfollow");
//
//            } else {
//                popup.getMenu().getItem(0).setTitle("Follow");
//            }
//
//            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                public boolean onMenuItemClick(MenuItem item) {
//                    switch (item.getItemId()) {
//                        case R.id.Follow:
//                            followUser(memeDetailModel, item);
//                            return true;
//                        case R.id.Report:
//                            memeAdapterListener.OnReportButtonClick(memeDetailModel);
//                            return true;
//                        case R.id.Block:
//                            blockUser(memeDetailModel);
//                            return true;
//
//                        case R.id.Share:
//                            memeAdapterListener.OnShareButtonClick(memeDetailModel);
//                            return true;
//
//                    }
//                    return false;
//                }
//            });
//        }
//        return popup;
//
//    }
//
//    private void deleteMeme(MemeDetailModel memeDetailModel) {
//        Call<JsonObject> call = restInterface.deleteMeme(memeDetailModel.getId());
//
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                Log.d(TAG, "onResponse: " + response.code());
//                if (response.isSuccessful()) {
//                    remove(memeDetailModel);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//            }
//        });
//    }
//
//    private void followUser(MemeDetailModel memeDetailModel, MenuItem item) {
//        Call<JsonObject> call = restInterface.followUser(memeDetailModel.getUsername());
//
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                if (!response.isSuccessful()) {
//                    Log.d(TAG, "Response Error");
//                    return;
//                }
//                JsonObject jsonObject = response.body();
//                boolean is_followed = jsonObject.get("is_following").getAsBoolean();
//                if (is_followed) {
//                    item.setTitle("Unfollow");
//                } else {
//                    item.setTitle("Follow");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Log.d(TAG, "Failure follow");
//                Toast.makeText(context, "Failure follow", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//    private void blockUser(MemeDetailModel memeDetailModel) {
//        Call<JsonObject> call = restInterface.blockUser(memeDetailModel.getUsername());
//
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                if (!response.isSuccessful()) {
//                    Log.d(TAG, "Response Error");
//                    return;
//                }
//
//                JsonObject jsonObject = response.body();
//                boolean is_blocked = jsonObject.get("is_blocked").getAsBoolean();
//                if (is_blocked) {
//                    remove(memeDetailModel);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Log.d(TAG, "Failure block");
//                Toast.makeText(context, "Failure block", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//}
