package dk.cphbusiness.server

import java.io.InputStream
import java.io.OutputStream
import java.lang.RuntimeException
import java.nio.ByteBuffer
import java.nio.charset.Charset


enum class Method {GET,PUT,POST,DELETE, NONHTTP}



/*
fun main() {
    val content = ChoirContent(/*filename*/)
    val server = WebServer(content, 4711)
    server.start()

    /* ChoirContent().publish()*/

}
*/


class Request(input : InputStream) {
    val resource: String
    val method: Method
    val body: String
    val headers = mutableMapOf<String, String>()

    init {
        var line = input.readLine()
        val parts = line.split(" ")
        if (parts.size != 3) {
            resource = ""
            method = Method.NONHTTP
        }
        else {
            resource = parts[1]
            method = Method.valueOf(parts[0])
        }
        line = input.readLine()
        while (line.isNotEmpty()) {
            val headerParts = line.split(":")
            headers[headerParts[0].trim().toLowerCase()] = headerParts[1].trim()
            line = input.readLine()
        }

        val contentLenghtText = headers["content-length"]
        if (contentLenghtText == null) body = ""
        else body = input.readString(contentLenghtText.toInt())
    }
}




class Response (val output : OutputStream) {
    val body = StringBuilder()
    fun append(text: String) {
        body.append(text)

    }

    fun send() {
        val head = """
            HTTP/1.1 200 OK
            Access-Control-Allow-Origin: *
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

const val EOF = -1
const val EOT = 4
const val LF = 10
const val CR = 13

fun InputStream.readLine(charset: Charset = Charsets.UTF_8): String {

    fun StringBuilder.consume(bytes: ByteBuffer): StringBuilder {
        bytes.limit(bytes.position()).position(0)
        this.append(charset.decode(bytes))
        bytes.clear()
        return this
    }

    val result = StringBuilder()
    val bytes = ByteBuffer.allocate(256)
    while (true) {
        val byte = this.read()
        if (byte == LF || byte == EOF || byte == EOT) return result.consume(bytes).toString()
        if (byte == CR) continue // ignore CRs
        if (bytes.position() > bytes.limit() - 4 && charset.isStart(byte)) result.consume(bytes)
        bytes.put(byte.toByte())
    }
    throw RuntimeException("No line ending found")
}

fun InputStream.readBytes(count: Int) = ByteArray(count).also { read(it, 0, count) }

fun InputStream.readString(count: Int, charset: Charset = Charsets.UTF_8) =
    charset.decode(ByteBuffer.wrap(readBytes(count))).toString()

/**
 * Is this byte a continuation to the previous byte(s).
 * In some charsets as UTF-8 a character uses more than one byte,
 * all bytes that is not the first of those bytes are continuations
 *
 * @param byte the byte as an integer to check
 * @return whether this byte is a continuation byte
 */
fun Charset.isContinuation(byte: Int) =
    when (this) {
        Charsets.UTF_8 -> byte and 0b1100_0000 == 0b1000_0000
        Charsets.UTF_16 -> byte != 0 // this is an approximation
        else -> false
    }

fun Charset.isStart(byte: Int) = !isContinuation(byte)

fun OutputStream.writeLine(line: String, charset: Charset = Charsets.UTF_8) =
    apply {
        write(line).write(LF)
    }

fun OutputStream.writeLine() = apply { write(LF) }

fun OutputStream.write(string: String, charset: Charset = Charsets.UTF_8) =
    apply {
        val buffer = charset.encode(string)
        write(buffer.array(), 0, buffer.limit())
    }





