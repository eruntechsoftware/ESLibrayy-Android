package com.birthstone.core.helper;

/**
 * 正则表达式
 */
public class DataTypeExpression {

    /**
     返回int类型正则表达式
     */
    public static String integer(){
        return "^[0-9]$";
    }

    /**
     返回Numeric类型正则表达式
     */
    public static String numeric(){
//        return "^(([0-9]+\\\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\\\.[0-9]+)|([0-9]*[1-9][0-9]*))$";
        return "[1-9]\\d*\\.?\\d*";
    }

    /**
     返回Money类型正则表达式
     @return 货币正则表达式
     */
    public static String money(){
        return "^[0-9]+(.[0-9]{2})?$";
    }

    /**
     返回Date类型正则表达式
     @return 日期正则表达式
     */
    public static String date(){
        return "^\\s*\\d{2,4}-\\d{1,2}-\\d{1,2}(\\s*\\d{1,2}:\\d{1,2}(:\\d{1,2})?)?\\s*$";
    }

    /**
     返回DateTime类型正则表达式
     @return 日期时间表达式
     */
    public static String dateTime(){
        return "^\\s*\\d{2,4}-\\d{1,2}-\\d{1,2}(\\s*\\d{1,2}:\\d{1,2}(:\\d{1,2})?)?\\s*$";
    }

    /**
     返回EMail类型正则表达式
     */
    public static String eMail(){
        return "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
    }

    /**
     返回Mobile类型正则表达式
     @return 手机号码正则表达式
     */
    public static String mobile(){
        return "^((13[0-9])|(14[^4,\\D])|(15[^4,\\D])|(18[0-9]))\\d{8}$|^1(7[0-9])\\d{8}$";
    }

    /**
     返回phone类型正则表达式
     @return 电话号码正则表达式
     */
    public static String phone(){
        return "((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)";
    }


    /**
     身份证号正则表达式
     @return 身份证号正则表达式
     */
    public static  String idCard(){
        return "^[1-9]\\\\d{5}(18|19|([23]\\\\d))\\\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\\\d{3}[0-9Xx]$)|(^[1-9]\\\\d{5}\\\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\\\d{2}$";
//        return "/^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$/";
    }

    /**
     URL正则表达式
     @return URL正则表达式
     */
    public static String URL(){
        return "/^((ht|f)tps?):\\/\\/[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-\\.,@?^=%&:\\/~\\+#]*[\\w\\-\\@?^=%&\\/~\\+#])?$/";
    }

    /**
     filePath正则表达式
     @return filePath正则表达式
     */
    public static String filePath (){
        return "([^\\.]*)\\.([^\\.]*)";
    }

}
