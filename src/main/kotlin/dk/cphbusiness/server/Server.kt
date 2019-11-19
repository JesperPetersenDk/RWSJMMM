package dk.cphbusiness.server

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.ServerSocket
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KFunction
import kotlin.reflect.full.memberExtensionFunctions
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf

class WebServer ( val content : ChoirContent , val port : Int) : CoroutineScope  {
    private val job = Job()
    override val coroutineContext : CoroutineContext
        get () = job

    fun getFunction(request: Request): Any?{
        val type = content::class
        val arguments = request.resource.split("/").filter { !it.isNullOrBlank() }
        if(arguments.size == 1) {
            for (member in type.memberFunctions) {
                if (arguments[0].equals(member.name, true) && request.method == Method.GET) {
                    return member.call(content)
                } else if(arguments[0].equals(member.name, true) && request.method == Method.PUT) {
                    println("put")
                    return member.call(content, Gson().fromJson(request.body,Member::class.java))
                }
            }
        }
        if(arguments.size == 2) {
            for (member in type.memberFunctions) {
                if (arguments[0].equals(member.name, true) && request.method == Method.GET) {
                    try {
                        return member.call(content, Integer.valueOf(arguments[1]))
                    } catch (e : Exception) {
                        return null
                    }
                }
            }
        }
        return null
    }

    fun handle(request: Request, response: Response) {
        val f = getFunction(request)
        response.append(Gson().toJson(f, Any::class.java))
        response.send()
    }

    fun start () {
        val serverSocket = ServerSocket(port)
        while (job.isActive) {
            val socket = serverSocket.accept()
            launch {
                handle(Request(socket.getInputStream()), Response(socket.getOutputStream()))
            }
        }
    }
    fun stop () {
        job.cancel()
    }
}

fun main() {

    WebServer(ChoirContent(),5000).start()
}

