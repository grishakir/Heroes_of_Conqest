package com.thekingames.medivalwarriors2.core.model.interfaces;

import android.support.annotation.NonNull;

import com.thekingames.medivalwarriors2.core.model.Effect;
import com.thekingames.medivalwarriors2.core.model.SpellDamage;

import java.io.Serializable;

public interface OnDamageListener extends BaseInterface {

    void makeDamage(@NonNull Double damage, @NonNull SpellDamage spell);

    void makeDamage(@NonNull Double damage, @NonNull Effect effect);
}
