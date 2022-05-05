package haui.cntt.myproject.service.Impl;

import haui.cntt.myproject.common.exception.BadRequestException;
import haui.cntt.myproject.common.file.FileUploadUtil;
import haui.cntt.myproject.persistance.entity.Slide;
import haui.cntt.myproject.persistance.repository.SlideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
public class SlideServiceImpl {
    @Autowired
    private SlideRepository slideRepository;

    static final String UPLOAD_DIR_SLIDE = "src/main/resources/static/slide/0/";

    public List<Slide> getAllActive() {
        return slideRepository.getAllActive();
    }

    @Transactional
    public void create(Slide slide, MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String typeOfFile = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = "slide-" + System.currentTimeMillis() + typeOfFile;
        FileUploadUtil.saveFile(UPLOAD_DIR_SLIDE, fileName, multipartFile);
        slide.setImage(fileName);
        slideRepository.save(slide);
    }

    @Transactional
    public void edit(long id, Slide slide, MultipartFile multipartFile) throws Throwable {
        Slide foundSlide = slideRepository.findById(id).orElseThrow(
                () -> {
                    throw new BadRequestException("Không tìm thấy slide !!!");
                }
        );
        foundSlide.setTitle(slide.getTitle());
        foundSlide.setTitleStrong(slide.getTitleStrong());
        foundSlide.setBriefDescription(slide.getBriefDescription());
        foundSlide.setLink(slide.getLink());

        if(!multipartFile.isEmpty())
        {
            FileUploadUtil.deleteFile(UPLOAD_DIR_SLIDE, foundSlide.getImage());

            String originalFilename = multipartFile.getOriginalFilename();
            String typeOfFile = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = "slide-" + System.currentTimeMillis() + typeOfFile;
            FileUploadUtil.saveFile(UPLOAD_DIR_SLIDE, fileName, multipartFile);
            foundSlide.setImage(fileName);
        }
        slideRepository.save(foundSlide);
    }

    @Transactional
    public void delete(long id) throws Throwable {
        Slide foundSlide = slideRepository.findById(id).orElseThrow(
                () -> {
                    throw new BadRequestException("Không tìm thấy slide !!!");
                }
        );
        FileUploadUtil.deleteFile(UPLOAD_DIR_SLIDE, foundSlide.getImage());
        slideRepository.delete(foundSlide);
    }

    @Transactional
    public void updateStatusHiddenFlag(long id) throws Throwable {
        Slide foundSlide = slideRepository.findById(id).orElseThrow(
                () -> {
                    throw new BadRequestException("Không tìm thấy slide !!!");
                }
        );
        foundSlide.setHiddenFlag(!foundSlide.getHiddenFlag());
        slideRepository.save(foundSlide);
    }

    public List<Slide> getAll() {
        return slideRepository.findAll();
    }

    public Slide getDetail(long slideId) throws Throwable{
        return slideRepository.findById(slideId).orElseThrow(() -> {
            throw new BadRequestException("Slide không tồn tại !!!");
        });
    }
}
