package com.plcoding.spotifycloneyt.other

data class Resources<out T>(val status: Status, val data: T?, val message: String?) {

    companion object{
        fun <T> success(data: T?) = Resources(Status.SUCCESS, data, null)

        fun <T> error(message: String?,data: T?) = Resources(Status.ERROR,data,message)

        fun <T> loading(data: T?) = Resources(Status.LOADING, data, null)
    }
}

enum class Status{
    SUCCESS,
    ERROR,
    LOADING
}