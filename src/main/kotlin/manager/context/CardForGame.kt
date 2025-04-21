package com.project.manager.context

import com.project.manager.strategy.ActionTypeStrategy

class CardForGame(
    val name: String,
    val point: Int,
    val number: Int,
    val action: ActionTypeStrategy,
)