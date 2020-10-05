package com.sudhar.netizen.CallBacks;

import okhttp3.ResponseBody;

public interface LikeListener {

    void OnLike(boolean isLiked, int LikeCount);

    void OnLikeError(int ResponseCode, ResponseBody Error);

    void OnLikeFailure(Throwable throwable);
}
