package com.yunhalee.msa.architecture.service.order.data.access.restaurant.exception

class RestaurantDataAccessException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}