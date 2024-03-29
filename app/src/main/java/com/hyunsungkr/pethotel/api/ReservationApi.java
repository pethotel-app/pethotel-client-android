package com.hyunsungkr.pethotel.api;

import com.hyunsungkr.pethotel.model.CancelHotel;
import com.hyunsungkr.pethotel.model.CancelReason;
import com.hyunsungkr.pethotel.model.MyReservationList;
import com.hyunsungkr.pethotel.model.Res;
import com.hyunsungkr.pethotel.model.Reservation;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReservationApi {

    @POST("/reservation")
    Call<Res> reservationSave(@Header("Authorization") String token,
                              @Body Reservation reservation);
    @GET("/reservation")
    Call<MyReservationList> getMyReservation(@Header("Authorization") String token,
                                             @Query("offset") int offset,
                                             @Query("limit") int limit);

    // 호텔 예약정보 삭제
    @DELETE("/reservation/{hotelId}/{petId}")
    Call<CancelHotel> deleteHotel(@Header("Authorization") String token,
                                  @Path("hotelId") int hotelId,
                                  @Path("petId") int petId);

    // 취소 이유, 금액
    @PUT("/reservation")
    Call<CancelReason> reasonCancel (@Header("Authorization") String token,
                                     @Body RequestBody requestBody);
}
