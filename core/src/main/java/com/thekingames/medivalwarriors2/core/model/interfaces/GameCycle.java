package com.thekingames.medivalwarriors2.core.model.interfaces;

public interface GameCycle extends BaseInterface {
    void begin();
    void pause();
    void resume();
    void end();
}
