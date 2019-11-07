package dk.cphbusiness.server

import java.io.InputStream
import java.io.OutputStream
import java.lang.StringBuilder
import java.lang.reflect.Member


enum class Method {GET,PUT,POST,DELETE}

data class Member(val id : Int, val firstName : String, val lastName: String)

/*
fun main() {
    val content = ChoirContent(/*filename*/)
    val server = WebServer(content, 4711)
    server.start()

    /* ChoirContent().publish()*/

}
*/

fun t(){
    TODO("Hello world")
}


class Request(input : InputStream) {
    val resource: String
    val method : Method
    init {
        val reader = input.bufferedReader()
        val line = reader.readLine()
        val parts = line.split(" ")
        resource = parts[1]
        method = Method.valueOf(parts[0])
    }

}



class Response (val output : OutputStream) {
    val body = StringBuilder()
    fun append(text : String) {
        body.append(text)

    }

    fun send() {
        val head = """
            HTTP/1.1 200 OK
            Content-Type: text/html; charset=UTF-8 
            Content-Length: ${body.length}
            Connection: close
            
            """.trimIndent()
        val writer = output.bufferedWriter()
        writer.append(head)
        writer.newLine()
        writer.append(body)
        writer.close()
    }
}





