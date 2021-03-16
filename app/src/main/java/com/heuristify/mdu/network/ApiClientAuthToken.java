package com.heuristify.mdu.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.helper.Helper;
import com.heuristify.mdu.sharedPreferences.SharedHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClientAuthToken {

    private static Retrofit retrofit = null;
    static BaseUrl urlHelper = new BaseUrl();

    public static Retrofit getRetrofitFactoryToken() {

        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.connectTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Log.e("token", "" + SharedHelper.getKey(MyApplication.getInstance(), Helper.JWT));

                    Request request = original.newBuilder()
                            .addHeader("content-type", "application/json")
                            .addHeader("Device-Type", "android")
                            .addHeader("Authorization",SharedHelper.getKey(MyApplication.getInstance(), Helper.JWT))
                            .addHeader("Connection", "close")

                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });


            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            OkHttpClient client = httpClient.build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(urlHelper.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
