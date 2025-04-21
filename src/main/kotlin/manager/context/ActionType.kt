package com.project.manager.context

enum class ActionType(val description: String) {
    NONE("Нет эффекта"),
    BLOCK("Пропускает ход"),
    STEAL("Минусует соперника"),
    DOUBLE_DOWN("Удваивает очки"),
}