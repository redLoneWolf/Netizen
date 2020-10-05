package com.sudhar.netizen.CallBacks;

import okhttp3.ResponseBody;

public interface BlockListener {
    void OnBlock(boolean isBlocked);

    void OnBlockError(int ResponseCode, ResponseBody Error);

    void OnBlockFailure(Throwable throwable);
}
