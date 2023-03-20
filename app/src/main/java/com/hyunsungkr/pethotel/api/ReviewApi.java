package com.hyunsungkr.pethotel.api;

import com.hyunsungkr.pethotel.model.HotelList;
import com.hyunsungkr.pethotel.model.HotelReviewList;
import com.hyunsungkr.pethotel.model.MyReviewList;
import com.hyunsungkr.pethotel.model.Res;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
}
