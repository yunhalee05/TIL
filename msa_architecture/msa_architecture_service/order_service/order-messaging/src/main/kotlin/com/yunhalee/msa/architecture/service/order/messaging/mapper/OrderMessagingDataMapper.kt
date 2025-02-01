package com.yunhalee.msa.architecture.service.order.messaging.mapper

import com.food.ordering.system.kafka.order.avro.model.PaymentOrderStatus
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel
import com.food.ordering.system.kafka.order.avro.model.PaymentResponseAvroModel
import com.food.ordering.system.kafka.order.avro.model.Product
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel
import com.food.ordering.system.kafka.order.avro.model.RestaurantOrderStatus
import com.yunhalee.msa.architecture.common.domain.valueobject.OrderApprovalStatus
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.message.PaymentResponse
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.message.RestaurantApprovalResponse
import com.yunhalee.msa.architecture.service.order.domain.core.event.OrderCanceledEvent
import com.yunhalee.msa.architecture.service.order.domain.core.event.OrderCreateEvent
import com.yunhalee.msa.architecture.service.order.domain.core.event.OrderPaidEvent
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
        val order = orderPaidEvent.order
        return RestaurantApprovalRequestAvroModel.newBuilder()
            .setId(UUID.randomUUID())
            .setSagaId("")
            .setOrderId(order.id!!.getValue())
            .setRestaurantId(order.restaurantId.getValue())
            .setOrderId(order.id!!.getValue())
            .setRestaurantOrderStatus(RestaurantOrderStatus
                .valueOf(order.orderStatus.name))
            .setProducts(order.orderItems.stream().map { orderItem ->
                Product.newBuilder()
                    .setId(orderItem.product.id!!.getValue().toString())
                    .setQuantity(orderItem.quantity)
                    .build()
            }.collect(Collectors.toList()))
            .setPrice(order.price.amount)
            .setCreatedAt(orderPaidEvent.createdAt.toInstant())
            .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
            .build()
    }


    fun paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel: PaymentResponseAvroModel): PaymentResponse {
        return PaymentResponse.builder()
            .id(paymentResponseAvroModel.id.toString())
            .sagaId(paymentResponseAvroModel.sagaId.toString())
            .paymentId(paymentResponseAvroModel.paymentId.toString())
            .customerId(paymentResponseAvroModel.customerId.toString())
            .orderId(paymentResponseAvroModel.orderId.toString())
            .price(paymentResponseAvroModel.price)
            .createdAt(paymentResponseAvroModel.createdAt)
            .paymentStatus(com.yunhalee.msa.architecture.common.domain.valueobject.PaymentStatus.valueOf(paymentResponseAvroModel.paymentStatus.name))
            .failureMessages(paymentResponseAvroModel.failureMessages)
            .build()
    }


    fun approvalResponseAvroModelToApprovalResponse(restaurantApprovalResponseAvroModel: RestaurantApprovalResponseAvroModel): RestaurantApprovalResponse {
        return RestaurantApprovalResponse.builder()
            .id(restaurantApprovalResponseAvroModel.id.toString())
            .sagaId(restaurantApprovalResponseAvroModel.sagaId.toString())
            .restaurantId(restaurantApprovalResponseAvroModel.restaurantId.toString())
            .orderId(restaurantApprovalResponseAvroModel.orderId.toString())
            .createdAt(restaurantApprovalResponseAvroModel.createdAt)
            .orderApprovalStatus(OrderApprovalStatus.valueOf(restaurantApprovalResponseAvroModel.orderApprovalStatus.name))
            .failureMessages(restaurantApprovalResponseAvroModel.failureMessages)
            .build()
    }


}