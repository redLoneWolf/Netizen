package com.sudhar.netizen.ViewModels;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RoomFac implements ViewModelProvider.Factory {

    private Application mApplication;
    //    private String ctype;
    private String id;


    public RoomFac(Application mApplication, String id) {
        this.mApplication = mApplication;
//        this.ctype = ctype;
        this.id = id;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new CommentViewModel(mApplication, id);
    }
}
