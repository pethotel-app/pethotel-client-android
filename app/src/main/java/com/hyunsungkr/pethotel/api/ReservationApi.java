package com.hyunsungkr.pethotel.api;

import com.hyunsungkr.pethotel.model.MyReservationList;
import com.hyunsungkr.pethotel.model.Res;
import com.hyunsungkr.pethotel.model.Reservation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ReservationApi {

    @POST("/reservation")
    Call<Res> reservationSave(@Header("Authorization") String token,
                              @Body Reservation reservation);
    @GET("/reservation")
    Call<MyReservationList> getMyReservation(@Header("Authorization") String token,
                                             @Query("offset") int offset,
                                             @Query("limit") int limit);

}
