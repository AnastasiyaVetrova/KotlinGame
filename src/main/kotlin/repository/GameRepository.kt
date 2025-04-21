package com.project.repository

import com.project.model.Game
import com.project.model.enums.StatusGame
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface GameRepository : JpaRepository<Game, Long> {

    @Modifying
    @Query("UPDATE Game g SET g.status= :status WHERE g.id = :id")
    fun updateStatusById(@Param("id") id: Long, @Param("status") status: StatusGame)
}