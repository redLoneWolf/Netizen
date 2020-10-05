package com.sudhar.netizen.RoomDB.Entities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CommentWithReplies {

    @Embedded
    CommentEntity comment;

    @Relation(entity = CommentEntity.class, parentColumn = "id", entityColumn = "parentId")
    List<CommentEntity> commentWithReplies;

    public CommentEntity getComment() {
        return comment;
    }

    public List<CommentEntity> getCommentWithReplies() {
        return commentWithReplies;
    }

    public CommentWithReplies(CommentEntity comment, List<CommentEntity> commentWithReplies) {
        this.comment = comment;
        this.commentWithReplies = commentWithReplies;
    }

    public static final DiffUtil.ItemCallback<CommentWithReplies> CALLBACK = new DiffUtil.ItemCallback<CommentWithReplies>() {
        @Override
        public boolean areItemsTheSame(@NonNull CommentWithReplies memeDetailModel, @NonNull CommentWithReplies t1) {
            return memeDetailModel.getComment().getId().equals(t1.getComment().getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CommentWithReplies memeDetailModel, @NonNull CommentWithReplies t1) {

            return memeDetailModel.getComment().getId().equals(t1.getComment().getId());
        }
    };
}
