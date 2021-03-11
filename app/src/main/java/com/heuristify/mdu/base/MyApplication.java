package com.heuristify.mdu.base;

import android.app.Activity;
import android.app.Application;

import com.heuristify.mdu.network.ApiClientAuth;
import com.heuristify.mdu.network.RetrofitServices;

import retrofit2.Retrofit;

public class MyApplication extends Application {

    private Activity activity = null;
    private static MyApplication mInstance;
    private RetrofitServices retrofitServices;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setCurrentActivity(Activity activity) {
        this.activity = activity;
    }

    public RetrofitServices getRetrofitServices() {
        Retrofit retrofit = ApiClientAuth.getRetrofitFactoryToken();
        retrofitServices = retrofit.create(RetrofitServices.class);
        return retrofitServices;
    }


}

