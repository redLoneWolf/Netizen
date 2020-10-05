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

public class RemoteCommentMediator extends RxRemoteMediator<Integer, CommentWithReplies> {
    private static final String TAG = "RemoteCommentMediator";
    private RestInterface restInterface;
    private AppDatabase database;
    private MainDao memeDao;
    private RemoteKeyDao remoteKeyDao;
    private String id;

    public RemoteCommentMediator(RestInterface restInterface, AppDatabase database, String id) {
        this.restInterface = restInterface;
        this.database = database;
        this.memeDao = database.mainDao();
        this.remoteKeyDao = database.remoteKeyDao();
        this.id = id;


    }

    @NotNull
    @Override
    public Single<MediatorResult> loadSingle(@NotNull LoadType loadType, @NotNull PagingState<Integer, CommentWithReplies> pagingState) {

        Single<RemoteKey> remoteKeySingle = null;

        switch (loadType) {
            case REFRESH:

                remoteKeySingle = Single.just(new RemoteKey("comment", 1, false));

                remoteKeyDao.insertOrReplace(new RemoteKey("comment", 1, false));


                break;

            case PREPEND:
                return Single.just(new MediatorResult.Success(true));
            case APPEND:
                remoteKeySingle = remoteKeyDao.remoteKeyByQuerySingle("comment");
                CommentWithReplies lastItem = pagingState.lastItemOrNull();

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


                        return restInterface.getCommentsSingle(remoteKey.getNextKey(), pagingState.getConfig().pageSize, "meme", id, null)
                                .subscribeOn(Schedulers.io())
                                .map(pagedResponse -> {


                                    database.runInTransaction(() -> {

                                        if (loadType == LoadType.REFRESH) {

                                                memeDao.deleteComments();

                                            remoteKeyDao.deleteByQuery("comment");
                                        }



                                        List<CommentEntity> comments = new ArrayList<>();

                                        for (Comment comment : pagedResponse.getResults()) {

                                            comments.add(new CommentEntity(comment.getId(), comment.getUsername(), null, "meme", id, comment.getUserId(), comment.getBody(), OffsetDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(comment.getCreated())), ""));

                                        }


                                        memeDao.insertComments(comments);


                                        if (pagedResponse.getNextPage() != null) {
                                            remoteKeyDao.insertOrReplace(new RemoteKey("comment", remoteKey.getNextKey() + 1, false));
                                        } else {
                                            remoteKeyDao.insertOrReplace(new RemoteKey("comment", remoteKey.getNextKey(), true));
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
