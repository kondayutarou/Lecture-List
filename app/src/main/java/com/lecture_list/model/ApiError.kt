package com.lecture_list.model

abstract class ApiError(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)

class ApiServerError(
    code: Int,
    message: String?
) : ApiError(message = "$code: $message")
