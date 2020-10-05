package com.sudhar.netizen.ViewModels;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.NetUtils.RetrofitClient;
import com.sudhar.netizen.RoomDB.Daos.MainDao;
import com.sudhar.netizen.RoomDB.Entities.Meme;
import com.sudhar.netizen.RoomDB.Entities.RemoteMemeMediator;
import com.sudhar.netizen.RoomDB.db.AppDatabase;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;



public class MemeViewModel extends AndroidViewModel {


    Flowable<PagingData<Meme>> FlowablePagedData;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public MemeViewModel(@NonNull Application application) {
        super(application);

        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);

        AppDatabase database = AppDatabase.getInstance(application);
        MainDao mainDao = database.mainDao();
        RestInterface restInterface = RetrofitClient.getClient(application).create(RestInterface.class);
        Pager<Integer, Meme> pager = new Pager<Integer, Meme>(
                new PagingConfig(10, 1, true), null, new RemoteMemeMediator(restInterface, database),
                mainDao::getMemeSource);


        Flowable<PagingData<Meme>> flowable = PagingRx.getFlowable(pager);

        FlowablePagedData = PagingRx.cachedIn(flowable, viewModelScope);


    }

    public Flowable<PagingData<Meme>> getFlowablePagedData() {
        return FlowablePagedData;
    }


}
