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
import com.birthstone.core.interfaces.*;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;
import com.birthstone.core.helper.ModeType;
import com.birthstone.core.helper.ModeTypeHelper;

import java.util.LinkedList;

import static android.text.InputType.*;


public class ESTextBox extends EditText implements ICollectible, IValidatible, IReleasable, IStateProtected,ICellTitleStyleRequire, IDataInitialize, View.OnFocusChangeListener, TextWatcher
{
    protected DataType mDataType;
    protected Boolean mIsRequired;
    protected Boolean mIsErrorDrawable;
    protected String mCollectSign;
    protected String mStateHiddenId;
    protected String mWantedStateValue;
    protected ModeType mModeType;
    protected Boolean mEmpty2Null = true;
    protected Boolean mached = true;
    protected Boolean isEmpty = true;
    protected int hintTextSize=12;
    protected String hintStr="";
    protected IChildView mActivity;
    protected String mName;
    protected String mExpressionMessage = "";
    protected String mExpression = "";
    protected String mMessage = "";
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
            this.mExpressionMessage = a.getString(R.styleable.ESTextBox_expressionMessage);
            this.mExpressionMessage = this.mExpressionMessage==null?"":mExpressionMessage;

            this.mExpression = a.getString(R.styleable.ESTextBox_expression);
            this.mExpression = this.mExpression==null?"*":mExpression;

            this.mMessage = a.getString(R.styleable.ESTextBox_message);
            this.mMessage = this.mMessage==null?"":mMessage;

            this.mIsRequired = a.getBoolean(R.styleable.ESTextBox_isRequired, false);
            this.mIsErrorDrawable = a.getBoolean(R.styleable.ESTextBox_isErrorDrawable, false);
            this.mCollectSign = a.getString(R.styleable.ESTextBox_collectSign);
            this.mEmpty2Null = a.getBoolean(R.styleable.ESTextBox_empty2Null, true);
            this.hintTextSize = a.getDimensionPixelOffset(R.styleable.ESTextBox_hintTextSize,12);
            this.mStateHiddenId = a.getString(R.styleable.ESTextBox_stateHiddenId);
            this.mWantedStateValue = a.getString(R.styleable.ESTextBox_wantedStateValue);
            this.mModeType = ModeTypeHelper.valueOf(a.getInt(R.styleable.ESTextBox_modeType, 0));
            this.addTextChangedListener(this);
            this.setOnFocusChangeListener(this);
            int value = a.getInt(R.styleable.ESTextBox_dataType, 0);
            this.setDataType(DataTypeHelper.valueOf(value));
            setInputTypeWithDataType(value);
            a.recycle();

            errorDrawable = this.getResources().getDrawable(R.mipmap.es_error);
            requiredDrawable = this.getResources().getDrawable(R.mipmap.es_required);

            hintStr = getHint().toString();
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
                mExpression="";
                break;
            case 1:
                ESTextBox.this.setInputType(InputType.TYPE_CLASS_NUMBER);
                mExpression = DataTypeExpression.integer();
                break;
            case 2:
                ESTextBox.this.setInputType(TYPE_CLASS_NUMBER |TYPE_NUMBER_FLAG_DECIMAL);
                mExpression = DataTypeExpression.numeric();
                break;
            case 3:
                ESTextBox.this.setInputType(InputType.TYPE_CLASS_DATETIME);
                mExpression = DataTypeExpression.date();
                break;
            case 4:
                ESTextBox.this.setInputType(InputType.TYPE_CLASS_DATETIME);
                mExpression = DataTypeExpression.dateTime();
                break;
            case 5:
                ESTextBox.this.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                mExpression = DataTypeExpression.eMail();
                break;
            case 6:
                ESTextBox.this.setInputType(TYPE_CLASS_TEXT |TYPE_TEXT_VARIATION_URI);
                mExpression = DataTypeExpression.URL();
                break;
            case 7:
                ESTextBox.this.setSingleLine(true);
                ESTextBox.this.setInputType(TYPE_CLASS_TEXT);
                mExpression = DataTypeExpression.idCard();
                break;
            case 8:
                ESTextBox.this.setInputType(InputType.TYPE_CLASS_PHONE);
                mExpression = DataTypeExpression.phone();
                break;
            case 9:
                ESTextBox.this.setInputType(InputType.TYPE_CLASS_PHONE);
                mExpression = DataTypeExpression.mobile();
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
            mached = !ValidatorHelper.isMached(mExpression, getText().toString());
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
        mached = ValidatorHelper.isMached(mExpression, getText().toString());

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
                mached = ValidatorHelper.isMached(mExpression, getText().toString());
                if(!mached)
                {
//                    if(mMessage!=null && mMessage.trim().length()>0)
//                    {
//                        ToastHelper.toastShow(this.getContext(), getHint().toString());
//                    }
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


    private void shakeAnimation ()
    {
        Animation shake = AnimationUtils.loadAnimation(this.getContext(), R.anim.es_shake);
        this.startAnimation(shake);
    }

    @Override
    public Boolean getIsEmpty() {
        return isEmpty;
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
                this.mExpression = "*";
                break;
            case Integer:
                this.mExpression = DataTypeExpression.integer();
                break;
            case Numeric:
                this.mExpression = DataTypeExpression.numeric();
                break;
            case Date:
                this.mExpression = DataTypeExpression.date();
                break;
            case DateTime:
                this.mExpression = DataTypeExpression.dateTime();
                break;
            case EMail:
                this.mExpression = DataTypeExpression.eMail();
                break;
            case URL:
                this.mExpression = DataTypeExpression.URL();
                break;
            case IDCard:
                this.mExpression = DataTypeExpression.idCard();
                break;
            case Phone:
                this.mExpression = DataTypeExpression.phone();
                break;
            case Mobile:
                this.mExpression = DataTypeExpression.mobile();
                break;
        }
    }

