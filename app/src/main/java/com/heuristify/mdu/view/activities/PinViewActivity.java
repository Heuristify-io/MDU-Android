package com.heuristify.mdu.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BaseActivity;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.databinding.ActivityPinViewBinding;

public class PinViewActivity extends BindingBaseActivity<ActivityPinViewBinding> implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBinding.editTextText1.setOnClickListener(this);
        dataBinding.editTextText2.setOnClickListener(this);
        dataBinding.editTextText3.setOnClickListener(this);
        dataBinding.editTextText4.setOnClickListener(this);
        dataBinding.linear1.setOnClickListener(this);
        dataBinding.linear2.setOnClickListener(this);
        dataBinding.linear3.setOnClickListener(this);
        dataBinding.linear4.setOnClickListener(this);
        dataBinding.textViewRequestCode.setOnClickListener(this);
        dataBinding.textViewBack.setOnClickListener(this);
        dataBinding.imageViewBack.setOnClickListener(this);
        dataBinding.cardView1.setOnClickListener(this);
        dataBinding.cardView2.setOnClickListener(this);
        dataBinding.cardView3.setOnClickListener(this);
        dataBinding.cardView4.setOnClickListener(this);
        dataBinding.buttonSignIn.setOnClickListener(this);
        dataBinding.linearBack.setOnClickListener(this);


        dataBinding.editTextText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                linearOneBackgroundChange();
                Log.d("pinvView1", "onKey: screen key pressed");
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
                linearTwoBackgroundChange();
                Log.d("pinvView2", "onKey: screen key pressed");
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
                linearThreeBackgroundChange();
                Log.d("pinvView3", "onKey: screen key pressed");

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
                linearFourBackgroundChange();
                Log.d("pinvView4", "onKey: screen key pressed");

            }
        });

        dataBinding.editTextText4.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Toast.makeText(mContext, "Action Done", Toast.LENGTH_SHORT).show();
                    linearBackgroundChange();
                }
                return false;
            }
        });


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
            showProgressDialog();
        } else if (v.getId() == R.id.editTextText1) {
            linearOneBackgroundChange();
        } else if (v.getId() == R.id.editTextText2) {

            linearTwoBackgroundChange();
        } else if (v.getId() == R.id.editTextText3) {
            linearThreeBackgroundChange();
        } else if (v.getId() == R.id.editTextText4) {
            linearFourBackgroundChange();
        } else if (v.getId() == R.id.linear1) {
            linearOneBackgroundChange();
        } else if (v.getId() == R.id.linear2) {
            linearTwoBackgroundChange();
        }
        if (v.getId() == R.id.linear3) {
            linearThreeBackgroundChange();
        } else if (v.getId() == R.id.linear4) {
            linearFourBackgroundChange();
        } else if (v.getId() == R.id.cardView1) {
            linearOneBackgroundChange();
        } else if (v.getId() == R.id.cardView2) {
            linearTwoBackgroundChange();
        } else if (v.getId() == R.id.cardView3) {
            linearThreeBackgroundChange();
        } else if (v.getId() == R.id.cardView4) {
            linearFourBackgroundChange();
        } else if (v.getId() == R.id.buttonSignIn) {
            startActivity(new Intent(PinViewActivity.this, AttendingActivity.class));
        }
    }

    private void linearFourBackgroundChange() {
        dataBinding.linear1.setBackgroundResource(R.drawable.circle);
        dataBinding.linear2.setBackgroundResource(R.drawable.circle);
        dataBinding.linear3.setBackgroundResource(R.drawable.circle);
        dataBinding.linear4.setBackgroundResource(R.drawable.circle2);
    }

    private void linearThreeBackgroundChange() {
        dataBinding.linear1.setBackgroundResource(R.drawable.circle);
        dataBinding.linear2.setBackgroundResource(R.drawable.circle);
        dataBinding.linear3.setBackgroundResource(R.drawable.circle2);
        dataBinding.linear4.setBackgroundResource(R.drawable.circle);
    }

    private void linearTwoBackgroundChange() {
        dataBinding.linear1.setBackgroundResource(R.drawable.circle);
        dataBinding.linear2.setBackgroundResource(R.drawable.circle2);
        dataBinding.linear3.setBackgroundResource(R.drawable.circle);
        dataBinding.linear4.setBackgroundResource(R.drawable.circle);
    }

    private void linearOneBackgroundChange() {
        dataBinding.linear1.setBackgroundResource(R.drawable.circle2);
        dataBinding.linear2.setBackgroundResource(R.drawable.circle);
        dataBinding.linear3.setBackgroundResource(R.drawable.circle);
        dataBinding.linear4.setBackgroundResource(R.drawable.circle);
    }

    private void linearBackgroundChange() {
        //same background
        dataBinding.linear1.setBackgroundResource(R.drawable.circle);
        dataBinding.linear2.setBackgroundResource(R.drawable.circle);
        dataBinding.linear3.setBackgroundResource(R.drawable.circle);
        dataBinding.linear4.setBackgroundResource(R.drawable.circle);
    }
}