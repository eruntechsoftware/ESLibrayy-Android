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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.birthstone.R;

/**
 * 日期：2015年9月1日
 * 作者：杜明悦
 * 功能：消息提醒对话框
 */
public class ESMessageBox extends android.app.Dialog
{

    Context context;
    View view;
    View backView;
    String message;
    TextView messageTextView;
    String title;
    TextView titleTextView;

    Button buttonAccept;
    Button buttonCancel;

    String buttonCancelText, buttonAcceptText;

    View.OnClickListener onAcceptButtonClickListener;
    View.OnClickListener onCancelButtonClickListener;

    /***
     * @param context 上下文
     * @param title   标题
     * @param message 消息文本
     */
    public ESMessageBox (Context context, String title, String message)
    {
        super(context, R.style.DialogTheme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCanceledOnTouchOutside(false);

        this.context = context;// init Context
        this.message = message;
        this.title = title;
    }

    public void setCancelButtonText (String buttonCancelText)
    {
        this.buttonCancelText = buttonCancelText;
    }

    public void setAcceptButtonText (String buttonAcceptText)
    {
        this.buttonAcceptText = buttonAcceptText;
    }

    public void addCancelButton (String buttonCancelText, View.OnClickListener onCancelButtonClickListener)
    {
        this.buttonCancelText = buttonCancelText;
        this.onCancelButtonClickListener = onCancelButtonClickListener;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagebox);

        view = (RelativeLayout) findViewById(R.id.contentDialog);
        backView = (RelativeLayout) findViewById(R.id.dialog_rootView);
        backView.setOnTouchListener(new OnTouchListener()
        {

            @Override
            public boolean onTouch (View v, MotionEvent event)
            {
                if (event.getX() < view.getLeft() || event.getX() > view.getRight() || event.getY() > view
                        .getBottom() || event.getY() < view.getTop())
                {
                    //dismiss();
                }
                return false;
            }
        });

        this.titleTextView = (TextView) findViewById(R.id.title);
        setTitle(title);

        this.messageTextView = (TextView) findViewById(R.id.message);
        setMessage(message);

        this.buttonAccept = (Button) findViewById(R.id.button_accept);
        if (buttonAcceptText != null && !buttonAcceptText.equals(""))
        {
            buttonAccept.setText(buttonAcceptText);
        }
        buttonAccept.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                if (onAcceptButtonClickListener != null)
                    onAcceptButtonClickListener.onClick(v);
                dismiss();
            }
        });


        this.buttonCancel = (Button) findViewById(R.id.button_cancel);
        if (buttonCancelText != null && !buttonCancelText.equals(""))
        {
            buttonCancel.setText(buttonCancelText);
        }
        //this.buttonCancel.setVisibility(View.VISIBLE);
        buttonCancel.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick (View v)
            {
                if (onCancelButtonClickListener != null)
                    onCancelButtonClickListener.onClick(v);
                dismiss();
            }
        });
    }

    @Override
    public void show ()
    {
        super.show();
        // set dialog enter animations
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.es_dialog_main_show_amination));
        backView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.es_dialog_root_show_amin));
    }

    // GETERS & SETTERS

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
        messageTextView.setText(message);
    }

    public TextView getMessageTextView ()
    {
        return messageTextView;
    }

    public void setMessageTextView (TextView messageTextView)
    {
        this.messageTextView = messageTextView;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
//		if(title == null) titleTextView.setVisibility(View.GONE);
//		else
//		{
//			titleTextView.setVisibility(View.VISIBLE);
//			titleTextView.setText(title);
//		}
    }

    public TextView getTitleTextView ()
    {
        return titleTextView;
    }

    public void setTitleTextView (TextView titleTextView)
    {
        this.titleTextView = titleTextView;
    }

    public Button getButtonAccept ()
    {
        return buttonAccept;
    }

    public void setButtonAccept (Button buttonAccept)
    {
        this.buttonAccept = buttonAccept;
    }

    public Button getButtonCancel ()
    {
        return buttonCancel;
    }

    public void setButtonCancel (Button buttonCancel)
    {
        this.buttonCancel = buttonCancel;
    }

    public void setOnAcceptButtonClickListener (View.OnClickListener onAcceptButtonClickListener)
    {
        this.onAcceptButtonClickListener = onAcceptButtonClickListener;
        if (buttonAccept != null)
            buttonAccept.setOnClickListener(onAcceptButtonClickListener);
    }

    public void setOnCancelButtonClickListener (View.OnClickListener onCancelButtonClickListener)
    {
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        if (buttonCancel != null)
            buttonCancel.setOnClickListener(onCancelButtonClickListener);
    }

    @Override
    public void dismiss ()
    {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.es_dialog_main_hide_amination);
        anim.setAnimationListener(new AnimationListener()
        {

            @Override
            public void onAnimationStart (Animation animation)
            {
            }

            @Override
            public void onAnimationRepeat (Animation animation)
            {
            }

            @Override
            public void onAnimationEnd (Animation animation)
            {
                view.post(new Runnable()
                {
                    @Override
                    public void run ()
                    {
                        ESMessageBox.super.dismiss();
                    }
                });

            }
        });

        view.startAnimation(anim);

        //屏蔽  加上后，出现闪烁两次情况
        //Animation backAnim = AnimationUtils.loadAnimation(context, R.anim.dialog_root_hide_amin);
        //backView.startAnimation(backAnim);
    }

}
