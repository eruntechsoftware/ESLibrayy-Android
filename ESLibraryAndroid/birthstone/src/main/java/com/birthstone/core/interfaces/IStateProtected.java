
package com.birthstone.core.interfaces;

import com.birthstone.core.helper.ModeType;

public interface IStateProtected
{
    String getStateHiddenId();
    void setStateHiddenId(String stateHiddenId);
    String getWantedStateValue();
    void setWantedStateValue(String wantedStateValue);
    void protectState(Boolean isMatched);
    void setModeType(ModeType modeType);
    ModeType getModeType();
}

