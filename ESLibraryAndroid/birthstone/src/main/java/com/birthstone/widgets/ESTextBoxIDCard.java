package com.birthstone.widgets;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import com.birthstone.core.helper.DataTypeExpression;

/**
 * 身份证号输入文本框
 */
public class ESTextBoxIDCard extends ESTextBox {
    public ESTextBoxIDCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mRegularExpression = DataTypeExpression.idCard();
//        this.mRegularTooltip = "请输入正确的身份证号码";
        this.setInputType(InputType.TYPE_CLASS_TEXT);
//        this.setHint("身份证");
    }
}
