package com.example.oneread.data.network;


import com.example.oneread.data.network.model.*;
import com.google.gson.JsonObject;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.*;

public interface IServiceAPI {


    /*================ USER ==================*/

    @GET("user/info/{username}")
    Observable<Response<User>> userInfo(@Path("username") String username);

    @Multipart
    @PATCH("user")
    Observable<Response<User>> updateUserInfo(@Header("Authorization") String auth, @Part MultipartBody.Part[] body);

    @PATCH("user/change-password")
    Observable<Response<User>> updatePassword(@Header("Authorization") String auth, @Body JsonObject password);

    @POST("user/verify-email")
    Observable<Response<User>> verifyEmail(@Header("Authorization") String auth);

    @GET("user/book-following")
    Observable<Response<Book>> listBookFollowing(@Header("Authorization") String auth);

    @GET("user/history")
    Observable<Response<HistoryRead>> listHistoryRead(@Header("Authorization") String auth);

    @GET("user/history/{book_endpoint}")
    Observable<Response<HistoryRead>> historyRead(@Header("Authorization") String auth, @Path("book_endpoint") String book_endpoint);

    @POST("user/register")
    Observable<Response<String>> register(@Body User user);

    @POST("user/login")
    Observable<Response<LoginResponse>> login(@Body User user);

    @POST("user/follow-book/{book_endpoint}")
    Observable<Response<Book>> followBook(@Header("Authorization") String auth, @Path("book_endpoint") String book_endpoint);

    @POST("user/unfollow-book/{book_endpoint}")
    Observable<Response<Book>> unfollowBook(@Header("Authorization") String auth, @Path("book_endpoint") String book_endpoint);

    @DELETE("user/history/all")
    Observable<Response<HistoryRead>> deleteAllHistoryRead(@Header("Authorization") String auth);

    @DELETE("user/history/single/{book_endpoint}")
    Observable<Response<HistoryRead>> deleteSingleHistoryRead(@Header("Authorization") String auth, @Path("book_endpoint") String book_endpoint);




    /*================ BOOK ==================*/

    @GET("book/all")
    Observable<Response<Book>> books(@Query("filter") String filter);

    @GET("book/suggest-book")
    Observable<Response<Book>> suggestBooks(@Header("Authorization") String auth);

    @GET("book/top-rating")
    Observable<Response<Book>> trending();

    @GET("book/top-search")
    Observable<Response<Book>> getTopSearch();

    @GET("book/relate-book/{book_endpoint}")
    Observable<Response<Book>> relateBooks(@Path("book_endpoint") String book_endpoint);

    @GET("book/detail/{book_endpoint}")
    Observable<Response<Book>> detailBook(@Path("book_endpoint") String book_endpoint);

    @PATCH("book/rate/{book_endpoint}")
    Observable<Response<Book>> rateBook(@Header("Authorization") String auth, @Body JsonObject rating, @Path("book_endpoint") String book_endpoint);



    @GET("genre/all")
    Observable<Response<Genre>> getGenres();




    /*================ CHAPTER ==================*/

    @GET("chapter/all/{book_endpoint}")
    Observable<Response<Chapter>> chapters(@Path("book_endpoint") String book_endpoint);

    @GET("chapter/detail/{book_endpoint}/{chapter_endpoint}")
    Observable<Response<Chapter>> detailChapter(@Path("book_endpoint") String book_endpoint, @Path("chapter_endpoint") String chapter_endpoint);

    @GET("chapter/detail/{book_endpoint}/{chapter_endpoint}")
    Observable<Response<Chapter>> detailChapter(@Header("Authorization") String auth, @Path("book_endpoint") String book_endpoint, @Path("chapter_endpoint") String chapter_endpoint);


    /*================ COMMENT ==================*/

    @GET("comment/{book_endpoint}")
    Observable<Response<Comment>> comments(@Path("book_endpoint") String book_endpoint);

    @GET("comment/detail/{comment_id}")
    Observable<Response<Comment>> detailComment(@Path("comment_id") Integer comment_id);

    @Multipart
    @POST("comment/{book_endpoint}")
    Observable<Response<Comment>> addComment(@Header("Authorization") String auth,
                                             @Path("book_endpoint") String book_endpoint,
                                             @Part MultipartBody.Part[] body);





    /*================ NOTIFY ==================*/

    @GET("notify/all")
    Observable<Response<Notify>> notifications(@Header("Authorization") String auth);

    @GET("notify/{endpoint}")
    Observable<Response<Notify>> detailNotification(@Header("Authorization") String auth, @Path("endpoint") String endpoint);

    @DELETE("notify/all-read")
    Observable<Response<Notify>> deleteAllReadNotification(@Header("Authorization") String auth);
}
