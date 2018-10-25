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
import android.widget.RatingBar;
import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.event.OnTextBoxChangedListener;
import com.birthstone.base.helper.InitializeHelper;
import com.birthstone.core.helper.*;
import com.birthstone.core.interfaces.*;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;

import java.util.LinkedList;

import static android.text.InputType.*;


public class ESRatingBar extends ESRatingBarBase implements ICollectible, IValidatible, IReleasable, ICellTitleStyleRequire, IDataInitialize
{
    protected DataType mDataType;
    protected Boolean mIsRequired;
    protected String mCollectSign;
    protected Boolean mEmpty2Null = true;
    protected Boolean mached = true;
    protected Boolean isEmpty = true;
    protected Activity mActivity;
    protected String mName;
    protected String mIsRequiredTooltip = "";
    protected String mRegularExpression = "";
    protected String mRegularTooltip = "";
    protected String hint;
    protected String mNameSpace = "http://schemas.android.com/res/com.birthstone.widgets";


    public ESRatingBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        try
        {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESRatingBar);
            mIsRequiredTooltip = a.getString(R.styleable.ESRatingBar_isRequiredTooltip);
            mRegularExpression = a.getString(R.styleable.ESRatingBar_regularExpression);
            if (mRegularExpression == null || "".equals(mRegularExpression))
            {
                mRegularExpression = "*";
            }
            mRegularTooltip = a.getString(R.styleable.ESRatingBar_regularTooltip);
            mIsRequired = a.getBoolean(R.styleable.ESRatingBar_isRequired, false);
            mCollectSign = a.getString(R.styleable.ESRatingBar_collectSign);
            mEmpty2Null = a.getBoolean(R.styleable.ESRatingBar_empty2Null, true);
            hint = a.getString(R.styleable.ESRatingBar_hint);

            int value = a.getInt(R.styleable.ESTextBox_dataType, 0);
            this.setDataType(DataTypeHelper.valueOf(value));
            a.recycle();

        }
        catch (Exception ex)
        {
            Log.e("TextBox", ex.getMessage());
        }

    }

    public Boolean dataValidator ()
    {
        try
        {
            if (mIsRequired)
            {
                if(getRating()!=0)
                {
                    isEmpty = true;
                    return false;
                }
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


    @Override
    public void setDataType(DataType dataType)
    {

    }

    public DataType getDataType()
    {
        return mDataType;
    }

    public Boolean getIsRequired ()
    {
        return mIsRequired;
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
                this.setRating(0);
                return;
            }
            if (this.mDataType == null)
            {
                this.setRating(data.getFloatValue());
            }
            else
            {
                this.setRating(data.getFloatValue());
            }
        }
    }

    public DataCollection collect ()
    {

        DataCollection datas = new DataCollection();
        datas.add(new Data(mName, getRating(), mDataType));
        return datas;
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

    public String getHint()
    {
        return hint;
    }

    public void setHint(String hint)
    {
        this.hint = hint;
    }
}
