package com.hyunsungkr.pethotel;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyunsungkr.pethotel.adapter.FavoriteAdapter;
import com.hyunsungkr.pethotel.api.FavoriteApi;
import com.hyunsungkr.pethotel.api.HotelApi;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Hotel;
import com.hyunsungkr.pethotel.model.HotelList;
import com.hyunsungkr.pethotel.model.Res;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Favorite#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Favorite extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Favorite() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Favorite.
     */
    // TODO: Rename and change types and number of parameters
    public static Favorite newInstance(String param1, String param2) {
        Favorite fragment = new Favorite();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    RecyclerView recyclerView;
    FavoriteAdapter adapter;
    ArrayList<Hotel> hotelArrayList = new ArrayList<>();
    int count = 0;
    int offset = 0;
    int limit = 10;
    String accessToken;
    private Hotel selectedHotel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_favorite, container, false);

        // 화면연결
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 헤더에 들어갈 억세스토큰 가져오기
        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        // 관심 리스트 불러오기
        getNetworkData();

        // 페이징처리
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                if(lastPosition + 1 == totalCount){
                    addNetworkData();
                }
            }
        });

        return rootView;
    }

    private void addNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        FavoriteApi api = retrofit.create(FavoriteApi.class);

        Call<HotelList> call = api.getFavoriteHotel(accessToken, offset, limit);

        call.enqueue(new Callback<HotelList>() {
            @Override
            public void onResponse(Call<HotelList> call, Response<HotelList> response) {
                if (response.isSuccessful()) {
                    HotelList hotelList = response.body();
                    hotelArrayList.addAll(hotelList.getItems());
                    adapter.notifyDataSetChanged();

                    // 오프셋 코드 처리
                    count = hotelList.getCount();
                    offset = offset + count;

                } else {
                    return;
                }
            }

            @Override
            public void onFailure(Call<HotelList> call, Throwable t) {

            }
        });
    }

    private void getNetworkData(){
        offset = 0;
        count = 0;

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        FavoriteApi api = retrofit.create(FavoriteApi.class);

        Call<HotelList> call = api.getFavoriteHotel(accessToken, offset, limit);
        call.enqueue(new Callback<HotelList>() {
            @Override
            public void onResponse(Call<HotelList> call, Response<HotelList> response) {
                hotelArrayList.clear();
                if(response.isSuccessful()){
                    count = response.body().getCount();

                    hotelArrayList.addAll(response.body().getItems());

                    offset = offset + count;

                    adapter = new FavoriteAdapter(getActivity(), hotelArrayList, Favorite.this);
                    adapter.setOnItemClickListener(new FavoriteAdapter.OnItemClickListener() {
                        @Override
                        public void onCardViewClick(int index) {
                            Hotel hotel = hotelArrayList.get(index);
                            Intent intent = new Intent(getActivity(), HotelInfoActivity.class);
                            intent.putExtra("hotel", hotel);
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(adapter);

                } else {
                    return;
                }
            }

            @Override
            public void onFailure(Call<HotelList> call, Throwable t) {

            }
        });

    }

    public void favoriteProcess(int index){
        selectedHotel = hotelArrayList.get(index);

        // 찜 버튼 클릭시 해제(찜 해제 API 호출)
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        HotelApi api = retrofit.create(HotelApi.class);
        Call<Res> call = api.deleteFavorite(accessToken, selectedHotel.getId());
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if(response.isSuccessful()){
                    hotelArrayList.remove(index);
                    adapter.notifyDataSetChanged();
                }else{

                }
            }
            @Override
            public void onFailure(Call<Res> call, Throwable t) {

            }
        });



    }


}