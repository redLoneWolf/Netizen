package com.sudhar.netizen.RoomDB.Entities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class Meme {

    @Embedded
    public MemeEntity meme;

    @Relation(entity = ImageModelEntity.class, parentColumn = "identifier", entityColumn = "identifier")
    public List<ImageModelEntity> images;

    @Relation(entity = CommentEntity.class, parentColumn = "identifier", entityColumn = "identifier")
    public List<CommentEntity> comments;

    public MemeEntity getMeme() {
        return meme;
    }

    public List<ImageModelEntity> getImages() {
        return images;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public static final DiffUtil.ItemCallback<Meme> CALLBACK = new DiffUtil.ItemCallback<Meme>() {
        @Override
        public boolean areItemsTheSame(@NonNull Meme memeDetailModel, @NonNull Meme t1) {
            return memeDetailModel.getMeme().getId().equals(t1.getMeme().getId());
        }


        @Override
        public boolean areContentsTheSame(@NonNull Meme memeDetailModel, @NonNull Meme t1) {

            return memeDetailModel.getMeme().getId().equals(t1.getMeme().getId());
        }
    };
}
