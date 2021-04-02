package com.heuristify.mdu.view.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.databinding.ActivityPinViewBinding;
import com.heuristify.mdu.helper.Helper;
import com.heuristify.mdu.mvvm.viewmodel.LoginViewModel;
import com.heuristify.mdu.mvvm.viewmodel.MedicineViewModel;
import com.heuristify.mdu.pojo.StockMedicineList;
import com.heuristify.mdu.sharedPreferences.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class PinViewActivity extends BindingBaseActivity<ActivityPinViewBinding> implements View.OnClickListener {
    LoginViewModel loginViewModel;
    private final String TAG = "PinViewActivity";
    private Observer<Response<ResponseBody>> observer;
    private Observer<Response<StockMedicineList>> medicine_observer;
    private int count;

    {
        count = 0;
    }

    MedicineViewModel medicineViewModel;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBinding.textViewRequestCode.setOnClickListener(this);
        dataBinding.textViewBack.setOnClickListener(this);
        dataBinding.imageViewBack.setOnClickListener(this);
        dataBinding.buttonSignIn.setOnClickListener(this);
        dataBinding.linearBack.setOnClickListener(this);
        medicineViewModel = ViewModelProviders.of(this).get(MedicineViewModel.class);


        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        observer = responseBodyResponse -> {
            if (responseBodyResponse.isSuccessful()) {
                try {
                    JSONObject jsonObject = new JSONObject(responseBodyResponse.body().string());
                    if (jsonObject != null) {
                        SharedHelper.putKey(MyApplication.getInstance(), Helper.NAME, jsonObject.optString("fullName"));
                        SharedHelper.putKey(MyApplication.getInstance(), Helper.PMDC, jsonObject.optString("pmdcNumber"));
                        SharedHelper.putKey(MyApplication.getInstance(), Helper.PHONE, jsonObject.optString("phoneNumber"));
                        SharedHelper.putKey(MyApplication.getInstance(), Helper.EMAIL, jsonObject.optString("email"));
                        SharedHelper.putKey(MyApplication.getInstance(), Helper.JWT, jsonObject.optString("JWTToken"));
                    }
                    callGetMedicineApi();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

                Toast.makeText(mContext, "PinCode Not Correct", Toast.LENGTH_SHORT).show();
            }


        };

        medicine_observer = responseBodyResponse -> {
           dismissProgressDialog();
            if (responseBodyResponse.isSuccessful()) {
                startActivity(new Intent(PinViewActivity.this, AttendingActivity.class));
                finish();
            } else {
                showMedicineDialog();
            }
        };


        loginViewModel.getError_msg().observe(this, s -> {
            Toast.makeText(mContext, "Some thing went wrong", Toast.LENGTH_SHORT).show();
        });

        loginViewModel.getProgress().observe(this, aBoolean -> {
            if (aBoolean) {
                showProgressDialog();
            } else {
                dismissProgressDialog();
            }
        });

        medicineViewModel.get_medicine_error_msg().observe(this, s -> {
            dismissProgressDialog();
            showMedicineDialog();
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
                if (s.length() > 0 && dataBinding.editTextText1.getText().length() > 0 && dataBinding.editTextText2.getText().length() > 0 && dataBinding.editTextText3.getText().length() > 0) {
                    sendLoginPinCode(dataBinding.editTextText1.getText().toString(), dataBinding.editTextText2.getText().toString(), dataBinding.editTextText3.getText().toString(), dataBinding.editTextText4.getText().toString());
                }

            }
        });

        dataBinding.editTextText4.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                dataBinding.editTextText4.clearFocus();
                if (dataBinding.editTextText1.getText().toString().length() > 0 && dataBinding.editTextText2.getText().toString().length() > 0 && dataBinding.editTextText3.getText().toString().length() > 0
                        && dataBinding.editTextText4.getText().toString().length() > 0) {
                    sendLoginPinCode(dataBinding.editTextText1.getText().toString(), dataBinding.editTextText2.getText().toString(),
                            dataBinding.editTextText3.getText().toString(), dataBinding.editTextText4.getText().toString());
                }
            }
            return false;
        });


    }

    private void showMedicineDialog() {
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.custom_medicine_dialog_view);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCancelable(false);
        mDialog.show();
        Button btnRetry = mDialog.findViewById(R.id.buttonRetry);
        Button btnCancel = mDialog.findViewById(R.id.buttonCancel);

        btnRetry.setOnClickListener(v -> {
            mDialog.dismiss();
            callGetMedicineApi();
        });

        btnCancel.setOnClickListener(v -> {
            SharedHelper.deleteAllSharedPrefs(this);
            mDialog.dismiss();
        });
    }

    private void callGetMedicineApi() {
        showProgressDialog();
        medicineViewModel.getStockMedicineList().observe((AppCompatActivity) mContext, medicine_observer);
    }

    private void sendLoginPinCode(String toStrin1, String toString2, String toString3, String toString4) {
        String pin_code = toStrin1 + toString2 + toString3 + toString4;
        loginViewModel.getLoginRepository(Integer.parseInt(pin_code)).observe((AppCompatActivity) mContext, observer);

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_pin_view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageViewBack) {
            count++;
            finishActivity();
        } else if (v.getId() == R.id.textViewBack) {
            count++;
            finishActivity();
        } else if (v.getId() == R.id.linearBack) {
            count++;
            finishActivity();
        } else if (v.getId() == R.id.textViewRequestCode) {
        } else if (v.getId() == R.id.buttonSignIn) {
            if (dataBinding.editTextText1.getText().toString().length() > 0 && dataBinding.editTextText2.getText().toString().length() > 0 && dataBinding.editTextText3.getText().toString().length() > 0
                    && dataBinding.editTextText4.getText().toString().length() > 0) {
                sendLoginPinCode(dataBinding.editTextText1.getText().toString(), dataBinding.editTextText2.getText().toString(), dataBinding.editTextText3.getText().toString(), dataBinding.editTextText4.getText().toString());
            }
        }
    }

    private void finishActivity() {
        if (count >= 2) {
            finish();
        } else {
            Toast.makeText(mContext, "Press Again To Exist", Toast.LENGTH_SHORT).show();
        }
    }

}