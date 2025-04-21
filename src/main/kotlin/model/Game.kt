package com.project.model

import com.project.model.enums.StatusGame
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "games")
class Game(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createDate: LocalDateTime = LocalDateTime.now(),

    @Enumerated(EnumType.STRING)
    var status: StatusGame = StatusGame.WAIT_FOR_PLAYER,

    @Column(name = "finished_at")
    var finishedDate: LocalDateTime? = null,

    @ManyToOne
    var winner: User? = null,
)
