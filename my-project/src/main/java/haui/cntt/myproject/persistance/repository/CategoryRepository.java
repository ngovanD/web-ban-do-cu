package haui.cntt.myproject.persistance.repository;

import haui.cntt.myproject.persistance.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "select * from category where hidden_flag = 0 and category_parent_id is null", nativeQuery = true)
    List<Category> getListCategoryHasNotParent();

    List<Category> findByCategoryParentId(long category_parent_id);

    Category findByName(String nameCategoryParent);
}
