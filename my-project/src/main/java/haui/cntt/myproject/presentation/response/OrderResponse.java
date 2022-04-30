package haui.cntt.myproject.presentation.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private long id;

    private String status;

    private long priceProduct;

    private long feeShipping;

    private String methodPayment;

    private DeliveryAddressResponse deliveryAddressResponse;

    private ProductResponse productResponse;

    private String createTime;

    //private User user;
}
