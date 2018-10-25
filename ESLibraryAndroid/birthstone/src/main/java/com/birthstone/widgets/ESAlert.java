package com.birthstone.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.birthstone.R;

public class ESAlert extends ESDialog
{

	private Context context;

	private int contentDialogBackgroundColor=Color.WHITE;
	private int textColor=Color.BLACK;
	private float textSize=16;

	private View contentDialog;
	private View backView;

	private String title;
	private String message;

	private ESTextView messageTextView;
	private ESTextView titleTextView;

	private ESButton buttonAccept;
	private ESButton buttonCancel;

	private String buttonCancelText, buttonAcceptText;

	private View.OnClickListener onAcceptButtonClickListener;
	private View.OnClickListener onCancelButtonClickListener;

	/***
	 * 构建弹出对话框
	 * @param context 上下文
	 * @param title 标题
	 * @param message 消息文本
	 */
	public ESAlert(Context context, String title, String message )
	{
		super(context, R.style.DialogTheme);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		this.setCanceledOnTouchOutside(false);
		this.context = context;
		this.message = message;
		this.title = title;
	}

	/***
	 * 构建弹出对话框
	 * @param context 上下文
	 * @param title 标题
	 * @param message 消息文本
	 * @param backgrouncolor 背景色 如：Color.red
	 * @param textColor 文本颜色
	 * @param textSize 文本字号
	 */
	public ESAlert(Context context, String title, String message, int backgrouncolor,int textColor, float textSize )
	{
		super(context, R.style.DialogTheme);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		this.setCanceledOnTouchOutside(false);
		this.contentDialogBackgroundColor = backgrouncolor;
		this.textColor = textColor;
		this.textSize = textSize;
		this.context = context;
		this.message = message;
		this.title = title;
	}

	public void setCancelButtonText(String buttonCancelText)
	{
		this.buttonCancelText = buttonCancelText;
	}
	
	public void setAcceptButtonText(String buttonAcceptText)
	{
		this.buttonAcceptText = buttonAcceptText;
	}

	public void addCancelButton(String buttonCancelText, View.OnClickListener onCancelButtonClickListener)
	{
		this.buttonCancelText = buttonCancelText;
		this.onCancelButtonClickListener = onCancelButtonClickListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.es_alert);

		contentDialog = (RelativeLayout) findViewById(R.id.contentDialog);
		contentDialog.setBackgroundColor(contentDialogBackgroundColor);
		backView = (RelativeLayout) findViewById(R.id.dialog_rootView);
		backView.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if(event.getX() < contentDialog.getLeft() || event.getX() > contentDialog.getRight() || event.getY() > contentDialog.getBottom() || event.getY() < contentDialog.getTop())
				{
					/**@author shenhao 修正弹出框点击边线时消失的问题*/
					//dismiss();
				}
				return false;
			}
		});

		this.titleTextView = (ESTextView) findViewById(R.id.title);
		setTitle(title);
		this.addView(titleTextView);

		this.messageTextView = (ESTextView) findViewById(R.id.message);
		this.messageTextView.setTextSize(textSize);
		this.messageTextView.setTextColor(textColor);
		setMessage(message);
		this.addView(messageTextView);

		this.buttonAccept = (ESButton) findViewById(R.id.button_accept);
		if(buttonAcceptText!=null && !buttonAcceptText.equals(""))
		{
			buttonAccept.setText(buttonAcceptText);
		}
		buttonAccept.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(onAcceptButtonClickListener != null) onAcceptButtonClickListener.onClick(v);
				dismiss();
			}
		});

		
		this.buttonCancel = (ESButton) findViewById(R.id.button_cancel);
		if(buttonCancelText!=null && !buttonCancelText.equals(""))
		{
			buttonCancel.setText(buttonCancelText);
		}
		this.buttonCancel.setVisibility(View.VISIBLE);
		buttonCancel.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if(onCancelButtonClickListener != null) onCancelButtonClickListener.onClick(v);
				dismiss();
			}
		});
	}

	@Override
	public void show()
	{
		super.show();
		// set dialog enter animations
		contentDialog.startAnimation(AnimationUtils.loadAnimation(context, R.anim.es_dialog_main_show_amination));
		backView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.es_dialog_root_show_amin));
	}

	// GETERS & SETTERS

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
		messageTextView.setText(message);
	}

	public ESTextView getMessageTextView()
	{
		return messageTextView;
	}

	public void setMessageTextView(ESTextView messageTextView)
	{
		this.messageTextView = messageTextView;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
//		if(title == null) titleTextView.setVisibility(View.GONE);
//		else
//		{
//			titleTextView.setVisibility(View.VISIBLE);
//			titleTextView.setText(title);
//		}
	}

	public ESTextView getTitleTextView()
	{
		return titleTextView;
	}

	public void setTitleTextView(ESTextView titleTextView)
	{
		this.titleTextView = titleTextView;
	}

	public ESButton getButtonAccept()
	{
		return buttonAccept;
	}

	public void setButtonAccept(ESButton buttonAccept)
	{
		this.buttonAccept = buttonAccept;
	}

	public ESButton getButtonCancel()
	{
		return buttonCancel;
	}

	public void setButtonCancel(ESButton buttonCancel)
	{
		this.buttonCancel = buttonCancel;
	}

	public void setOnAcceptButtonClickListener(View.OnClickListener onAcceptButtonClickListener)
	{
		this.onAcceptButtonClickListener = onAcceptButtonClickListener;
		if(buttonAccept != null) buttonAccept.setOnClickListener(onAcceptButtonClickListener);
	}

	public void setOnCancelButtonClickListener(View.OnClickListener onCancelButtonClickListener)
	{
		this.onCancelButtonClickListener = onCancelButtonClickListener;
		if(buttonCancel != null)
		buttonCancel.setOnClickListener(onCancelButtonClickListener);
	}

	@Override
	public void dismiss()
	{
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.es_dialog_main_hide_amination);
		anim.setAnimationListener(new AnimationListener()
		{

			@Override
			public void onAnimationStart(Animation animation)
			{
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
			}

			@Override
			public void onAnimationEnd(Animation animation)
			{
				contentDialog.post(new Runnable()
				{
					@Override
					public void run()
					{
						ESAlert.super.dismiss();
					}
				});

			}
		});

		contentDialog.startAnimation(anim);
		
		//屏蔽  加上后，出现闪烁两次情况
		//Animation backAnim = AnimationUtils.loadAnimation(context, R.anim.dialog_root_hide_amin);
		//backView.startAnimation(backAnim);
	}

}
