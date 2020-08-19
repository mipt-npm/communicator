package scientifik.communicator.zmq.proxy

import scientifik.communicator.zmq.platform.UniqueID
import scientifik.communicator.zmq.platform.ZmqContext

/**
 * Starts a proxy that listens to the given port.
 * This method blocks the thread and is recommended to be called from a separate program.
 * */
fun startProxy(port: Int) {
    ZmqProxy(port).start()
}

internal class Worker(
    val identity: ByteArray,
    val functions: MutableList<String>,
    var lastHeartbeatTime: Long
)

internal class ZmqProxy(private val port: Int) {
    internal val workers: MutableList<Worker> = mutableListOf()
    internal val functionSchemes: MutableMap<String, Pair<String, String>> = hashMapOf()
    internal val workersByFunction: MutableMap<String, MutableList<Worker>> = hashMapOf()

    // query id -> client identity
    internal val receivedQueries: MutableMap<UniqueID, ByteArray> = hashMapOf()
    internal val sentQueries: MutableSet<UniqueID> = hashSetOf()
    internal val sentResults: MutableSet<UniqueID> = hashSetOf()

    fun start(): Unit = ZmqContext().use { ctx ->
        val frontend = ctx.createRouterSocket()
        val backend = ctx.createRouterSocket()
        frontend.bind("tcp://*:$port")
        backend.bind("tcp://*:${port + 1}")
        val poller = ctx.backendContext.createPoller(2)
        poller.register(frontend.backendSocket)
        poller.register(backend.backendSocket)

        while (true) {
            if (poller.poll() < 0) continue
            if (poller.pollin(0)) handleFrontend(frontend, backend)
            if (poller.pollin(1)) handleBackend(frontend, backend)
        }
    }
}
