package com.birthstone.widgets;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.birthstone.R;


/**
 * 时间：2015-08-31
 * <p>
 * 功能：ProgressDialog进度条类
 */
public class ESProgressDialog extends AlertDialog
{
    Context context;
    String title;
    TextView titleTextView;


    public ESProgressDialog (Context context, String title)
    {
        super(context, R.style.loadingDialogStyle);
        this.title = title;
        this.context = context;
    }


    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.es_progressdialog);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        titleTextView = (TextView) findViewById(R.id.content);
        titleTextView.setText(title);
        WindowManager windowManager = this.getWindow().getWindowManager();

        Display display = windowManager.getDefaultDisplay();

        int screenWidth = display.getWidth();

        int screenHeight = display.getHeight();
        this.getWindow().setLayout(screenWidth, screenHeight);

    }

    @Override
    public void dismiss ()
    {
        super.dismiss();
        this.context = null;
    }
}
