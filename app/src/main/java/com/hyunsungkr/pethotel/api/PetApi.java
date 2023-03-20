package com.hyunsungkr.pethotel.api;

import com.hyunsungkr.pethotel.model.Pet;
import com.hyunsungkr.pethotel.model.PetInfoList;
import com.hyunsungkr.pethotel.model.Res;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PetApi {

    @GET("/pets")
    Call<PetInfoList> getPetList(@Header("Authorization") String token);

    @POST("/pets")
    Call<Res> addPet(@Header("Authorization") String token,
                     @Body Pet pet);
}
