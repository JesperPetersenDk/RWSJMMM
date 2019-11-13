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

    fun getFunctionNames(): MutableMap<String, KFunction<*>> {
        val type = content::class
        val map = mutableMapOf<String, KFunction<*>>();
        for (member in type.memberFunctions) {
            for (method in Method.values()) {
                if (member.name.contains(method.name.toLowerCase()))
                    map.put(member.name.toLowerCase(), member)
            }
        }
        return map
    }

    fun handle(request: Request, response: Response) {
        val URL = request.resource
        val argument = URL.split("/");
        val endpoint = argument[1]
        val map = getFunctionNames()
        val requestedEndpoint = map.get(endpoint)

        if (requestedEndpoint == null) {
            response.append("Error url")
            response.send()
            return
        }

        when (argument.size) {
            2 -> {
                if(request.resource.contains("put")) {

                    val json = request.resource
                    val member = requestedEndpoint.call(content, Gson().fromJson(json, Member::class.java))
                    response.append(Gson().toJson(member))
                    response.send()

                    println(request.resource)
                } else {
                    val members = requestedEndpoint.call(content)
                    response.append(Gson().toJson(members))
                }
            }
            3 -> {
                val member = requestedEndpoint.call(content, Integer.valueOf(argument[2]))
                response.append(Gson().toJson(member))
            }
            else -> {
                response.append("Error url")
                response.send()
            }
        }
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

