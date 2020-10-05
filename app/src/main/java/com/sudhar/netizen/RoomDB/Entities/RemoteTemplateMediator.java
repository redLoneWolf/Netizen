package com.sudhar.netizen.RoomDB.Entities;

import androidx.paging.LoadType;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxRemoteMediator;

import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.RoomDB.Daos.MainDao;
import com.sudhar.netizen.RoomDB.Daos.RemoteKeyDao;
import com.sudhar.netizen.RoomDB.db.AppDatabase;
import com.sudhar.netizen.models.Comment;
import com.sudhar.netizen.models.ImageModel;
import com.sudhar.netizen.models.TemplateDetailModel;

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

public class RemoteTemplateMediator extends RxRemoteMediator<Integer, Template> {

    private RestInterface restInterface;
    private AppDatabase database;
    private MainDao mainDao;
    private RemoteKeyDao remoteKeyDao;


    public RemoteTemplateMediator(RestInterface restInterface, AppDatabase database) {
        this.restInterface = restInterface;
        this.database = database;
        this.mainDao = database.mainDao();
        this.remoteKeyDao = database.remoteKeyDao();
    }

    @NotNull
    @Override
    public Single<MediatorResult> loadSingle(@NotNull LoadType loadType, @NotNull PagingState<Integer, Template> pagingState) {
        Single<RemoteKey> remoteKeySingle = null;

        switch (loadType) {
            case REFRESH:

                remoteKeySingle = Single.just(new RemoteKey("template", 1, false));

                remoteKeyDao.insertOrReplace(new RemoteKey("template", 1, false));


                break;

            case PREPEND:
                return Single.just(new MediatorResult.Success(true));
            case APPEND:
                remoteKeySingle = remoteKeyDao.remoteKeyByQuerySingle("template");
                Template lastItem = pagingState.lastItemOrNull();

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


                        return restInterface.getTemplates(remoteKey.getNextKey(), pagingState.getConfig().pageSize)
                                .subscribeOn(Schedulers.io())
                                .map(pagedResponse -> {


                                    database.runInTransaction(() -> {
                                        if (loadType == LoadType.REFRESH) {
                                            mainDao.deleteTemplates();
                                            mainDao.deleteComments();
                                            mainDao.deleteImages();
                                            remoteKeyDao.deleteByQuery("meme");
                                        }


                                        List<TemplateEntity> templates = new ArrayList<>();

                                        for (TemplateDetailModel template : pagedResponse.getResults()) {

                                            templates.add(new TemplateEntity(template.getId(), template.getDescription(), template.getUsername(), OffsetDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(template.getCreated())), template.getContentType(), template.is_following(), template.is_liked(), template.getLikes(), template.is_bookmarked(), template.getUserId(), ""));

                                            List<CommentEntity> comments = new ArrayList<>();

                                            if (template.getComments() != null) {
                                                for (Comment comment : template.getComments()) {
                                                    comments.add(new CommentEntity(comment.getId(), comment.getUsername(), null, template.getContentType(), template.getId(), comment.getUserId(), comment.getBody(), OffsetDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(comment.getCreated())), ""));
                                                }
                                                mainDao.insertComments(comments);
                                            }

                                            List<ImageModelEntity> images = new ArrayList<>();
                                            for (ImageModel image : template.getImages()) {
                                                images.add(new ImageModelEntity(image.getId(), template.getId(), template.getContentType(), image.getImageUrl(), OffsetDateTime.from(DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(template.getCreated())), ""));
                                            }

                                            mainDao.insertImages(images);
                                        }

                                        mainDao.insertTemplates(templates);
                                        if (pagedResponse.getNextPage() != null) {
                                            remoteKeyDao.insertOrReplace(new RemoteKey("template", remoteKey.getNextKey() + 1, false));
                                        } else {
                                            remoteKeyDao.insertOrReplace(new RemoteKey("template", remoteKey.getNextKey(), true));
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
