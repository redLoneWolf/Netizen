package com.sudhar.netizen.CallBacks;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sudhar.netizen.CommentAdapter;
import com.sudhar.netizen.ContentType;
import com.sudhar.netizen.NetUtils.RestInterface;
import com.sudhar.netizen.RoomDB.Daos.MainDao;
import com.sudhar.netizen.RoomDB.Entities.MemeEntity;
import com.sudhar.netizen.RoomDB.db.AppDatabase;
import com.sudhar.netizen.UserMentionAdapter;
import com.sudhar.netizen.ViewerCommentAdapter;
import com.sudhar.netizen.models.Comment;
import com.sudhar.netizen.models.MemeDetailModel;
import com.sudhar.netizen.models.TemplateDetailModel;

import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralCallBacks {

    RestInterface restInterface;
    FollowListener followListener;
    BlockListener blockListener;
    CommentListener commentListener;
    ReplyListener replyListener;

    LikeListener likeListener;

    BookmarkListener bookmarkListener;

    SearchUserListener searchUserListener;

    DeleteContentListener deleteContentListener;

    GetContentListener getContentListener;

    public GeneralCallBacks(RestInterface restInterface) {
        this.restInterface = restInterface;

    }

    public void setFollowListener(FollowListener followListener) {
        this.followListener = followListener;
    }

    public void setBlockListener(BlockListener blockListener) {
        this.blockListener = blockListener;
    }

    public void setCommentListener(CommentListener commentListener) {
        this.commentListener = commentListener;
    }

    public void setReplyListener(ReplyListener replyListener) {
        this.replyListener = replyListener;
    }

    public void setLikeListener(LikeListener likeListener) {
        this.likeListener = likeListener;
    }

    public void setBookmarkListener(BookmarkListener bookmarkListener) {
        this.bookmarkListener = bookmarkListener;
    }

    public void setSearchUserListener(SearchUserListener searchUserListener) {
        this.searchUserListener = searchUserListener;
    }

    public void setDeleteContentListener(DeleteContentListener deleteContentListener) {
        this.deleteContentListener = deleteContentListener;
    }

    public void setGetContentListener(GetContentListener getContentListener) {
        this.getContentListener = getContentListener;
    }

    public void followUser(String Username, int UserId) {
        Call<JsonObject> call = restInterface.followUser(Username);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    followListener.OnFollowError(response.code(), response.errorBody());
                    return;
                }
                JsonObject jsonObject = response.body();
                boolean is_followed = jsonObject.get("is_following").getAsBoolean();
                followListener.OnFollow(is_followed, Username);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                followListener.OnFollowFailure(t);
            }
        });

    }

    public void blockUser(String username) {
        Call<JsonObject> call = restInterface.blockUser(username);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    blockListener.OnBlockError(response.code(), response.errorBody());
                    return;
                }

                JsonObject jsonObject = response.body();
                boolean is_blocked = jsonObject.get("is_blocked").getAsBoolean();
                blockListener.OnBlock(is_blocked);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                blockListener.OnBlockFailure(t);
            }
        });
    }

    public void postComment(String commentInput, String contentTypeString, String id) {

        Call<Comment> call = restInterface.postComment(contentTypeString, id, commentInput);

        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(@NotNull Call<Comment> call, @NotNull Response<Comment> response) {
                if (!response.isSuccessful()) {
                    commentListener.OnCommentError(response.code(), response.errorBody());
                    return;
                }

                Comment newComment = response.body();
                commentListener.OnComment(newComment);
            }

            @Override
            public void onFailure(@NotNull Call<Comment> call, @NotNull Throwable t) {
                commentListener.OnCommentFailure(t);
            }
        });

    }

    public void postReply(TextInputEditText commentInputField, String contentTypeString, String id, String parentId, ViewerCommentAdapter repliesAdapter) {
        String commentInput = commentInputField.getText().toString().trim();
        Call<Comment> call = restInterface.postComment(contentTypeString, id, parentId, commentInput);

        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(@NotNull Call<Comment> call, @NotNull Response<Comment> response) {
                if (!response.isSuccessful()) {
                    replyListener.OnReplyError(response.code(), response.errorBody());
                    return;
                }

                Comment newComment = response.body();
                replyListener.OnReply(newComment, repliesAdapter);
            }

            @Override
            public void onFailure(@NotNull Call<Comment> call, @NotNull Throwable t) {
                replyListener.OnReplyFailure(t);
            }
        });

    }

    public void postLike(String contentType, String id) {


        Call<JsonObject> call = restInterface.postLike(contentType, id);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    likeListener.OnLikeError(response.code(), response.errorBody());
                    return;
                }
                JsonObject jsonObject = response.body();
                boolean is_liked = jsonObject.get("is_liked").getAsBoolean();
                int likes = jsonObject.get("likes").getAsInt();
                String object_id = jsonObject.get("post").getAsString();

                if (object_id.equals(id)) {
                    likeListener.OnLike(is_liked, likes);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                likeListener.OnLikeFailure(t);
            }
        });

    }

    public void postBookmark(String contentType, String id) {
        Call<JsonObject> call = restInterface.postBookmark(contentType, id);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (!response.isSuccessful()) {
                    bookmarkListener.OnBookmarkError(response.code(), response.errorBody());
                    return;
                }
                JsonObject jsonObject = response.body();
                boolean is_bookmarked = jsonObject.get("is_bookmarked").getAsBoolean();

                String object_id = jsonObject.get("post").getAsString();

                if (object_id.equals(id)) {
                    bookmarkListener.OnBookmark(is_bookmarked);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                bookmarkListener.OnBookmarkFailure(t);
            }
        });
    }

    public void searchUsers(String Username) {
        Call<JsonObject> call = restInterface.getUsers(Username);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonObject jsonObject = response.body();

                JsonArray jsonArray = jsonObject.getAsJsonArray("results");

                List<UserMentionAdapter.DataModel> dataModels = new ArrayList<>();
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject jsonObject1 = jsonElement.getAsJsonObject();
                    String username = jsonObject1.get("username").getAsString();
                    dataModels.add(new UserMentionAdapter.DataModel(username, ""));

                }
                searchUserListener.OnFound(dataModels);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                searchUserListener.OnSearchFailure(t);
            }
        });
    }


    public void deleteMeme(String id, String contentType) {
        Call<JsonObject> call = restInterface.deleteMeme(id);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (!response.isSuccessful()) {
                    deleteContentListener.OnDeleteError(ContentType.MEME, response.code(), response.errorBody());
                }
                deleteContentListener.OnDelete(id, ContentType.MEME);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                deleteContentListener.OnDeleteFailure(ContentType.MEME, t);
            }
        });
    }

    public void deleteTemplate(String id, String contentType) {
        Call<JsonObject> call = restInterface.deleteTemplate(id);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (!response.isSuccessful()) {
                    deleteContentListener.OnDeleteError(ContentType.TEMPLATE, response.code(), response.errorBody());
                }
                deleteContentListener.OnDelete(id, ContentType.TEMPLATE);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                deleteContentListener.OnDeleteFailure(ContentType.TEMPLATE, t);
            }
        });
    }

