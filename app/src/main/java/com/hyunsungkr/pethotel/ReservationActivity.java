package com.hyunsungkr.pethotel;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyunsungkr.pethotel.api.CouponApi;
import com.hyunsungkr.pethotel.api.HotelApi;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.PointApi;
import com.hyunsungkr.pethotel.api.ReservationApi;
import com.hyunsungkr.pethotel.api.UserApi;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Coupon;
import com.hyunsungkr.pethotel.model.Hotel;
import com.hyunsungkr.pethotel.model.Pet;
import com.hyunsungkr.pethotel.model.Point;
import com.hyunsungkr.pethotel.model.PointRes;
import com.hyunsungkr.pethotel.model.Res;
import com.hyunsungkr.pethotel.model.Reservation;
import com.hyunsungkr.pethotel.model.User;
import com.hyunsungkr.pethotel.model.UserRes;

import java.text.DecimalFormat;
import java.util.ArrayList;

import kr.co.bootpay.android.Bootpay;
import kr.co.bootpay.android.BootpayAnalytics;
import kr.co.bootpay.android.events.BootpayEventListener;
import kr.co.bootpay.android.models.BootItem;
import kr.co.bootpay.android.models.BootUser;
import kr.co.bootpay.android.models.Payload;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReservationActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    ImageView imgBack;
    TextView txtHotelName;
    TextView txtReservation;
    TextView txtTime;
    TextView txtPrice;
    TextView txtUser;
    TextView txtPet;
    EditText editEtc;
    TextView txtPrice2;
    TextView txtCoupon;
    EditText editPoint;
    TextView txtTotalPoint;
    TextView txtFinalPrice;
    Button btnApproval;

    int totalPoint;
    int couponId;
    int discount;
    int point = 0;
    int finalPrice;
    int couponPrice;
    int price;

    Hotel hotel;
    User user;
    Pet pet;
    Reservation reservation;
    String content;
    ArrayList<User> userArrayList = new ArrayList<>();

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            // 유저가 선택한 쿠폰 정보 가져오기
            if (result.getResultCode() == 101){
                Coupon coupon = (Coupon) result.getData().getSerializableExtra("coupon");

                couponId = coupon.getId();
                discount = coupon.getDiscount();

                // 유저가 선택한 쿠폰이 있을 경우 금액 차감
                couponPrice = price - discount;
                DecimalFormat myFormatter = new DecimalFormat("###,###");
                txtFinalPrice.setText(myFormatter.format(couponPrice) + "원");

                // 사용한 쿠폰 이름 셋팅
                txtCoupon.setText(coupon.getDescription() + " " + myFormatter.format(discount) + "원");
                // 버튼에도 셋팅
                btnApproval.setText(myFormatter.format(couponPrice) + "원 결제하기");
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        imgBack = findViewById(R.id.imgBack);
        txtHotelName = findViewById(R.id.txtHotelName);
        txtReservation = findViewById(R.id.txtReservation);
        txtTime = findViewById(R.id.txtTime);
        txtPrice = findViewById(R.id.txtDiscount);
        txtUser = findViewById(R.id.txtUser);
        txtPet = findViewById(R.id.txtPet);
        editEtc = findViewById(R.id.editEtc);
        txtPrice2 = findViewById(R.id.txtPrice2);
        txtCoupon = findViewById(R.id.txtCoupon);
        editPoint = findViewById(R.id.editPoint);
        txtTotalPoint = findViewById(R.id.txtTotalPoint);
        txtFinalPrice = findViewById(R.id.txtFinalPrice);
        btnApproval = findViewById(R.id.btnApproval);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 뒤로가기 클릭시 액티비티 종료
                finish();
            }
        });

        // 유저 정보 불러오기
        UserCheck();

        // 호텔정보 액티비티에서 정보 받아오기(호텔정보, 유저, 펫, 예약정보)
        hotel = (Hotel) getIntent().getSerializableExtra("hotel");
        pet = (Pet) getIntent().getSerializableExtra("pet");
        reservation = (Reservation) getIntent().getSerializableExtra("reservation");

        // 호텔 이름 셋팅
        txtHotelName.setText(hotel.getTitle());

        // 체크인 일정 셋팅 ex) 2023-02-28 (화) ~ 2023-03-01(수) | 1박
        String checkIn = reservation.getCheckInDate();
        String checkOut = reservation.getCheckOutDate();
        txtReservation.setText(checkIn + " ~ " + checkOut);

        // 체크인과 체크아웃 시간 셋팅 ex) 체크인 12:00 | 체크아웃 12:00
        String checkInTime = "12:00";
        String checkOutTime = "12:00";
        txtTime.setText("체크인 " + checkInTime + " | 체크아웃 " + checkOutTime);

        // 예약 금액 셋팅
        DecimalFormat myFormatter = new DecimalFormat("###,###");
        txtPrice.setText(myFormatter.format(reservation.getPrice()) + "원");
        btnApproval.setText(myFormatter.format(reservation.getPrice()) + "원 결제하기");

        // 예약자 반려동물 셋팅 ex) 반려동물 | 장군이
        txtPet.setText("반려동물 | " + pet.getName());

        // 기타 사항 입력 받는 정보 가져오기
        content = editEtc.getText().toString().trim();

        // 예약 금액 셋팅
        txtPrice2.setText(myFormatter.format(reservation.getPrice()) + "원");

        // 쿠폰 클릭시 새로운 액티비티 열고 그 액티비티에서 쿠폰 퍼센트 가져오기
        txtCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReservationActivity.this, UseCouponActivity.class);
                launcher.launch(intent);
            }
        });

        // 원본 결제 금액
        price = reservation.getPrice();
        txtFinalPrice.setText(myFormatter.format(price) + "원");

        // 포인트 사용시 입력 받는 정보 가져오기
        editPoint.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             }

             @Override
             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
             }

             @Override
             public void afterTextChanged(Editable editable) {
                 // 유저가 텍스트를 입력할때마다 최종금액 변동시키기
                 String usePoint = editPoint.getText().toString().trim();
                 point = Integer.parseInt(usePoint);
                 finalPrice = couponPrice - point;
                 DecimalFormat myFormatter = new DecimalFormat("###,###");
                 txtFinalPrice.setText(myFormatter.format(finalPrice) + "원");
                 btnApproval.setText(myFormatter.format(finalPrice) + "원 결제하기");

             }
         });

        // 현재 내 포인트 가져와서 표시
        CheckPoint();

        // 결제창 띄우기(결제 완료시 내 예약 액티비티로 넘어감)
        BootpayAnalytics.init(this, Config.access_key);
        btnApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 구매자 정보 설정
                BootUser bootUser = new BootUser();
                bootUser.setUsername(user.getName());
                bootUser.setPhone("010-5091-4467");
                bootUser.setArea("TEST");
                bootUser.setEmail(user.getEmail());

                // 구매 아이템 설정
                BootItem bootItem = new BootItem();
                bootItem.setName(hotel.getTitle());
                bootItem.setPrice((double) finalPrice);
                bootItem.setId(hotel.getId()+"");

                // 결제 항목 설정
                Payload payload = new Payload();
                payload.setApplicationId(Config.access_key);
                payload.setOrderName("테스트 결제");
                payload.setPg("다날");
                payload.setMethod("카드수기");
                payload.setOrderName(bootItem.getName());
                payload.setPrice(bootItem.getPrice());
                payload.setUser(bootUser);
                payload.setOrderId(bootItem.getId());

                // 부트패이 실행
                Bootpay.init(getSupportFragmentManager(),getApplicationContext()).setPayload(payload).setEventListener(new BootpayEventListener() {
                    @Override
                    public void onCancel(String data) {
                    }

                    @Override
                    public void onError(String data) {
                    }

                    @Override
                    public void onClose() {
                        Bootpay.removePaymentWindow();
                    }

                    @Override
                    public void onIssued(String data) {
                    }

                    @Override
                    public boolean onConfirm(String data) {
                        return true;
                    }

                    @Override
                    public void onDone(String data) {
                        // 유저 예약 정보 저장
                        ReservationSave();
                    }
                }).requestPayment();

            }
        });
    }

    // 회원 정보 가져오기
    private void UserCheck() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(ReservationActivity.this);
        UserApi api = retrofit.create(UserApi.class);

        // 헤더에 들어갈 억세스토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<UserRes> call = api.userCheck(accessToken);
        call.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if (response.isSuccessful()) {
                    UserRes userRes = response.body();
                    userArrayList.addAll( userRes.getUser() );
                    user = userArrayList.get(0);

                    // 예약자 정보 셋팅 ex) 김이름 | 010-1234-5678
                    txtUser.setText(user.getName() + " | " + user.getPhone());

                } else {
                }
            }

            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {

            }
        });
    }

    // 네트워크 로직처리시에 화면에 보여주는 함수
    void showProgress(String message) {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);
        dialog.show();
    }

    // 로직처리가 끝나면 화면에서 사라지는 함수
    void dismissProgress() {
        dialog.dismiss();
    }

    // 포인트 조회 API 호출
    void CheckPoint() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(ReservationActivity.this);
        PointApi api = retrofit.create(PointApi.class);

        // 헤더에 들어갈 억세스토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<PointRes> call = api.totalPoint(accessToken);
        call.enqueue(new Callback<PointRes>() {
            @Override
            public void onResponse(Call<PointRes> call, Response<PointRes> response) {
                // 서버에서 보낸 응답이 200 OK일때
                if (response.isSuccessful()) {
                    PointRes pointRes = response.body();
                    totalPoint = pointRes.getTotalPoint();
                    txtTotalPoint.setText(totalPoint+"p");

                } else {
                }
            }

            @Override
            public void onFailure(Call<PointRes> call, Throwable t) {
            }
        });
    }

    // 포인트 사용 API 호출
    void UsePoint() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(ReservationActivity.this);
        PointApi api = retrofit.create(PointApi.class);

        // 헤더에 들어갈 억세스토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        String content = hotel.getTitle() + " 결제";
        int addPoint = -point;
        Point usePoint = new Point(content, addPoint);

        Call<Res> call = api.usePoint(accessToken, usePoint);
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {}
                else {}
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
            }
        });
    }

    // 쿠폰 사용 API 호출
    void UseCoupon() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(ReservationActivity.this);
        CouponApi api = retrofit.create(CouponApi.class);

        // 헤더에 들어갈 억세스토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<Res> call = api.useCoupon(couponId, accessToken);
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {}
                else {}
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
            }

        });
    }

    // 예약정보 저장 API 호출
    void ReservationSave() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(ReservationActivity.this);
        ReservationApi api = retrofit.create(ReservationApi.class);

        // 헤더에 들어갈 억세스토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        int petId = pet.getId();
        int hotelId = hotel.getId();
        String checkInDate = reservation.getCheckInDate();
        String checkOutDate = reservation.getCheckOutDate();
        int price = reservation.getPrice();

        Reservation reservation = new Reservation(petId, hotelId, checkInDate, checkOutDate, content, price);

        Call<Res> call = api.reservationSave(accessToken, reservation);
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                // 서버에서 보낸 응답이 200 OK일때
                if (response.isSuccessful()) {

                    // 유저가 사용한 포인트가 있다면 차감
                    if (point != 0) {
                        UsePoint();
                    }
                    // 유저가 사용한 쿠폰이 있다면 사용 처리
                    if (couponId != 0) {
                        UseCoupon();
                    }

                    // 결제 완료 예약 액티비티 종료하고 내 예약정보 액티비티로 이동
                    Intent intent = new Intent(ReservationActivity.this, MyReservationActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                }
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
            }

        });
    }

}