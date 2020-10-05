package com.sudhar.netizen.CallBacks;

import com.sudhar.netizen.models.Comment;

import okhttp3.ResponseBody;

public interface CommentListener {

    void OnComment(Comment comment);

    void OnCommentError(int ResponseCode, ResponseBody Error);

    void OnCommentFailure(Throwable throwable);
}
