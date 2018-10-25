

package com.birthstone.core.interfaces;


/// <summary>
/// ʵֻѭ
/// </summary>
public interface IControlSearcherHandler
{
    /// <summary>
    /// </summary>
    /// <param name="control"></param>
    void handle(Object control);
    //bool StateIsMatched(string wanted, string current);
    Boolean isPicked(Object control);
}

