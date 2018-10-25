

package com.birthstone.core.interfaces;


import com.birthstone.core.parse.DataCollection;

public interface ICollectible
{
    DataCollection collect();
    String[] getCollectSign();
    void setCollectSign(String collectSign);
}

