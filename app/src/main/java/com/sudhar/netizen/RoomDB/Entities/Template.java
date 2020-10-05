package com.sudhar.netizen.RoomDB.Entities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class Template {

    @Embedded
    public TemplateEntity template;

    @Relation(entity = ImageModelEntity.class, parentColumn = "identifier", entityColumn = "identifier")
    public List<ImageModelEntity> images;

    @Relation(entity = CommentEntity.class, parentColumn = "identifier", entityColumn = "identifier")
    public List<CommentEntity> comments;


    public TemplateEntity getTemplate() {
        return template;
    }

    public List<ImageModelEntity> getImages() {
        return images;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public static final DiffUtil.ItemCallback<Template> CALLBACK = new DiffUtil.ItemCallback<Template>() {
        @Override
        public boolean areItemsTheSame(@NonNull Template template, @NonNull Template t1) {
            return template.getTemplate().getId().equals(t1.getTemplate().getId());
        }


        @Override
        public boolean areContentsTheSame(@NonNull Template template, @NonNull Template t1) {

            return template.getTemplate().getId().equals(t1.getTemplate().getId());
        }
    };
}
