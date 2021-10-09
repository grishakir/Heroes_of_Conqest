package com.thekingames.medivalwarriors2.core.controller

class PlayerController() : Controller() {


    override fun begin() {
        super.begin()
        addOnClickListeners()
        setOnClickListeners()
    }

    override fun onTick() {

    }

    override fun end() {

    }
}