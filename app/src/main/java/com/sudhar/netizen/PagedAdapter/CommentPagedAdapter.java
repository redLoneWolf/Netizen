package com.sudhar.netizen.PagedAdapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sudhar.netizen.CommentAdapter;
import com.sudhar.netizen.MemeFragmentDirections;
import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.NetUtils.RetrofitClient;
import com.sudhar.netizen.R;
import com.sudhar.netizen.RoomDB.Daos.MainDao;
import com.sudhar.netizen.RoomDB.Entities.CommentEntity;
import com.sudhar.netizen.RoomDB.Entities.CommentWithReplies;
import com.sudhar.netizen.RoomDB.db.AppDatabase;
import com.sudhar.netizen.SharedPreferenceHelper;
import com.sudhar.netizen.ViewerFragmentDirections;
import com.sudhar.netizen.models.Comment;
import com.sudhar.netizen.models.PagedResponse;

import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentPagedAdapter extends PagingDataAdapter<CommentWithReplies, CommentPagedAdapter.FullCommentVH> {

    private static final String TAG = "CommentPagedAdapter";
    Context context;
    SharedPreferenceHelper sharedPreferenceHelper;

    ReplyButtonListener replyButtonListener;

    public void setReplyButtonListener(ReplyButtonListener replyButtonListener) {
        this.replyButtonListener = replyButtonListener;
    }

    public interface ReplyButtonListener {
        void OnReplyButtonClick(CommentAdapter RepliesAdapter, String ParentId, String Username, String Body);
    }


    public CommentPagedAdapter(Context context) {
        super(CommentWithReplies.CALLBACK);
        this.context = context;
        sharedPreferenceHelper = new SharedPreferenceHelper(context);
    }


    public SpannableStringBuilder makeSpannable(String text, String regex, int itemType) {

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


            spannable.setSpan(getClickableSpan(itemType, group.substring(1)), start, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        sb.setLength(0);
        matcher.appendTail(sb);
        spannable.append(sb.toString());
        return spannable;
    }

    private ClickableSpan getClickableSpan(int itemType, String group) {
        ClickableSpan clickableSpan = null;


        if (group.equals(sharedPreferenceHelper.getUsername())) {
            clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Navigation.findNavController(widget).navigate(R.id.action_viewerFragment_to_profileFragment);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.BLUE);
                    ds.setUnderlineText(false);
                }
            };
        } else {
            clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    ViewerFragmentDirections.ActionViewerFragmentToUserFragment action = ViewerFragmentDirections.actionViewerFragmentToUserFragment(group);
                    Log.d(TAG, "onClick: " + group);
                    Navigation.findNavController(widget).navigate(action);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.BLUE);
                    ds.setUnderlineText(false);
                }
            };
        }


        return clickableSpan;
    }


    @NonNull
    @Override
    public FullCommentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FullCommentVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FullCommentVH holder, int position) {

        CommentWithReplies item = getItem(position);
        if (item != null) {

            CommentEntity comment = item.getComment();

            holder.mUsername.setText(item.getComment().getUsername());

            SpannableStringBuilder ss = makeSpannable(comment.getBody(), "@(\\w)+\\b", 0);

            holder.mComment.setText(ss);
            holder.mComment.setMovementMethod(LinkMovementMethod.getInstance());

            if (comment.getUserId() == sharedPreferenceHelper.getUserId() && comment.getUsername().equals(sharedPreferenceHelper.getUsername())) {

                holder.mUsername.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Navigation.findNavController(v).navigate(R.id.action_viewerFragment_to_profileFragment);
                    }
                });
            } else {
                holder.mUsername.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewerFragmentDirections.ActionViewerFragmentToUserFragment action = ViewerFragmentDirections.actionViewerFragmentToUserFragment(comment.getUsername());
                        Navigation.findNavController(v).navigate(action);
                    }
                });
            }


//            if (item.getCommentWithReplies() != null) {
//                CommentAdapter commentAdapter = new CommentAdapter(context, 0);
//
//                commentAdapter.setLayoutType("full");
//
//                commentAdapter.setCommentList(item.getCommentWithReplies());
//
//                LinearLayoutManager llmTools = new LinearLayoutManager(context);
//                holder.mRepliesRV.setLayoutManager(llmTools);
//                holder.mRepliesRV.setAdapter(commentAdapter);
//
//
//            }


        }
    }


    public class FullCommentVH extends RecyclerView.ViewHolder {

        TextView mUsername, mComment, mCommentRepliesViewBtn, mCommentReplyBtn;
        RecyclerView mRepliesRV;

        public FullCommentVH(@NonNull View itemView) {
            super(itemView);
            mUsername = itemView.findViewById(R.id.Username);
            mComment = itemView.findViewById(R.id.CommentBody);
            mRepliesRV = itemView.findViewById(R.id.RepliesRv);
            mRepliesRV.setVisibility(View.GONE);

            mCommentRepliesViewBtn = itemView.findViewById(R.id.CommentRepliesViewBtn);
            mCommentReplyBtn = itemView.findViewById(R.id.CommentReplyBtn);


//            mCommentRepliesViewBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if (mRepliesRV.getVisibility() == View.GONE) {
//                        mRepliesRV.setVisibility(View.VISIBLE);
//                        CommentEntity commentEntity = getItem(getLayoutPosition()).getComment();
//                        Call<PagedResponse<Comment>> call = RetrofitClient.getClient(context).create(RestInterface.class).getComments(null, null, commentEntity.getContentType(), commentEntity.getObjectId(), commentEntity.getId());
//
//                        call.enqueue(new Callback<PagedResponse<Comment>>() {
//                            @Override
//                            public void onResponse(Call<PagedResponse<Comment>> call, Response<PagedResponse<Comment>> response) {
//                                if (!response.isSuccessful()) {
//                                    return;
//                                }
//                                CommentAdapter commentAdapter = new CommentAdapter(context, 0);
//                                commentAdapter.setLayoutType("full");
//                                commentAdapter.setReplyButtonListener(replyButtonListener);
//                                List<CommentEntity> comments = new ArrayList<>();
//
//                                for (Comment comment : response.body().getResults()) {
//
//                                    comments.add(new CommentEntity(comment.getId(), comment.getUsername(), null, "meme", commentEntity.getId(), comment.getUserId(), comment.getBody(), OffsetDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(comment.getCreated())), ""));
//
//                                }
//
//                                commentAdapter.setCommentList(comments);
//                                mRepliesRV.setAdapter(commentAdapter);
//
//                            }
//
//                            @Override
//                            public void onFailure(Call<PagedResponse<Comment>> call, Throwable t) {
//
//                            }
//                        });
//
//                    } else {
//                        mRepliesRV.setVisibility(View.GONE);
//                    }
//
//                }
//            });


            mCommentReplyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    String Username = getItem(position).getComment().getUsername();
                    String Body = getItem(position).getComment().getBody();
                    String CommentId = getItem(position).getComment().getId();

                    replyButtonListener.OnReplyButtonClick((CommentAdapter) mRepliesRV.getAdapter(), CommentId, Username, Body);
                }
            });


        }
    }
}
