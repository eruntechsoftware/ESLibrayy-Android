package com.birthstone.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.birthstone.core.helper.DataType;
import com.birthstone.core.helper.DataTypeExpression;

/**
 * 手机号码输入框
 */
public class ESTextBoxMobile extends ESTextBox {

    public ESTextBoxMobile(Context context, AttributeSet attrs ) {
        super(context, attrs);
        this.mDataType = DataType.Mobile;
        this.mRegularExpression = DataTypeExpression.mobile();
//        this.mRegularTooltip = "请输入正确的手机号";
        this.setInputTypeWithDataType(DataType.Mobile.ordinal());
//        this.setHint("大陆地区手机号");
    }
}
