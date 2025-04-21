package com.project.manager.strategy


import com.project.manager.context.ActionResult
import com.project.manager.context.ActionType

interface ActionTypeStrategy {
    fun getType(): ActionType

    fun performAction(score: Int, point: Int): ActionResult
}

