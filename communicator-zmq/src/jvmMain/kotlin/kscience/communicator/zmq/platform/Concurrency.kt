package kscience.communicator.zmq.platform

import kotlin.concurrent.thread
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
internal actual inline fun <T1, T2> runInBackground(
    supplier: () -> T1,
    noinline volatileJob: (T1) -> T2
) {
    contract {
        callsInPlace(supplier, InvocationKind.EXACTLY_ONCE)
        callsInPlace(volatileJob)
    }

    val arg = supplier()
    thread(isDaemon = true) { volatileJob(arg) }
}
