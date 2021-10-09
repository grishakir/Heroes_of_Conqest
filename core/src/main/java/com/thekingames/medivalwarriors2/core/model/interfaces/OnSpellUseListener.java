package com.thekingames.medivalwarriors2.core.model.interfaces;


import com.thekingames.medivalwarriors2.core.model.Spell;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public interface OnSpellUseListener extends BaseInterface {
    void onSpellUse(@NotNull Spell spell);
}
