package com.project.manager.strategy

import com.project.manager.context.ActionResult
import com.project.manager.context.ActionType

class StealStrategy : ActionTypeStrategy {
    override fun getType(): ActionType {
        return ActionType.STEAL
    }

    override fun performAction(score: Int, point: Int): ActionResult {
        return ActionResult("Выберите соперника, у которого отнимите очки: $point", score, false)
    }
}
