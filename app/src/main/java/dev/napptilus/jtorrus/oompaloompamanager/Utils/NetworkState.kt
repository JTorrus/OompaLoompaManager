package dev.napptilus.jtorrus.oompaloompamanager.Utils

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

data class NetworkState (val status: Status, val message: String? = null) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.RUNNING)

        fun error(message: String?) = NetworkState(Status.FAILED, message)
    }
}