package com.sudhar.netizen.ViewModels;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class MyViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String ctype;
    private String id;


    public MyViewModelFactory(Application mApplication, String ctype, String id) {
        this.mApplication = mApplication;
        this.ctype = ctype;
        this.id = id;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new CommentWithoutRoom(mApplication, ctype, id);
    }
}
