package com.project.manager.context

import com.project.utils.GameConstant.MAX_SCORE
import com.project.utils.GameConstant.MIN_SCORE

class Player(
    val id: Long,
    var score: Int = 0,
    var skipNextTurn: Boolean = false
){
    fun setNewScore(newScore: Int) {
       this.score = newScore.coerceIn(MIN_SCORE, MAX_SCORE)
    }
}