package dk.cphbusiness.server

import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class WebServer ( val content : WebContent , val port : Int = 80) {
    fun start () { TODO (" Implement ␣ start ") }
    fun stop () { TODO (" Implement ␣ stop ") }
}

class Server(val port : Int = 3724) {
    fun handle(request: Request, response: Response) {
        val URL = request.resource
        if (getTextFromUrl(URL).toLowerCase().contains("moodle")) {
            response.append("<h1>")
            response.append("FOR HELVEDE MOODLE :(")
            response.append("</h1>")
        }
        println(request.resource)
        response.append("<h2>")
        response.append("Hej " + getTextFromUrl(request.resource))

        response.append("</h2>")
        response.send()
    }

    fun getTextFromUrl(text : String) : String {
        return text.split("/")[1]
    }
    fun serve() {
        val serverSocket = ServerSocket(port)
        while (true) {
            val socket = serverSocket.accept()
            // instead of a thread, use a coroutine
            thread {
                handle(Request(socket.getInputStream()), Response(socket.getOutputStream()))
            }
        }
    }

}

