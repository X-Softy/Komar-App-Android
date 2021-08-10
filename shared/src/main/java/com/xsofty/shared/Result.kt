package com.xsofty.shared

sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()

    sealed class Error : Result<Nothing>() {
        object NetworkError : Error()
        data class GenericError(val errorResponse: ErrorResponse? = null) : Error()
    }

    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error.NetworkError -> "NetworkError"
            is Error.GenericError -> "Error[errorResponse=$errorResponse]"
            Loading -> "Loading"
        }
    }
}

data class ErrorResponse(
    val code: Int? = null,
    val message: String? = null
)

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val Result<*>.succeeded
    get() = this is Result.Success && data != null

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}

val <T> Result<T>.data: T?
    get() = (this as? Result.Success)?.data
