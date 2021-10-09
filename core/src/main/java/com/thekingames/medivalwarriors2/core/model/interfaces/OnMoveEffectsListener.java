package com.thekingames.medivalwarriors2.core.model.interfaces;


import com.thekingames.medivalwarriors2.core.model.Effect;

import java.io.Serializable;

public interface OnMoveEffectsListener extends BaseInterface {
    void onPut(Effect effect);
    void onRemove(Effect effect);
}
