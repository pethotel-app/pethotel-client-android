package com.hyunsungkr.pethotel.api;

import com.hyunsungkr.pethotel.model.Point;
import com.hyunsungkr.pethotel.model.PointList;
import com.hyunsungkr.pethotel.model.PointRes;
import com.hyunsungkr.pethotel.model.Res;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PointApi {

    @GET("/benefit/totalPoint")
    Call<PointRes> totalPoint(@Header("Authorization") String token);

    @POST("/benefit/addPoint")
    Call<Res> usePoint(@Header("Authorization") String token,
                       @Body Point point);
}
