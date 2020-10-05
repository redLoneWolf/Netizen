package com.sudhar.netizen.CallBacks;

import okhttp3.ResponseBody;

public interface BookmarkListener {

    void OnBookmark(boolean isBookmarked);

    void OnBookmarkError(int ResponseCode, ResponseBody Error);

    void OnBookmarkFailure(Throwable throwable);
}
