package com.yunhalee.msa.architecture.service.order.messaging.mapper

import com.food.ordering.system.kafka.order.avro.model.PaymentOrderStatus
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel
import com.food.ordering.system.kafka.order.avro.model.Product
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel
import com.food.ordering.system.kafka.order.avro.model.RestaurantOrderStatus
import com.yunhalee.msa.architecture.service.order.domain.core.event.OrderCanceledEvent
import com.yunhalee.msa.architecture.service.order.domain.core.event.OrderCreateEvent
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.stream.Collectors

@Component
class OrderMessagingDataMapper {

    fun orderCreatedEventToPaymentRequestAvroModel(orderCreatedEvent: OrderCreateEvent): PaymentRequestAvroModel {
        val order = orderCreatedEvent.order
        return PaymentRequestAvroModel.newBuilder()
            .setId(UUID.randomUUID())
            .setSagaId("")
            .setOrderId(orderCreatedEvent.order.id!!.getValue())
            .setCustomerId(order.customerId.getValue())
            .setPrice(order.price.amount)
            .setCreatedAt(orderCreatedEvent.createdAt.toInstant())
            .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
            .build()
    }

    fun orderCanceledEventToPaymentRequestAvroModel(orderCanceledEvent: OrderCanceledEvent): PaymentRequestAvroModel {
        val order = orderCanceledEvent.order
        return PaymentRequestAvroModel.newBuilder()
            .setId(UUID.randomUUID())
            .setSagaId("")
            .setOrderId(orderCanceledEvent.order.id!!.getValue())
            .setCustomerId(order.customerId.getValue())
            .setPrice(order.price.amount)
            .setCreatedAt(orderCanceledEvent.createdAt.toInstant())
            .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
            .build()
    }


    fun orderPaidEventToRestaurantApprovalRequestAvroModel(orderPaidEvent: OrderPaidEvent): RestaurantApprovalRequestAvroModel {
        val order= orderPaidEvent.getOrder()
        return RestaurantApprovalRequestAvroModel.newBuilder()
            .setId(UUID.randomUUID())
            .setSagaId("")
            .setOrderId(order.getId().getValue().toString())
            .setRestaurantId(order.getRestaurantId().getValue().toString())
            .setOrderId(order.getId().getValue().toString())
            .setRestaurantOrderStatus(RestaurantOrderStatus
                .valueOf(order.getOrderStatus().name()))
            .setProducts(order.getItems().stream().map { orderItem ->
                Product.newBuilder()
                    .setId(orderItem.getProduct().getId().getValue().toString())
                    .setQuantity(orderItem.getQuantity())
                    .build()
            }.collect(Collectors.toList()))
            .setPrice(order.getPrice().getAmount())
            .setCreatedAt(orderPaidEvent.getCreatedAt().toInstant())
            .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
            .build()
    }


}