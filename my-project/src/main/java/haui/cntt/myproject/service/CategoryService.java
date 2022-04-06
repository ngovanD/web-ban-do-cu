package haui.cntt.myproject.service;

import haui.cntt.myproject.persistance.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<Category> getListCategoryHasNotParent();
    List<Category> getListCategoryChild(long parentId);
    Category getDetail(long id) throws Throwable;
    Category create(Category category, String fileName);
    void edit(long id, Category infoCategory) throws Throwable;
    void delete(long id) throws Throwable;
}
