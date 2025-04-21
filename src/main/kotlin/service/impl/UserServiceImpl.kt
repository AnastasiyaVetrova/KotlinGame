package com.project.service.impl

import com.project.model.User
import com.project.repository.UserRepository
import com.project.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    @Transactional
    override fun register(name: String, password: String): Long {
        if (userRepository.existsUserByName(name)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User with name $name already exists")
        }
        val passwordHash = passwordEncoder.encode(password)
        val user = User(name = name, passwordHash = passwordHash)
        val result = userRepository.save(user)
        return result.id ?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User registration error")
    }

    override fun login(name: String): Long {
        val user = userRepository.findByName(name)
        return user?.id ?: throw ResponseStatusException(HttpStatus.NOT_FOUND   , "User not fount")
    }
}