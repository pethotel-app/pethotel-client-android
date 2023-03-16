package com.hyunsungkr.pethotel.api;

import com.hyunsungkr.pethotel.model.HotelList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface FavoriteApi {

    @GET("/favorite")
    Call<HotelList> getFavoriteHotel(@Header("Authorization") String token,
                                 @Query("offset") int offset,
                                 @Query("limit") int limit);

}
