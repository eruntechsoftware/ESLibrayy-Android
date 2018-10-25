package com.birthstone.base.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.birthstone.R;
import com.birthstone.core.helper.File;


/**
 * @author 杜明悦 ?
 */
public class UINavigationBar extends RelativeLayout implements View.OnClickListener
{

	/**
	 * 标题栏的根布局
	 */
	private RelativeLayout mRelativeLayout;
	/**
	 * 标题栏的左边按返回图标
	 */
	private ImageView mIvLeft;
	/**
	 * 标题栏的右边图标
	 */
	private ImageView mIvRight;
	/**
	 * 标题栏左边按钮文字
	 */
	private TextView mTvLeft;
	/**
	 * 标题栏文字标题
	 */
	private TextView mTvTilte;
	/**
	 * 标题栏的背景颜色
	 */
	public static int BACKGROUND_COLOR = 0;

	/**
	 * 标题栏的左侧返回按钮
	 */
	public static int LEFT_IMAGE_RESID = 0;

	/**
	 * 标题文字颜色
	 */
	public static int TITLE_TEXT_COLOR=0;

	/**
	 * 右边按钮的文字颜色
	 */
	public static int BUTTON_TEXT_COLOR=0;
	/**
	 * 右边保存按钮的文字大小
	 */
	private int left_button_textSize;


	/**
	 * 标题栏的显示的标题文字
	 */
	private String title_text;
	/**
	 * 标题栏的显示的标题文字颜色
	 */
	private int title_textColor;
	/**
	 * 标题栏的显示的标题文字大小
	 */
	private int title_textSize;

	/**
	 * 标题栏的顶部分割线
	 */
	private View line;
	/**
	 * 标题栏的右边按钮文字
	 */
	private TextView mTvRight;
	/**
	 * 右边保存按钮的资源图片
	 */
	private int right_button_image_id;
	/**
	 * 右边保存按钮的文字
	 */
	private String right_button_text;
	/**
	 * 右边按钮的文字颜色
	 */
	private int right_button_textColor;
	/**
	 * 右边保存按钮的文字大小
	 */
	private int right_button_textSize;

	/**
	 * 返回按钮上显示的文字
	 */
	private String left_button_text;
	/**
	 * 返回按钮的资源图片
	 */
	private int left_button_imageId;

	/*
	 * navigationbar事件监听接口
	 * */
	public IUINavigationBar UINavigationBarDelegat;

	public UINavigationBar(Context context)
	{
		super(context);
		setId(R.id.uiNavigationBar);
		initView(context);
	}


	public UINavigationBar(Context context, boolean isShowBtnBack)
	{
		super(context);
		initView(context);
		mIvLeft.setVisibility(isShowBtnBack ? VISIBLE : GONE);
	}

	private void initView(Context context)
	{
		if(BACKGROUND_COLOR==0)
		{
			BACKGROUND_COLOR = R.color.es_white;
		}
		if(BUTTON_TEXT_COLOR==0)
		{
			BUTTON_TEXT_COLOR = R.color.es_white;
		}
		if(TITLE_TEXT_COLOR==0)
		{
			TITLE_TEXT_COLOR = R.color.es_white;
		}
		if(LEFT_IMAGE_RESID==0)
		{
			LEFT_IMAGE_RESID=R.drawable.es_icon_back;
		}

		/**加载布局文件*/
		View.inflate(context, R.layout.es_actionbar, this);
		mRelativeLayout = (RelativeLayout) findViewById(R.id.relay_background);
		mIvLeft = (ImageView) findViewById(R.id.iv_left);
		mIvLeft.setImageResource( LEFT_IMAGE_RESID);
		mIvLeft.setOnClickListener(this);
		mTvLeft = (TextView) findViewById(R.id.tv_left);
		mTvLeft.setTextColor(getResources().getColor(BUTTON_TEXT_COLOR));
		mTvLeft.setOnClickListener(this);
		mTvTilte = (TextView) findViewById(R.id.tv_title);
		mTvTilte.setTextColor(getResources().getColor(TITLE_TEXT_COLOR));
		mTvRight = (TextView) findViewById(R.id.tv_right);
		mTvRight.setTextColor(getResources().getColor(BUTTON_TEXT_COLOR));
		mTvRight.setOnClickListener(this);
		mIvRight = (ImageView) findViewById(R.id.iv_right);
		mIvRight.setOnClickListener(this);
		line = findViewById(R.id.line);
		mRelativeLayout.setBackgroundColor(BACKGROUND_COLOR);
	}

	@Override
	public void setBackgroundColor(int backgroundColor)
	{
		if(mRelativeLayout!=null)
		{
			mRelativeLayout.setBackgroundColor(backgroundColor);
		}
	}

	@Override
	public void setBackground(Drawable background)
	{
		if(mRelativeLayout!=null)
		{
			mRelativeLayout.setBackground(background);
		}
	}

	/*
	 * 设置导航栏背景色
	 * @param color 标题栏背景色
	 * */
	public static void setBarTintColor(int color)
	{
		BACKGROUND_COLOR = color;
	}

	public static void setTitleTextColor(int rescolor)
	{
		TITLE_TEXT_COLOR=rescolor;
	}

	public static void setButtonTextColor(int rescolor)
	{
		BUTTON_TEXT_COLOR=rescolor;
	}

	public static void setLeftImageResid(int leftImageResid)
	{
		LEFT_IMAGE_RESID=leftImageResid;
	}

	/**
	 * 设置标题栏文本
	 * @param tilte 标题文本
	 * **/
	public void setTitle(String tilte)
	{
		if (TextUtils.isEmpty(tilte))
		{
			mTvTilte.setVisibility(GONE);
		}
		else
		{
			mTvTilte.setText(tilte);
			mTvTilte.setVisibility(VISIBLE);
		}
	}

