package com.example

import com.example.model.Member
import com.example.model.Members
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

    fun getAllMembers(): List<Member> {
        var members = listOf<Member>()
        transaction {
            members = Members.selectAll().map {
                Member(
                    id = it[Members.id],
                    name = it[Members.name].toString(),
                    age = it[Members.age]
                )
            }
        }
        return members
    }

    fun getMember(id: Int): Member? {
        var member: Member? = null
        transaction {
            member = Members.select { Members.id eq id }
                .map { convertToMember(it) }
                .firstOrNull()
        }
        return member
    }

    fun insertMember(name: String, age: Int) {
        transaction {
            Members.insert {
                it[this.name] = name
                it[this.age] = age
            }
        }
    }

    fun deleteMember(id: Int){
        transaction {
            Members.deleteWhere { Members.id eq id }
        }
    }

    private fun convertToMember(row: ResultRow): Member {
        return Member(
            id = row[Members.id],
            name = row[Members.name],
            age = row[Members.age]
        )
    }
}