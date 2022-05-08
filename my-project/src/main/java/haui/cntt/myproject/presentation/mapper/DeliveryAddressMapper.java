package haui.cntt.myproject.presentation.mapper;

import haui.cntt.myproject.persistance.entity.DeliveryAddress;
import haui.cntt.myproject.presentation.request.DeliveryAddressRequest;
import haui.cntt.myproject.presentation.response.DeliveryAddressResponse;

public class DeliveryAddressMapper {
    private DeliveryAddressMapper() {
        super();
    }

    public static DeliveryAddress convertToDeliveryAddress(DeliveryAddressRequest deliveryAddressRequest) {
        return DeliveryAddress.builder()
                .id(deliveryAddressRequest.getId())
                .province(deliveryAddressRequest.getProvince())
                .codeProvince(deliveryAddressRequest.getCodeProvince())
                .district(deliveryAddressRequest.getDistrict())
                .codeDistrict(deliveryAddressRequest.getCodeDistrict())
                .commune(deliveryAddressRequest.getCommune())
                .codeCommune(deliveryAddressRequest.getCodeCommune())
                .detail(deliveryAddressRequest.getDetail())
                .fullName(deliveryAddressRequest.getFullName())
                .cellphone(deliveryAddressRequest.getCellphone())
                .build();
    }

    public static DeliveryAddressResponse convertToDeliveryAddressResponse(DeliveryAddress deliveryAddress) {
        return DeliveryAddressResponse.builder()
                .id(deliveryAddress.getId())
                .province(deliveryAddress.getProvince())
                .codeProvince(deliveryAddress.getCodeProvince())
                .district(deliveryAddress.getDistrict())
                .codeDistrict(deliveryAddress.getCodeDistrict())
                .commune(deliveryAddress.getCommune())
                .codeCommune(deliveryAddress.getCodeCommune())
                .detail(deliveryAddress.getDetail())
                .fullName(deliveryAddress.getFullName())
                .cellphone(deliveryAddress.getCellphone())
                .build();
    }
}
