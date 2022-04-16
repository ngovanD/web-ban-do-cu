package haui.cntt.myproject.presentation.mapper;

import haui.cntt.myproject.persistance.entity.Address;
import haui.cntt.myproject.presentation.request.AddressRequest;
import haui.cntt.myproject.presentation.response.AddressResponse;

public class AddressMapper {
    private AddressMapper() {
        super();
    }

    public static Address convertToAddress(AddressRequest addressRequest) {
        return Address.builder()
                .province(addressRequest.getProvince())
                .codeProvince(addressRequest.getCodeProvince())
                .district(addressRequest.getDistrict())
                .codeDistrict(addressRequest.getCodeDistrict())
                .commune(addressRequest.getCommune())
                .codeCommune(addressRequest.getCodeCommune())
                .detail(addressRequest.getDetail())
                .build();
    }

    public static AddressResponse convertToAddressResponse(Address address) {
        return AddressResponse.builder()
                .province(address.getProvince())
                .codeProvince(address.getCodeProvince())
                .district(address.getDistrict())
                .codeDistrict(address.getCodeDistrict())
                .commune(address.getCommune())
                .codeCommune(address.getCodeCommune())
                .detail(address.getDetail())
                .build();
    }
}
