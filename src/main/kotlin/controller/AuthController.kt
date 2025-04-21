package com.project.controller

import com.project.jwt.JwtUtil
import com.project.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService,
    private val jwtUtil: JwtUtil,
    private val authenticationManager: AuthenticationManager
) {

    @PostMapping("/register")
    fun register(@RequestParam name: String, @RequestParam password: String): ResponseEntity<String> {

        val userId = userService.register(name, password)

        val token = jwtUtil.generateToken(name, userId)
        return ResponseEntity.ok(token)
    }

    @PostMapping("/login")
    fun login(@RequestParam name: String, @RequestParam password: String): ResponseEntity<String> {
        return try {
            val auth = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(name, password)
            )
            val userId = userService.login(auth.name)
            val token = jwtUtil.generateToken(name, userId)
            ResponseEntity.ok(token)

        } catch (e: AuthenticationException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect login or password")
        }
    }
}
