package com.example.bankwallet.api

data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val username: String,
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String
)