    public void setIsRequired (Boolean arg0)
    {
        mIsRequired = arg0;
    }

    public void dataInitialize ()
    {
        String classnameString = this.getContext().getPackageName() + ".R$id";
        mName = InitializeHelper.getName(classnameString, getId());
    }

    public Object getChildView ()
    {
        return mActivity;
    }

    public void setChildView (Object arg0)
    {
        mActivity = (IChildView) arg0;

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

    public void drawExpression (Canvas canvas)
    {
        if (mExpression != null && !"".equals(mExpression))
        {
            Paint mPaint = new Paint();
            mPaint.setColor(Color.RED);
            mPaint.setTextSize(this.getTextSize());
            mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.FILL);
            Rect rect = new Rect();
            mPaint.getTextBounds(mExpression, 0, mExpression.length(), rect);
            canvas.drawText(mExpression, this.getWidth() - rect.width() - 8, this.getHeight() / 2 + rect
                    .height() / 2, mPaint);
        }
    }

    public void drawError (Canvas canvas)
    {
        if(this.mIsErrorDrawable) {
            int width = 48;
            int height = 48;
            int textWidth = this.getWidth();
            int textHeight = this.getHeight();
            errorDrawable.setBounds(textWidth - width * 2, textHeight / 2 - height / 2, textWidth - width, textHeight / 2 + height / 2);
            errorDrawable.draw(canvas);
        }
    }

    public void drawRequired (Canvas canvas)
    {
        if(this.mIsErrorDrawable) {
            int width = 48;
            int height = 48;
            int textWidth = this.getWidth();
            int textHeight = this.getHeight();
//        requiredDrawable.setBounds(textWidth - width-8, textHeight / 2, textWidth, textHeight/2 + height/2);
            requiredDrawable.setBounds(textWidth - width * 2, textHeight / 2 - height / 2, textWidth - width, textHeight / 2 + height / 2);
            requiredDrawable.draw(canvas);
        }
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
        return mExpressionMessage;
    }

    public void setTipText (String tipText)
    {
        this.mExpressionMessage = tipText;
        this.mExpression = tipText;
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


    public String getStateHiddenId() {
        return mStateHiddenId;
    }

    public String getWantedStateValue() {
        return mWantedStateValue;
    }

    public void setStateHiddenId(String stateHiddenId)
    {
        this.mStateHiddenId = stateHiddenId;
    }

    public void setWantedStateValue(String wantedStateValue)
    {
        this.mWantedStateValue = wantedStateValue;
    }

    public void protectState(Boolean arg0)
    {
        if (arg0)
        {
            this.setVisibility(View.VISIBLE);
        }
        else
        {
            this.setVisibility(View.GONE);
        }
    }

    public void setModeType(ModeType modeType)
    {
        this.mModeType = modeType;
    }

    public ModeType getModeType()
    {
        return mModeType;
    }

    /**
     * 提示校验错误
     * **/
    public void message()
    {
        ToastHelper.toastShow(this.getContext(),mMessage.equals("")?hintStr:mMessage);
    }

    @Override
    public void expressionMessage() {
        ToastHelper.toastShow(this.getContext(),mExpressionMessage.equals("")?hintStr:mExpressionMessage);
    }
}
