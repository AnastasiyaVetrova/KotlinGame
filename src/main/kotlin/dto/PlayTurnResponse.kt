package com.project.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class PlayTurnResponse(
    var description: String,
    var turn: Int,
    var currentPlayerId: Long,
    var nextPlayerId: Long,
    var scope: Int,
    var victimPlayerId: Long? = null,
    var victimScope: Int? = null
)
