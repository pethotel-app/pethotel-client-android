package com.hyunsungkr.pethotel.api;

import com.hyunsungkr.pethotel.model.HotelList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface HotelApi {
    @GET("/hotel/{hotelId}")
    Call<HotelList> checkHotel(@Header("Authorization") String token,
                           @Path("hotelId") int hotelId);
}
