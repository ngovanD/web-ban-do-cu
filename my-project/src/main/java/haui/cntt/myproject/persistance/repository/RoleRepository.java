package haui.cntt.myproject.persistance.repository;

import haui.cntt.myproject.common.enum_.RoleEnum;
import haui.cntt.myproject.persistance.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}