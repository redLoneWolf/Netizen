package com.sudhar.netizen.RoomDB.Entities;

import android.util.Log;

import androidx.paging.LoadType;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxRemoteMediator;

import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.RoomDB.Daos.MainDao;
import com.sudhar.netizen.RoomDB.Daos.RemoteKeyDao;
import com.sudhar.netizen.RoomDB.db.AppDatabase;
import com.sudhar.netizen.models.Comment;
import com.sudhar.netizen.models.ImageModel;
import com.sudhar.netizen.models.MemeDetailModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class RemoteMemeMediator extends RxRemoteMediator<Integer, Meme> {
    private static final String TAG = "RemoteMemeMediator";

    private RestInterface restInterface;
    private AppDatabase database;
    private MainDao memeDao;
    private RemoteKeyDao remoteKeyDao;

    public RemoteMemeMediator(RestInterface restInterface, AppDatabase database) {

        this.restInterface = restInterface;
        this.database = database;
        this.memeDao = database.mainDao();
        this.remoteKeyDao = database.remoteKeyDao();
    }

    @NotNull
    @Override
    public Single<MediatorResult> loadSingle(@NotNull LoadType loadType, @NotNull PagingState<Integer, Meme> pagingState) {

        Single<RemoteKey> remoteKeySingle = null;

        switch (loadType) {
            case REFRESH:

                remoteKeySingle = Single.just(new RemoteKey("meme", 1, false));

                remoteKeyDao.insertOrReplace(new RemoteKey("meme", 1, false));


                break;

            case PREPEND:
                return Single.just(new MediatorResult.Success(true));
            case APPEND:
                remoteKeySingle = remoteKeyDao.remoteKeyByQuerySingle("meme");
                Meme lastItem = pagingState.lastItemOrNull();

                if (lastItem == null) {
                    return Single.just(new MediatorResult.Success(true));
                }

                break;
        }

        return remoteKeySingle
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<RemoteKey, SingleSource<MediatorResult>>() {
                    @Override
                    public SingleSource<MediatorResult> apply(RemoteKey remoteKey) throws Throwable {
                        if (loadType != LoadType.REFRESH && remoteKey.isReachedEnd()) {
                            return Single.just(new MediatorResult.Success(true));
                        }
                        Log.d(TAG, "apply: " + loadType);
                        database.runInTransaction(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });

                        return restInterface.getMemes(remoteKey.getNextKey(), pagingState.getConfig().pageSize)
                                .subscribeOn(Schedulers.io())
                                .map(pagedResponse -> {


                                    database.runInTransaction(() -> {

                                        if (loadType == LoadType.REFRESH) {
                                            memeDao.deleteMemes();
                                            memeDao.deleteComments();
                                            memeDao.deleteImages();
                                            remoteKeyDao.deleteByQuery("meme");
                                        }


                                        List<MemeEntity> memes = new ArrayList<>();
//
                                        for (MemeDetailModel meme : pagedResponse.getResults()) {

                                            memes.add(new MemeEntity(meme.getId(), meme.getDescription(), meme.getUsername(), OffsetDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(meme.getCreated())), meme.getContentType(), meme.is_following(), meme.is_liked(), meme.getLikes(), meme.is_bookmarked(), meme.getUserId(), ""));

                                            List<CommentEntity> comments = new ArrayList<>();

                                            for (Comment comment : meme.getComments()) {
                                                comments.add(new CommentEntity(comment.getId(), comment.getUsername(), null, meme.getContentType(), meme.getId(), comment.getUserId(), comment.getBody(), OffsetDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(comment.getCreated())), ""));
//                                                for (Comment reply : comment.getReplies()) {
//                                                    comments.add(new CommentEntity(reply.getId(), reply.getUsername(), reply.getId(), meme.getContentType(), meme.getId(), reply.getUserId(), reply.getBody(), OffsetDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(reply.getCreated())), ""));
//
//                                                }
                                            }

                                            List<ImageModelEntity> images = new ArrayList<>();
                                            for (ImageModel image : meme.getImages()) {
                                                images.add(new ImageModelEntity(image.getId(), meme.getId(), meme.getContentType(), image.getImageUrl(), OffsetDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(meme.getCreated())), ""));
                                            }
                                            memeDao.insertComments(comments);
                                            memeDao.insertImages(images);
                                        }

                                        memeDao.insertMemes(memes);
                                        if (pagedResponse.getNextPage() != null) {
                                            remoteKeyDao.insertOrReplace(new RemoteKey("meme", remoteKey.getNextKey() + 1, false));
                                        } else {
                                            remoteKeyDao.insertOrReplace(new RemoteKey("meme", remoteKey.getNextKey(), true));
                                        }


                                    });
                                    return new MediatorResult.Success(pagedResponse.getNextPage() == null);
                                });

                    }
                }).onErrorResumeNext(throwable -> {
                    if (throwable instanceof IOException || throwable instanceof HttpException) {
                        return Single.just(new MediatorResult.Error(throwable));
                    }

                    return Single.error(throwable);
                });


    }
}


