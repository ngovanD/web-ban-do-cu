package haui.cntt.myproject.service.Impl;

import com.sun.xml.internal.stream.Entity;
import haui.cntt.myproject.persistance.entity.Property;
import haui.cntt.myproject.persistance.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PropertyServiceImpl {
    @Autowired
    PropertyRepository propertyRepository;

    public List<Property> getAll() {
        return propertyRepository.findAll();
    }

    @Transactional
    public void create(Property convertToProperty) {
        propertyRepository.save(convertToProperty);
    }

    @Transactional
    public void delete(long propertyId) {
        Property foundProperty = propertyRepository.findById(propertyId).orElse(null);
        propertyRepository.delete(foundProperty);
    }
}
