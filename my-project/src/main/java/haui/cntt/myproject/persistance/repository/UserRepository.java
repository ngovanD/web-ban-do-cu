package haui.cntt.myproject.persistance.repository;

import haui.cntt.myproject.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByCellphone(String cellphone);

//    @Query(value = "select case when count(*) > 0 then 'true' else 'false' end result " +
//            "from user u " +
//            "where u.username = :username and hidden_flag = 1 ", nativeQuery = true)
//    boolean checkLocked(String username);
}
