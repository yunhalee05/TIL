package com.yunhalee.msa.architecture.service.order.application.rest

import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create.CreateOrderCommand
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create.CreateOrderResponse
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.track.TrackOrderQuery
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.track.TrackOrderResponse
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.input.service.OrderApplicationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.logging.Logger

@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderApplicationService: OrderApplicationService
) {

    private val logger = Logger.getLogger(OrderController::class.java.name)

    @PostMapping
    fun createOrder(@RequestBody createOrderCommand: CreateOrderCommand): ResponseEntity<CreateOrderResponse> {
        logger.info("Creating order for customer=${createOrderCommand.customerId} and restaurant=${createOrderCommand.restaurantId}")
        val createOrderResponse = orderApplicationService.createOrder(createOrderCommand)
        logger.info("Order created with tracking id=${createOrderResponse.orderTrackingId}")
        return ResponseEntity.ok(createOrderResponse)
    }

    @GetMapping("/{trackingId}")
    fun getOrderByTrackingId(@PathVariable trackingId: UUID): ResponseEntity<TrackOrderResponse> {
        logger.info("Tracking order with tracking id=$trackingId")
        val trackOrderResponse = orderApplicationService.trackOrder(TrackOrderQuery(orderTrackingId = trackingId))
        logger.info("Returning order status with tracking id = ${trackOrderResponse.orderTrackingId} and status = ${trackOrderResponse.orderStatus}")
        return ResponseEntity.ok(trackOrderResponse)
    }
}