package com.sudhar.netizen.models;

import androidx.annotation.NonNull;
import androidx.paging.rxjava3.RxPagingSource;

import com.sudhar.netizen.NetUtils.RestInterface;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CommentPagingSource extends RxPagingSource<Integer, Comment> {
    @NonNull
    private RestInterface restInterface;

    @NonNull
    private String mContentType;

    @NonNull
    private String mObjectId;

//    @NonNull
//    private String mParentId;


    public CommentPagingSource(@NonNull RestInterface restInterface, @NonNull String mContentType, @NonNull String mObjectId) {
        this.restInterface = restInterface;
        this.mContentType = mContentType;
        this.mObjectId = mObjectId;
    }

    Integer nextPageNumber;

    @NotNull
    @Override
    public Single<LoadResult<Integer, Comment>> loadSingle(@NotNull LoadParams<Integer> loadParams) {
        nextPageNumber = loadParams.getKey();
        if (nextPageNumber == null) {
            nextPageNumber = 1;
        }

        return restInterface.getCommentsSingle(nextPageNumber, loadParams.getLoadSize(), mContentType, mObjectId, null).subscribeOn(Schedulers.io())
                .map(commentPagedResponse -> toLoadResult(commentPagedResponse, nextPageNumber + 1))
                .onErrorReturn(LoadResult.Error::new);

    }

    private LoadResult<Integer, Comment> toLoadResult(@NonNull PagedResponse<Comment> response, Integer nextPageNumber) {
        if (response.getNextPage() == null) {
            nextPageNumber = null;
        }


        return new LoadResult.Page<>(
                response.getResults(),
                null, // Only paging forward.
                nextPageNumber,
                LoadResult.Page.COUNT_UNDEFINED,
                LoadResult.Page.COUNT_UNDEFINED);
    }


}
