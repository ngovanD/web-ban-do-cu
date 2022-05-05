package haui.cntt.myproject.presentation.controller.user;

import haui.cntt.myproject.presentation.mapper.DeliveryAddressMapper;
import haui.cntt.myproject.presentation.mapper.OrderMapper;
import haui.cntt.myproject.presentation.mapper.ProductMapper;
import haui.cntt.myproject.presentation.request.DeliveryAddressRequest;
import haui.cntt.myproject.presentation.request.OrderRequest;
import haui.cntt.myproject.presentation.response.OrderResponse;
import haui.cntt.myproject.presentation.response.ProductResponse;
import haui.cntt.myproject.service.Impl.DeliveryAddressServiceImpl;
import haui.cntt.myproject.service.Impl.OrderServiceImpl;
import haui.cntt.myproject.service.Impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user/order")
public class OrderController {
    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private DeliveryAddressServiceImpl deliveryAddressService;

    @GetMapping("/create/{id}")
    public String getPageCreateOrder(Model model, @PathVariable(value = "id") long productId) throws Throwable {
        ProductResponse productResponse = ProductMapper.convertToProductResponse(productService.getDetailProduct(productId));
        model.addAttribute("productResponse", productResponse);
        model.addAttribute("orderRequest", new OrderRequest());
        return "checkout";
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(HttpServletRequest request, HttpServletResponse response,
                                         Model model, @RequestBody OrderRequest orderedRequest) throws Throwable {
        OrderResponse orderResponse = OrderMapper.convertToOrderResponse(
                orderService.create(OrderMapper.convertToOrder(orderedRequest))
        );

        if (orderedRequest.getMethodPayment().equals("VNPAY")) {
            String link = orderService.createLink(
                    orderResponse.getId()
                    , orderResponse.getPriceProduct() + orderResponse.getFeeShipping()
                    , request.getRemoteAddr()
                    , MvcUriComponentsBuilder.fromController(OrderController.class).toUriString() + "/response/vnpay");
            return ResponseEntity.ok().body(link);
        }
        return ResponseEntity.ok().body("Tạo đơn thành công !!!");
    }

    @GetMapping(value = "/response/vnpay", name = "responsePaymentVnpay")
    public String checkPaymentVnpay(@RequestParam(name = "vnp_ResponseCode") String vnp_ResponseCode,
                                                    @RequestParam(name = "vnp_TxnRef") String vnp_TxnRef,
                                                    @RequestParam(name = "vnp_Amount") String vnp_Amount,
                                                    HttpServletRequest request, HttpServletResponse response) throws Throwable {

        //log.info("Mapped checkPaymentVnpay method {{GET: /payment/zalopay/orderId}}");
        orderService.checkResultPaidVnpay(vnp_ResponseCode, vnp_TxnRef, vnp_Amount);
        return "redirect:/user/order/" + Long.parseLong(vnp_TxnRef);
    }

    @GetMapping("/get-all")
    public String getAll(Model model, @RequestParam(name = "page", required = false, defaultValue = "1") int page
            , @RequestParam(name = "status", required = false, defaultValue = "all") String status) throws Throwable {
        Page<OrderResponse> orderResponseList = orderService.getAllOfUser(page - 1, status)
                .map(OrderMapper::convertToOrderResponse);
        model.addAttribute("list_order", orderResponseList.getContent());
        model.addAttribute("current_page", page);
        model.addAttribute("total_page", orderResponseList.getTotalPages());
        model.addAttribute("status", status);
        return "my_list_order";
    }

    @GetMapping("/{id}")
    public String getDetail(Model model, @PathVariable(value = "id") long orderId) throws Throwable {
        OrderResponse orderResponse = OrderMapper.convertToOrderResponse(orderService.getDetail(orderId));
        model.addAttribute("orderResponse", orderResponse);
        return "my_order_detail";
    }

    @GetMapping("/completed/{id}")
    public String completed(Model model, @PathVariable(value = "id") long orderId) throws Throwable {
        orderService.completed(orderId);
        return "redirect:/user/order/" + orderId;
    }

    @PostMapping("/change-delivery-address/{id}")
    public ResponseEntity<String> changeDeliveryAddress(Model model
            , @PathVariable(value = "id") long orderId, @RequestBody DeliveryAddressRequest deliveryAddressRequest) throws Throwable {
        deliveryAddressService.changeDelivery(orderId, DeliveryAddressMapper.convertToDeliveryAddress(deliveryAddressRequest));
        return ResponseEntity.ok().body("Đổi địa chỉ thành công !!!");
    }

    @PutMapping("/cancel-order/{id}")
    public ResponseEntity<String> cancelOrder(Model model, @PathVariable(value = "id") long orderId) throws Throwable {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().body("Đã hủy đơn hàng !!!");
    }

    @PostMapping("/repayment/{id}")
    public ResponseEntity<String> changeDeliveryAddress(@PathVariable(value = "id") long orderId
            , @RequestParam(value = "methodPayment", required = true) String methodPayment
            , HttpServletRequest request) throws Throwable {
        OrderResponse orderResponse = OrderMapper.convertToOrderResponse(orderService.repayment(orderId, methodPayment));
        if (orderResponse.getMethodPayment().equals("VNPAY")) {
            String link = orderService.createLink(
                    orderResponse.getId()
                    , orderResponse.getPriceProduct() + orderResponse.getFeeShipping()
                    , request.getRemoteAddr()
                    , MvcUriComponentsBuilder.fromController(OrderController.class).toUriString() + "/response/vnpay");
            return ResponseEntity.ok().body(link);
        }
        return ResponseEntity.ok().body("Tạo đơn thành công !!!");
    }
}