package com.hyunsungkr.pethotel.api;

import com.hyunsungkr.pethotel.model.KeywordList;
import com.hyunsungkr.pethotel.model.KeywordResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface KeywordApi {

    @PUT("/hotel/search")
    Call<KeywordResponse> putKeyword(@Query("keyword") String keyword);

    @GET("/hotel/search/rank")
    Call<KeywordList> getKeywordRank(@Query("today") String today,
                                     @Query("offset") int offset,
                                     @Query("limit") int limit);
}
