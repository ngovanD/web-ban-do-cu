package haui.cntt.myproject.persistance.repository;

import haui.cntt.myproject.persistance.entity.ImageProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageProductRepository extends JpaRepository<ImageProduct, Long> {
}
