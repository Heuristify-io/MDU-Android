package com.heuristify.mdu.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.databinding.ActivityPinViewBinding;
import com.heuristify.mdu.mvvm.viewmodel.LoginViewModel;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class PinViewActivity extends BindingBaseActivity<ActivityPinViewBinding> implements View.OnClickListener, LifecycleOwner {
    LoginViewModel loginViewModel;
    private String TAG = "PinViewActivity";
    private Context context;
    private Observer observer, progressObserver, errorObserver;
    private String pin_code = "";
    private LifecycleOwner lifecycleOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBinding.textViewRequestCode.setOnClickListener(this);
        dataBinding.textViewBack.setOnClickListener(this);
        dataBinding.imageViewBack.setOnClickListener(this);
        dataBinding.buttonSignIn.setOnClickListener(this);
        dataBinding.linearBack.setOnClickListener(this);
        context = this;

        lifecycleOwner = this;
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        observer = new Observer<Response<ResponseBody>>() {
            @Override
            public void onChanged(Response<ResponseBody> responseBodyResponse) {
                if (responseBodyResponse.isSuccessful()) {
                    Toast.makeText(mContext, "Login Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PinViewActivity.this, AttendingActivity.class));
                }
                Log.e(TAG, "response" + responseBodyResponse.code());
//                loginViewModel.getLoginRepository(Integer.parseInt(pin_code)).removeObserver(this);
            }
        };


//        errorObserver = new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                Log.e(TAG, "getError_msg " + s);
//                loginViewModel.getError_msg().removeObserver(this);
//                hideProgressDialog();
//
//            }
//        };

        loginViewModel.getError_msg().observe(lifecycleOwner, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e(TAG, "getError_msg " + s);
//                loginViewModel.getError_msg().removeObserver(this);
            }
        });

        loginViewModel.getProgress().observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.e(TAG, "getProgress ");
                if (aBoolean) {
                    showProgressDialog();
                } else {
                    hideProgressDialog();
                }
//                loginViewModel.getProgress().removeObserver(this);
            }
        });


        dataBinding.editTextText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    dataBinding.editTextText1.clearFocus();
                    dataBinding.editTextText2.requestFocus();
                }
            }
        });

        dataBinding.editTextText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    dataBinding.editTextText2.clearFocus();
                    dataBinding.editTextText3.requestFocus();
                }
                if (s.length() == 0) {
                    dataBinding.editTextText1.requestFocus();
                    dataBinding.editTextText2.clearFocus();
                }

            }
        });

        dataBinding.editTextText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    dataBinding.editTextText3.clearFocus();
                    dataBinding.editTextText4.requestFocus();
                }
                if (s.length() == 0) {
                    dataBinding.editTextText2.requestFocus();
                    dataBinding.editTextText3.clearFocus();
                }
            }
        });

        dataBinding.editTextText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    dataBinding.editTextText3.requestFocus();
                    dataBinding.editTextText4.clearFocus();
                }

            }
        });

        dataBinding.editTextText4.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    dataBinding.editTextText4.clearFocus();
                    if (dataBinding.editTextText1.getText().toString().length() > 0 && dataBinding.editTextText2.getText().toString().length() > 0 && dataBinding.editTextText3.getText().toString().length() > 0
                            && dataBinding.editTextText4.getText().toString().length() > 0) {
                        sendLoginPinCode(dataBinding.editTextText1.getText().toString(), dataBinding.editTextText2.getText().toString(), dataBinding.editTextText3.getText().toString(), dataBinding.editTextText4.getText().toString());
                    } else {
                        Toast.makeText(mContext, "PinView Not Valid", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });


    }

    private void sendLoginPinCode(String toStrin1, String toString2, String toString3, String toString4) {
        pin_code = toStrin1 + toString2 + toString3 + toString4;
        loginViewModel.getLoginRepository(Integer.parseInt(pin_code)).observe((AppCompatActivity) context, observer);

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_pin_view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageViewBack) {
            finish();
        } else if (v.getId() == R.id.textViewBack) {
            finish();
        } else if (v.getId() == R.id.linearBack) {
            finish();
        } else if (v.getId() == R.id.textViewRequestCode) {
        } else if (v.getId() == R.id.buttonSignIn) {
            if (dataBinding.editTextText1.getText().toString().length() > 0 && dataBinding.editTextText2.getText().toString().length() > 0 && dataBinding.editTextText3.getText().toString().length() > 0
                    && dataBinding.editTextText4.getText().toString().length() > 0) {
                sendLoginPinCode(dataBinding.editTextText1.getText().toString(), dataBinding.editTextText2.getText().toString(), dataBinding.editTextText3.getText().toString(), dataBinding.editTextText4.getText().toString());
            } else {
                Toast.makeText(mContext, "PinView Not Valid", Toast.LENGTH_SHORT).show();
            }
        }
    }

}