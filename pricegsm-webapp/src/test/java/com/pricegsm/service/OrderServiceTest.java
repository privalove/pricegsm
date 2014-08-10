package com.pricegsm.service;

import com.pricegsm.dao.OrderDao;
import com.pricegsm.domain.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * User: o.logunov
 * Date: 09.08.14
 * Time: 1:05
 */
@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    private OrderDao orderDao;

    @Test
    public void testName() {
        // Given
        Order order = new Order();
        order.setId(101L);

        when(orderDao.findByUserIdOrderId(100L, 101L)).thenReturn(order);

        // When
        Order currentUserOrderById = orderService.getCurrentUserOrderById(100L, 101L);

        // Then
        assertThat(currentUserOrderById).isEqualTo(order);
    }
}
