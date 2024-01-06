package com.speer.service.repository

import com.speer.service.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IUserRepository : JpaRepository<User, Int> {
    fun findByEmail(email: String): User?
}