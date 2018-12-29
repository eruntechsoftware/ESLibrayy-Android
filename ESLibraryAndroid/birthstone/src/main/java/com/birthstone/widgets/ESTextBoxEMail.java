package com.birthstone.widgets;

import android.content.Context;
import android.util.AttributeSet;

import android.util.Log;
import com.birthstone.core.helper.DataType;
import com.birthstone.core.helper.DataTypeExpression;
import com.birthstone.core.helper.ToastHelper;
import com.vondear.rxtool.RxRegTool;

/**
 * 电子邮箱输入文本框
 */
public class ESTextBoxEMail extends ESTextBox
{
	public ESTextBoxEMail(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.mExpression = DataTypeExpression.eMail();
		//        this.mMessage = "请输入正确的邮箱地址";
		this.setInputTypeWithDataType(DataType.EMail.ordinal());
		//        this.setHint("eMail地址");
	}

	public Boolean dataValidator()
	{
		try
		{
			mMessage = getHint().toString();
			if (mIsRequired)
			{
				if (getText().toString().trim().equals(""))
				{
					isEmpty = true;
					return false;
				}
				String email = getText().toString().trim();
				if(!RxRegTool.isEmail(email))
				{
					mached=false;
					mMessage = getHint().toString();
				}
				return mached;
			}
			return true;
		}
		catch(Exception ex)
		{
			Log.e("Validator", ex.getMessage());
		}
		return true;
	}

	/**
	 * 提示校验错误
	 * **/
	public void hint()
	{
		ToastHelper.toastShow(this.getContext(), mMessage);
	}
}
