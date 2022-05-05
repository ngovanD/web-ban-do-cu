package haui.cntt.myproject.service.Impl;

import haui.cntt.myproject.persistance.entity.Property;
import haui.cntt.myproject.persistance.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
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

    public Page<Property> getAllPage(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        return propertyRepository.findAll(pageable);
    }

    @Transactional
    public void edit(long propertyId, Property convertToProperty) {
        Property foundProperty = propertyRepository.findById(propertyId).orElse(null);
        foundProperty.setName(convertToProperty.getName());
        foundProperty.setUnit(convertToProperty.getUnit());
        foundProperty.setNote(convertToProperty.getNote());
        propertyRepository.save(foundProperty);
    }
}
