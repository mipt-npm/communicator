package scientifik.communicator.factories

import scientifik.communicator.api.*
import scientifik.communicator.zmq.server.ZmqTransportServer

/**
 * Function server that can use multiple transport servers to listen to requests using multiple protocols.
 * Host part of endpoint address is ignored, server can work only on local host.
 * Multiple endpoints with the same protocol are ignored, only one port for each protocol is supported.
 * Multiple protocols on one port are not supported.
 */
class TransportFunctionServer(override val endpoints: List<Endpoint>) : FunctionServer {
    constructor(vararg endpoints: Endpoint) : this(endpoints.toList())

    private val transportServers: List<TransportServer>

    init {
        val actualEndpoints = endpoints
            .asSequence()
            .map { Endpoint(it.protocol, ":${it.port}") }
            .map { it.protocol to it.port }
            .toSet()

        check(actualEndpoints.size == endpoints.size) { "Invalid endpoints list. Read docs for DefaultFunctionServer." }

        transportServers = actualEndpoints.map { (protocol, port) ->
            when (protocol) {
                "ZMQ" -> ZmqTransportServer(port)
                else -> error("Protocol $protocol is not supported.")
            }
        }
    }

    override suspend fun <T, R> register(name: String, spec: FunctionSpec<T, R>, function: suspend (T) -> R): suspend (T) -> R {
        val payloadFunction = function.toBinary(spec)
        transportServers.forEach { it.register(name, payloadFunction) }
        return function
    }

    override suspend fun unregister(name: String): Unit = transportServers.forEach { it.unregister(name) }
    override fun close(): Unit = transportServers.forEach(TransportServer::close)
}