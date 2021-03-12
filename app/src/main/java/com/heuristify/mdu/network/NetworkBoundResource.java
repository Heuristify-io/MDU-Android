package com.heuristify.mdu.network;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class NetworkBoundResource<T, V> {

    private final MediatorLiveData<Resource<T>> result = new MediatorLiveData<>();


    @MainThread
    protected NetworkBoundResource() {
        result.setValue(Resource.loading(null));

        // Always load the data from DB intially so that we have
        LiveData<T> dbSource = loadFromDb();

        result.removeSource(dbSource);
        if (shouldFetch()) {
            fetchFromNetwork(dbSource);
        } else {
            result.addSource(dbSource, newData -> {
                if(null != newData)
                    result.setValue(Resource.success(newData)); ;
            });
        }

    }

    /**
     * This method fetches the data from remoted service and save it to local db
     * @param dbSource - Database source
     */
    private void fetchFromNetwork(final LiveData<T> dbSource) {
        result.addSource(dbSource, newData -> result.setValue(Resource.loading(newData)));
        createCall().enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                result.removeSource(dbSource);

                Log.e("NetworkError",response.toString());
                if(response.isSuccessful())
                {
                    Log.e("NetworkError","Successful" + response.toString());
                    saveResultAndReInit(response.body());
                }

                else  if(response.code()==400)
                {
                    try {
                        JSONObject jsonObject=new JSONObject(response.errorBody().string());
                        result.addSource(dbSource, newData -> {
                            try {
                                result.postValue(Resource.error(jsonObject.getString("msg"), newData));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                result.postValue(Resource.error(response.errorBody().toString(), newData));
                            }
                        });

                    }


                    catch (JSONException e) {
                        e.printStackTrace();
                        result.addSource(dbSource, newData -> {

                            result.postValue(Resource.error(response.errorBody().toString(), newData));

                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(response.code()==405)
                {

                    result.setValue(Resource.error("405",null));
                    // result.addSource(dbSource, newData -> result.postValue(Resource.error("405", newData)));

                }
                else if(response.code()==440)
                {
                    try {
                        JSONObject jsonObject= null;
                        try {
                            jsonObject = new JSONObject(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        XuberApplication.getInstance().ShowTokenExperied(jsonObject.getString("msg"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(response.code()==503)
                {

//                    XuberApplication.getInstance().ShowMaintainceDialog();
                }

                else{
                    result.setValue(Resource.error("network Connection Error",null));
                    // result.addSource(dbSource, newData -> result.postValue(Resource.error("network Connection Error", newData)));

                }

            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                if(t!=null && t.getMessage()!=null)
                {
                    Log.e("NetworkError",t.getMessage());
                }

                if(t!=null)
                {
                    result.setValue(Resource.error(getCustomErrorMessage(t),null));

                }
               /* result.removeSource(dbSource);
                result.addSource(dbSource, newData -> result.setValue(Resource.error(getCustomErrorMessage(t), newData)));*/
            }
        });
    }

    private String getCustomErrorMessage(Throwable t){
        if (t instanceof IOException) {

            return  "network Connection Error";
            // logging probably not necessary
        }
        else if(t instanceof TimeoutException){
            return "Connection Timeout Please Check Your internet Connection";
        }
        else {
            return "Internet  Connection Error Please Check Your internet Connection";
        }
    }

    @SuppressLint("StaticFieldLeak")
    @MainThread
    private void saveResultAndReInit(T response) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                saveCallResult(response);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
              /*  result.addSource(loadFromDb(), newData -> {


                    if (null != newData)

                });*/
                result.setValue(Resource.success(response));
            }
        }.execute();
    }

    @WorkerThread
    protected abstract void saveCallResult(T item);

    @MainThread
    private boolean shouldFetch() {
        return true;
    }

    @NonNull
    @MainThread
    protected abstract LiveData<T> loadFromDb();

    @NonNull
    @MainThread
    protected abstract Call<T> createCall();

    public final LiveData<Resource<T>> getAsLiveData() {
        return result;
    }
}
