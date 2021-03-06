package haui.cntt.myproject.presentation.mapper;

import haui.cntt.myproject.persistance.entity.Property;
import haui.cntt.myproject.presentation.request.ProductPropertyRequest;
import haui.cntt.myproject.presentation.request.PropertyRequest;
import haui.cntt.myproject.presentation.response.PropertyResponse;

public class PropertyMapper {
    private PropertyMapper() {
        super();
    }

    public static PropertyResponse convertToPropertyResponse(Property property) {
        return PropertyResponse.builder()
                .id(property.getId())
                .name(property.getName())
                .note(property.getNote())
                .unit(property.getUnit())
                .build();
    }

    public static Property convertToProperty(PropertyRequest propertyRequest) {
        return Property.builder()
                .id(propertyRequest.getId())
                .name(propertyRequest.getName())
                .note(propertyRequest.getNote())
                .unit(propertyRequest.getUnit())
                .build();
    }

    public static ProductPropertyRequest convertToPropertyRequest(Property property) {
        return ProductPropertyRequest.builder()
                .propertyId(property.getId())
                .name(property.getName())
                .note(property.getNote())
                .build();
    }
}
