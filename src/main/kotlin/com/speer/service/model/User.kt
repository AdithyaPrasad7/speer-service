package com.speer.service.model

import com.speer.service.dto.response.UserResponse
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*


@Entity
@Table(name = "users")
data class User(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name")
    var name: String,

    @Column(name = "email", unique = true)
    var email: String,

    @Column(name = "phone")
    var phone: String,

    @Column(name = "password")
    private var password: String
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return Collections.singleton(SimpleGrantedAuthority("user"))
    }

    override fun getPassword(): String {
        return password
    }

    fun setPassword(value: String) {
        password = value
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true;
    }

    override fun isAccountNonLocked(): Boolean {
        return true;
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    fun comparePassword(password: String): Boolean {
        return BCryptPasswordEncoder().matches(password, this.password)
    }

    fun toResponse(): UserResponse {
        return UserResponse(name = name, email = email, phone = phone)
    }

}
