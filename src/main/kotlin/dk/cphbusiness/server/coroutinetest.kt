package dk.cphbusiness.server

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun test() = runBlocking {
    for (i in 1..1_000_000) {
        launch {
            delay(100)
            println("CR$i")
        }
    }
}

fun main() {
    test()
}