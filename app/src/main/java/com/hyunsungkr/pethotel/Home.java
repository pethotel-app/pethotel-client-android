package com.hyunsungkr.pethotel;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyunsungkr.pethotel.adapter.NearhotelAdapter;
import com.hyunsungkr.pethotel.adapter.RecommendhotelAdapter;
import com.hyunsungkr.pethotel.api.HotelApi;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Hotel;
import com.hyunsungkr.pethotel.model.HotelList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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

    ImageView imgSearch;
    TextView editSearch;
    TextView txtAll;
    ImageView imgEvent;
    LinearLayout linearLayout;

    RecyclerView nearRecyclerView;
    NearhotelAdapter adapter1;
    ArrayList<Hotel> NearhotelList = new ArrayList<>();

    RecyclerView recommendRecylerView;
    RecommendhotelAdapter adapter2;
    ArrayList<Hotel> reccomendhotelList = new ArrayList<>();

    // 페이징 처리를 위한 변수
    int count = 0;
    int offset = 0;
    int limit = 10;

    // 위도값 경도값 변수 선언
    private double currentLat;
    private double currentLng;

    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        imgSearch = rootView.findViewById(R.id.imgSearch);
        editSearch = rootView.findViewById(R.id.txtSearch);
        txtAll = rootView.findViewById(R.id.txtAll);
        imgEvent = rootView.findViewById(R.id.imgEvent);
        linearLayout = rootView.findViewById(R.id.linearLayout);


        nearRecyclerView = rootView.findViewById(R.id.nearRecyclerView);
        nearRecyclerView.setHasFixedSize(true);
        nearRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,true));

        recommendRecylerView = rootView.findViewById(R.id.recommendRecyclerView);
        recommendRecylerView.setHasFixedSize(true);
        recommendRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        // todo: 클릭 이벤트 작성하기.

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        txtAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo : 내 주변 호텔 리스트 전체 가져오는 화면 개발 후 연결
                Intent intent = new Intent(getActivity(),AllNearbyHotelsActivity.class);
                startActivity(intent);
            }
        });

        imgEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo : 이벤트 액티비티 개발 후에 연결
            }
        });






        // API 호출에 필요한 내 위치 정보를 가져오기
        // 위치를 가져오기 위해서는, 시스템서비스로부터 로케이션 매니저를 받아온다.

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);


        // 로케이션 리스너를 만든다.
        // 위치가 변할 때마다 호출되는 함수 작성!
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                // 위도 경도 값을 여기서 뽑아내서 우리에 맞는 코드를 작성
                currentLat = location.getLatitude();
                currentLng = location.getLongitude();
                Log.i("MyLocation", "" + currentLng + " " + currentLat);


            }
        };

        getNetworkData();

        return rootView;
    }

    void getNetworkData(){
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        HotelApi api = retrofit.create(HotelApi.class);

        // todo: 억세스토큰 하드코딩 제거
        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME,MODE_PRIVATE);
        String accessToken = "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTY3ODA4MTUxNywianRpIjoiMmFmMjk3MmMtYjNiMC00OWE1LTkwN2MtY2RlNTNiZDIzNDc3IiwidHlwZSI6ImFjY2VzcyIsInN1YiI6NiwibmJmIjoxNjc4MDgxNTE3fQ.VP_pcHsGR1tZHlafCV9hN0qZ6MHdVz56NMRFGGsfujQ";


        offset = 0;

        Log.i("MyLocation",""+currentLng+" "+currentLat);

        Call<HotelList> call = api.getNearHotel(accessToken , currentLng , currentLat ,offset,limit);
        call.enqueue(new Callback<HotelList>() {
            @Override
            public void onResponse(Call<HotelList> call, Response<HotelList> response) {
                if(response.isSuccessful()){

                    NearhotelList.clear();

                    count = response.body().getCount();
                    NearhotelList.addAll(response.body().getItems());


                    adapter1 = new NearhotelAdapter(getActivity(),NearhotelList);

                    adapter1.setOnItemClickListener(new NearhotelAdapter.OnItemClickListener() {
                        @Override
                        public void onImageClick(int index) {

                            Hotel hotel = NearhotelList.get(index);

                            Intent intent = new Intent(getActivity(),HotelInfoActivity.class);
                            intent.putExtra("hotel",hotel);
                            startActivity(intent);
                        }
                    });

                    nearRecyclerView.setAdapter(adapter1);
                }
                else{
                    return;
                }
            }

            @Override
            public void onFailure(Call<HotelList> call, Throwable t) {

            }
        });

    }

}

