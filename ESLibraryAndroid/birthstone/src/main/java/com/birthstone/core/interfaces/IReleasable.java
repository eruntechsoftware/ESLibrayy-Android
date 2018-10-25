
package com.birthstone.core.interfaces;

import com.birthstone.core.parse.Data;

import java.util.LinkedList;


public interface IReleasable
{
	LinkedList<String> getRequest();
    void release(String dataName, Data data);
    String getName();
}

