package com.hyunsungkr.pethotel.api;

import com.hyunsungkr.pethotel.model.HotelList;
import com.hyunsungkr.pethotel.model.HotelReviewList;
import com.hyunsungkr.pethotel.model.MyReviewList;
import com.hyunsungkr.pethotel.model.Res;
import com.hyunsungkr.pethotel.model.ReviewSummary;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReviewApi {

    @GET("/review/{hotelId}")
    Call<HotelReviewList> checkReview(@Header("Authorization") String token,
                                      @Path("hotelId") int hotelId,
                                    @Query("offset") int offset,
                                    @Query("limit") int limit);

    @GET("/review/my")
    Call<MyReviewList> myReview(@Header("Authorization") String token,
                                   @Query("offset") int offset,
                                   @Query("limit") int limit);

    @DELETE("/review/{hotelId}")
    Call<Res> deleteReview(@Header("Authorization") String token,
                       @Path("hotelId") int hotelId);

    // 리뷰 작성 API

    @POST("/review/{hotelId}")
    @Multipart
    Call<Res> addReview(@Header("Authorization") String token,
                        @Path("hotelId") int hotelId,
                        @Part MultipartBody.Part photo,
                        @Part("content")RequestBody content,
                        @Part("rating")RequestBody rating);

    // 리뷰 요약 가져오는 API

    @GET("/review/summary/{hotelId}")
    Call<ReviewSummary> getSummaryReview(@Header("Authorization") String token,
                                         @Path("hotelId") int hotelId);


}
