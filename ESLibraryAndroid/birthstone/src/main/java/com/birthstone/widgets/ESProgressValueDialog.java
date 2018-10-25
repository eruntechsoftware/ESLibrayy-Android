package com.birthstone.widgets;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.birthstone.R;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;


/**
 * 时间：2015-08-31
 * <p>
 * 功能：ProgressDialog进度条类
 */
public class ESProgressValueDialog extends AlertDialog
{
	private Context context;
	private String title;
	private TextView titleTextView;
    private DonutProgress arcProgress;


    public ESProgressValueDialog(Context context, String title)
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
        setContentView(R.layout.es_progressvaluedialog);
        titleTextView = (TextView) findViewById(R.id.content);
        titleTextView.setText(title);
		arcProgress = (DonutProgress)findViewById(R.id.progress_view);
        WindowManager windowManager = this.getWindow().getWindowManager();

        Display display = windowManager.getDefaultDisplay();

        int screenWidth = display.getWidth();

        int screenHeight = display.getHeight();
        this.getWindow().setLayout(screenWidth, screenHeight);

    }

    /**
	 * 设置进度条最大值
	 * **/
    public void setMaxValue(int maxValue)
	{
		if(arcProgress!=null)
		{
			arcProgress.setMax(maxValue);
		}
	}

	/**
	 * 设置当前进度值
	 * **/
	public void setProgressValue(int progressValue)
	{
		arcProgress.setProgress(progressValue);
	}

	/**
	 * 设置标题内容
	 * **/
	public void setTitle(String title)
	{
		this.title = title;
	}

	@Override
    public void dismiss ()
    {
        super.dismiss();
        this.context = null;
    }
}
