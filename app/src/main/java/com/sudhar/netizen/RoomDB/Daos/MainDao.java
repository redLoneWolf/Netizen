package com.sudhar.netizen.RoomDB.Daos;

import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.sudhar.netizen.RoomDB.Entities.CommentEntity;
import com.sudhar.netizen.RoomDB.Entities.CommentWithReplies;
import com.sudhar.netizen.RoomDB.Entities.ImageModelEntity;
import com.sudhar.netizen.RoomDB.Entities.Meme;
import com.sudhar.netizen.RoomDB.Entities.MemeEntity;
import com.sudhar.netizen.RoomDB.Entities.Template;
import com.sudhar.netizen.RoomDB.Entities.TemplateEntity;

import java.util.List;

@Dao
public interface MainDao {

    //    General

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComment(CommentEntity memeEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImage(ImageModelEntity memeEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImages(List<ImageModelEntity> imageModelEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComments(List<CommentEntity> commentEntities);


    @Query("DELETE  FROM images_table")
    void deleteImages();

    @Query("DELETE  FROM comment_table")
    void deleteComments();

    @Query("SELECT * FROM comment_table WHERE ObjectId = :objectId AND ContentType = :contentType AND parentId=null ORDER BY created desc LIMIT 3 ")
    List<CommentEntity> getComments(String objectId, String contentType);

    @Query("SELECT * FROM images_table WHERE ObjectId = :objectId AND ContentType = :contentType")
    List<ImageModelEntity> getImages(String objectId, String contentType);

    @Query("SELECT * FROM comment_table")
    List<CommentWithReplies> getc();

    @Query("SELECT * FROM comment_table where parentId=:id")
    List<CommentEntity> getReplies(String id);


    @Query("SELECT * FROM comment_table WHERE ObjectId = :objectId AND ContentType = :contentType")
    PagingSource<Integer, CommentEntity> getCommentsSource(String objectId, String contentType);


    @Query("SELECT * FROM comment_table WHERE ObjectId = :objectId AND ContentType = :contentType AND parentId is NULL")
    PagingSource<Integer, CommentWithReplies> getCommentsReplySource(String objectId, String contentType);

    //    MEME

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeme(MemeEntity memeEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMemes(List<MemeEntity> memeEntities);

    @Query("SELECT * FROM meme_table where id = :id")
    MemeEntity getMemeById(String id);

    @Query("SELECT * FROM meme_table")
    List<MemeEntity> getMemeList();


    @Transaction
    @Query("SELECT * FROM meme_table")
    PagingSource<Integer, Meme> getMemeSource();

    @Transaction
    @Query("SELECT * FROM meme_table where id = :id")
    Meme getMemeAllById(String id);

    @Query("DELETE  FROM meme_table")
    void deleteMemes();

    @Query("UPDATE meme_table SET is_liked=:is_liked,likes =:likes WHERE id = :id")
    void updateLike(boolean is_liked, int likes, String id);

    @Query("UPDATE meme_table SET is_bookmarked=:is_bookmarked WHERE id = :id")
    void updateBookmark(boolean is_bookmarked, String id);

    @Query("UPDATE meme_table SET is_following=:is_following WHERE username=:username")
    void updateFollow(boolean is_following, String username);

//    Template

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTemplate(TemplateEntity templateEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTemplates(List<TemplateEntity> templateEntities);

    @Query("SELECT * FROM template_table where id = :id")
    TemplateEntity getTemplateById(String id);

    @Query("SELECT * FROM template_table")
    List<TemplateEntity> getTemplateList();


    @Transaction
    @Query("SELECT * FROM template_table")
    PagingSource<Integer, Template> getTemplateSource();

    @Query("DELETE  FROM template_table")
    void deleteTemplates();


}
