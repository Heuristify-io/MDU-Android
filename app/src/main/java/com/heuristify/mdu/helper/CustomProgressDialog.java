package com.heuristify.mdu.helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.heuristify.mdu.R;

public class CustomProgressDialog {

    private Dialog mDialog;
    private ProgressBar mProgressBar;
    private Context context;

    public CustomProgressDialog(Context context) {
        this.context = context;
    }

    public void showProgress(){

        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.custom_progress_dialog_view);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mProgressBar = (ProgressBar) mDialog.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setIndeterminate(true);
        mDialog.setCancelable(true);
        mDialog.show();
    }

    public void dismiss(){
        if(mDialog != null && mDialog.isShowing()){
            mProgressBar.setVisibility(View.GONE);
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
