

package com.birthstone.core.helper;


import com.birthstone.core.constant.SplitString;

/// <summary>
/// </summary>
public class StringToArray
{
    public static String[] stringConvertArray(String str)
    {
        String[] array = new String[6];
        array[0] = "Empty";
        if (str != null)
        {
            array=str.replace(SplitString.Sep1, "!").split("!");
        }
        return array;
    }
}
