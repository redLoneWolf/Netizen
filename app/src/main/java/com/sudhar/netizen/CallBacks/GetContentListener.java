package com.sudhar.netizen.CallBacks;

import com.sudhar.netizen.ContentType;
import com.sudhar.netizen.RoomDB.Entities.MemeEntity;
import com.sudhar.netizen.RoomDB.Entities.TemplateEntity;
import com.sudhar.netizen.models.MemeDetailModel;
import com.sudhar.netizen.models.TemplateDetailModel;

import okhttp3.ResponseBody;

public interface GetContentListener {

    void OnSuccess(MemeDetailModel memeDetailModel);

    void OnSuccess(TemplateDetailModel templateDetailModel);

    void OnContentError(ContentType contentType, int ResponseCode, ResponseBody Error);

    void OnContentFailure(ContentType contentType, Throwable throwable);

}
