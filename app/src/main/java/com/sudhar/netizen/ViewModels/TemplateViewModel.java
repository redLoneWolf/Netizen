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
import com.sudhar.netizen.RoomDB.Entities.RemoteTemplateMediator;
import com.sudhar.netizen.RoomDB.Entities.Template;
import com.sudhar.netizen.RoomDB.db.AppDatabase;


import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class TemplateViewModel extends AndroidViewModel {

    Flowable<PagingData<Template>> FlowablePagedData;



    @RequiresApi(api = Build.VERSION_CODES.O)
    public TemplateViewModel(@NonNull Application application) {
        super(application);

        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);

        AppDatabase database = AppDatabase.getInstance(application);
        MainDao mainDao = database.mainDao();
        RestInterface restInterface = RetrofitClient.getClient(application).create(RestInterface.class);
        Pager<Integer, Template> pager = new Pager<Integer, Template>(
                new PagingConfig(10, 1, true), null, new RemoteTemplateMediator( restInterface, database),
                mainDao::getTemplateSource);


        Flowable<PagingData<Template>> flowable = PagingRx.getFlowable(pager);

        FlowablePagedData = PagingRx.cachedIn(flowable, viewModelScope);


    }

    public Flowable<PagingData<Template>> getFlowablePagedData() {
        return FlowablePagedData;
    }
}
