package com.sudhar.netizen.CallBacks;

import com.sudhar.netizen.UserMentionAdapter;

import java.util.List;

import okhttp3.ResponseBody;

public interface SearchUserListener {
    void OnFound(List<UserMentionAdapter.DataModel> users);

    void OnSearchError(int ResponseCode, ResponseBody Error);

    void OnSearchFailure(Throwable throwable);
}
