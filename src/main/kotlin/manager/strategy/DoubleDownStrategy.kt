package com.project.manager.strategy

import com.project.manager.context.ActionResult
import com.project.manager.context.ActionType

class DoubleDownStrategy : ActionTypeStrategy {
    override fun getType(): ActionType {
        return ActionType.DOUBLE_DOWN
    }

    override fun performAction(score: Int, point: Int): ActionResult {
        return ActionResult("Поздравляем! Ваши очки удваиваются!", score * 2, true)
    }
}
