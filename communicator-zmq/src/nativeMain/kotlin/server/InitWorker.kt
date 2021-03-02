package space.kscience.communicator.zmq.server

import kotlinx.coroutines.runBlocking
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.native.concurrent.freeze

internal actual fun initWorker(worker: ZmqWorker): Any =
    Worker.start().execute(TransferMode.SAFE, worker::freeze) { runBlocking { it.start() } }