package com.sudhar.netizen.CallBacks;

import okhttp3.ResponseBody;

public interface FollowListener {
    void OnFollow(boolean isFollowing, String username);

    void OnFollowError(int ResponseCode, ResponseBody Error);

    void OnFollowFailure(Throwable throwable);
}
