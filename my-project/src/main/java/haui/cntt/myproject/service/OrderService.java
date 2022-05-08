package haui.cntt.myproject.service;

import haui.cntt.myproject.persistance.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
public interface OrderService {
    Order create(Order order) throws Throwable;

    @Transactional
    void cancelOrder(long orderId) throws Throwable;

    String createLink(long orderId, long totalPrice, String ipClient, String returnUrl) throws Exception;

    @Transactional
    void checkResultPaidVnpay(String vnp_ResponseCode, String vnp_TxnRef, String vnp_Amount) throws Throwable;

    @Transactional
    void completed(long orderId) throws Throwable;

    Page<Order> getAllOfUser(int page, String status) throws Throwable;

    Order getDetail(long orderId) throws Throwable;

    Page<Order> getAll(int page, String id, String status, String from, String to) throws Throwable;

    @Transactional
    void confirmRefund(long orderId) throws Throwable;

    @Transactional
    void paySeller(long orderId) throws Throwable;

    int getNewOrder(String from, String to);

    Map<String, Integer> getOrderByMonth(String from, String to);

    @Transactional
    Order repayment(long orderId, String methodPayment) throws Throwable;

    Page<Order> getMyOrderSale(int page, String status, String from, String to);
}
