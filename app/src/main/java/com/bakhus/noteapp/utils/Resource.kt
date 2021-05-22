package com.bakhus.noteapp.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T) : Resource<T>(data = data, null)

    class Error<T>(message: String, data: T? = null) : Resource<T>(data = data, message = message)

    class Loading<T>(data: T? = null) : Resource<T>(data)

}


//data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
//    companion object {
//        fun <T> success(data: T?): Resource<T> {
//            return Resource(Status.SUCCESS, data, null)
//        }
//        fun <T> error(msg: String, data: T?): Resource<T> {
//            return Resource(Status.ERROR, data, msg)
//        }
//        fun <T> loading(data: T?): Resource<T> {
//            return Resource(Status.LOADING, data, null)
//        }
//    }
//}
//
//enum class Status {
//    SUCCESS,
//    ERROR,
//    LOADING
//}