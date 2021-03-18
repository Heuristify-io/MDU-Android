package com.heuristify.mdu.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.heuristify.mdu.database.AppDatabase;
import com.heuristify.mdu.database.DatabaseClient;
import com.heuristify.mdu.network.ApiClientAuth;
import com.heuristify.mdu.network.ApiClientAuthToken;
import com.heuristify.mdu.network.RetrofitServices;

import retrofit2.Retrofit;

public class MyApplication extends Application {

    private Activity activity = null;
    private static MyApplication mInstance;
    private RetrofitServices retrofitServices;
    private RetrofitServices retrofitServicesWithToken;
    private DatabaseClient databaseClient;


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

    public Activity getActivity(){
        return activity;
    }

    public RetrofitServices getRetrofitServices() {
        Retrofit retrofit = ApiClientAuth.getRetrofitFactoryToken();
        retrofitServices = retrofit.create(RetrofitServices.class);
        return retrofitServices;
    }

    public RetrofitServices getRetrofitServicesWithToken() {
        Retrofit retrofit = ApiClientAuthToken.getRetrofitFactoryToken();
        retrofitServicesWithToken = retrofit.create(RetrofitServices.class);
        return retrofitServicesWithToken;
    }

    public DatabaseClient getLocalDb(Context context){
        databaseClient = DatabaseClient.getInstance(context);
        return databaseClient;
    }

}

