package com.hyunsungkr.pethotel.api;

import com.hyunsungkr.pethotel.model.Check;
import com.hyunsungkr.pethotel.model.Res;
import com.hyunsungkr.pethotel.model.User;
import com.hyunsungkr.pethotel.model.UserMyPageRes;
import com.hyunsungkr.pethotel.model.UserRes;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface UserApi {

    @POST("/user/login")
    Call<UserRes> login(@Body User user);

    @POST("/user/register")
    Call<UserRes> register(@Body User user);

    @POST("/user/phone")
    Call<Check> checkPhone(@Body User user);

    @POST("/user/email")
    Call<Check> checkEmail(@Body User user);

    @GET("/user/info")
    Call<UserRes> userCheck(@Header("Authorization") String token);

    @GET("/user/mypage")
    Call<UserMyPageRes> userMypage(@Header("Authorization") String token);


    @POST("/user/id/search")
    Call<UserRes> searchId(@Body User user);

    @POST("/user/password/search")
    Call<UserRes> searchPassword(@Body User user);

    @POST("/user/change/password")
    Call<Res> changePassword(@Body User user);

    // 내정보 수정(이름, 이메일)
    @PUT("/user/info")
    Call<UserRes> userChangeName(@Header("Authorization") String token,
                                 @Body User user );
    // 내정보 수정 (비밀번호)
    @POST("/user/change/password")
    Call<UserRes> changePassword(@Header("Authorization") String token,@Body Map<String, String> body);

    // 프로필 이미지 등록
    @Multipart
    @PUT("/user/profile")
    Call<Res> setProfile(@Header("Authorization") String token, @Part MultipartBody.Part photo );

}