	/**
	 * 设置标题栏文本
	 * @param tilte 标题文本
	 * @param rescolor 标题颜色，res下的资源
	 * **/
	public void setTitle(String tilte, int rescolor)
	{
		mTvTilte.setTextColor(getResources().getColor(rescolor));
		if (TextUtils.isEmpty(tilte))
		{
			mTvTilte.setVisibility(GONE);
		}
		else
		{
			mTvTilte.setText(tilte);
			mTvTilte.setVisibility(VISIBLE);
		}
	}


	/**
	 * 设置左侧按钮文本
	 * @param text 按钮文本
	 * **/
	public void setLeftText(String text)
	{
		mTvLeft.setTextColor(getResources().getColor(BUTTON_TEXT_COLOR));
		if (TextUtils.isEmpty(text))
		{
			mTvLeft.setVisibility(GONE);
		}
		else
		{
			mTvLeft.setVisibility(VISIBLE);
			mTvLeft.setText(text);
			mIvLeft.setVisibility(GONE);
		}
	}

	/**
	 * 设置左侧按钮文本
	 * @param text 按钮文本
	 * @param rescolor 按钮文本，res下的资源
	 * **/
	public void setLeftText(String text, int rescolor)
	{
		mTvLeft.setTextColor(getResources().getColor(rescolor));
		if (TextUtils.isEmpty(text))
		{
			mTvLeft.setVisibility(GONE);
		}
		else
		{
			mTvLeft.setVisibility(VISIBLE);
			mTvLeft.setText(text);
			mIvLeft.setVisibility(GONE);
		}
	}


	public void setLeftTextColor(int textColor)
	{
		mTvLeft.setTextColor(getResources().getColor(textColor));
	}

	/**
	 * 设置右侧按钮文本
	 * @param text 按钮文本
	 * **/
	public void setRightText(String text)
	{
		if (TextUtils.isEmpty(text))
		{
			mTvRight.setVisibility(GONE);
		}
		else
		{
			mTvRight.setVisibility(VISIBLE);
			mTvRight.setText(text);
			mIvRight.setVisibility(GONE);
		}
	}

	/**
	 * 设置右侧按钮文本
	 * @param text 按钮文本
	 * @param rescolor 按钮文本，res下的资源
	 * **/
	public void setRightText(String text, int rescolor)
	{
		mTvRight.setTextColor(getResources().getColor(rescolor));
		if (TextUtils.isEmpty(text))
		{
			mTvRight.setVisibility(GONE);
		}
		else
		{
			mTvRight.setVisibility(VISIBLE);
			mTvRight.setText(text);
			mIvRight.setVisibility(GONE);
		}
	}
	/**
	 * 设置右侧文字颜色
	 *
	 * @param rescolor res下的资源id
	 */
	public void setRightTextColor(int rescolor)
	{
		mTvRight.setTextColor(getResources().getColor(rescolor));
	}

	/**
	 * 设置左边按钮图片资源
	 *
	 * @param resId
	 */
	public void setLeftButtonImage(int resId)
	{
		if (resId == 0)
		{
			mIvLeft.setVisibility(View.GONE);
			mTvLeft.setVisibility(View.VISIBLE);
		}
		else
		{
			mIvLeft.setVisibility(View.VISIBLE);
			mIvLeft.setImageResource(resId);
			mTvLeft.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置右边按钮图片资源
	 *
	 * @param resId
	 */
	public void setRightButtonImage(int resId)
	{
		if (resId == 0)
		{
			mIvRight.setVisibility(View.GONE);
			mTvRight.setVisibility(View.VISIBLE);
		}
		else
		{
			mIvRight.setVisibility(View.VISIBLE);
			mIvRight.setImageResource(resId);
			mTvRight.setVisibility(View.GONE);
		}
	}


	/**
	 * 设置是否显示分割线
	 *
	 * @param visibility
	 */
	public void setLineIsVisible(int visibility)
	{
		line.setVisibility(visibility);
	}

	/**
	 * 设置是否显示右边按钮
	 *
	 * @param visibility
	 */
	public void setRightButtonVisibility(int visibility)
	{
		mIvRight.setVisibility(visibility);
		mTvRight.setVisibility(visibility);
	}

	/**
	 * 设置是否显示左边按钮
	 *
	 * @param visibility
	 */
	public void setLeftButtonVisibility(int visibility)
	{
		mIvLeft.setVisibility(visibility);
		mTvLeft.setVisibility(GONE);
	}

	/**
	 * 设置标题栏背景色
	 *
	 * @param resId
	 */
	public void setTitleBarBackground(int resId)
	{
		mRelativeLayout.setBackgroundResource(resId);
	}

	/**
	 * 左侧点击
	 **/
	public void setLeftViewClickListener(OnClickListener listener)
	{

		this.mIvLeft.setOnClickListener(listener);
		this.mTvLeft.setOnClickListener(listener);

	}

	/**
	 * 右侧侧点击
	 **/
	public void setRightViewClickListener(OnClickListener listener)
	{

		this.mIvRight.setOnClickListener(listener);
		this.mTvRight.setOnClickListener(listener);

	}

	@Override
	public void onClick(View view)
	{
		if (view.getId() == R.id.iv_right || view.getId() == R.id.tv_right)
		{
			if (UINavigationBarDelegat != null)
			{
				UINavigationBarDelegat.onRightClick();
			}
		}

		if (view.getId() == R.id.iv_left || view.getId() == R.id.tv_left)
		{
			if (UINavigationBarDelegat != null)
			{
				UINavigationBarDelegat.onLeftClick();
			}
		}
	}
}
