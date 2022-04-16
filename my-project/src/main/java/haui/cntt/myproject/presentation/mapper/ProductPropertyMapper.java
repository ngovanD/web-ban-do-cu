package haui.cntt.myproject.presentation.mapper;

import haui.cntt.myproject.persistance.entity.ProductProperty;
import haui.cntt.myproject.persistance.entity.Property;
import haui.cntt.myproject.presentation.request.ProductPropertyRequest;
import haui.cntt.myproject.presentation.response.ProductPropertyResponse;

public class ProductPropertyMapper {
    private ProductPropertyMapper() {
        super();
    }

    public static ProductPropertyResponse convertToProductPropertyResponse(ProductProperty productProperty) {
        return ProductPropertyResponse.builder()
                .propertyId(productProperty.getProperty().getId())
                .propertyName(productProperty.getProperty().getName())
                .value(productProperty.getValue())
                .note(productProperty.getProperty().getNote())
                .build();
    }
    public static ProductProperty convertToProductProperty(ProductPropertyRequest productPropertyRequest) {
        return ProductProperty.builder()
                .property(Property.builder().id(productPropertyRequest.getPropertyId()).build())
                .value(productPropertyRequest.getValue())
                .build();
    }
}
