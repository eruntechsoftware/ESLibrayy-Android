
package com.birthstone.core.interfaces;

public interface IStateProtected
{
    String getStateHiddenId();
    void setStateHiddenId(String stateHiddenId);
    String getWantedStateValue();
    void setWantedStateValue(String wantedStateValue);
    void protectState(Boolean isMatched);
}

