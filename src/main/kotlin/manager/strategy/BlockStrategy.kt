package com.project.manager.strategy

import com.project.manager.context.ActionResult
import com.project.manager.context.ActionType

class BlockStrategy: ActionTypeStrategy {
    override fun getType(): ActionType {
        return ActionType.BLOCK
    }

    override fun performAction(score: Int, point: Int): ActionResult {
        return ActionResult("Вы пропускаете ход: 1", score, changeNextTurn = true, skipNextTurn = true)
    }
}