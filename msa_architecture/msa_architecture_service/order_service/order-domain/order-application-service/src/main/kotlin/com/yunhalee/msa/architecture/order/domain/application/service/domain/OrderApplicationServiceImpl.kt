package com.yunhalee.msa.architecture.order.domain.application.service.domain

import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create.CreateOrderCommand
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create.CreateOrderResponse
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.track.TrackOrderQuery
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.track.TrackOrderResponse
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.input.service.OrderApplicationService
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import java.util.logging.Logger

@Service
@Validated
class OrderApplicationServiceImpl(
    private val orderCreateCommandHandler: OrderCreateCommandHandler,
    private val orderTrackCommandHandler: OrderTrackCommandHandler
): OrderApplicationService {

    private val logger = Logger.getLogger(OrderApplicationServiceImpl::class.java.name)

    override fun createOrder(createOrderCommand: CreateOrderCommand): CreateOrderResponse {
        return orderCreateCommandHandler.createOrder(createOrderCommand)
    }

    override fun trackOrder(trackOrderQuery: TrackOrderQuery): TrackOrderResponse {
        return orderTrackCommandHandler.trackOrder(trackOrderQuery)
    }
}