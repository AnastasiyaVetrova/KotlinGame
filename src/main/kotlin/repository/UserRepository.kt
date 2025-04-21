package com.project.repository

import com.project.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByName(username: String): User?

    fun existsUserByName(username: String): Boolean
}