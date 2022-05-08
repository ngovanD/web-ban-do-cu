package haui.cntt.myproject.service;

import haui.cntt.myproject.persistance.entity.Blog;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface BlogService {
    void create(Blog blog, MultipartFile multipartFile) throws IOException;

    void edit(long id, Blog blog, MultipartFile multipartFile) throws Throwable;

    void delete(long id) throws Throwable;

    void updateStatusHiddenFlag(long id) throws Throwable;

    List<Blog> getAll();

    Blog getDetail(long blogId) throws Throwable;

    List<Blog> getTop4();
}
