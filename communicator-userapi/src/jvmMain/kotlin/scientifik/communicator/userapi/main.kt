package scientifik.communicator.userapi

import kotlinx.coroutines.runBlocking
import scientifik.communicator.api.IntCoder

internal val TestCoder = IntCoder()

internal object TestClient : Client("tcp://localhost:8888") {
    val f by function(coder = TestCoder)

    init {
        start()
    }
}

internal object TestServer : Server("tcp://localhost:8888") {
    val f by function(coder = TestCoder) { x -> x + 1 }

    init {
        start()
    }
}

fun main(): Unit = runBlocking {
    println("Initializing server")
    TestServer
    println("Initializing client")
    TestClient
    println("Result is ${TestClient.f(1)}")
    TestServer.close()
    TestClient.close()
}