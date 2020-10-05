package com.sudhar.netizen.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingSource;
import androidx.paging.rxjava3.PagingRx;

import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.NetUtils.RetrofitClient;
import com.sudhar.netizen.RoomDB.Daos.MainDao;
import com.sudhar.netizen.RoomDB.Entities.CommentEntity;
import com.sudhar.netizen.RoomDB.Entities.CommentWithReplies;
import com.sudhar.netizen.RoomDB.Entities.Meme;
import com.sudhar.netizen.RoomDB.Entities.RemoteCommentMediator;
import com.sudhar.netizen.RoomDB.Entities.RemoteMemeMediator;
import com.sudhar.netizen.RoomDB.Entities.Template;
import com.sudhar.netizen.RoomDB.db.AppDatabase;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class CommentViewModel extends AndroidViewModel {

    Flowable<PagingData<CommentWithReplies>> FlowablePagedData;


    public CommentViewModel(@NonNull Application application, String id) {
        super(application);

        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);


        AppDatabase database = AppDatabase.getInstance(application);
        MainDao mainDao = database.mainDao();
        RestInterface restInterface = RetrofitClient.getClient(application).create(RestInterface.class);
        RemoteCommentMediator remoteCommentMediator = new RemoteCommentMediator(restInterface, database, id);


        Pager<Integer, CommentWithReplies> pager = new Pager<Integer, CommentWithReplies>(
                new PagingConfig(10, 1, true), null, remoteCommentMediator,
                () -> mainDao.getCommentsReplySource(id, "meme"));


        Flowable<PagingData<CommentWithReplies>> flowable = PagingRx.getFlowable(pager);

        FlowablePagedData = PagingRx.cachedIn(flowable, viewModelScope);

    }

    public Flowable<PagingData<CommentWithReplies>> getFlowablePagedData() {
        return FlowablePagedData;
    }


}
