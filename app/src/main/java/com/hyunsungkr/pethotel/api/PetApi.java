package com.hyunsungkr.pethotel.api;

import com.hyunsungkr.pethotel.model.Pet;
import com.hyunsungkr.pethotel.model.PetInfoList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface PetApi {

    @GET("pets/")
    Call<PetInfoList> getPetList(@Header("Authorization") String token);
}
