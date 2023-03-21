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
import android.widget.RatingBar;
import android.widget.Toast;

import com.hyunsungkr.pethotel.api.NetworkClient;
import com.hyunsungkr.pethotel.api.ReviewApi;
import com.hyunsungkr.pethotel.config.Config;
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

public class ReviewWriteActivity extends AppCompatActivity {

    ImageView imgBack;
    ImageView imgPhoto;
    EditText editReview;
    RatingBar ratingBar;
    Button btnSave;
    float userRating;

    File photoFile;
    private ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);

        imgBack = findViewById(R.id.imgBack);
        imgPhoto = findViewById(R.id.imgPhoto);
        editReview = findViewById(R.id.editReview);
        ratingBar = findViewById(R.id.ratingBar);
        btnSave = findViewById(R.id.btnSave);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // 사용자가 ratingBar를 클릭하여 rating을 변경할 때 호출됩니다.
                // 변경된 rating 값을 변수에 저장하거나 DB에 저장하는 등의 작업을 수행할 수 있습니다.
                // 아래는 rating 값을 변수에 저장하는 예시 코드입니다.
                userRating = rating;
            }
        });




        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(photoFile == null){
                    Toast.makeText(ReviewWriteActivity.this, "사진은 필수입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String content = editReview.getText().toString().trim();



                if(content.isEmpty()){
                    Toast.makeText(ReviewWriteActivity.this, "내용은 필수입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                showProgress("리뷰 업로드 중...");

                Retrofit retrofit = NetworkClient.getRetrofitClient(ReviewWriteActivity.this);
                ReviewApi api = retrofit.create(ReviewApi.class);

                // 멀티파트로 파일을 보내는 경우, 파일 파라미터를 만든다.
                RequestBody fileBody = RequestBody.create(photoFile, MediaType.parse("image/jpg"));
                MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", photoFile.getName(), fileBody);

                // 멀티파트로 텍스트를 보내는 경우, 텍스트 파라미터 만든다.
                RequestBody contentBody = RequestBody.create(content, MediaType.parse("text/plain"));
                RequestBody ratingBody = RequestBody.create(String.valueOf(userRating), MediaType.parse("text/plain"));


                // 헤더에 들어갈 억세스토큰 가져오기
                SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

                // todo : hotelId 하드코딩 제거
                Call<Res> call = api.addReview(accessToken,5,photo,contentBody,ratingBody);

                call.enqueue(new Callback<Res>() {
                    @Override
                    public void onResponse(Call<Res> call, Response<Res> response) {
                        dismissProgress();
                        if(response.isSuccessful()){
                            finish();
                        }else{
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<Res> call, Throwable t) {

                    }
                });

            }
        });




    }


    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReviewWriteActivity.this);
        builder.setTitle(R.string.alert_title);
        builder.setItems(R.array.alert_photo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == 0) {
                    // 사진찍는 코드를 실행
                    camera();
                } else if (i == 1) {
                    // 앨범에서 사진 가져오는 코드 실행
                    album();
                }

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void album() {
        if (checkPermission()) {
            displayFileChoose();
        } else {
            requestPermission();
        }
    }

    private void displayFileChoose() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "SELECT IMAGE"), 300);
    }

    private void camera() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                ReviewWriteActivity.this, android.Manifest.permission.CAMERA);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ReviewWriteActivity.this,
                    new String[]{android.Manifest.permission.CAMERA},
                    1000);
            Toast.makeText(ReviewWriteActivity.this, "카메라 권한 필요합니다.",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (i.resolveActivity(ReviewWriteActivity.this.getPackageManager()) != null) {

                // 사진의 파일명을 만들기
                String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                photoFile = getPhotoFile(fileName);

                Uri fileProvider = FileProvider.getUriForFile(ReviewWriteActivity.this,
                        "com.hyunsungkr.pethotelapp.fileprovider", photoFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                startActivityForResult(i, 100);

            } else {
                Toast.makeText(ReviewWriteActivity.this, "이 핸드폰에는 카메라 앱이 없습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }


    }

    private File getPhotoFile(String fileName) {
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(fileName, ".jpg", storageDirectory);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ReviewWriteActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Log.i("DEBUGGING5", "true");
            Toast.makeText(ReviewWriteActivity.this, "권한 수락이 필요합니다.",
                    Toast.LENGTH_SHORT).show();
        } else {
            Log.i("DEBUGGING6", "false");
            ActivityCompat.requestPermissions(ReviewWriteActivity.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 500);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ReviewWriteActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_DENIED) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ReviewWriteActivity.this, "권한 허가 되었음",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReviewWriteActivity.this, "아직 승인하지 않았음",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 500: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ReviewWriteActivity.this, "권한 허가 되었음",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReviewWriteActivity.this, "아직 승인하지 않았음",
                            Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {

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

            imgPhoto.setImageBitmap(photo);
            imgPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);


        } else if (requestCode == 300 && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {

            Uri albumUri = data.getData();
            String fileName = getFileName(albumUri);
            try {

                ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(albumUri, "r");
                if (parcelFileDescriptor == null) return;
                FileInputStream inputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
                photoFile = new File(this.getCacheDir(), fileName);
                FileOutputStream outputStream = new FileOutputStream(photoFile);
                IOUtils.copy(inputStream, outputStream);

//                //임시파일 생성
//                File file = createImgCacheFile( );
//                String cacheFilePath = file.getAbsolutePath( );


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

                imgPhoto.setImageBitmap(photo);
                imgPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);

//                imageView.setImageBitmap( getBitmapAlbum( imageView, albumUri ) );

            } catch (Exception e) {
                e.printStackTrace();
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
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    //앨범에서 선택한 사진이름 가져오기
    public String getFileName(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        try {
            if (cursor == null) return null;
            cursor.moveToFirst();
            @SuppressLint("Range") String fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            cursor.close();
            return fileName;

        } catch (Exception e) {
            e.printStackTrace();
            cursor.close();
            return null;
        }
    }


    // 네트워크 로직 처리시에 화면에 보여주는 함수
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
}