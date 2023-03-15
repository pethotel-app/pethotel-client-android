package com.hyunsungkr.pethotel.api;

import com.hyunsungkr.pethotel.model.HotelList;
import com.hyunsungkr.pethotel.model.HotelReviewList;

import retrofit2.Call;
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
}
