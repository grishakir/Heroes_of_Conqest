package com.thekingames.medivalwarriors2.core.model.shop;

import com.thekingames.medivalwarriors2.core.model.Unit;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public interface Modifier extends Serializable {
    void apply(@NotNull Unit unit);
    void cancel(@NotNull Unit unit);
}
