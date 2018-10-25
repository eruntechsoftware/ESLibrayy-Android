package com.birthstone.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.*;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.event.OnTextBoxChangedListener;
import com.birthstone.base.helper.InitializeHelper;
import com.birthstone.core.helper.*;
import com.birthstone.core.interfaces.ICellTitleStyleRequire;
import com.birthstone.core.interfaces.ICollectible;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.core.interfaces.IReleasable;
import com.birthstone.core.interfaces.IValidatible;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;

import java.util.LinkedList;

import static android.text.InputType.*;


public class ESTextBox extends EditText implements ICollectible, IValidatible, IReleasable, ICellTitleStyleRequire, IDataInitialize, View.OnFocusChangeListener, TextWatcher
{
    protected DataType mDataType;
    protected Boolean mIsRequired;
    protected String mCollectSign;
    protected Boolean mEmpty2Null = true;
    protected Boolean mached = true;
    protected Boolean isEmpty = true;
    protected int hintTextSize=12;
    protected Activity mActivity;
    protected String mName;
    protected String mIsRequiredTooltip = "";
    protected String mRegularExpression = "";
    protected String mRegularTooltip = "";
    protected String mNameSpace = "http://schemas.android.com/res/com.birthstone.widgets";

    private OnTextBoxChangedListener onTextBoxChangedListener;
    private Drawable errorDrawable, requiredDrawable;
//	private Drawable[] drawables;

    public ESTextBox (Context context)
    {
        super(context);
    }

    public ESTextBox (Context context, AttributeSet attrs)
    {
        super(context, attrs);
        try
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESTextBox);
            mIsRequiredTooltip = a.getString(R.styleable.ESTextBox_isRequiredTooltip);
            mRegularExpression = a.getString(R.styleable.ESTextBox_regularExpression);
            if (mRegularExpression == null || "".equals(mRegularExpression))
            {
                mRegularExpression = "*";
            }
            mRegularTooltip = a.getString(R.styleable.ESTextBox_regularTooltip);
            mIsRequired = a.getBoolean(R.styleable.ESTextBox_isRequired, false);
            mCollectSign = a.getString(R.styleable.ESTextBox_collectSign);
            mEmpty2Null = a.getBoolean(R.styleable.ESTextBox_empty2Null, true);
			hintTextSize = a.getDimensionPixelOffset(R.styleable.ESTextBox_hintTextSize,12);
            this.addTextChangedListener(this);
            this.setOnFocusChangeListener(this);
            int value = a.getInt(R.styleable.ESTextBox_dataType, 0);
            this.setDataType(DataTypeHelper.valueOf(value));
            setInputTypeWithDataType(value);
            a.recycle();

            errorDrawable = this.getResources().getDrawable(R.mipmap.es_error);
            requiredDrawable = this.getResources().getDrawable(R.mipmap.es_required);

