package com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.input.service

import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create.CreateOrderCommand
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create.CreateOrderResponse
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.track.TrackOrderQuery
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.track.TrackOrderResponse
import jakarta.validation.Valid

interface OrderApplicationService {

    fun createOrder(@Valid createOrderCommand: CreateOrderCommand): CreateOrderResponse

    fun trackOrder(@Valid trackOrderQuery: TrackOrderQuery): TrackOrderResponse

}