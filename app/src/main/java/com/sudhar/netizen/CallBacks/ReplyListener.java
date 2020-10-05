package com.sudhar.netizen.CallBacks;

import com.sudhar.netizen.CommentAdapter;
import com.sudhar.netizen.ViewerCommentAdapter;
import com.sudhar.netizen.models.Comment;

import okhttp3.ResponseBody;

public interface ReplyListener {
    void OnReply(Comment comment, ViewerCommentAdapter RepliesAdapter);

    void OnReplyError(int ResponseCode, ResponseBody Error);

    void OnReplyFailure(Throwable throwable);
}
