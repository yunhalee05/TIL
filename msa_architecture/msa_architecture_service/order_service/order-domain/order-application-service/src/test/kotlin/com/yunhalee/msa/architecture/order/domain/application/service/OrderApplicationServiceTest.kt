package com.yunhalee.msa.architecture.order.domain.application.service

import com.yunhalee.msa.architecture.common.domain.valueobject.CustomerID
import com.yunhalee.msa.architecture.common.domain.valueobject.Money
import com.yunhalee.msa.architecture.common.domain.valueobject.OrderId
import com.yunhalee.msa.architecture.common.domain.valueobject.OrderStatus
import com.yunhalee.msa.architecture.common.domain.valueobject.ProductID
import com.yunhalee.msa.architecture.common.domain.valueobject.RestaurantID
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create.CreateOrderCommand
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create.CreateOrderResponse
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create.OrderAddress
import com.yunhalee.msa.architecture.order.domain.application.service.domain.dto.create.OrderItem
import com.yunhalee.msa.architecture.order.domain.application.service.domain.mapper.OrderDataMapper
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.input.service.OrderApplicationService
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.repository.CustomerRepository
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.repository.OrderRepository
import com.yunhalee.msa.architecture.order.domain.application.service.domain.ports.output.repository.RestaurantRepository
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Customer
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Order
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Product
import com.yunhalee.msa.architecture.service.order.domain.core.entity.Restaurant
import com.yunhalee.msa.architecture.service.order.domain.core.exception.OrderDomainException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.function.Executable
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.List
import java.util.Optional
import java.util.UUID


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = [OrderTestConfiguration::class])
class OrderApplicationServiceTest {

    @Autowired
    private lateinit var orderApplicationService: OrderApplicationService

    @Autowired
    private lateinit var orderDataMapper: OrderDataMapper

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var restaurantRepository: RestaurantRepository

    private lateinit var createOrderCommand: CreateOrderCommand
    private lateinit var createOrderCommandWrongPrice: CreateOrderCommand
    private lateinit var createOrderCommandWrongProductPrice: CreateOrderCommand
    private val CUSTOMER_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb41")
    private val RESTAURANT_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb45")
    private val PRODUCT_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb48")
    private val ORDER_ID = UUID.fromString("15a497c1-0f4b-4eff-b9f4-c402c8c07afb")
    private val SAGA_ID = UUID.fromString("15a497c1-0f4b-4eff-b9f4-c402c8c07afa")
    private val PRICE = BigDecimal("200.00")
    @BeforeAll
    fun init() {
        createOrderCommand = CreateOrderCommand.builder()
            .customerId(CUSTOMER_ID)
            .restaurantId(RESTAURANT_ID)
            .address(OrderAddress.builder()
                .street("street_1")
                .postalCode("1000AB")
                .city("Paris")
                .build())
            .price(PRICE)
            .items(listOf(OrderItem.builder()
                .productId(PRODUCT_ID)
                .quantity(1)
                .price(BigDecimal("50.00"))
                .subTotal(BigDecimal("50.00"))
                .build(),
                OrderItem.builder()
                    .productId(PRODUCT_ID)
                    .quantity(3)
                    .price(BigDecimal("50.00"))
                    .subTotal(BigDecimal("150.00"))
                    .build()))
            .build()
        createOrderCommandWrongPrice = CreateOrderCommand.builder()
            .customerId(CUSTOMER_ID)
            .restaurantId(RESTAURANT_ID)
            .address(OrderAddress.builder()
                .street("street_1")
                .postalCode("1000AB")
                .city("Paris")
                .build())
            .price(BigDecimal("250.00"))
            .items(List.of(OrderItem.builder()
                .productId(PRODUCT_ID)
                .quantity(1)
                .price(BigDecimal("50.00"))
                .subTotal(BigDecimal("50.00"))
                .build(),
                OrderItem.builder()
                    .productId(PRODUCT_ID)
                    .quantity(3)
                    .price(BigDecimal("50.00"))
                    .subTotal(BigDecimal("150.00"))
                    .build()))
            .build()
        createOrderCommandWrongProductPrice = CreateOrderCommand.builder()
            .customerId(CUSTOMER_ID)
            .restaurantId(RESTAURANT_ID)
            .address(OrderAddress.builder()
                .street("street_1")
                .postalCode("1000AB")
                .city("Paris")
                .build())
            .price(BigDecimal("210.00"))
            .items(List.of(OrderItem.builder()
                .productId(PRODUCT_ID)
                .quantity(1)
                .price(BigDecimal("60.00"))
                .subTotal(BigDecimal("60.00"))
                .build(),
                OrderItem.builder()
                    .productId(PRODUCT_ID)
                    .quantity(3)
                    .price(BigDecimal("50.00"))
                    .subTotal(BigDecimal("150.00"))
                    .build()))
            .build()
        val customer = Customer(CustomerID(CUSTOMER_ID))
        val restaurantResponse: Restaurant = Restaurant.builder()
            .restaurantId(RestaurantID(createOrderCommand.restaurantId))
            .products(listOf(Product(ProductID(PRODUCT_ID), "product-1", Money(BigDecimal("50.00"))),
                Product(ProductID(PRODUCT_ID), "product-2", Money(BigDecimal("50.00")))))
            .active(true)
            .build()
        val order: Order = orderDataMapper.createOrderCommandToOrder(createOrderCommand)
        order.setId(OrderId(ORDER_ID))
        Mockito.`when`(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(Optional.of(customer))
        Mockito.`when`(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand))).thenReturn(Optional.of(restaurantResponse))
        Mockito.`when`(orderRepository.save(any())).thenReturn(order)
    }

    @Test
    fun testCreateOrder() {
        val createOrderResponse: CreateOrderResponse = orderApplicationService.createOrder(createOrderCommand)
        assertEquals(OrderStatus.PENDING, createOrderResponse.orderStatus)
        assertEquals("Order created successfully", createOrderResponse.message)
        Assertions.assertNotNull(createOrderResponse.orderTrackingId)
    }

    @Test
    fun testCreateOrderWithWrongTotalPrice() {
        val orderDomainException: OrderDomainException = Assertions.assertThrows(OrderDomainException::class.java,
            Executable { orderApplicationService.createOrder(createOrderCommandWrongPrice) })
        assertEquals("Total price: 250.00 is not equal to Order items total: 200.00!", orderDomainException.message)
    }

    @Test
    fun testCreateOrderWithWrongProductPrice() {
        val orderDomainException: OrderDomainException = Assertions.assertThrows(OrderDomainException::class.java,
            Executable { orderApplicationService.createOrder(createOrderCommandWrongProductPrice) })
        assertEquals("Order item price: 60.00 is not valid for product $PRODUCT_ID", orderDomainException.message)
    }

    @Test
    fun testCreateOrderWithPassiveRestaurant() {
        val restaurantResponse: Restaurant = Restaurant.builder()
            .restaurantId(RestaurantID(createOrderCommand.restaurantId))
            .products(List.of(Product(ProductID(PRODUCT_ID), "product-1", Money(BigDecimal("50.00"))),
                Product(ProductID(PRODUCT_ID), "product-2", Money(BigDecimal("50.00")))))
            .active(false)
            .build()
        Mockito.`when`(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
            .thenReturn(Optional.of(restaurantResponse))
        val orderDomainException: OrderDomainException = Assertions.assertThrows(OrderDomainException::class.java) { orderApplicationService.createOrder(createOrderCommand) }
        assertEquals("Restaurant with id $RESTAURANT_ID is currently not active!", orderDomainException.message)
    }
}
