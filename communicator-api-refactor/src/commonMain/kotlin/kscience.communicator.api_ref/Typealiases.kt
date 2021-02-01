package kscience.communicator.api_ref

/**
 * Represents binary object used by communicator API to transfer arguments, results of function, etc.
 *
 * Currently, it is [ByteArray], but it can be replaced by more efficient platform specific blob types (JVM ByteBuffer,
 * JavaScript ArrayBuffer...); or it can be bound to kotlinx.io binary types.
 */
typealias Payload = ByteArray

/**
 * Represents `suspend` function that takes and returns [Payload].
 */
typealias PayloadFunction = suspend (Payload) -> Payload
