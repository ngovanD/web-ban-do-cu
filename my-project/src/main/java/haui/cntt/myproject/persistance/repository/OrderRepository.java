package haui.cntt.myproject.persistance.repository;

import haui.cntt.myproject.persistance.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "select * from ordered where product_id = :productId and (status != 'PENDING' or status !='CANCELED')limit 1", nativeQuery = true)
    Optional<Order> findByProductId(@Param(value = "productId") long productId);

    @Query(value = "select * from ordered where user_id = :userId order by id desc", nativeQuery = true)
    Page<Order> getAllOrder(Pageable pageable, @Param(value = "userId") long userId);

    @Query(value = "select * from ordered where user_id = :userId and status = :status order by id desc", nativeQuery = true)
    Page<Order> filterOrder(Pageable pageable, @Param(value = "userId") long userId, @Param(value = "status") String status);

    @Query(value = "select * from ordered where (id = :id or :id = '') and (status = :status or :status = 'all') and (date(created_date) between :from and :to)  order by id desc", nativeQuery = true)
    Page<Order> filterAllOrder(Pageable pageable
            , @Param(value = "id") String id , @Param(value = "status") String status
            , @Param(value = "from") String from, @Param(value = "to") String to);

    @Query(value = "select count(*) from ordered where (date(created_date) between :from and :to)", nativeQuery = true)
    int getNewOrder(@Param(value = "from") String from, @Param(value = "to") String to);

    @Query(value = "select count(*) from ordered where month(created_date) = :month and year(created_date) = :year", nativeQuery = true)
    int getOrderOfMonth(@Param(value = "month") int month, @Param(value = "year") int year);
}