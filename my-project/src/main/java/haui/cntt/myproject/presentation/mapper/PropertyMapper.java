package haui.cntt.myproject.presentation.mapper;

import haui.cntt.myproject.persistance.entity.Property;
import haui.cntt.myproject.presentation.request.PropertyRequest;
import haui.cntt.myproject.presentation.response.PropertyResponse;

public class PropertyMapper {
    private PropertyMapper(){super();}

    public static PropertyResponse convertToPropertyResponse(Property property)
    {
        return PropertyResponse.builder()
                .id(property.getId())
                .name(property.getName())
                .build();
    }

    public static Property convertToProperty(PropertyRequest propertyRequest) {
        return Property.builder()
                .name(propertyRequest.getName())
                .build();
    }
}
