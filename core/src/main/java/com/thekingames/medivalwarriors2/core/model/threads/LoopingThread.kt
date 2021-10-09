package com.thekingames.medivalwarriors2.core.model.threads

import android.os.Handler
import android.util.Log
import com.thekingames.medivalwarriors2.core.model.Effect
import com.thekingames.medivalwarriors2.core.model.interfaces.OnTickListener
import java.util.concurrent.CopyOnWriteArrayList


class LoopingThread : Thread() {
    private lateinit var handler: Handler
    private lateinit var handler1ms: Handler
    var isRunning = false
    var onTickEventListeners: CopyOnWriteArrayList<OnTickListener> = CopyOnWriteArrayList()
    var onTick1MSEventListeners: CopyOnWriteArrayList<OnTickListener> = CopyOnWriteArrayList()
    private lateinit var runnable100MS: Runnable
    private lateinit var runnable1MS: Runnable
    var speed = 1

    override fun run() {
        this.handler = Handler()
        this.handler1ms = Handler()
        runnable100MS = Runnable {
            if (isRunning) {
                val iterator = onTickEventListeners.iterator()
                while (iterator.hasNext()) {
                    iterator.next().onTick()
                }
            }
            handler.postDelayed(runnable100MS, 100L/speed)
        }
        runnable1MS = Runnable {
            if (isRunning) {
                val iterator = onTick1MSEventListeners.iterator()
                while (iterator.hasNext()) {
                    iterator.next().onTick()
                }
            }
            handler.postDelayed(runnable1MS, 1L)
        }

        handler.post(runnable100MS)
        handler1ms.post(runnable1MS)
    }

    fun onStart() {
        isRunning = true
    }

    fun onStop() {
        isRunning = false
    }

    fun finish() {
        this.onStop()
        this.onTickEventListeners.clear()
        this.onTick1MSEventListeners.clear()
    }
}