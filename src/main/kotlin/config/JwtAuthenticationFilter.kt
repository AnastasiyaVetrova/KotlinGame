package config

import com.project.jwt.JwtUtil
import com.project.service.impl.UserDetailsServiceImpl
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsServiceImpl: UserDetailsServiceImpl
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val path = request.servletPath
        if (path.startsWith("/auth")) {
            return chain.doFilter(request, response)
        }
        val token = getTokenFromRequest(request)
        val username = jwtUtil.getUserNameFromToken(token)
        val userDetails = userDetailsServiceImpl.loadUserByUsername(username)
        if (jwtUtil.validateToken(token, userDetails.username)) {
            val authentication = UsernamePasswordAuthenticationToken(
                userDetails, null, emptyList()
            )
            SecurityContextHolder.getContext().authentication = authentication
        }
        chain.doFilter(request, response)
    }

    private fun getTokenFromRequest(request: HttpServletRequest): String {
        val bearerToken = request.getHeader("Authorization")
        val token = bearerToken?.takeIf { it.startsWith("Bearer ") }?.removePrefix("Bearer ")
        return token ?: throw ServletException("Token is missing or invalid")
    }
}
