package haui.cntt.myproject.persistance.repository;

import haui.cntt.myproject.persistance.entity.ProductProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPropertyRepository extends JpaRepository<ProductProperty, Long> {
}
