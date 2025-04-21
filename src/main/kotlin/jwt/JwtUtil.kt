package com.project.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtUtil {

    @Value("\${spring.security.jwt.time}")
    private var jwtTime: Long = 10000L

    private val key: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    fun generateToken(name: String, userId: Long): String {
        val date = Date()
        return Jwts.builder()
            .setSubject(name)
            .claim("id", userId)
            .setIssuedAt(date)
            .setExpiration(Date(System.currentTimeMillis() + jwtTime))
            .signWith(key)
            .compact()
    }

    fun getUserNameFromToken(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

    fun getIdFromToken(header: String): Long {
        val token = header.substring(7)
        val claim = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

        return claim["id"].toString().toLong()
    }

    fun getExpirationDateFromToken(token: String): Date {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
            .expiration
    }

    fun isTokenExpired(token: String): Boolean {
        return getExpirationDateFromToken(token).before(Date())
    }

    fun validateToken(token: String, name: String): Boolean {

        return name == getUserNameFromToken(token) && !isTokenExpired(token)
    }
}