package com.sudhar.netizen.CallBacks;

import com.sudhar.netizen.ContentType;
import com.sudhar.netizen.models.MemeDetailModel;
import com.sudhar.netizen.models.TemplateDetailModel;

import okhttp3.ResponseBody;

public interface DeleteContentListener {

    void OnDelete(String id, ContentType contentType);

    void OnDeleteError(ContentType contentType, int ResponseCode, ResponseBody Error);

    void OnDeleteFailure(ContentType contentType, Throwable throwable);

}
