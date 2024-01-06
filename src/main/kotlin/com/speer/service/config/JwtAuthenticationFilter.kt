package com.speer.service.config

import com.speer.service.auth.JwtTokenUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtAuthenticationFilter(
    private var jwtTokenUtil: JwtTokenUtil,
    private var userDetailService: UserDetailsService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Do not process further and execute next filter in chain
            filterChain.doFilter(request, response)
            return
        }

        val jwtToken = authHeader.substring(7)
        try {
            val userEmail = jwtTokenUtil.getEmail(jwtToken)

            if (SecurityContextHolder.getContext().authentication == null) {
                val userDetails: UserDetails = this.userDetailService.loadUserByUsername(userEmail)
                // Check if jwt token is valid
                if (jwtTokenUtil.isTokenValid(jwtToken)) {
                    var authToken: UsernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        } catch (e: Exception) {
            return response.sendError(403)
        } finally {
            filterChain.doFilter(request, response)
        }
    }
}