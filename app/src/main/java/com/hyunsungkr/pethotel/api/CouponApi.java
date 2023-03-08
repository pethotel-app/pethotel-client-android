package com.hyunsungkr.pethotel.api;

import com.hyunsungkr.pethotel.model.Res;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface CouponApi {

    @DELETE("/benefit/coupon/{couponId}")
    Call<Res> useCoupon(@Path("couponId") int couponId,
                        @Header("Authorization") String token);

}
