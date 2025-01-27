package com.yunhalee.msa.architecture.order.domain.application.service.domain

import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.track.TrackOrderQuery
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.track.TrackOrderResponse
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class OrderTrackCommandHandler {

    private val logger = Logger.getLogger(OrderTrackCommandHandler::class.java.name)

    fun trackOrder(trackOrderQuery: TrackOrderQuery):TrackOrderResponse {
        logger.info("Track order command handler")
    }
}