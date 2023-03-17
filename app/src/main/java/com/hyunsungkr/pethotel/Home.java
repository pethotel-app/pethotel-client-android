package com.hyunsungkr.pethotel;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
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

    private ProgressDialog dialog;
    ImageView imgSearch;
    TextView editSearch;
    TextView txtAll;
    ImageView imgEvent;
    TextView txtSearch;
    RecyclerView nearRecyclerView;
    NearhotelAdapter adapter1;
    ArrayList<Hotel> NearhotelList = new ArrayList<>();
    RecyclerView recommendRecylerView;


    // 페이징 처리를 위한 변수
    int count = 0;
    int offset = 0;
    int limit = 10;

    // 위도값 경도값 변수 선언
    private double currentLat;
    private double currentLng;

    int currentCount;

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
        txtSearch = rootView.findViewById(R.id.txtSearch);


        nearRecyclerView = rootView.findViewById(R.id.nearRecyclerView);
        nearRecyclerView.setHasFixedSize(true);
        nearRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        recommendRecylerView = rootView.findViewById(R.id.recommendRecyclerView);
        recommendRecylerView.setHasFixedSize(true);
        recommendRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // todo: 클릭 이벤트 작성하기.

        // 검색바 클릭시 검색 액티비티로
        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        // 전체보기 클릭
        txtAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllNearbyHotelsActivity.class);
                startActivity(intent);
            }
        });

        // 이벤트 베너 클릭
        imgEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EventActivity.class);
                startActivity(intent);
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

                // 위도 경도 값을 뽑아내기
                currentLat = location.getLatitude();
                currentLng = location.getLongitude();

                if(currentCount == 0){
                    getNetworkData();
                }
                currentCount = currentCount + 1;

            }
        };

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

        }

        // 위치기반으로 GPS 정보 가져오는 코드를 실행하는 부분
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, -1, locationListener);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getNetworkData();
    }

    void getNetworkData(){
        showProgress("호텔정보 가져오는 중...");
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        HotelApi api = retrofit.create(HotelApi.class);

        // 헤더에 들어갈 억세스토큰 가져오기
        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;

        Call<HotelList> call = api.getNearHotel(accessToken , currentLng , currentLat ,offset,limit);
        call.enqueue(new Callback<HotelList>() {
            @Override
            public void onResponse(Call<HotelList> call, Response<HotelList> response) {
                dismissProgress();

                if(response.isSuccessful()){

                    NearhotelList.clear();

                    count = response.body().getCount();
                    NearhotelList.addAll(response.body().getItems());

                    adapter1 = new NearhotelAdapter(getActivity(),NearhotelList);

                    adapter1.setOnItemClickListener(new NearhotelAdapter.OnItemClickListener() {
                        @Override
                        public void onImageClick(int index) {

                            Hotel hotel = NearhotelList.get(index);
                            Intent intent = new Intent(getActivity(), HotelInfoActivity.class);
                            intent.putExtra("hotel",hotel);
                            startActivity(intent);
                        }
                    });
                    adapter1.notifyDataSetChanged();
                    nearRecyclerView.setAdapter(adapter1);
                }
                else {
                    return;
                }
            }

            @Override
            public void onFailure(Call<HotelList> call, Throwable t) {
                dismissProgress();

            }
        });

    }

    // 네트워크 로직처리시에 화면에 보여주는 함수
    void showProgress(String message) {
        dialog = new ProgressDialog(getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);
        dialog.show();
    }

    // 로직처리가 끝나면 화면에서 사라지는 함수
    void dismissProgress() {
        dialog.dismiss();
    }


}

