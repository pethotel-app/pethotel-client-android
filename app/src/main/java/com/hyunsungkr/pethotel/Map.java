package com.hyunsungkr.pethotel;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hyunsungkr.pethotel.adapter.NearhotelAdapter;
import com.hyunsungkr.pethotel.api.HotelApi;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Hotel;
import com.hyunsungkr.pethotel.model.HotelList;
import com.hyunsungkr.pethotel.model.Pet;
import com.hyunsungkr.pethotel.model.Reservation;
import com.hyunsungkr.pethotel.model.User;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Map#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Map extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Map() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Map.
     */
    // TODO: Rename and change types and number of parameters
    public static Map newInstance(String param1, String param2) {
        Map fragment = new Map();
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

    MapView sView = null;
    LocationManager locationManager;
    LocationListener locationListener;
    Double currentLat;
    Double currentLng;
    int offset = 0;
    int limit = 10;
    ArrayList<Hotel> NearhotelList = new ArrayList<>();
    LatLng myLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_map, container, false);

        // 화면 연결
        sView = rootView.findViewById(R.id.Gmap);
        sView.onCreate(savedInstanceState);
        sView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // 위치를 가져오기 위해서 시스템 서비스로부터 로케이션 매니저를 받아온다
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        // 현재 위치 표시
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);

        // 로케이션 리스너를 만들어 위치가 변할때마다 호출되는 함수 작성
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                // 위도 경도 값을 여기서 뽑아내서 저장
                currentLat = location.getLatitude();
                currentLng = location.getLongitude();

                // 위치 정보를 가져왔으면 리스너를 제거
                locationManager.removeUpdates(this);

                // 현재 내 위치기반 호텔 10곳 가져오기
                Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
                HotelApi api = retrofit.create(HotelApi.class);

                // 헤더에 들어갈 억세스토큰 가져오기
                SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

                offset = 0;

                Call<HotelList> call = api.getNearHotel(accessToken, currentLng, currentLat, offset, limit);
                call.enqueue(new Callback<HotelList>() {
                    @Override
                    public void onResponse(Call<HotelList> call, Response<HotelList> response) {
                        if(response.isSuccessful()){

                            NearhotelList.clear();
                            NearhotelList.addAll(response.body().getItems());

                            // 마커 찍기(내 주변 10곳)
                            int listSize = NearhotelList.size();
                            for (int i = 0; i < listSize && i <= 10; i++) {
                                Hotel hotel = NearhotelList.get(i);
                                googleMap.addMarker(new MarkerOptions().position(new LatLng(hotel.getLatitude(), hotel.getLongtitude()))
                                        .title(hotel.getTitle()).snippet(hotel.getAddr())).setTag(i);
                            }

                        }
                        else {
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<HotelList> call, Throwable t) {

                    }
                });

                // 내 위치정보를 가져오기
                myLocation = new LatLng(currentLat, currentLng);

                // 지도의 중심을 내 위치로 셋팅
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17));

                // 마커 클릭시 이벤트 처리
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        int tag = (int) marker.getTag();
                        // 새로 열릴 액티비티를 지정하고 Intent 객체 생성
                        Hotel hotel = NearhotelList.get(tag);
                        Intent intent = new Intent(getActivity(), HotelInfoActivity.class);
                        intent.putExtra("hotel", hotel);
                        startActivity(intent);
                        return true;
                    }
                });
            }
        };

        // 권한요청
        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    100);
        }

        // 위치기반으로 GPS 정보 가져오는 코드를 실행하는 부분
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }

    //이 메서드가 없으면 지도가 보이지 않음
    @Override
    public void onStart() {
        super.onStart();
        sView.onStart();
    }

    @Override
    public void onStop () {
        super.onStop();
        sView.onStop();

    }

    @Override
    public void onSaveInstanceState (@Nullable Bundle outState){
        super.onSaveInstanceState(outState);
        sView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        sView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sView.onLowMemory();
    }

}