			String hintStr = getHint().toString();
			SpannableString spannableString =  new SpannableString(hintStr);
			AbsoluteSizeSpan ass = new AbsoluteSizeSpan(hintTextSize, true);
			spannableString.setSpan(ass, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			this.setHint(new SpannedString(spannableString));
        }
        catch (Exception ex)
        {
            Log.e("TextBox", ex.getMessage());
        }

    }

    public ESTextBox (Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    /**
     * 根据数据类型设置键盘类型
     *
     * @param value 数据类型枚举
     */
    public void setInputTypeWithDataType (int value)
    {
        switch (value)
        {
            case 0:
//                ESTextBox.this.setInputType(TYPE_CLASS_TEXT |TYPE_TEXT_VARIATION_NORMAL);
                mRegularExpression="";
                break;
            case 1:
                ESTextBox.this.setInputType(InputType.TYPE_CLASS_NUMBER);
                mRegularExpression = DataTypeExpression.integer();
                break;
            case 2:
                ESTextBox.this.setInputType(TYPE_CLASS_NUMBER |TYPE_NUMBER_FLAG_DECIMAL);
                mRegularExpression = DataTypeExpression.numeric();
                break;
            case 3:
                ESTextBox.this.setInputType(InputType.TYPE_CLASS_DATETIME);
                mRegularExpression = DataTypeExpression.date();
                break;
            case 4:
                ESTextBox.this.setInputType(InputType.TYPE_CLASS_DATETIME);
                mRegularExpression = DataTypeExpression.dateTime();
                break;
            case 5:
                ESTextBox.this.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                mRegularExpression = DataTypeExpression.eMail();
                break;
            case 6:
                ESTextBox.this.setInputType(TYPE_CLASS_TEXT |TYPE_TEXT_VARIATION_URI);
                mRegularExpression = DataTypeExpression.URL();
                break;
            case 7:
                ESTextBox.this.setSingleLine(true);
                ESTextBox.this.setInputType(TYPE_CLASS_TEXT);
                mRegularExpression = DataTypeExpression.idCard();
                break;
            case 8:
                ESTextBox.this.setInputType(InputType.TYPE_CLASS_PHONE);
                mRegularExpression = DataTypeExpression.phone();
                break;
            case 9:
                ESTextBox.this.setInputType(InputType.TYPE_CLASS_PHONE);
                mRegularExpression = DataTypeExpression.mobile();
                break;
        }
    }

    @Override
    public void onFocusChange (View view, boolean hasFocus)
    {
        if(this.getText().toString().trim().length()>0)
        {
            isEmpty = false;
        }
        else
        {
            isEmpty = true;
        }
        if (!hasFocus && mDataType!=DataType.String)
        {
            mached = !ValidatorHelper.isMached(mRegularExpression, getText().toString());
        }
    }

    public void onTextChanged (CharSequence s, int start, int before, int count)
    {
        if(this.getText().toString().trim().length()>0)
        {
            isEmpty = false;
        }
        else
        {
            isEmpty = true;
        }
    }

    public void beforeTextChanged (CharSequence s, int start, int count, int after)
    {
        setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    public void afterTextChanged (Editable s)
    {
        mached = ValidatorHelper.isMached(mRegularExpression, getText().toString());

        if (onTextBoxChangedListener != null)
        {
            onTextBoxChangedListener.onTextBoxChanged(getText().toString());
        }
    }

    public Boolean dataValidator ()
    {
        try
        {
            if (mIsRequired)
            {
                if(getText().toString().trim().equals(""))
                {
                    isEmpty = true;
                    return false;
                }
                mached = ValidatorHelper.isMached(mRegularExpression, getText().toString());
                if(!mached)
                {
                    invalidate();
                }
                return mached;
            }
            return true;
        }
        catch (Exception ex)
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
        ToastHelper.toastShow(this.getContext(),getHint().toString());
    }

    private void shakeAnimation ()
    {
        Animation shake = AnimationUtils.loadAnimation(this.getContext(), R.anim.es_shake);
        this.startAnimation(shake);
    }


    public DataType getDataType ()
    {
        return mDataType;
    }

    public Boolean getIsRequired ()
    {
        return mIsRequired;
    }

    public void setDataType (DataType arg0)
    {
        mDataType = arg0;
        switch (arg0)
        {
            case String:
                this.mRegularExpression = "*";
                break;
            case Integer:
                this.mRegularExpression = DataTypeExpression.integer();
                break;
            case Numeric:
                this.mRegularExpression = DataTypeExpression.numeric();
                break;
            case Date:
                this.mRegularExpression = DataTypeExpression.date();
                break;
            case DateTime:
                this.mRegularExpression = DataTypeExpression.dateTime();
                break;
            case EMail:
                this.mRegularExpression = DataTypeExpression.eMail();
                break;
            case URL:
                this.mRegularExpression = DataTypeExpression.URL();
                break;
            case IDCard:
                this.mRegularExpression = DataTypeExpression.idCard();
                break;
            case Phone:
                this.mRegularExpression = DataTypeExpression.phone();
                break;
            case Mobile:
                this.mRegularExpression = DataTypeExpression.mobile();
                break;
        }
    }

    public void setIsRequired (Boolean arg0)
    {
        mIsRequired = arg0;
    }

    public void dataInitialize ()
    {
        if (mActivity != null)
        {
            String classnameString = mActivity.getPackageName() + ".R$id";
            mName = InitializeHelper.getName(classnameString, getId());
        }
    }

    public Object getActivity ()
    {
        return mActivity;
    }

    public void setActivity (Object arg0)
    {
        if (arg0 instanceof Activity)
        {
            mActivity = (Activity) arg0;
        }
    }

    public LinkedList<String> getRequest ()
    {
        LinkedList<String> list = new LinkedList<String>();
        list.add(mName);
        return list;
    }

    public void release (String dataName, Data data)
    {
        if (dataName.toUpperCase().equals(mName.toUpperCase()) && data != null)
        {
            if (data.getValue() == null)
            {
                this.setText("");
                return;
            }
            if (this.mDataType == null)
            {
                this.setText(data.getValue().toString());
            }
            else if (this.mDataType.equals(DataType.Date))
            {
                this.setText(DateTimeHelper.getDateString(data.getValue().toString(), DateTimeHelper.getDateFormat()));
            }
            else if (this.mDataType.equals(DataType.DateTime))
            {
                this.setText(DateTimeHelper.getDateString(data.getValue().toString(), DateTimeHelper.getDateTimeFormat()));
            }
            else
            {
                this.setText(data.getValue().toString());
            }
        }
    }

    public DataCollection collect ()
    {

        DataCollection datas = new DataCollection();
        if (this.getText().equals("") && mEmpty2Null.equals(true))
        {
            datas.add(new Data(this.mName, null, mDataType));
        }
        else
        {
            datas.add(new Data(mName, getText().toString(), mDataType));
        }
        return datas;
    }

    protected void onDraw (Canvas canvas)
    {
        if (isEmpty && mIsRequired)
        {
            drawRequired(canvas);
        }
        else if (!isEmpty && !mached)
        {
            drawError(canvas);
        }
        super.onDraw(canvas);
    }

    public void drawRegularExpression (Canvas canvas)
    {
        if (mRegularExpression != null && !"".equals(mRegularExpression))
        {
            Paint mPaint = new Paint();
            mPaint.setColor(Color.RED);
            mPaint.setTextSize(this.getTextSize());
            mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.FILL);
            Rect rect = new Rect();
            mPaint.getTextBounds(mRegularExpression, 0, mRegularExpression.length(), rect);
            canvas.drawText(mRegularExpression, this.getWidth() - rect.width() - 8, this.getHeight() / 2 + rect
                    .height() / 2, mPaint);
        }
    }

    public void drawError (Canvas canvas)
    {
        int width = 48;
        int height = 48;
        int textWidth = this.getWidth();
        int textHeight = this.getHeight();
        errorDrawable.setBounds(textWidth - width*2, textHeight/2-height/2, textWidth - width, textHeight/2 + height/2);
        errorDrawable.draw(canvas);
    }

    public void drawRequired (Canvas canvas)
    {
        int width = 48;
        int height = 48;
        int textWidth = this.getWidth();
        int textHeight = this.getHeight();
//        requiredDrawable.setBounds(textWidth - width-8, textHeight / 2, textWidth, textHeight/2 + height/2);
        requiredDrawable.setBounds(textWidth - width*2, textHeight/2-height/2, textWidth - width, textHeight/2 + height/2);
        requiredDrawable.draw(canvas);
    }

    public String[] getCollectSign ()
    {
        return StringToArray.stringConvertArray(this.mCollectSign);
    }

    public Boolean getEmpty2Null ()
    {
        return mEmpty2Null;
    }

    public void setEmpty2Null (Boolean empty2Null)
    {
        this.mEmpty2Null = empty2Null;
    }

    public String getTipText ()
    {
        return mIsRequiredTooltip;
    }

    public void setTipText (String tipText)
    {
        this.mIsRequiredTooltip = tipText;
        this.mRegularExpression = tipText;
    }

    public String getNameSpace ()
    {
        return mNameSpace;
    }

    public void setNameSpace (String nameSpace)
    {
        this.mNameSpace = nameSpace;
    }

    public void setName (String name)
    {
        this.mName = name;
    }

    public void setCollectSign (String collectSign)
    {
        this.mCollectSign = collectSign;
    }

    public String getName ()
    {
        return mName;
    }

    public OnTextBoxChangedListener getOnTextBoxChangedListener ()
    {
        return onTextBoxChangedListener;
    }

    public void setOnTextBoxChangedListener (OnTextBoxChangedListener onTextBoxChangedListener)
    {
        this.onTextBoxChangedListener = onTextBoxChangedListener;
    }
}
