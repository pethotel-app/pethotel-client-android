package com.hyunsungkr.pethotel.api;

import com.hyunsungkr.pethotel.model.User;
import com.hyunsungkr.pethotel.model.UserRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {

    @POST("/user/login")
    Call<UserRes> login(@Body User user);

    @POST("/user/register")
    Call<UserRes> register(@Body User user);

}
