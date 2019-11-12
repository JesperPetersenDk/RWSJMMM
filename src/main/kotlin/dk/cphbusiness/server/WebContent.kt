package dk.cphbusiness.server

import java.lang.reflect.Member

data class Member(val id : Int, val firstName : String, val lastName: String)

class ChoirContent() : IWebContent {
    fun getMember(): List<Member> =
        TODO()

    fun getMember(id: Int) : Member? =
        TODO()

    fun putMember(member: Member) : Member =
        TODO()

    override fun save() {
        TODO()
    }


}
