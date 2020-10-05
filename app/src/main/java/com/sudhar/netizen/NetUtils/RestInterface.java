package com.sudhar.netizen.NetUtils;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.sudhar.netizen.models.Comment;
import com.sudhar.netizen.models.ContentList;
import com.sudhar.netizen.models.MemeDetailModel;
import com.sudhar.netizen.models.PagedResponse;
import com.sudhar.netizen.models.TemplateDetailModel;
import com.sudhar.netizen.models.TokenResponse;
import com.sudhar.netizen.models.User;

import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestInterface {
    String IP = "Host";
    String BASE_URL = IP + "/api/v1/";

    ///////         TOKEN
    @FormUrlEncoded
    @POST("api/token/")
    Call<TokenResponse> login(@Field("email") String mEmail, @Field("password") String mPassword);

    @FormUrlEncoded
    @POST("api/token/refresh/")
    Call<TokenResponse> refresh(@Field("refresh") String mRefresh);

    /////////////// REGISTER

    @FormUrlEncoded
    @POST("register/")
    Call<TokenResponse> signUp(@Field("email") String mEmail, @Field("email2") String mEmail2, @Field("username") String mUsername, @Field("password") String mPassword);


    /////////////       USERS
    @GET("users/")
    Call<List<User>> getUsers();

    @GET("users/{username}")
    Call<User> getUser(@Path("username") String Username);

    @GET("users/")
    Call<JsonObject> getUsers(@Query("search") String Username);


    @PATCH("profile/")
    Call<JsonObject> patchUserInfo(@Body JsonObject body);


    @Multipart
    @POST("profile/profilePic/")
    Call<JsonObject> uploadProfilePic(@Part MultipartBody.Part images);

    @GET("profile/")
    Call<User> getMe();

    @GET("{username}/templates/")
    Call<ContentList> getUserTemplates(@Path("username") String username, @Query("page") int page);

    @GET("{username}/memes/")
    Call<ContentList> getUserMemes(@Path("username") String username, @Query("page") int page);


    @FormUrlEncoded
    @POST("password/")
    Call<JsonObject> changePassword(@Field("old_password") String oldPassword, @Field("new_password") String newPassword, @Field("new_password2") String newPassword2);


    /////////   CONTENTS
    ////////    MEME
    @Multipart
    @POST("memes/upload/")
    Call<MemeDetailModel> uploadMeme(@Part("description") RequestBody des, @Part List<MultipartBody.Part> images);

    @GET("memes/")
    Single<PagedResponse<MemeDetailModel>> getMemes(@Query("page") int page, @Query("page_size") int size);

    @GET("memes/{id}/")
    Call<MemeDetailModel> getMeme(@Path("id") String id);

    @DELETE("memes/{id}/")
    Call<JsonObject> deleteMeme(@Path("id") String id);

    @PATCH("memes/{id}/")
    Call<JsonObject> patchMeme(@Path("id") String id, @Body JsonObject body);

    ////////    CONTENTS
    ///////     TEMPLATE

    @Multipart
    @POST("templates/upload/")
    Call<TemplateDetailModel> uploadTemplate(@Part("description") RequestBody des, @Part List<MultipartBody.Part> images);


    @GET("templates/")
    Single<PagedResponse<TemplateDetailModel>> getTemplates(@Query("page") int page, @Query("page_size") int size);

    @GET("templates/{id}/")
    Call<TemplateDetailModel> getTemplate(@Path("id") String id);

    @DELETE("templates/{id}/")
    Call<JsonObject> deleteTemplate(@Path("id") String id);

    @PATCH("templates/{id}/")
    Call<JsonObject> patchTemplate(@Path("id") String id, @Body JsonObject body);

    /////// COMMENTs
    @FormUrlEncoded
    @POST("comment/")
    Call<Comment> postComment(@Field("content_type") String contentType, @Field("object_id") String objectId, @Field("body") String Body);

    @FormUrlEncoded
    @POST("comment/")
    Call<Comment> postComment(@Field("content_type") String contentType, @Field("object_id") String objectId, @Field("parent_id") String parentId, @Field("body") String Body);

    @GET("l/")
    Call<PagedResponse<Comment>> getComments(@Nullable @Query("page") Integer page, @Nullable @Query("page_size") Integer pageSize
            , @NonNull @Query("content_type") String contentType, @NonNull @Query("object_id") String objectId,
                                             @Nullable @Query("parent_id") String parentId);

    @GET("l/")
    Single<PagedResponse<Comment>> getCommentsSingle(@Nullable @Query("page") Integer page, @Nullable @Query("page_size") Integer pageSize
            , @NonNull @Query("content_type") String contentType, @NonNull @Query("object_id") String objectId,
                                                     @Nullable @Query("parent_id") String parentId);

    ////// ACTIVITIES

    @FormUrlEncoded
    @POST("like/")
    Call<JsonObject> postLike(@Field("content_type") String contentType, @Field("object_id") String objectId);

    @FormUrlEncoded
    @POST("bookmark/")
    Call<JsonObject> postBookmark(@Field("content_type") String contentType, @Field("object_id") String objectId);

    //// REPORT


    @FormUrlEncoded
    @POST("report/content/")
    Call<JsonObject> reportContent(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("report/user/")
    Call<JsonObject> reportUser(@FieldMap HashMap<String, String> data);


    /// RELATIONSHIPS

    @FormUrlEncoded
    @POST("follow/")
    Call<JsonObject> followUser(@Field("to_follow") String Username);

    @FormUrlEncoded
    @POST("block/")
    Call<JsonObject> blockUser(@Field("user_to_block") String Username);

}
