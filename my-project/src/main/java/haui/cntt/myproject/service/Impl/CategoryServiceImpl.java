package haui.cntt.myproject.service.Impl;

import com.github.slugify.Slugify;
import haui.cntt.myproject.common.exception.BadRequestException;
import haui.cntt.myproject.persistance.entity.Category;
import haui.cntt.myproject.persistance.repository.CategoryRepository;
import haui.cntt.myproject.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> getListCategoryHasNotParent() {
        return categoryRepository.getListCategoryHasNotParent();
    }

    public List<Category> getListCategoryChild(long parentId) {
        return categoryRepository.findByCategoryParentId(parentId);
    }

    public Category getDetail(long id) throws Throwable{
        return categoryRepository.findById(id).orElseThrow(
                ()->{throw new BadRequestException("loại sản phẩm không tồn tại!!!");}
        );
    }

    @Transactional
    public Category create(Category category, String originalFilename) {

        String typeOfFile = originalFilename.substring(originalFilename.lastIndexOf("."));

        String newFileName = new Slugify().slugify(category.getName() + LocalDateTime.now());
        category.setImage(newFileName + typeOfFile);

        Category categoryParent = categoryRepository.findById(category.getCategoryParent().getId()).orElse(null);
        category.setCategoryParent(categoryParent);

        return categoryRepository.save(category);
    }

    @Transactional
    public Category createTest(Category category) {

        if(category.getCategoryParent() != null)
        {
            Category categoryParent = categoryRepository.findById(category.getCategoryParent().getId()).orElse(null);
            category.setCategoryParent(categoryParent);
        }
        return categoryRepository.save(category);
    }

    @Transactional
    public void edit(long id, Category infoCategory) throws Throwable{
        Category foundCategory = categoryRepository.findById(id).orElseThrow(
                ()->{throw new BadRequestException("Loại sản phẩm không tồn tại!!!");}
        );
        foundCategory.setName(infoCategory.getName());
        foundCategory.setImage(infoCategory.getImage());

        Category foundCategoryParent = categoryRepository.findById(infoCategory.getCategoryParent().getId()).orElseThrow(
                ()->{throw new BadRequestException("Loại sản phẩm không tồn tại!!!");}
        );
        foundCategory.setCategoryParent(foundCategoryParent);

        categoryRepository.save(foundCategory);
    }

    @Transactional
    public void delete(long id) throws Throwable{
        Category foundCategory = categoryRepository.findById(id).orElseThrow(
                ()->{throw new BadRequestException("Loại sản phẩm không tồn tại!!!");}
        );
        categoryRepository.delete(foundCategory);
    }

    public HashMap<Category, List<Category>> getTreeCategory()
    {
        List<Category> categoryList = getListCategoryHasNotParent();

        HashMap<Category, List<Category>> treeCategory = new HashMap<>();

        for(Category item : categoryList)
        {
            treeCategory.put(item, getListCategoryChild(item.getId()));
        }

        return treeCategory;
    }
}
