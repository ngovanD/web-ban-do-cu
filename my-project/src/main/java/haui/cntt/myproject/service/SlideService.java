package haui.cntt.myproject.service;

import haui.cntt.myproject.persistance.entity.Slide;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface SlideService {
    public List<Slide> getAllActive();

    public void create(Slide slide, MultipartFile multipartFile) throws IOException;

    public void edit(long id, Slide slide, MultipartFile multipartFile) throws Throwable;

    public void delete(long id) throws Throwable;

    public void updateStatusHiddenFlag(long id) throws Throwable;

    public List<Slide> getAll();

    public Slide getDetail(long slideId) throws Throwable;
}
