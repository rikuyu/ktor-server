package com.example.model

import org.jetbrains.exposed.sql.Table

object Members : Table("members") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 32)
    val age = integer("age")
}

data class Member(
    val id: Int? = null,
    val name: String,
    val age: Int
)