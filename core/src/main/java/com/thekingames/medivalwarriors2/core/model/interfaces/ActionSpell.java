package com.thekingames.medivalwarriors2.core.model.interfaces;


import com.thekingames.medivalwarriors2.core.model.Unit;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public interface ActionSpell extends BaseInterface {
    void action(Unit target, @NotNull Unit initiator);
}