//    public void getTemplate(String id) {
//        Call<TemplateDetailModel> call = restInterface.getTemplate(id);
//
//        call.enqueue(new Callback<TemplateDetailModel>() {
//            @Override
//            public void onResponse(Call<TemplateDetailModel> call, Response<TemplateDetailModel> response) {
//                if (!response.isSuccessful()) {
//                    getContentListener.OnContentError(ContentType.TEMPLATE, response.code(), response.errorBody());
//                    return;
//                }
//
//                TemplateDetailModel templateDetailModel = response.body();
//                getContentListener.OnSuccess(templateDetailModel);
//
//            }
//
//            @Override
//            public void onFailure(Call<TemplateDetailModel> call, Throwable t) {
//                getContentListener.OnContentFailure(ContentType.TEMPLATE, t);
//            }
//        });
//    }

    public void getMeme(String id) {
        Call<MemeDetailModel> call = restInterface.getMeme(id);

        call.enqueue(new Callback<MemeDetailModel>() {
            @Override
            public void onResponse(Call<MemeDetailModel> call, Response<MemeDetailModel> response) {
                if (!response.isSuccessful()) {
                    getContentListener.OnContentError(ContentType.MEME, response.code(), response.errorBody());
                    return;
                }

                MemeDetailModel memeDetailModel = response.body();


                getContentListener.OnSuccess(memeDetailModel);


            }

            @Override
            public void onFailure(Call<MemeDetailModel> call, Throwable t) {
                getContentListener.OnContentFailure(ContentType.MEME, t);
            }
        });

    }


}
