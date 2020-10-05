package com.sudhar.netizen.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.NetUtils.RetrofitClient;
import com.sudhar.netizen.models.Comment;
import com.sudhar.netizen.models.CommentPagingSource;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class CommentWithoutRoom extends AndroidViewModel {

    Flowable<PagingData<Comment>> FlowablePagedData;

    public CommentWithoutRoom(@NonNull Application application, String contenttype, String objectid) {
        super(application);


        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);

        RestInterface restInterface = RetrofitClient.getClient(application).create(RestInterface.class);

//        Pager<Integer, Comment> pager = Pager<>(
//                new PagingConfig( 10),
//                () -> CommentPagingSource(restInterface, contenttype,objectid));

        Pager<Integer, Comment> pager = new Pager<>(new PagingConfig(10), () -> new CommentPagingSource(restInterface, contenttype, objectid));


        Flowable<PagingData<Comment>> flowable = PagingRx.getFlowable(pager);

        FlowablePagedData = PagingRx.cachedIn(flowable, viewModelScope);

    }

    public Flowable<PagingData<Comment>> getFlowablePagedData() {
        return FlowablePagedData;
    }
}
