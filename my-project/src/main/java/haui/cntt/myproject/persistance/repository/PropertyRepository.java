package haui.cntt.myproject.persistance.repository;

import haui.cntt.myproject.persistance.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
}
