package dk.cphbusiness.server

import com.google.gson.Gson

data class Member(val id : Int, val firstName : String, val lastName: String)

val db = mutableListOf<Member>()

fun initDb() {
    db.add(Member(1, "Kurt", "Hansen"))
    db.add(Member(2, "Bob", "Jensen"))
    db.add(Member(3, "Rasmus", "Ibsen"))
    db.add(Member(4, "Magnus", "Boesen"))
    db.add(Member(5, "Torben", "Justesen"))
}


class ChoirContent() : IWebContent {
    init {
        initDb()
    }

    fun getMembers(): List<Member> {
        return db
    }
    fun getMember(id: Int) : Member? {
        val member = db.get(id-1)
        if(member == null) {
            return null
        }
        return member
    }

    fun putMember(member: Member) : Member {
        var lastId = db.size
        if (lastId == 0) {
            lastId = 1
        }

        val tempMember = member.copy(id = lastId)
        db.add(tempMember)

        return member
    }


    override fun save() {
        TODO()
    }

}
