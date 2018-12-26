package com.birthstone.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.birthstone.core.helper.DataType;
import com.birthstone.core.helper.DataTypeExpression;

/**
 * 数字输入文本框
 */
public class ESTextBoxNumber extends ESTextBox {
    public ESTextBoxNumber(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mDataType = DataType.Numeric;
        this.mExpression = DataTypeExpression.numeric();
//        this.mMessage = "请输入正确的数字";
        this.setInputTypeWithDataType(DataType.Numeric.ordinal());
    }
}
