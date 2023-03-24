package com.hyunsungkr.pethotel;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyunsungkr.pethotel.adapter.FavoriteAdapter;
import com.hyunsungkr.pethotel.adapter.MyPetAdapter;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.PetApi;
import com.hyunsungkr.pethotel.api.UserApi;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Hotel;
import com.hyunsungkr.pethotel.model.Pet;
import com.hyunsungkr.pethotel.model.PetInfoList;
import com.hyunsungkr.pethotel.model.Res;
import com.hyunsungkr.pethotel.model.UserMyPage;
import com.hyunsungkr.pethotel.model.UserMyPageRes;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static MyInfo newInstance(String param1, String param2) {
        MyInfo fragment = new MyInfo();
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

    ImageView imgPhoto;
    ImageView imgMyPoint;
    ImageView imgMyCoupon;
    ImageView imgMyReview;
    ImageView imgMyFavorite;
    ImageView imgMyReservation;
    ImageView imgMyinfo;

    TextView txtName;
    TextView txtPoint;
    TextView txtCoupon;
    TextView txtPetRegister;
    TextView txtCheckReservation;
    TextView txtInquiry;

    ArrayList<UserMyPage> mypageList = new ArrayList<>();

    RecyclerView recyclerView;
    MyPetAdapter adapter;
    ArrayList<Pet> petList = new ArrayList<>();

    // 인텐트에 담기위한 멤버변수
    String MyPoint = "";
    int userId;

    private int deleteIndex;
    private Pet selectedPet;
    final int adminId = 55;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_info, container, false);

        imgPhoto = rootView.findViewById(R.id.imgPhoto);
        imgMyPoint = rootView.findViewById(R.id.imgMyPoint);
        imgMyCoupon = rootView.findViewById(R.id.imgMyCoupon);
        imgMyReview = rootView.findViewById(R.id.imgMyReview);
        imgMyFavorite = rootView.findViewById(R.id.imgMyFavorite);
        imgMyReservation = rootView.findViewById(R.id.imgMyReservation);
        imgMyinfo = rootView.findViewById(R.id.imgMyinfo);

        txtName = rootView.findViewById(R.id.txtName);
        txtPoint = rootView.findViewById(R.id.txtPoint);
        txtCoupon = rootView.findViewById(R.id.txtCoupon);
        txtPetRegister = rootView.findViewById(R.id.txtPetRegister);
        txtCheckReservation = rootView.findViewById(R.id.txtCheckReservation);
        txtInquiry = rootView.findViewById(R.id.txtInquiry);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        txtInquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userId == adminId){
                    Intent intent = new Intent(getActivity(),AdminActivity.class);
                    startActivity(intent);
                }else{
                Intent intent = new Intent(getActivity(),ChatActivity.class);
                startActivity(intent);}
            }
        });

        imgMyPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),PointActivity.class);
                intent.putExtra("point", MyPoint);
                startActivity(intent);
            }
        });

        imgMyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CouponActivity.class);
                startActivity(intent);
            }
        });

        imgMyinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ChangeMyInfoActivity.class);
                startActivity(intent);
            }
        });

        imgMyReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MyReviewActivity.class);
                startActivity(intent);
            }
        });

        imgMyReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MyReservationActivity.class);
                startActivity(intent);
            }
        });

        imgMyFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, new Favorite());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        txtPetRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PetRegister.class);
                startActivity(intent);
            }
        });

        txtCheckReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MyReservationActivity.class);
                startActivity(intent);

            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getNetworkData();
        getPetData();
    }

    void getNetworkData(){
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        UserApi api = retrofit.create(UserApi.class);

        // 헤더에 들어갈 억세스토큰 가져오기
        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<UserMyPageRes> call = api.userMypage(accessToken);
        call.enqueue(new Callback<UserMyPageRes>() {
            @Override
            public void onResponse(Call<UserMyPageRes> call, Response<UserMyPageRes> response) {
                if(response.isSuccessful()){
                    mypageList.clear(); // 기존 데이터 삭제.
                    mypageList.addAll(response.body().getItems());
                    txtName.setText(mypageList.get(0).getName());
                    txtPoint.setText(mypageList.get(0).getTotalPoint()+"");
                    MyPoint = mypageList.get(0).getTotalPoint()+"";
                    userId = mypageList.get(0).getId();
                    txtCoupon.setText(mypageList.get(0).getCntCoupon()+"");
                    Glide.with(getActivity()).load(mypageList.get(0).getUserImgUrl()).placeholder(R.drawable.icon2).into(imgPhoto);



                }else{
                    return;
                }
            }

            @Override
            public void onFailure(Call<UserMyPageRes> call, Throwable t) {

            }
        });

    }

    void getPetData(){

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        PetApi api = retrofit.create(PetApi.class);

        // 헤더에 들어갈 억세스토큰 가져오기
        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<PetInfoList> call = api.getPetList(accessToken);
        call.enqueue(new Callback<PetInfoList>() {
            @Override
            public void onResponse(Call<PetInfoList> call, Response<PetInfoList> response) {
                if (response.isSuccessful()){

                    petList.clear();
                    petList.addAll(response.body().getItems());

                    adapter = new MyPetAdapter(getActivity(),petList);
                    adapter.setOnItemClickListener(new MyPetAdapter.OnItemClickListener() {
                        @Override
                        public void onCardViewClick(int index) {
                            Pet pet = petList.get(index);
                            Intent intent = new Intent(getActivity(), UpdatePetActivity.class);
                            intent.putExtra("pet", pet);
                            startActivity(intent);
                        }

                        @Override
                        public void deleteProcess(int index) {
                            MyInfo.this.deleteProcess(index);
                        }
                    });
                    recyclerView.setAdapter(adapter);

                }else{
                    return;
                }
            }

            @Override
            public void onFailure(Call<PetInfoList> call, Throwable t) {

            }
        });

    }

    public void deleteProcess(int index) {
        selectedPet = petList.get(index);
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        PetApi api = retrofit.create(PetApi.class);

        // 헤더에 들어갈 억세스토큰 가져오기
        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<Res> call = api.deletePet(accessToken, selectedPet.getId());
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if(response.isSuccessful()){
                    petList.remove(selectedPet);
                    adapter.notifyDataSetChanged();
                }else{
                    return;
                }
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {

            }
        });
    }

}
