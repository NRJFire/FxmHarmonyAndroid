package com.sofac.fxmharmony.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.sofac.fxmharmony.R;

/**
 * Created by Maxim on 01.09.2017.
 */

public class ProgressBar {

    ProgressDialog pd;

    public ProgressBar(Context context) {
        this.pd = new ProgressDialog(context, R.style.MyTheme);
        pd.setCancelable(false);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void showView() {
        pd.show();

    }

    public void dismissView() {
        pd.dismiss();
    }

}
