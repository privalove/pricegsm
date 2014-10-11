package com.pricegsm.controller;

import com.googlecode.flyway.test.annotation.FlywayTest;
import com.pricegsm.config.WebAppConfigurationAware;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * User: o.logunov
 * Date: 08.04.14
 * Time: 22:42
 */
@FlywayTest
public class OrderControllerTest extends WebAppConfigurationAware {
    @Test
    public void testOrder() throws Exception {
        mockMvc.perform(get("/order"));
    }

    @Test
    public void testOrders() throws Exception {
        mockMvc.perform(get("/order/orders.json"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.payload.orders", hasSize(2)))
                .andExpect(jsonPath("$.payload.orders[0].id", is(1001)))
                .andExpect(jsonPath("$.payload.orders[0].buyer.id", is(10001)))
                .andExpect(jsonPath("$.payload.orders[0].seller.id", is(2)))
                .andExpect(jsonPath("$.payload.orders[0].priceListPosition", is(0)))
                .andExpect(jsonPath("$.payload.orders[0].sendDate", is(1393621200000L)))
                .andExpect(jsonPath("$.payload.orders[0].status", is("DECLINED")))
                .andExpect(jsonPath("$.payload.orders[0].phone", is("+375291234567")))
                .andExpect(jsonPath("$.payload.orders[0].contactName", is("пётр петров")))
                .andExpect(jsonPath("$.payload.orders[0].delivery", is(true)))
                .andExpect(jsonPath("$.payload.orders[0].pickup", is(false)))
                .andExpect(jsonPath("$.payload.orders[0].deliveryFree", is(false)))
                .andExpect(jsonPath("$.payload.orders[0].place", is("ул. Орловская")))
                .andExpect(jsonPath("$.payload.orders[0].fromTime", is(25200000)))
                .andExpect(jsonPath("$.payload.orders[0].toTime", is(28800000)))
                .andExpect(jsonPath("$.payload.orders[0].deliveryDate", is("2014-03-01")))
                .andExpect(jsonPath("$.payload.orders[0].totalPrice", is(5000.0)))
                .andExpect(jsonPath("$.payload.orders[0].totalAmount", is(200)))
                .andExpect(jsonPath("$.payload.orders[0].currency.id", is(1)))
                .andExpect(jsonPath("$.payload.orders[0].orderPositions", hasSize(2)))
                .andExpect(jsonPath("$.payload.orders[0].orderPositions[0].id", is(1002)))
                .andExpect(jsonPath("$.payload.orders[0].orderPositions[0].order.id", is(1001)))
                .andExpect(jsonPath("$.payload.orders[0].orderPositions[0].product.id", is(6)))
                .andExpect(jsonPath("$.payload.orders[0].orderPositions[0].totalPrice", is(100.0)))
                .andExpect(jsonPath("$.payload.orders[0].orderPositions[0].price", is(20.0)))
                .andExpect(jsonPath("$.payload.orders[0].orderPositions[0].amount", is(5)))
                .andExpect(jsonPath("$.payload.orders[0].orderPositions[0].priceListPosition", is(10100)))
                .andExpect(jsonPath("$.payload.orders[0].orderPositions[1].id", is(1001)))
                .andExpect(jsonPath("$.payload.orders[0].orderPositions[1].order.id", is(1001)))
                .andExpect(jsonPath("$.payload.orders[0].orderPositions[1].product.id", is(4)))
                .andExpect(jsonPath("$.payload.orders[0].orderPositions[1].totalPrice", is(100.0)))
                .andExpect(jsonPath("$.payload.orders[0].orderPositions[1].price", is(20.0)))
                .andExpect(jsonPath("$.payload.orders[0].orderPositions[1].amount", is(5)))
                .andExpect(jsonPath("$.payload.orders[0].orderPositions[1].priceListPosition", is(9900)))
                .andExpect(jsonPath("$.payload.orders[1].id", is(1002)))
                .andExpect(jsonPath("$.payload.orders[1].buyer.id", is(10001)))
                .andExpect(jsonPath("$.payload.orders[1].seller.id", is(2)))
                .andExpect(jsonPath("$.payload.orders[1].priceListPosition", is(0)))
                .andExpect(jsonPath("$.payload.orders[1].sendDate", is(1393621200000L)))
                .andExpect(jsonPath("$.payload.orders[1].status", is("CONFIRMED")))
                .andExpect(jsonPath("$.payload.orders[1].phone", is("+375291234567")))
                .andExpect(jsonPath("$.payload.orders[1].contactName", is("пётр петров")))
                .andExpect(jsonPath("$.payload.orders[1].delivery", is(false)))
                .andExpect(jsonPath("$.payload.orders[1].pickup", is(true)))
                .andExpect(jsonPath("$.payload.orders[1].deliveryFree", is(false)))
                .andExpect(jsonPath("$.payload.orders[1].place", is("ул. Орловская1")))
                .andExpect(jsonPath("$.payload.orders[1].fromTime", is(25200000)))
                .andExpect(jsonPath("$.payload.orders[1].toTime", is(28800000)))
                .andExpect(jsonPath("$.payload.orders[1].deliveryDate", is("2014-03-01")))
                .andExpect(jsonPath("$.payload.orders[1].totalPrice", is(5000.0)))
                .andExpect(jsonPath("$.payload.orders[1].totalAmount", is(200)))
                .andExpect(jsonPath("$.payload.orders[1].currency.id", is(2)))
                .andExpect(jsonPath("$.payload.orders[1].orderPositions", hasSize(2)))
                .andExpect(jsonPath("$.payload.orders[1].orderPositions[0].id", is(1003)))
                .andExpect(jsonPath("$.payload.orders[1].orderPositions[0].order.id", is(1002)))
                .andExpect(jsonPath("$.payload.orders[1].orderPositions[0].product.id", is(4)))
                .andExpect(jsonPath("$.payload.orders[1].orderPositions[0].totalPrice", is(110.0)))
                .andExpect(jsonPath("$.payload.orders[1].orderPositions[0].price", is(11.0)))
                .andExpect(jsonPath("$.payload.orders[1].orderPositions[0].amount", is(10)))
                .andExpect(jsonPath("$.payload.orders[1].orderPositions[0].priceListPosition", is(10000)))
                .andExpect(jsonPath("$.payload.orders[1].orderPositions[1].id", is(1004)))
                .andExpect(jsonPath("$.payload.orders[1].orderPositions[1].order.id", is(1002)))
                .andExpect(jsonPath("$.payload.orders[1].orderPositions[1].product.id", is(5)))
                .andExpect(jsonPath("$.payload.orders[1].orderPositions[1].totalPrice", is(110.0)))
                .andExpect(jsonPath("$.payload.orders[1].orderPositions[1].price", is(11.0)))
                .andExpect(jsonPath("$.payload.orders[1].orderPositions[1].amount", is(10)))
                .andExpect(jsonPath("$.payload.orders[1].orderPositions[1].priceListPosition", is(10100)));
    }


    @Test
    public void testGetPriceList() throws Exception {
        mockMvc.perform(get("/order/10000/99/pricelist.json"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.payload.priceList.position", is(99)))
                .andExpect(jsonPath("$.payload.priceList.sellToDate", is("2015-03-01")))
                .andExpect(jsonPath("$.payload.priceList.sellFromDate", is("2014-03-01")))
                .andExpect(jsonPath("$.payload.priceList.user.id", is(10000)))
                .andExpect(jsonPath("$.payload.priceList.currency.id", is(1)))
                .andExpect(jsonPath("$.payload.priceList.positions", hasSize(2)))
                .andExpect(jsonPath("$.payload.priceList.positions[0].id", is(9900)))
                .andExpect(jsonPath("$.payload.priceList.positions[0].amount", is(999)))
                .andExpect(jsonPath("$.payload.priceList.positions[0].prices", hasSize(3)))
                .andExpect(jsonPath("$.payload.priceList.positions[0].prices[0].id", is(53)))
                .andExpect(jsonPath("$.payload.priceList.positions[0].prices[0].price", is(536.0)))
                .andExpect(jsonPath("$.payload.priceList.positions[0].prices[0].minOrderQuantity", is(102)))
                .andExpect(jsonPath("$.payload.priceList.positions[0].prices[1].id", is(33)))
                .andExpect(jsonPath("$.payload.priceList.positions[0].prices[1].price", is(556.0)))
                .andExpect(jsonPath("$.payload.priceList.positions[0].prices[1].minOrderQuantity", is(12)))
                .andExpect(jsonPath("$.payload.priceList.positions[0].prices[2].id", is(13)))
                .andExpect(jsonPath("$.payload.priceList.positions[0].prices[2].price", is(566.0)))
                .andExpect(jsonPath("$.payload.priceList.positions[0].prices[2].minOrderQuantity", is(2)))
                .andExpect(jsonPath("$.payload.priceList.positions[0].specification.id", is(1)))
                .andExpect(jsonPath("$.payload.priceList.positions[0].product.id", is(1)))
                .andExpect(jsonPath("$.payload.priceList.positions[1].id", is(10000)))
                .andExpect(jsonPath("$.payload.priceList.positions[1].amount", is(999)))
                .andExpect(jsonPath("$.payload.priceList.positions[1].prices", hasSize(3)))
                .andExpect(jsonPath("$.payload.priceList.positions[1].prices[0].id", is(54)))
                .andExpect(jsonPath("$.payload.priceList.positions[1].prices[0].price", is(536.0)))
                .andExpect(jsonPath("$.payload.priceList.positions[1].prices[0].minOrderQuantity", is(102)))
                .andExpect(jsonPath("$.payload.priceList.positions[1].prices[1].id", is(34)))
                .andExpect(jsonPath("$.payload.priceList.positions[1].prices[1].price", is(556.0)))
                .andExpect(jsonPath("$.payload.priceList.positions[1].prices[1].minOrderQuantity", is(12)))
                .andExpect(jsonPath("$.payload.priceList.positions[1].prices[2].id", is(14)))
                .andExpect(jsonPath("$.payload.priceList.positions[1].prices[2].price", is(566.0)))
                .andExpect(jsonPath("$.payload.priceList.positions[1].prices[2].minOrderQuantity", is(2)))
                .andExpect(jsonPath("$.payload.priceList.positions[1].specification.id", is(2)))
                .andExpect(jsonPath("$.payload.priceList.positions[1].product.id", is(2)));
    }

    @Test
    public void testSaveOrder() throws Exception {
        // Given
        mockMvc.perform(post("/order/1003/order.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"modifiedBy\":0,\"id\":1003,\"orderPositions\":[{\"modifiedBy\":0,\"id\":1005," +
                        "\"product\":{\"modifiedBy\":0,\"id\":6,\"name\":\"iPhone 5S 32Gb\"," +
                        "\"yandexId\":\"10495486\",\"searchQuery\":\"iphone 5s 32gb\"," +
                        "\"colorQuery\":\"gold\",\"type\":{\"modifiedBy\":0,\"id\":1," +
                        "\"name\":\"Мобильный телефон\",\"yandexId\":91491,\"active\":true}," +
                        "\"color\":{\"modifiedBy\":0,\"id\":5,\"name\":\"Gold\",\"yandexColor\":\"gold\"," +
                        "\"code\":\"#d8ce71\",\"active\":true},\"active\":true," +
                        "\"vendor\":{\"id\":1,\"name\":\"Apple\"}},\"totalPrice\":110,\"price\":11," +
                        "\"amount\":10,\"version\":1,\"priceListPosition\":112," +
                        "\"order\":{\"id\":1003,\"name\":\"com.pricegsm.domain.Order@280\"}}," +
                        "{\"modifiedBy\":0,\"id\":1006,\"product\":{\"modifiedBy\":0,\"id\":5," +
                        "\"name\":\"iPhone 5S 32Gb\",\"yandexId\":\"10495486\",\"searchQuery\":\"iphone 5s 32gb\"," +
                        "\"colorQuery\":\"gray\",\"type\":{\"modifiedBy\":0,\"id\":1,\"name\":\"Мобильный телефон\"," +
                        "\"yandexId\":91491,\"active\":true},\"color\":{\"modifiedBy\":0,\"id\":4," +
                        "\"name\":\"Space Gray\",\"yandexColor\":\"gray\",\"code\":\"#726f6f\",\"active\":true}," +
                        "\"active\":true,\"vendor\":{\"id\":1,\"name\":\"Apple\"}},\"totalPrice\":110," +
                        "\"price\":11,\"amount\":10,\"version\":1,\"priceListPosition\":111," +
                        "\"order\":{\"id\":1003,\"name\":\"com.pricegsm.domain.Order@280\"}}]," +
                        "\"seller\":{\"modifiedBy\":0,\"id\":2,\"active\":true,\"name\":\"Поставщик\"," +
                        "\"email\":\"seller@pricegsm.com\",\"sellerPickup\":false,\"sellerDelivery\":true," +
                        "\"buyerDelivery\":false,\"sellerDeliveryFree\":false,\"sellerDeliveryMin\":1," +
                        "\"sellerDeliveryPaid\":true,\"sellerDeliveryCost\":300,\"sellerFreeReplacement\":0," +
                        "\"sellerFreeRepair\":0,\"balance\":0,\"emailValid\":true," +
                        "\"authorities\":[{\"authority\":\"USER_ROLE\"}],\"enabled\":true," +
                        "\"username\":\"seller@pricegsm.com\",\"accountNonExpired\":true,\"accountNonLocked\":true," +
                        "\"credentialsNonExpired\":true,\"sellerDeliveryFrom\":25200000," +
                        "\"sellerDeliveryTo\":50400000,\"sellerPickupFrom\":25200000,\"sellerPickupTo\":50400000," +
                        "\"buyerDeliveryFrom\":25200000,\"buyerDeliveryTo\":50400000," +
                        "\"region\":{\"id\":1,\"name\":\"Москва\"}},\"priceListPosition\":0," +
                        "\"sendDate\":1393617600000,\"status\":\"SENT\",\"version\":1,\"phone\":\"+375291234567\"," +
                        "\"contactName\":\"пётр петров\",\"delivery\":true,\"pickup\":false,\"deliveryFree\":false," +
                        "\"place\":\"ул. Орловская1\",\"deliveryDate\":\"2014-02-01\",\"totalPrice\":220," +
                        "\"totalAmount\":20,\"currency\":{\"modifiedBy\":0,\"id\":2,\"name\":\"Евро\"," +
                        "\"code\":\"EUR\",\"symbol\":\"€\"},\"buyer\":{\"id\":3,\"name\":\"Покупатель\"}," +
                        "\"fromTime\":25200000,\"toTime\":28800000}")
        )
                .andExpect(jsonPath("$.payload.order.id", is(1003)))
                .andExpect(jsonPath("$.payload.order.buyer.id", is(3)))
                .andExpect(jsonPath("$.payload.order.seller.id", is(2)))
                .andExpect(jsonPath("$.payload.order.priceListPosition", is(0)))
                .andExpect(jsonPath("$.payload.order.sendDate", is(1393617600000L)))
                .andExpect(jsonPath("$.payload.order.status", is("SENT")))
                .andExpect(jsonPath("$.payload.order.phone", is("+375291234567")))
                .andExpect(jsonPath("$.payload.order.contactName", is("пётр петров")))
                .andExpect(jsonPath("$.payload.order.delivery", is(true)))
                .andExpect(jsonPath("$.payload.order.pickup", is(false)))
                .andExpect(jsonPath("$.payload.order.deliveryFree", is(false)))
                .andExpect(jsonPath("$.payload.order.place", is("ул. Орловская1")))
                .andExpect(jsonPath("$.payload.order.fromTime", is(25200000)))
                .andExpect(jsonPath("$.payload.order.toTime", is(28800000)))
                .andExpect(jsonPath("$.payload.order.deliveryDate", is("2014-02-01")))
                .andExpect(jsonPath("$.payload.order.totalPrice", is(220.0)))
                .andExpect(jsonPath("$.payload.order.totalAmount", is(20)))
                .andExpect(jsonPath("$.payload.order.currency.id", is(2)))
                .andExpect(jsonPath("$.payload.order.orderPositions", hasSize(2)))
                .andExpect(jsonPath("$.payload.order.orderPositions[0].id", is(1005)))
                .andExpect(jsonPath("$.payload.order.orderPositions[0].order.id", is(1003)))
                .andExpect(jsonPath("$.payload.order.orderPositions[0].product.id", is(6)))
                .andExpect(jsonPath("$.payload.order.orderPositions[0].totalPrice", is(110.0)))
                .andExpect(jsonPath("$.payload.order.orderPositions[0].price", is(11.0)))
                .andExpect(jsonPath("$.payload.order.orderPositions[0].amount", is(10)))
                .andExpect(jsonPath("$.payload.order.orderPositions[0].priceListPosition", is(112)))
                .andExpect(jsonPath("$.payload.order.orderPositions[1].id", is(1006)))
                .andExpect(jsonPath("$.payload.order.orderPositions[1].order.id", is(1003)))
                .andExpect(jsonPath("$.payload.order.orderPositions[1].product.id", is(5)))
                .andExpect(jsonPath("$.payload.order.orderPositions[1].totalPrice", is(110.0)))
                .andExpect(jsonPath("$.payload.order.orderPositions[1].price", is(11.0)))
                .andExpect(jsonPath("$.payload.order.orderPositions[1].amount", is(10)))
                .andExpect(jsonPath("$.payload.order.orderPositions[1].priceListPosition", is(111)));
    }

    @Test
    public void testDeleteOrder() throws Exception  {
        mockMvc.perform(delete("/order/1004/delete"));
    }

    @Test
    public void testDeleteOrderPosition() {
        // Given
//        todo
        // When

        // Then
    }
}
