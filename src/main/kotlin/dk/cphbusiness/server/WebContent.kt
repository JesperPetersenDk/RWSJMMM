package dk.cphbusiness.server

import java.lang.reflect.Member

data class Member(val id : Int, val firstName : String, val lastName: String)

class ChoirContent() : IWebContent {
    fun getMembers(): List<Member> {
        println("fnadkjgfnsdjlkfmsljdnfg")
        return mutableListOf()
    }
    fun getMember(id: Int) : Member? {
        println("id: " + id)
        return null
    }

    fun putMember(member: Member) : Member =
        TODO()

    override fun save() {
        TODO()
    }


}
