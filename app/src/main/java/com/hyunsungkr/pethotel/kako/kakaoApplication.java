package com.hyunsungkr.pethotel.kako;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class kakaoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this, "70ab4549f53f7d034f21655a21dcdecb");
    }
}
