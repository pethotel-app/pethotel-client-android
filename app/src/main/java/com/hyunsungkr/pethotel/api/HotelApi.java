package com.hyunsungkr.pethotel.api;

import com.hyunsungkr.pethotel.model.HotelList;
import com.hyunsungkr.pethotel.model.Res;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface HotelApi {

    // 내 주변 호텔 가져오는 API
    @GET("/hotel/near")
    Call<HotelList> getNearHotel(@Header("Authorization") String token, @Query("long") double longtitude,
                                 @Query("lat") double lat,
                                 @Query("offset") int offset,
                                 @Query("limit") int limit);



}
