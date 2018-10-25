package com.birthstone.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.birthstone.core.helper.DataType;
import com.birthstone.core.helper.DataTypeExpression;

/**
 * 电子邮箱输入文本框
 */
public class ESTextBoxEMail extends ESTextBox {
    public ESTextBoxEMail(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mRegularExpression = DataTypeExpression.eMail();
//        this.mRegularTooltip = "请输入正确的邮箱地址";
        this.setInputTypeWithDataType(DataType.EMail.ordinal());
//        this.setHint("eMail地址");
    }
}
