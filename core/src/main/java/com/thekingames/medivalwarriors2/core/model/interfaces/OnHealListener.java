package com.thekingames.medivalwarriors2.core.model.interfaces;

import android.support.annotation.NonNull;

import com.thekingames.medivalwarriors2.core.model.Effect;
import com.thekingames.medivalwarriors2.core.model.SpellHeal;

import java.io.Serializable;

public interface OnHealListener extends BaseInterface {
    void makeHeal(@NonNull Double heal);

    void makeHeal(@NonNull Double heal, @NonNull SpellHeal spell);

    void makeHeal(@NonNull Double heal, @NonNull Effect effect);
}
