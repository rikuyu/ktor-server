package com.example

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseService {

    fun dbConnect(
        url: String,
        driver: String,
        user: String,
        password: String
    ) {
        Database.connect(
            url = url,
            driver = driver,
            user = user,
            password = password
        )
    }

    fun dsl() {
        transaction {
            addLogger(StdOutSqlLogger)

            val id = Member.insert {
                it[name] = "Kotlin"
            } get Member.id
            println("inserted id: $id")

            val member = Member.select { Member.id eq id }.single()
            println("id: ${member[Member.id]}")
            println("name: ${member[Member.name]}")
        }
    }

    object Member : Table("member") {
        val id = integer("id").autoIncrement()
        val name = varchar("name", 32)
    }

    fun dao() {
        transaction {
            addLogger(StdOutSqlLogger)

            val member = MemberEntity.new {
                name = "kotlin"
            }
            println("inserted id: ${member.id}")

            MemberEntity.findById(member.id)?.let {
                println("id: ${it.id}")
                println("name: ${it.name}")
            }
        }
    }

    object MemberTable : IntIdTable("member") {
        val name = varchar("name", 32)
    }

    class MemberEntity(id: EntityID<Int>) : IntEntity(id) {
        companion object : IntEntityClass<MemberEntity>(MemberTable)

        var name by MemberTable.name
    }
}