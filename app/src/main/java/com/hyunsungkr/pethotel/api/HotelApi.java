package com.hyunsungkr.pethotel.api;

import com.hyunsungkr.pethotel.model.HotelList;


import com.hyunsungkr.pethotel.model.Res;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HotelApi {
    @GET("/hotel/{hotelId}")
    Call<HotelList> checkHotel(@Header("Authorization") String token,
                           @Path("hotelId") int hotelId);

    // 내 주변 호텔 가져오는 API
    @GET("/hotel/near")
    Call<HotelList> getNearHotel(@Header("Authorization") String token, @Query("long") double longtitude,
                                 @Query("lat") double lat,
                                 @Query("offset") int offset,
                                 @Query("limit") int limit);

    // 찜하는 API
    @POST("/favorite/{hotelId}")
    Call<Res> setFavorite(@Header("Authorization") String token, @Path("hotelId") int hotelId);

    // 찜 해제하는 API
    @DELETE("/favorite/{hotelId}")
    Call<Res> deleteFavorite(@Header("Authorization") String token, @Path("hotelId") int hotelId);


    // 찜 목록조회하는 API

    // 추천된 호텔 가져오는 API
    @GET("/hotel/recommend")
    Call<HotelList> getRecommendHotel(@Header("Authorization") String token, @Query("count") int count);




    // 검색한 호텔 리스트 가져오는 API
    @GET("/hotel/search")
    Call<HotelList> getSearchHotel(@Header("Authorization") String token,
                                   @Query("keyword") String keyword,
                                   @Query("offset") int offset,
                                   @Query("limit") int limit);

}





