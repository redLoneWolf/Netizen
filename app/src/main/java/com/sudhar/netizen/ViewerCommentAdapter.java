package com.sudhar.netizen;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.sudhar.netizen.models.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewerCommentAdapter extends RecyclerView.Adapter<ViewerCommentAdapter.FullCommentVH> {
    private static final String TAG = "ViewerCommentFragment";
    List<Comment> mCommentList;
    SharedPreferenceHelper sharedPreferenceHelper;
    Context context;

    public ViewerCommentAdapter(Context context, List<Comment> comments) {
        this.sharedPreferenceHelper = new SharedPreferenceHelper(context);
        this.context = context;
        this.mCommentList = comments;
        setHasStableIds(true);
    }


    @NonNull
    @Override
    public FullCommentVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FullCommentVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FullCommentVH holder, int position) {
        Comment comment = mCommentList.get(position);

        holder.mUsername.setText(comment.getUsername());


        SpannableStringBuilder ss = makeSpannable(comment.getBody(), "@(\\w)+\\b");

        holder.mComment.setText(ss);

        holder.mComment.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    void add(Comment comment) {
        mCommentList.add(comment);

    }

    public void append(Comment comment) {
        mCommentList.add(getItemCount(), comment);
        notifyItemInserted(getItemCount());

    }

    public void addAll(List<Comment> comments) {
        for (Comment comment : comments) {
            add(comment);
        }
        notifyDataSetChanged();
    }

    public void remove(Comment comment) {
        int position = mCommentList.indexOf(comment);
        if (position > -1) {
            mCommentList.remove(position);
            notifyItemRemoved(position);
        }
    }


    public Comment getItem(int position) {
        return mCommentList.get(position);
    }

    public void clear() {

        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
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


            spannable.setSpan(getClickableSpan(group.substring(1)), start, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        sb.setLength(0);
        matcher.appendTail(sb);
        spannable.append(sb.toString());
        return spannable;
    }

    private ClickableSpan getClickableSpan(String group) {
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

    public class FullCommentVH extends RecyclerView.ViewHolder {

        TextView mUsername, mComment, mCommentRepliesViewBtn, mCommentReplyBtn;
        RecyclerView mRepliesRV;

        public FullCommentVH(@NonNull View itemView) {
            super(itemView);
            mUsername = itemView.findViewById(R.id.Username);
            mComment = itemView.findViewById(R.id.CommentBody);

            mCommentRepliesViewBtn = itemView.findViewById(R.id.CommentRepliesViewBtn);
            mCommentRepliesViewBtn.setVisibility(View.GONE);
            mCommentReplyBtn = itemView.findViewById(R.id.CommentReplyBtn);


        }
    }

}
