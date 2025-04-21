package com.project.service

interface UserService {
    fun register(name: String, password: String): Long

    fun login(name: String): Long
}