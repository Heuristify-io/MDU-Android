package com.heuristify.mdu.helper;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.Window;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;

import com.heuristify.mdu.R;

public class CustomDialog extends ProgressDialog {

    private Dialog mDialog;


    public CustomDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setIndeterminate(true);
        String message = context.getResources().getString(R.string.dialog_message);
        setMessage(Html.fromHtml(message));
        Drawable drawable = new ProgressBar(context).getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(context, R.color.purple_700), PorterDuff.Mode.SRC_IN);
        setIndeterminateDrawable(drawable);
    }
}
