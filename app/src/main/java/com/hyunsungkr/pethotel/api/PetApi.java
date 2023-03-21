package com.hyunsungkr.pethotel.api;

import com.hyunsungkr.pethotel.model.Pet;
import com.hyunsungkr.pethotel.model.PetInfoList;
import com.hyunsungkr.pethotel.model.Res;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface PetApi {

    @GET("/pets")
    Call<PetInfoList> getPetList(@Header("Authorization") String token);

    @Multipart
    @POST("/pets")
    Call<Res> addPet(@Header("Authorization") String token,
                     @Part MultipartBody.Part photo,
                     @Part("name") RequestBody name,
                     @Part("classification") RequestBody classification,
                     @Part("species") RequestBody species,
                     @Part("age") RequestBody age,
                     @Part("weight") RequestBody weight,
                     @Part("gender") RequestBody gender);

    @Multipart
    @PUT("/pets/{petId}")
    Call<Res> updatePet(@Header("Authorization") String token, @Path("petId") int petId,
                        @Part MultipartBody.Part photo,
                        @Part("name") RequestBody name,
                        @Part("classification") RequestBody classification,
                        @Part("species") RequestBody species,
                        @Part("age") RequestBody age,
                        @Part("weight") RequestBody weight,
                        @Part("gender") RequestBody gender,
                        @Part("petImgUrl") RequestBody petImgUrl);


}
