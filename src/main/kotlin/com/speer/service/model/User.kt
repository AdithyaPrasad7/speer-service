package com.speer.service.model

import jakarta.persistence.*


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
    var password: String
) {
}
