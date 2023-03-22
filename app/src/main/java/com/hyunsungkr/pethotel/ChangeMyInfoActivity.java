package com.hyunsungkr.pethotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.PetApi;
import com.hyunsungkr.pethotel.api.UserApi;
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Res;
import com.hyunsungkr.pethotel.model.User;
import com.hyunsungkr.pethotel.model.UserRes;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangeMyInfoActivity extends AppCompatActivity {

    ImageView imgBack;
    ImageView imgProfile;
    ImageView imgChangePassword;
    TextView txtChangeProfile;
    TextView txtLogout;
    TextView txtEmail;
    EditText editName;
    EditText editPhone;
    Button btnSave;
    TextView txtChangePassword;
    String accessToken;
    User user;
    File photoFile;
    private ProgressDialog dialog;

    ArrayList<User> userArrayList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_my_info);
        imgBack = findViewById(R.id.imgBack);
        imgProfile = findViewById(R.id.imgProfile);
        txtChangePassword = findViewById(R.id.txtChangePassword);
        txtChangeProfile = findViewById(R.id.txtChangeProfile);
        txtLogout = findViewById(R.id.txtLogout);
        txtEmail = findViewById(R.id.txtEmail);
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        btnSave = findViewById(R.id.btnSave);
        imgChangePassword = findViewById(R.id.imgChangePassword);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        UserCheck();

        // 로그아웃
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeMyInfoActivity.this);
                builder.setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // sp의 억세스 토큰 삭제
                                SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.remove(Config.ACCESS_TOKEN);
                                editor.apply();
                                Intent intent = new Intent(ChangeMyInfoActivity.this,LoginActivity.class);
                                startActivity(intent);
                                Toast.makeText(ChangeMyInfoActivity.this,"로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        // 뒤로가기 버튼
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        // 비밀번호 변경으로 이동
        imgChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePassword();
            }
        });
        // 비밀번호 변경으로 이동
        txtChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePassword();
            }
        });


        // 저장버튼 클릭 리스너
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = editName.getText().toString().trim();
                String Phone = editPhone.getText().toString().trim();
                updateUser(accessToken, Name, Phone);

                // 변경된 정보 없을때
                if (Name.equals(user.getName()) && Phone.equals(user.getPhone())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangeMyInfoActivity.this);
                    builder.setMessage("변경된 사항이 없습니다.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // 변경 할 때 띄울 다이얼로그
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangeMyInfoActivity.this);
                    builder.setMessage("변경하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(ChangeMyInfoActivity.this,"저장되었습니다",Toast.LENGTH_SHORT).show();
                                    finish(); // 액티비티 종료
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss(); // 다이얼로그 닫기
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        // 이미지 변경 클릭시 이미지 변경
        txtChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

    }

    private void UserCheck() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(ChangeMyInfoActivity.this);
        UserApi api = retrofit.create(UserApi.class);

        Call<UserRes> call = api.userCheck(accessToken);
        call.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if (response.isSuccessful()) {
                    UserRes userRes = response.body();
                    userArrayList.addAll( userRes.getUser() );
                    user = userArrayList.get(0);

                    // 예약자 정보 셋팅 ex) 김이름 | 010-1234-5678
                    txtEmail.setText(user.getEmail());
                    editName.setText(user.getName());
                    editPhone.setText(user.getPhone());
                    Glide.with(ChangeMyInfoActivity.this).load(user.getUserImgUrl()).into(imgProfile);


                } else {
                }
            }

            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {

            }
        });
    }

    public void updateUser(String token, String name, String phone) {
        User user = new User();
        user.setName(name);
        user.setPhone(phone);

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        UserApi userApi = retrofit.create(UserApi.class);
        Call<UserRes> call = userApi.userChangeName(accessToken, user);

        call.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if(response.isSuccessful()){
                    // 변경된 유저 정보 받아온 후 처리
                    UserRes userRes = response.body();

                } else {
                    // 에러 처리
                    // response.errorBody() 등을 사용하여 에러 메시지를 받아올 수 있음
                }
            }

            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {
                // 통신 실패 처리
            }
        });
    }

    public void ChangePassword(){
        Intent intent = new Intent(ChangeMyInfoActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeMyInfoActivity.this);
        builder.setTitle(R.string.alert_title);
        builder.setItems(R.array.alert_photo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(i == 0){
                    // 사진찍는 코드를 실행
                    camera();
                } else if(i == 1){
                    // 앨범에서 사진 가져오는 코드 실행
                    album();
                }

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void album(){
        if(checkPermission()){
            displayFileChoose();
        }else{
            requestPermission();
        }
    }

    private void displayFileChoose() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "SELECT IMAGE"), 300);
    }

    private void setImageProfile() {

        showProgress("프로필 이미지 저장 중...");
        // 프로필 저장 API 호출
        Retrofit retrofit = NetworkClient.getRetrofitClient(ChangeMyInfoActivity.this);
        UserApi api = retrofit.create(UserApi.class);

        // 헤더에 들어갈 억세스토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        // 멀티파트로 파일을 보내는 경우, 파일 파라미터를 만든다
        RequestBody fileBody = RequestBody.create(photoFile, MediaType.parse("image/*"));
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", photoFile.getName(), fileBody);

        Call<Res> call = api.setProfile(accessToken, photo);
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                // 서버에서 보낸 응답이 200 OK일때
                dismissProgress();
                if (response.isSuccessful()) {

                } else {
                }
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                dismissProgress();
            }

        });

    }

    // 네트워크 로직 처리시에 화면에 보여주는 함수
    void showProgress(String message){
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);
        dialog.show();
    }

    // 로직처리가 끝나면 화면에서 사라지는 함수
    void dismissProgress(){
        dialog.dismiss();
    }

    private void camera(){
        int permissionCheck = ContextCompat.checkSelfPermission(
                ChangeMyInfoActivity.this, android.Manifest.permission.CAMERA);

        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ChangeMyInfoActivity.this,
                    new String[]{android.Manifest.permission.CAMERA} ,
                    1000);
            Toast.makeText(ChangeMyInfoActivity.this, "카메라 권한이 필요합니다.",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(i.resolveActivity(ChangeMyInfoActivity.this.getPackageManager())  != null  ){

                // 사진의 파일명을 만들기
                String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                photoFile = getPhotoFile(fileName);

                Uri fileProvider = FileProvider.getUriForFile(ChangeMyInfoActivity.this,
                        "com.blockent.postingapp.fileprovider", photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                startActivityForResult(i, 100);

            } else{
                Toast.makeText(ChangeMyInfoActivity.this, "이폰에는 카메라 앱이 없습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }


    }

    private File getPhotoFile(String fileName) {
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try{
            return File.createTempFile(fileName, ".jpg", storageDirectory);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private void requestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(ChangeMyInfoActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Log.i("DEBUGGING5", "true");
            Toast.makeText(ChangeMyInfoActivity.this, "권한 수락이 필요합니다.",
                    Toast.LENGTH_SHORT).show();
        }else{
            Log.i("DEBUGGING6", "false");
            ActivityCompat.requestPermissions(ChangeMyInfoActivity.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 500);
        }
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(ChangeMyInfoActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_DENIED){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ChangeMyInfoActivity.this, "권한 허가 되었음",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChangeMyInfoActivity.this, "아직 승인하지 않았음",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 500: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ChangeMyInfoActivity.this, "권한 허가 되었음",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChangeMyInfoActivity.this, "아직 승인하지 않았음",
                            Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100 && resultCode == RESULT_OK){

            Bitmap photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

            ExifInterface exif = null;
            try {
                exif = new ExifInterface(photoFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            photo = rotateBitmap(photo, orientation);

            // 압축시킨다. 해상도 낮춰서
            OutputStream os;
            try {
                os = new FileOutputStream(photoFile);
                photo.compress(Bitmap.CompressFormat.JPEG, 50, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
            }

            photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

            imgProfile.setImageBitmap(photo);
            imgProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
            setImageProfile();


        }else if(requestCode == 300 && resultCode == RESULT_OK && data != null &&
                data.getData() != null){

            Uri albumUri = data.getData( );
            String fileName = getFileName( albumUri );
            try {

                ParcelFileDescriptor parcelFileDescriptor = getContentResolver( ).openFileDescriptor( albumUri, "r" );
                if ( parcelFileDescriptor == null ) return;
                FileInputStream inputStream = new FileInputStream( parcelFileDescriptor.getFileDescriptor( ) );
                photoFile = new File( this.getCacheDir( ), fileName );
                FileOutputStream outputStream = new FileOutputStream( photoFile );
                IOUtils.copy( inputStream, outputStream );

                // 압축시킨다. 해상도 낮춰서
                Bitmap photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                OutputStream os;
                try {
                    os = new FileOutputStream(photoFile);
                    photo.compress(Bitmap.CompressFormat.JPEG, 60, os);
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                }

                imgProfile.setImageBitmap(photo);
                imgProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                setImageProfile();

//                imageView.setImageBitmap( getBitmapAlbum( imageView, albumUri ) );

            } catch ( Exception e ) {
                e.printStackTrace( );
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    //앨범에서 선택한 사진이름 가져오기
    public String getFileName( Uri uri ) {
        Cursor cursor = getContentResolver( ).query( uri, null, null, null, null );
        try {
            if ( cursor == null ) return null;
            cursor.moveToFirst( );
            @SuppressLint("Range") String fileName = cursor.getString( cursor.getColumnIndex( OpenableColumns.DISPLAY_NAME ) );
            cursor.close( );
            return fileName;

        } catch ( Exception e ) {
            e.printStackTrace( );
            cursor.close( );
            return null;
        }
    }

}