package com.project.manager.strategy

import com.project.manager.context.ActionResult
import com.project.manager.context.ActionType

class NoneStrategy : ActionTypeStrategy {
    override fun getType(): ActionType {
        return ActionType.NONE
    }

    override fun performAction(score: Int, point: Int): ActionResult {
        return ActionResult("Вам добавлены очки: $point", score + point, true)
    }
}