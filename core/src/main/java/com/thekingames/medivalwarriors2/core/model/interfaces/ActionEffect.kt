package com.thekingames.medivalwarriors2.core.model.interfaces
import com.thekingames.medivalwarriors2.core.model.Unit
import java.io.Serializable

interface ActionEffect:BaseInterface {
    fun put(holder:Unit,initiator:Unit)

    fun tick(count:Int,holder:Unit,initiator:Unit)

    fun remove(holder:Unit,initiator:Unit)
}

abstract class InstantActionEffect : ActionEffect {
    override fun tick(count:Int,holder:Unit,initiator:Unit) {

    }
}

abstract class PeriodicActionEffect : ActionEffect {
    override fun put(holder:Unit,initiator:Unit) {

    }

    override fun remove(holder:Unit,initiator:Unit) {

    }
}
