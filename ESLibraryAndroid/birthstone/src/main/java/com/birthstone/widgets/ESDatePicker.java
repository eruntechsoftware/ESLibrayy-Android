package com.birthstone.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import com.birthstone.R;
import com.birthstone.base.activity.Activity;
import com.birthstone.base.helper.InitializeHelper;
import com.birthstone.core.helper.DataType;
import com.birthstone.core.helper.StringToArray;
import com.birthstone.core.helper.ToastHelper;
import com.birthstone.core.helper.ValidatorHelper;
import com.birthstone.core.interfaces.ICellTitleStyleRequire;
import com.birthstone.core.interfaces.ICollectible;
import com.birthstone.core.interfaces.IDataInitialize;
import com.birthstone.core.interfaces.IReleasable;
import com.birthstone.core.interfaces.IValidatible;
import com.birthstone.core.parse.Data;
import com.birthstone.core.parse.DataCollection;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;


@SuppressLint({"DrawAllocation", "DefaultLocale"})
public class ESDatePicker extends android.widget.DatePicker implements ICollectible, IReleasable, ICellTitleStyleRequire, IValidatible, IDataInitialize {
    protected DataType mDataType;
    protected Boolean mIsRequired;
    protected String mCollectSign;
    protected Boolean mEmpty2Null = true;
    protected Activity mActivity;
    protected String mName;
    protected String mTime;
    protected String mTipText = "";
    protected String mNameSpace = "http://schemas.android.com/res/com.birthStone.widgets";

    public ESDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ESDatePicker);
            mIsRequired = a.getBoolean(R.styleable.ESDatePicker_isRequired,false);
            mEmpty2Null = a.getBoolean(R.styleable.ESDatePicker_empty2Null, true);
            mCollectSign = a.getString(R.styleable.ESDatePicker_collectSign);
            mDataType = com.birthstone.core.helper.DataType.DateTime;
            a.recycle();
        } catch (Exception ex) {
            Log.e("DatePicker", ex.getMessage());
        }
    }

    public ESDatePicker(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    protected OnDateChangedListener ChangedListener = new OnDateChangedListener() {

        public void onDateChanged(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        }
    };

    /**
     * 数据收集，返回DataCollection
     **/
    public DataCollection collect() {
        DataCollection datas = new DataCollection();
        mTime = this.getYear() + "-" + (this.getMonth() + 1) + "-" + this.getDayOfMonth();
        if (mTime.equals("") && mEmpty2Null.equals(true)) {
            datas.add(new Data(this.mName, null, mDataType));
        } else {
            datas.add(new Data(mName, mTime, mDataType));
        }
        return datas;
    }

    public LinkedList<String> getRequest() {
        LinkedList<String> list = new LinkedList<String>();
        list.add(mName);
        return list;
    }

    public void release(String dataName, Data data) {
        if (dataName.toUpperCase().equals(mName.toUpperCase()) && data != null) {
            if (data.getValue() == null) {
                return;
            }
            if (this.mDataType == null) {
                return;
            } else {
                String[] times = data.getValue().toString().split("-");
                if (times != null && times.length > 0) {
                    this.init(Integer.valueOf(times[0]), Integer.valueOf(times[1]) - 1, Integer.valueOf(times[2]), ChangedListener);
                }
            }
        }
    }

    public void dataInitialize() {
        Date date = Calendar.getInstance().getTime();
        this.init(date.getYear(), date.getMonth(), date.getDay(), ChangedListener);
        if (mActivity != null) {
            String classnameString = mActivity.getPackageName() + ".R$id";
            mName = InitializeHelper.getName(classnameString, getId());
        }
    }

    /**
     *
     * 数据类型校验，并返回是否成功
     *
     * @return 是否校验成功
     **/
    public Boolean dataValidator() {
        try {
            mTipText = ValidatorHelper.dataTypeValidator(mDataType, mTime);
            Log.v("DataTypeValidator", mTipText);
//            if (mIsRequired) {
//                Log.v("IsRequiredValidator", mTipText);
//                mTipText = ValidatorHelper.requiredValidator(mTime);
//            }
            invalidate();
            if (mTipText.length() != 0) {
                return false;
            }
        } catch (Exception ex) {
            Log.v("Validator", ex.getMessage());
        }
        return true;
    }

    /**
     * 提示校验错误
     * **/
    public void hint()
    {
//        ToastHelper.toastShow(this.getContext(), getHint().toString());
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint mPaint = new Paint();
        mPaint.setColor(Color.RED);

        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        // ַ
        canvas.drawText(mTipText, 8, this.getHeight() / 2 + 5, mPaint);
    }

    public String getName() {
        return mName;
    }

    public Object getActivity() {
        return mActivity;
    }

    public void setActivity(Object obj) {
        if (obj instanceof Activity) {
            mActivity = (Activity) obj;
        }
    }

    public void setDataType(com.birthstone.core.helper.DataType dataType) {
        this.mDataType = dataType;
    }

    public com.birthstone.core.helper.DataType getDataType() {
        return mDataType;
    }

    public void setIsRequired(Boolean isRequired) {
        this.mIsRequired = isRequired;
    }

    public Boolean getIsRequired() {
        return mIsRequired;
    }

    public String getTipText() {
        return mTipText;
    }

    public void setTipText(String tipText) {
        this.mTipText = tipText;
    }

    public String getNameSpace() {
        return mNameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.mNameSpace = nameSpace;
    }

    public String[] getCollectSign() {
        return StringToArray.stringConvertArray(this.mCollectSign);
    }

    public void setCollectSign(String collectSign) {
        this.mCollectSign = collectSign;
    }

    public Boolean getEmpty2Null() {
        return mEmpty2Null;
    }

    public void setEmpty2Null(Boolean empty2Null) {
        this.mEmpty2Null = empty2Null;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }

    public OnDateChangedListener getChangedListener() {
        return ChangedListener;
    }

    public void setChangedListener(OnDateChangedListener changedListener) {
        ChangedListener = changedListener;
    }

    public void setName(String name) {
        this.mName = name;
    }
}
