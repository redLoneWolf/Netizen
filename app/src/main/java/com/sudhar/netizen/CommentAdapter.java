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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sudhar.netizen.PagedAdapter.CommentPagedAdapter;
import com.sudhar.netizen.RoomDB.Daos.MainDao;
import com.sudhar.netizen.RoomDB.Entities.CommentEntity;
import com.sudhar.netizen.RoomDB.db.AppDatabase;
import com.sudhar.netizen.models.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "CommentAdapter";
    List<CommentEntity> mCommentList = new ArrayList<>();
    Context context;
    private static final int VIEW_TYPE_NO_COMMENTS = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int FULL_LAYOUT_TYPE = 2;
    private static final int COMMENT_ONLY_LAYOUT_TYPE = 3;
    private CommentPagedAdapter.ReplyButtonListener replyButtonListener;

    private String layoutType;
    SharedPreferenceHelper sharedPreferenceHelper;

    int MAX;

    public CommentAdapter(Context context, int MAX) {
        setHasStableIds(true);
        this.context = context;
        sharedPreferenceHelper = new SharedPreferenceHelper(context);
        this.MAX = MAX;

    }

    public void setCommentList(List<CommentEntity> mCommentList) {
        this.mCommentList = mCommentList;
        if (mCommentList.size() > 0 && mCommentList.size() > 3 && MAX != 0) {
            this.mCommentList = mCommentList.subList(0, 3);
        }
        notifyDataSetChanged();
    }


    public void setReplyButtonListener(CommentPagedAdapter.ReplyButtonListener replyButtonListener) {
        this.replyButtonListener = replyButtonListener;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                return new CommentOnlyVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_only_item, parent, false));


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


                CommentEntity comment = mCommentList.get(position);
                CommentOnlyVH commentVH = (CommentOnlyVH) holder;

                commentVH.mUsername.setText(comment.getUsername());

                if (comment.getUserId() == sharedPreferenceHelper.getUserId() && comment.getUsername().equals(sharedPreferenceHelper.getUsername())) {
                    commentVH.mUsername.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Navigation.findNavController(v).navigate(R.id.action_memeFragment_to_profileFragment);
                        }
                    });
                } else {
                    commentVH.mUsername.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MemeFragmentDirections.ActionMemeFragmentToUserFragment action = MemeFragmentDirections.actionMemeFragmentToUserFragment(comment.getUsername());
                            Navigation.findNavController(v).navigate(action);
                        }
                    });
                }

                SpannableStringBuilder spannableString = makeSpannable(comment.getBody(), "@(\\w)+\\b", COMMENT_ONLY_LAYOUT_TYPE);

                commentVH.mComment.setText(spannableString, TextView.BufferType.SPANNABLE);

                commentVH.mComment.setMovementMethod(LinkMovementMethod.getInstance());





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

        switch (itemType) {
            case COMMENT_ONLY_LAYOUT_TYPE:
                if (group.equals(sharedPreferenceHelper.getUsername())) {
                    clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(@NonNull View widget) {
                            Navigation.findNavController(widget).navigate(R.id.action_memeFragment_to_profileFragment);
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
                            MemeFragmentDirections.ActionMemeFragmentToUserFragment action = MemeFragmentDirections.actionMemeFragmentToUserFragment(group);
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
                break;
            case FULL_LAYOUT_TYPE:
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
                break;
            case VIEW_TYPE_NO_COMMENTS:
                break;

        }
        return clickableSpan;
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {

        return mCommentList.size();

    }

    void add(CommentEntity comment) {
        mCommentList.add(comment);

    }

    public void append(CommentEntity comment) {
        mCommentList.add(getItemCount(), comment);
        notifyItemInserted(getItemCount());

    }

    public void addAll(List<CommentEntity> comments) {
        for (CommentEntity comment : comments) {
            add(comment);
        }
        notifyDataSetChanged();
    }

    public void remove(CommentEntity comment) {
        int position = mCommentList.indexOf(comment);
        if (position > -1) {
            mCommentList.remove(position);
            notifyItemRemoved(position);
        }
    }


    public CommentEntity getItem(int position) {
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


    class CommentOnlyVH extends RecyclerView.ViewHolder {

        TextView mUsername, mComment;

        public CommentOnlyVH(@NonNull View itemView) {
            super(itemView);
            mUsername = itemView.findViewById(R.id.Username);
            mComment = itemView.findViewById(R.id.CommentBody);
        }
    }


}
