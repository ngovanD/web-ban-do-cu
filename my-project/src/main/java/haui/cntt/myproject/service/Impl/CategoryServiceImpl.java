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
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> getListCategoryHasNotParent() {
        return categoryRepository.getListCategoryHasNotParent();
    }

    @Override
    public List<Category> getListCategoryChild(long parentId) {
        return categoryRepository.findByCategoryParentId(parentId);
    }

    @Override
    public Category getDetail(long id) throws Throwable{
        return categoryRepository.findById(id).orElseThrow(
                ()->{throw new BadRequestException("loại sản phẩm không tồn tại!!!");}
        );
    }

    @Override
    @Transactional
    public Category create(Category category, String originalFilename) {

        String typeOfFile = originalFilename.substring(originalFilename.lastIndexOf("."));

        String newFileName = new Slugify().slugify(category.getName() + LocalDateTime.now());
        category.setImage(newFileName + typeOfFile);

        Category categoryParent = categoryRepository.findById(category.getCategoryParent().getId()).orElse(null);
        category.setCategoryParent(categoryParent);

        return categoryRepository.save(category);
    }

    @Override
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

    @Override
    @Transactional
    public void delete(long id) throws Throwable{
        Category foundCategory = categoryRepository.findById(id).orElseThrow(
                ()->{throw new BadRequestException("Loại sản phẩm không tồn tại!!!");}
        );
        categoryRepository.delete(foundCategory);
    }
}
