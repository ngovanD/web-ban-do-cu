package haui.cntt.myproject.persistance.repository;

import haui.cntt.myproject.persistance.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
