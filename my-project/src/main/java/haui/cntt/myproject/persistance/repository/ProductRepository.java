package haui.cntt.myproject.persistance.repository;

import haui.cntt.myproject.persistance.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select * from product order by rand() limit :limit", nativeQuery = true)
    List<Product> getRandomProduct(@Param(value = "limit") int limit);

    @Query(value = "select * from product order by id desc limit :limit", nativeQuery = true)
    List<Product> getNewProduct(@Param(value = "limit") int limit);

    @Query(value = "select * from product where product.view > 100 limit :limit", nativeQuery = true)
    List<Product> getHotProduct(@Param(value = "limit") int limit);

    @Query(value = "select * " +
            "from product inner join category child on product.category_id = child.id " +
            "inner join category parent on parent.id = child.category_parent_id " +
            "inner join address on product.id = address.product_id " +
            "where (parent.slug = :slug or child.slug = :slug) and price >= :min and price <= :max and (code_province = :code_province or 0 = :code_province) " +
            "order by price", nativeQuery = true)
    Page<Product> filterProductAndSortByPrice(Pageable pageable
            , @Param(value = "slug") String slug, @Param(value = "min") int min
            , @Param(value = "max") int max, @Param(value = "code_province") int codeProvince);

    @Query(value = "select * " +
            "from product inner join category child on product.category_id = child.id " +
            "inner join category parent on parent.id = child.category_parent_id " +
            "inner join address on product.id = address.product_id " +
            "where (parent.slug = :slug or child.slug = :slug) and price >= :min and price <= :max and (code_province = :code_province or 0 = :code_province)  " +
            "order by product.created_date desc", nativeQuery = true)
    Page<Product> filterProductAndSortByCreateDate(Pageable pageable
            , @Param(value = "slug") String slug, @Param(value = "min") int min
            , @Param(value = "max") int max, @Param(value = "code_province") int codeProvince);

    @Query(value = "select * " +
            "from product inner join category child on product.category_id = child.id " +
            "inner join category parent on parent.id = child.category_parent_id " +
            "inner join address on product.id = address.product_id " +
            "where (parent.slug = :slug or child.slug = :slug or '' = :slug) and price >= :min and price <= :max and (code_province = :code_province or 0 = :code_province) ", nativeQuery = true)
    List<Product> filterProductList(
            @Param(value = "slug") String slug, @Param(value = "min") int min
            , @Param(value = "max") int max, @Param(value = "code_province") int codeProvince);
}
