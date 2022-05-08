package haui.cntt.myproject.service;

import haui.cntt.myproject.persistance.entity.DeliveryAddress;
import org.springframework.stereotype.Service;

@Service
public interface DeliveryAddressService {
    DeliveryAddress getInfoDelivery(long productId) throws Throwable;

    void changeDelivery(long orderId, DeliveryAddress convertToDeliveryAddress) throws Throwable;
}
