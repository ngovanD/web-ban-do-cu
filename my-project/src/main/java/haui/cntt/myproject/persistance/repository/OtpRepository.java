package haui.cntt.myproject.persistance.repository;

import haui.cntt.myproject.persistance.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByCellphone(String cellphone);
}
