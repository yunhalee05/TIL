package com.yunhalee.msa.architecture.order.domain.application.service.domain

import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.track.TrackOrderQuery
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.track.TrackOrderResponse
import com.yunhalee.msa.architecture.order.domain.application.service.domain.mapper.OrderDataMapper
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.repository.OrderRepository
import com.yunhalee.msa.architecture.service.order.domain.core.exception.OrderDomainException
import com.yunhalee.msa.architecture.service.order.domain.core.exception.OrderNotFoundException
import com.yunhalee.msa.architecture.service.order.domain.core.valueobject.TrackingId
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.logging.Logger

@Component
class OrderTrackCommandHandler(
    private val orderDataMapper: OrderDataMapper,
    private val orderRepository: OrderRepository
) {

    private val logger = Logger.getLogger(OrderTrackCommandHandler::class.java.name)

    @Transactional(readOnly = true)
    fun trackOrder(trackOrderQuery: TrackOrderQuery):TrackOrderResponse {
        val order = orderRepository.findByTrackingId(TrackingId(trackOrderQuery.orderTrackingId)).orElseThrow {
            logger.warning("Order not found. id : ${trackOrderQuery.orderTrackingId}")
            throw OrderNotFoundException("Order not found. id : ${trackOrderQuery.orderTrackingId}")
        }
        logger.info("Track order command handler")
        return orderDataMapper.orderToTrackOrderResponse(order)
    }
}