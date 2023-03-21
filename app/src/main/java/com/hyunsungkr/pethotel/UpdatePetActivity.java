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
import com.hyunsungkr.pethotel.config.Config;
import com.hyunsungkr.pethotel.model.Pet;
import com.hyunsungkr.pethotel.model.Res;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdatePetActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    ImageView imgBack;
    ImageView imgAdd;
    EditText editName;
    TextView txtDog;
    TextView txtCat;
    EditText editSpecies;
    EditText editAge;
    EditText editWeight;
    TextView txtMale;
    TextView txtFemale;
    Button btnSave;
    File photoFile;
    int ClassificationType = -1;
    int genderType = -1;
    Pet pet;
    RequestBody nameBody;
    RequestBody classificationBody;
    RequestBody speciesBody;
    RequestBody ageBody;
    RequestBody weightBody;
    RequestBody genderBody;
    RequestBody petImgUrlBody;
    MultipartBody.Part photo;
    String petImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pet);

        imgBack = findViewById(R.id.imgBack);
        imgAdd = findViewById(R.id.imgAdd);
        editName = findViewById(R.id.editName);
        txtDog = findViewById(R.id.txtDog);
        txtCat = findViewById(R.id.txtCat);
        editSpecies = findViewById(R.id.editSpecies);
        editAge = findViewById(R.id.editAge);
        editWeight = findViewById(R.id.editWeight);
        txtMale = findViewById(R.id.txtMale);
        txtFemale = findViewById(R.id.txtFemale);
        btnSave = findViewById(R.id.btnSave);

        // 뒤로가기 처리
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // 누른 반려동물의 정보를 받아와서 셋팅한다
        pet = (Pet) getIntent().getSerializableExtra("pet");
        petImgUrl = pet.getPetImgUrl();

        // 사진 셋팅
        Glide.with(UpdatePetActivity.this).load(petImgUrl).placeholder(R.drawable.icon2).into(imgAdd);

        // 받아온 정보 셋팅
        editName.setText(pet.getName());
        editSpecies.setText(pet.getSpecies());
        editAge.setText(pet.getAge()+"");
        editWeight.setText(pet.getWeight()+"");

        if (pet.getClassification() == 0) {
            // 0이면 강아지, 1이면 고양이
            ClassificationType = 0;
            txtDog.setBackgroundResource(R.drawable.edittext3);
            txtDog.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        } else {
            ClassificationType = 1;
            txtCat.setBackgroundResource(R.drawable.edittext3);
            txtCat.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        }

        if (pet.getGender() == 0) {
            // 0이면 남, 1이면 여
            genderType = 0;
            txtMale.setBackgroundResource(R.drawable.edittext3);
            txtMale.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        } else {
            genderType = 1;
            txtFemale.setBackgroundResource(R.drawable.edittext3);
            txtFemale.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        }

        // 아래 코드는 수정시 입력받는 정보

        // 반려동물 사진받기
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        // 강아지, 고양이 버튼
        txtDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 강아지 버튼 클릭시 강아지 버튼 활성화, 고양이 버튼 비활성화
                txtDog.setBackgroundResource(R.drawable.edittext3);
                txtCat.setBackgroundResource(R.drawable.edittext2);
                txtDog.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                txtCat.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                ClassificationType = 0;
            }
        });

        txtCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 고양이 버튼 클릭시 고양이 버튼 활성화, 강아지 버튼 비활성화
                txtCat.setBackgroundResource(R.drawable.edittext3);
                txtDog.setBackgroundResource(R.drawable.edittext2);
                txtCat.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                txtDog.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                ClassificationType = 1;
            }
        });

        // 성별 버튼
        txtMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtMale.setBackgroundResource(R.drawable.edittext3);
                txtFemale.setBackgroundResource(R.drawable.edittext2);
                txtMale.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                txtFemale.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                genderType = 0;
            }
        });

        txtFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtFemale.setBackgroundResource(R.drawable.edittext3);
                txtMale.setBackgroundResource(R.drawable.edittext2);
                txtFemale.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                txtMale.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                genderType = 1;
            }
        });

        // 반려동물 정보 가져와서 저장하기
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 이름 정보 가져오기
                String name = editName.getText().toString().trim();
                if (name == "") {
                    Toast.makeText(UpdatePetActivity.this, "정보를 다 입력해주세요", Toast.LENGTH_SHORT).show();
                }

                // 종류(강아지, 고양이) 정보 가져오기
                int classification = ClassificationType;
                if (classification == -1) {
                    Toast.makeText(UpdatePetActivity.this, "종류를 선택해주세요", Toast.LENGTH_SHORT).show();
                }

                // 종 정보 가져오기
                String species = editSpecies.getText().toString().trim();

                // 나이와 무게정보 가져오기
                int age = Integer.parseInt(editAge.getText().toString().trim());
                int weight = Integer.parseInt(editWeight.getText().toString().trim());

                if (species == "" && age == 0 && weight == 0) {
                    Toast.makeText(UpdatePetActivity.this, "정보를 다 입력해주세요", Toast.LENGTH_SHORT).show();
                }

                // 성별 정보 가져오기
                int gender = genderType;
                if (gender == -1) {
                    Toast.makeText(UpdatePetActivity.this, "성별을 선택해주세요", Toast.LENGTH_SHORT).show();
                }

                // 멀티파트로 파일을 보내는 경우, 파일 파라미터를 만든다
                nameBody = RequestBody.create(name, MediaType.parse("text/plain"));
                classificationBody = RequestBody.create(String.valueOf(classification), MediaType.parse("text/plain"));
                speciesBody = RequestBody.create(species, MediaType.parse("text/plain"));
                ageBody = RequestBody.create(String.valueOf(age), MediaType.parse("text/plain"));
                weightBody = RequestBody.create(String.valueOf(weight), MediaType.parse("text/plain"));
                genderBody = RequestBody.create(String.valueOf(gender), MediaType.parse("text/plain"));
                petImgUrlBody = RequestBody.create(petImgUrl, MediaType.parse("text/plain"));

                // 사진이 선택 되었는지 확인
                if (photoFile != null) {
                    RequestBody fileBody = RequestBody.create(photoFile, MediaType.parse("image/*"));
                    photo = MultipartBody.Part.createFormData("photo", photoFile.getName(), fileBody);
                } else {
                    RequestBody fileBody = RequestBody.create(MultipartBody.FORM, "");
                    photo = MultipartBody.Part.createFormData("photo", "", fileBody);
                }

                setNetworkData();
            }
        });
    }

    private void setNetworkData(){
        showProgress("반려동물 정보 저장 중...");
        // 반려동물 저장 API 호출
        Retrofit retrofit = NetworkClient.getRetrofitClient(UpdatePetActivity.this);
        PetApi api = retrofit.create(PetApi.class);

        // 헤더에 들어갈 억세스토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<Res> call = api.updatePet(accessToken, pet.getId(), photo, nameBody, classificationBody, speciesBody, ageBody,
                weightBody, genderBody, petImgUrlBody);
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                // 서버에서 보낸 응답이 200 OK일때
                dismissProgress();
                if (response.isSuccessful()) {
                    finish();


                } else {
                }
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                dismissProgress();
            }

        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePetActivity.this);
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

    private void camera(){
        int permissionCheck = ContextCompat.checkSelfPermission(
                UpdatePetActivity.this, android.Manifest.permission.CAMERA);

        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(UpdatePetActivity.this,
                    new String[]{android.Manifest.permission.CAMERA} ,
                    1000);
            Toast.makeText(UpdatePetActivity.this, "카메라 권한이 필요합니다.",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(i.resolveActivity(UpdatePetActivity.this.getPackageManager())  != null  ){

                // 사진의 파일명을 만들기
                String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                photoFile = getPhotoFile(fileName);

                Uri fileProvider = FileProvider.getUriForFile(UpdatePetActivity.this,
                        "com.blockent.postingapp.fileprovider", photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                startActivityForResult(i, 100);

            } else{
                Toast.makeText(UpdatePetActivity.this, "이폰에는 카메라 앱이 없습니다.",
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
        if(ActivityCompat.shouldShowRequestPermissionRationale(UpdatePetActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Log.i("DEBUGGING5", "true");
            Toast.makeText(UpdatePetActivity.this, "권한 수락이 필요합니다.",
                    Toast.LENGTH_SHORT).show();
        }else{
            Log.i("DEBUGGING6", "false");
            ActivityCompat.requestPermissions(UpdatePetActivity.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 500);
        }
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(UpdatePetActivity.this,
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
                    Toast.makeText(UpdatePetActivity.this, "권한 허가 되었음",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdatePetActivity.this, "아직 승인하지 않았음",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 500: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(UpdatePetActivity.this, "권한 허가 되었음",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdatePetActivity.this, "아직 승인하지 않았음",
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

            imgAdd.setImageBitmap(photo);
            imgAdd.setScaleType(ImageView.ScaleType.CENTER_CROP);


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

                imgAdd.setImageBitmap(photo);
                imgAdd.setScaleType(ImageView.ScaleType.CENTER_CROP);

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
}