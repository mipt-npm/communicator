package scientifik.communicator.zmq.platform

/** 16-byte unique query ID */
class QueryID(val bytes: ByteArray) {

    /** Generates random query ID using UUID */
    constructor() : this(generateUUID())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is QueryID) return false
        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }

    override fun toString(): String = uuidToString(bytes)

}

internal expect fun generateUUID(): ByteArray

/** Converts UUID to its canonical string representation (like "123e4567-e89b-12d3-a456-426655440000") */
internal expect fun uuidToString(bytes: ByteArray): String