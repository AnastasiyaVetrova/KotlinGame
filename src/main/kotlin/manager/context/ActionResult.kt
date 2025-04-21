package com.project.manager.context

class ActionResult(
    var description: String,
    var scope: Int,
    var changeNextTurn: Boolean,
    var skipNextTurn: Boolean = false
)
