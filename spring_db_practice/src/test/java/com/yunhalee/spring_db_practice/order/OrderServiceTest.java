package com.yunhalee.spring_db_practice.order;

import com.yunhalee.spring_db_practice.domain.Order;
import com.yunhalee.spring_db_practice.exception.order.NotEnoughMoneyException;
import com.yunhalee.spring_db_practice.repository.order.OrderRepository;
import com.yunhalee.spring_db_practice.service.order.OrderService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;

@SpringBootTest
public class OrderServiceTest {
    
    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    private final Logger log = org.slf4j.LoggerFactory.getLogger(OrderServiceTest.class);

    @Test
    void complete() throws NotEnoughMoneyException {
        // given
        Order order = new Order();
        order.setUsername("정상");

        // when
        orderService.order(order);

        // then
        Order findOrder = orderRepository.findById(order.getId()).get();
        assertThat(findOrder.getPayStatus()).isEqualTo("완료");
    }

    @Test
    void runtimeException() {
        // given
        Order order = new Order();
        order.setUsername("예외");

        // when
        assertThatThrownBy(() -> orderService.order(order))
                .isInstanceOf(RuntimeException.class);

        // then
        Optional<Order> orderOptional = orderRepository.findById(order.getId());
        assertThat(orderOptional.isEmpty()).isTrue();
    }

    @Test
    void bizException() {
        // given
        Order order = new Order();
        order.setUsername("잔고부족");

        // when
        try {
            orderService.order(order);
            fail("잔고 부족 예외가 발생해야 합니다.");
        } catch (NotEnoughMoneyException e) {
            // then
            log.info("고객에게 잔고 부족을 알리고 별도의 계좌로 입금하도록 안내.");

        }

        // then
        Order findOrder = orderRepository.findById(order.getId()).get();
        assertThat(findOrder.getPayStatus()).isEqualTo("대기");
    }
}
